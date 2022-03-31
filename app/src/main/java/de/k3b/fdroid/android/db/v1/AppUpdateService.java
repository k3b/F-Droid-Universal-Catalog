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

package de.k3b.fdroid.android.db.v1;

import org.fdroid.model.AppCommon;

import de.k3b.fdroid.android.db.AppDao;
import de.k3b.fdroid.android.model.App;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class AppUpdateService {
    private final AppDao appDao;
    private final AppCategoryUpdateService appCategoryUpdateService;
    private final LocalizedUpdateService localizedUpdateService;

    public AppUpdateService(AppDao appDao,
                            AppCategoryUpdateService appCategoryUpdateService,
                            LocalizedUpdateService localizedUpdateService) {
        this.appDao = appDao;
        this.appCategoryUpdateService = appCategoryUpdateService;
        this.localizedUpdateService = localizedUpdateService;
    }

    public App update(int repoId, org.fdroid.model.v1.App v1App) {
        App roomApp = appDao.findByPackageName(repoId, v1App.getPackageName());
        if (roomApp == null) {
            roomApp = new App();
            roomApp.repoId = repoId;
            AppCommon.copyCommon(roomApp, v1App);
            appDao.insertAll(roomApp);
        } else {
            AppCommon.copyCommon(roomApp, v1App);
            appDao.updateAll(roomApp);
        }
        if (appCategoryUpdateService != null)
            appCategoryUpdateService.update(roomApp.id, v1App.getCategories());
        if (localizedUpdateService != null)
            localizedUpdateService.update(repoId, v1App.getLocalized());
        return roomApp;
    }
}
