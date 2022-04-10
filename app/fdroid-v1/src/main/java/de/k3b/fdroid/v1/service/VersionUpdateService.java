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

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.common.VersionCommon;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.VersionRepository;
/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class VersionUpdateService {
    private final AppRepository appRepository;
    private final VersionRepository versionRepository;

    public VersionUpdateService(AppRepository appRepository, VersionRepository versionRepository) {
        this.appRepository = appRepository;
        this.versionRepository = versionRepository;
    }

    public Version update(int repoId, String packageName, de.k3b.fdroid.v1.domain.Version v1Version) {
        Version roomVersion = versionRepository.findForPackageNameAndVersionCode(repoId, packageName, v1Version.getVersionCode());
        if (roomVersion == null) {
            App app = appRepository.findByRepoIdAndPackageName(repoId, packageName);
            if (app != null) {
                roomVersion = new Version();
                roomVersion.appId = app.id;
                VersionCommon.copyCommon(roomVersion, v1Version);
                versionRepository.insert(roomVersion);
            }
        } else {
            VersionCommon.copyCommon(roomVersion, v1Version);
            versionRepository.update(roomVersion);
        }

        return roomVersion;
    }
}
