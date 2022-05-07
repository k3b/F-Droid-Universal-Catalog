/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid.v1 the fdroid json catalog-format-v1 parser.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package de.k3b.fdroid.v1.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.common.VersionCommon;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.interfaces.VersionRepository;
import de.k3b.fdroid.service.HardwareProfileService;
import de.k3b.fdroid.service.VersionService;
import de.k3b.fdroid.util.StringUtil;
import de.k3b.fdroid.v1.domain.UpdateService;

/**
 * {@link UpdateService} that updates {@link de.k3b.fdroid.domain.Version}
 * from {@link de.k3b.fdroid.v1.domain.Version} using a {@link VersionRepository}
 */
public class VersionUpdateService implements UpdateService, ProgressObservable {
    private static final int PROGRESS_INTERVALL = 100;

    private final AppRepository appRepository;
    private final VersionRepository versionRepository;

    private final VersionService versionService = new VersionService();

    PackageCollector packageCollector = new PackageCollector();
    private final HardwareProfileService hardwareProfileService;
    ProgressObserver progressObserver = null;
    private int progressCounter = 0;
    private int progressCountdown = 0;

    public VersionUpdateService(AppRepository appRepository,
                                VersionRepository versionRepository,
                                HardwareProfileService hardwareProfileService) {
        this.appRepository = appRepository;
        this.versionRepository = versionRepository;
        this.hardwareProfileService = hardwareProfileService;
    }

    public void init() {
        if (this.hardwareProfileService != null) {
            this.hardwareProfileService.init();
        }
        progressCounter = 0;
        progressCountdown = 0;
    }

    // update(repoId, packageName,v1Version) -> update(repoId, packageName,List<v1Version>)
    public void updateCollectVersions(int repoId, String packageName, de.k3b.fdroid.v1.domain.Version v1Version) {
        packageCollector.update(repoId, packageName, v1Version);
    }

    // update(repoId, packageName,List<v1Version>) -> update(app,List<v1Version>)
    private void updateGetCorrespondingApp(int repoId, String packageName, List<de.k3b.fdroid.v1.domain.Version> v1VersionList) {
        App app = getOrCreateApp(packageName);

        update(repoId, app, v1VersionList);
    }

    // most processing is done here
    private void update(int repoId, App app, List<de.k3b.fdroid.v1.domain.Version> v1VersionList) {
        progressCounter++;
        if (progressObserver != null && (--progressCountdown) <= 0) {
            progressObserver.onProgress(progressCounter, ".", app.getPackageName());
            progressCountdown = PROGRESS_INTERVALL;
        }

        List<Version> roomVersionList = versionRepository.findByAppId(app.getId());
        updateMapV1ToDbContentent(app.getId(), repoId, roomVersionList, v1VersionList);

        versionService.fixMaxSdk(roomVersionList);
        if (v1VersionList.size() > 14) {
            List<Version> deletedVersionList = versionService.removeInterimVersions(roomVersionList, repoId);
            deleteAll(deletedVersionList);
        }

        versionService.recalculateSearchFields(app, roomVersionList);

        saveAll(roomVersionList);

        appRepository.update(app);

        if (this.hardwareProfileService != null) {
            this.hardwareProfileService.updateAppProfiles(app, roomVersionList);
        }


    }

    private void updateMapV1ToDbContentent(int appId, int repoId, List<Version> roomVersionList, List<de.k3b.fdroid.v1.domain.Version> v1VersionList) {
        Map<Integer, Version> roomCode2Version = new HashMap<>();
        for (Version roomVersion : roomVersionList) {
            if (roomVersion.getRepoId() == repoId) {
                roomCode2Version.put(roomVersion.getVersionCode(), roomVersion);
            }
        }

        Map<Integer, Version> roomCode2VersionRemaining = new HashMap<>(roomCode2Version);
        for (de.k3b.fdroid.v1.domain.Version v1Version : v1VersionList) {
            int versionCode = v1Version.getVersionCode();
            Version roomVersion = roomCode2Version.get(versionCode);
            if (roomVersion == null) {
                roomVersion = new Version(appId, repoId);
                roomVersionList.add(roomVersion);
                roomCode2Version.put(versionCode, roomVersion);
            } else {
                roomCode2VersionRemaining.remove(versionCode);
            }

            VersionCommon.copyCommon(roomVersion, v1Version);
            roomVersion.setNativecode(StringUtil.toCsvStringOrNull(v1Version.getNativecode()));
        }

        for (Version roomVersion : roomCode2VersionRemaining.values()) {
            roomVersionList.remove(roomVersion);
            versionRepository.delete(roomVersion);
        }
    }

    private App getOrCreateApp(String packageName) {
        App app = appRepository.findByPackageName(packageName);

        if (app == null) {
            app = new App(packageName);
            appRepository.insert(app);
        }
        return app;
    }

    private void deleteAll(List<Version> roomVersionList) {
        for (Version roomVersion : roomVersionList) {
            if (roomVersion.getId() != 0) {
                versionRepository.delete(roomVersion);
            }
        }
    }

    private void saveAll(List<Version> roomVersionList) {
        for (Version roomVersion : roomVersionList) {
            if (roomVersion.getId() == 0) {
                versionRepository.insert(roomVersion);
            } else {
                versionRepository.update(roomVersion);
            }
        }
    }

    public void setProgressObserver(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
        if (this.hardwareProfileService != null)
            hardwareProfileService.setProgressObserver(progressObserver);
    }

    private class PackageCollector {
        private String lastPackageName = null;
        private List<de.k3b.fdroid.v1.domain.Version> v1VersionList = new ArrayList<>();

        public void update(int repoId, String packageName, de.k3b.fdroid.v1.domain.Version v1Version) {
            if (v1VersionList.size() > 0 && (packageName == null || packageName.compareTo(lastPackageName) != 0)) {
                // packagename null or changed : Process collected versons
                VersionUpdateService.this.updateGetCorrespondingApp(repoId, lastPackageName, v1VersionList);

                v1VersionList = new ArrayList<>();
            }
            if (packageName != null && v1Version != null) {
                lastPackageName = packageName;
                v1VersionList.add(v1Version);
            }
        }
    }

}
