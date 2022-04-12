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
import de.k3b.fdroid.domain.common.AppCommon;
import de.k3b.fdroid.domain.interfaces.AppRepository;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class AppUpdateService {
    private final AppRepository appRepository;
    private final AppCategoryUpdateService appCategoryUpdateService;
    private final LocalizedUpdateService localizedUpdateService;

    public AppUpdateService(AppRepository appRepository,
                            AppCategoryUpdateService appCategoryUpdateService,
                            LocalizedUpdateService localizedUpdateService) {
        this.appRepository = appRepository;
        this.appCategoryUpdateService = appCategoryUpdateService;
        this.localizedUpdateService = localizedUpdateService;

    }

    public void init() {
        this.appCategoryUpdateService.init();
        this.localizedUpdateService.init();
    }

    public App update(int repoId, de.k3b.fdroid.v1.domain.App v1App) {
        App roomApp = appRepository.findByRepoIdAndPackageName(repoId, v1App.getPackageName());
        if (roomApp == null) {
            roomApp = new App();
            roomApp.repoId = repoId;
            AppCommon.copyCommon(roomApp, v1App);
            appRepository.insert(roomApp);
        } else {
            AppCommon.copyCommon(roomApp, v1App);
            appRepository.update(roomApp);
        }
        if (appCategoryUpdateService != null)
            appCategoryUpdateService.update(roomApp.id, v1App.getCategories());
        if (localizedUpdateService != null)
            localizedUpdateService.update(roomApp.id, v1App.getLocalized());
        return roomApp;
    }
}
