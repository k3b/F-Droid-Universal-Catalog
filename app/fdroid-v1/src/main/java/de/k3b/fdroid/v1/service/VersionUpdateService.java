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

import java.text.DecimalFormat;

import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.common.VersionCommon;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.ProgressListener;
import de.k3b.fdroid.domain.interfaces.VersionRepository;
import de.k3b.fdroid.util.StringUtil;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class VersionUpdateService {
    private final AppRepository appRepository;
    private final VersionRepository versionRepository;

    private final DecimalFormat numFormatter = new DecimalFormat("00");
    ProgressListener progressListener = null;
    private de.k3b.fdroid.domain.App lastApp = null;
    private String lastPackageName = null;
    private Version minVersion = null;
    private Version maxVersion = null;
    private StringBuilder signer = new StringBuilder();

    public VersionUpdateService(AppRepository appRepository, VersionRepository versionRepository,
                                ProgressListener progressListener) {
        this.appRepository = appRepository;
        this.versionRepository = versionRepository;
        this.progressListener = progressListener;
    }

    public Version update(int repoId, String packageName, de.k3b.fdroid.v1.domain.Version v1Version) {
        if (lastPackageName != null && (packageName == null || packageName.compareTo(lastPackageName) != 0)) {
            saveVersionAggregate(repoId, packageName);
        }
        lastPackageName = packageName;

        Version roomVersion = null;
        if (v1Version != null) {
            roomVersion = versionRepository.findByRepoPackageAndVersionCode(repoId, packageName, v1Version.getVersionCode());
            if (roomVersion == null) {
                de.k3b.fdroid.domain.App app = appRepository.findByRepoIdAndPackageName(repoId, packageName);

                if (app == null) {
                    app = new de.k3b.fdroid.domain.App();
                    app.setPackageName(packageName);
                    app.setRepoId(repoId);
                    appRepository.insert(app);
                }
                lastApp = app;

                roomVersion = new Version();
                roomVersion.setAppId(app.getId());
                VersionCommon.copyCommon(roomVersion, v1Version);

                roomVersion.setNativecode(StringUtil.toCsvStringOrNull(v1Version.getNativecode()));

                versionRepository.insert(roomVersion);
            } else {
                VersionCommon.copyCommon(roomVersion, v1Version);
                versionRepository.update(roomVersion);
            }

            String s = roomVersion.getSigner();
            if (!StringUtil.isEmpty(s) && !signer.toString().contains(s)) {
                signer.append(s).append(" ");
            }
            if (minVersion == null || minVersion.getVersionCode() > roomVersion.getVersionCode()) {
                minVersion = roomVersion;
            }
            if (maxVersion == null || maxVersion.getVersionCode() < roomVersion.getVersionCode()) {
                maxVersion = roomVersion;
            }
        }
        return roomVersion;
    }

    private void saveVersionAggregate(int repoId, String packageName) {
        if (progressListener != null) {
            progressListener.onProgress(".", packageName);
        }
        if (lastApp == null) {
            lastApp = appRepository.findByRepoIdAndPackageName(repoId, packageName);
        }

        if (minVersion != null && lastApp != null) {
            StringBuilder sdk = new StringBuilder();
            add(sdk, minVersion.getMinSdkVersion(), minVersion.getTargetSdkVersion(), minVersion.getMaxSdkVersion());

            StringBuilder code = new StringBuilder();
            add(code, minVersion.getVersionName(), minVersion.getVersionCode());

            if (minVersion.getVersionCode() != maxVersion.getVersionCode()) {
                add(sdk.append(" - "), maxVersion.getMinSdkVersion(), maxVersion.getTargetSdkVersion(), maxVersion.getMaxSdkVersion());
                add(code.append(" - "), maxVersion.getVersionName(), maxVersion.getVersionCode());
            }

            lastApp.setSearchVersion(code.toString());
            lastApp.setSearchSdk(sdk.toString());
            lastApp.setSearchSigner(signer.toString());
            appRepository.update(lastApp);
        }

        this.lastPackageName = null;
        this.lastApp = null;

        minVersion = null;
        maxVersion = null;
        signer = new StringBuilder();

    }

    private void add(StringBuilder code, String versionName, int versionCode) {
        if (!StringUtil.isEmpty(versionName)) {
            code.append(versionName);
        }
        if (!StringUtil.isEmpty(versionCode)) {
            code.append("(").append(versionCode).append(")");
        }
    }

    private void add(StringBuilder version, int minSdkVersion, int targetSdkVersion, int maxSdkVersion) {
        version.append("[");
        if (!StringUtil.isEmpty(minSdkVersion)) version.append(numFormatter.format(minSdkVersion));
        version.append(",");
        if (!StringUtil.isEmpty(targetSdkVersion))
            version.append(numFormatter.format(targetSdkVersion));
        version.append(",");
        if (!StringUtil.isEmpty(maxSdkVersion)) version.append(numFormatter.format(maxSdkVersion));
        version.append("]");
    }
}
