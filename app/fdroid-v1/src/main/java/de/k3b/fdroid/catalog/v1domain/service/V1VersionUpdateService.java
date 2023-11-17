/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid.v1domain the fdroid json catalog-format-v1 parser.
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

package de.k3b.fdroid.catalog.v1domain.service;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.catalog.v1domain.entity.IV1UpdateService;
import de.k3b.fdroid.catalog.v1domain.entity.V1Version;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.entity.common.VersionCommon;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.VersionRepository;
import de.k3b.fdroid.domain.service.HardwareProfileService;
import de.k3b.fdroid.domain.service.VersionService;
import de.k3b.fdroid.domain.util.ExceptionUtils;
import de.k3b.fdroid.domain.util.StringUtil;

/**
 * {@link IV1UpdateService} that updates {@link Version}
 * from {@link V1Version} using a {@link VersionRepository}
 */
public class V1VersionUpdateService implements IV1UpdateService, ProgressObservable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    private static final int PROGRESS_INTERVALL = 100;

    @Nullable
    private final AppRepository appRepository;
    @Nullable
    private final VersionRepository versionRepository;

    private final VersionService versionService = new VersionService();

    PackageCollector packageCollector = new PackageCollector();
    @Nullable
    private final HardwareProfileService hardwareProfileService;
    ProgressObserver progressObserver = null;
    private int progressCounter = 0;
    private int progressCountdown = 0;
    private int nextMockAppId = 142;
    private int nextMockVersionId = 72;

    public V1VersionUpdateService(@Nullable AppRepository appRepository,
                                  @Nullable VersionRepository versionRepository,
                                  @Nullable HardwareProfileService hardwareProfileService) {
        this.appRepository = appRepository;
        this.versionRepository = versionRepository;
        this.hardwareProfileService = hardwareProfileService;
    }

    public V1VersionUpdateService init() {
        if (this.hardwareProfileService != null) {
            this.hardwareProfileService.init();
        }
        progressCounter = 0;
        progressCountdown = 0;
        return this;
    }

    // update(repoId, packageName,v1Version) -> update(repoId, packageName,List<v1Version>)
    public void updateCollectVersions(int repoId, String packageName, V1Version v1Version) {
        packageCollector.update(repoId, packageName, v1Version);
    }

    // update(repoId, packageName,List<v1Version>) -> update(app,List<v1Version>)
    private void updateCorrespondingApp(
            int repoId, String packageName,
            List<V1Version> v1VersionList)
            throws PersistenceException {
        App roomApp = null;
        try {
            roomApp = getOrCreateApp(packageName);

            update(repoId, roomApp, v1VersionList);
        } catch (Exception ex) {
            // thrown by j2se hibernate database problem
            // hibernate DataIntegrityViolationException -> NestedRuntimeException
            // hibernate org.hibernate.exception.DataException inherits from PersistenceException
            String message = "PersistenceException in " + getClass().getSimpleName()
                    + ".update(repo=" + repoId + ", app("
                    + (roomApp == null ? "?" : roomApp.getAppId())
                    + ")=" + packageName + ") "
                    + ExceptionUtils.getParentCauseMessage(ex, PersistenceException.class);
            LOGGER.error(message + "\n\tv1Version=" + v1VersionList, ex);
            throw new PersistenceException(message, ex);
        }
    }

    // most processing is done here
    private void update(int repoId, App app,
                        List<V1Version> v1VersionList) {
        progressCounter++;
        if (progressObserver != null && (--progressCountdown) <= 0) {
            progressObserver.onProgress(progressCounter, ".", app.getPackageName());
            progressCountdown = PROGRESS_INTERVALL;
        }

        List<Version> roomVersionList = (versionRepository == null)
                ? new ArrayList<>()
                : versionRepository.findByAppId(app.getId());
        update(repoId, app, roomVersionList, v1VersionList);

        saveAll(roomVersionList);

        if (appRepository != null) appRepository.update(app);

        if (this.hardwareProfileService != null) {
            this.hardwareProfileService.updateAppProfiles(app, roomVersionList);
        }

    }

    protected void update(int repoId, App app, List<Version> roomVersionList, List<V1Version> v1VersionList) {
        updateMapV1ToDbContentent(app.getId(), repoId, roomVersionList, v1VersionList);

        versionService.fixMaxSdk(roomVersionList);
        if (v1VersionList.size() > 14) {
            List<Version> deletedVersionList = versionService.removeInterimVersions(roomVersionList, repoId);
            deleteAll(deletedVersionList);
        }

        versionService.recalculateSearchFields(app, roomVersionList);
    }

    private void updateMapV1ToDbContentent(int appId, int repoId, List<Version> roomVersionList, List<V1Version> v1VersionList) {
        Map<Integer, Version> roomCode2Version = new HashMap<>();
        for (Version roomVersion : roomVersionList) {
            if (roomVersion.getRepoId() == repoId) {
                roomCode2Version.put(roomVersion.getVersionCode(), roomVersion);
            }
        }

        Map<Integer, Version> roomCode2VersionRemaining = new HashMap<>(roomCode2Version);
        for (V1Version v1Version : v1VersionList) {
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
            if (versionRepository != null) versionRepository.delete(roomVersion);
        }
    }

    private App getOrCreateApp(String packageName) {
        App app = null;

        if (appRepository != null) {
            app = appRepository.findByPackageName(packageName);

            if (app == null) {
                app = new App(packageName);
                appRepository.insert(app);
            }
        } else {
            app = new App(packageName);
            app.setId(nextMockAppId++);
        }
        return app;
    }

    private void deleteAll(List<Version> roomVersionList) {
        if (versionRepository != null) {
            for (Version roomVersion : roomVersionList) {
                if (roomVersion.getId() != 0) {
                    versionRepository.delete(roomVersion);
                }
            }
        }
    }

    private void saveAll(List<Version> roomVersionList) {
        if (versionRepository != null) {
            for (Version roomVersion : roomVersionList) {
                if (roomVersion.getId() == 0) {
                    versionRepository.insert(roomVersion);
                } else {
                    versionRepository.update(roomVersion);
                }
            }
        } else {
            for (Version roomVersion : roomVersionList) {
                if (roomVersion.getId() == 0) {
                    roomVersion.setId(nextMockVersionId++);
                }
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
        private List<V1Version> v1VersionList = new ArrayList<>();

        public void update(int repoId, String packageName, V1Version v1Version) {
            if (v1VersionList.size() > 0 && (packageName == null || packageName.compareTo(lastPackageName) != 0)) {
                // packagename null or changed : Process collected versons
                V1VersionUpdateService.this.updateCorrespondingApp(repoId, lastPackageName, v1VersionList);

                v1VersionList = new ArrayList<>();
            }
            if (packageName != null && v1Version != null) {
                lastPackageName = packageName;
                v1VersionList.add(v1Version);
            }
        }
    }

}
