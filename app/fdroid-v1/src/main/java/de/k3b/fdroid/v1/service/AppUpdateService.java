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

import java.lang.reflect.Constructor;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.common.AppCommon;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.ProgressListener;
import de.k3b.fdroid.util.StringUtil;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class AppUpdateService<APP extends App> {
    private final AppRepository<APP> appRepository;
    private final AppCategoryUpdateService appCategoryUpdateService;
    private final LocalizedUpdateService localizedUpdateService;
    private final ProgressListener progressListener;

    private Class<?> appClass = App.class;

    public AppUpdateService(AppRepository<APP> appRepository,
                            AppCategoryUpdateService appCategoryUpdateService,
                            LocalizedUpdateService localizedUpdateService,
                            ProgressListener progressListener) {
        this.appRepository = appRepository;
        this.appCategoryUpdateService = appCategoryUpdateService;
        this.localizedUpdateService = localizedUpdateService;

        this.progressListener = progressListener;
    }

    public void init() {
        this.appCategoryUpdateService.init();
        this.localizedUpdateService.init();
    }

    /**
     * must be used if APP != App
     */
    public AppUpdateService<APP> setAppClass(Class<APP> appClass) {
        this.appClass = appClass;
        return this;
    }

    protected APP createApp() {
        try {
            Constructor<?> constructor = appClass.getConstructor();
            return (APP) constructor.newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot create App with default constructor", e);
        }
    }

    public APP update(int repoId, de.k3b.fdroid.v1.domain.App v1App) {
        APP roomApp = appRepository.findByRepoIdAndPackageName(repoId, v1App.getPackageName());
        if (roomApp == null) {
            roomApp = createApp();
            roomApp.setRepoId(repoId);
            AppCommon.copyCommon(roomApp, v1App);
            roomApp.setSearchCategory(StringUtil.toString(v1App.getCategories(), ","));
            appRepository.insert(roomApp);
            if (progressListener != null) {
                progressListener.onProgress("+", roomApp.getPackageName());
            }
        } else {
            AppCommon.copyCommon(roomApp, v1App);
            roomApp.setSearchCategory(StringUtil.toString(v1App.getCategories(), ","));
            appRepository.update(roomApp);
            if (progressListener != null) {
                progressListener.onProgress(".", roomApp.getPackageName());
            }
        }
        if (appCategoryUpdateService != null)
            appCategoryUpdateService.update(roomApp.getId(), v1App.getCategories());
        if (localizedUpdateService != null) {
            localizedUpdateService.update(roomApp.getId(), roomApp, v1App.getLocalized());
            appRepository.update(roomApp);
        }
        return roomApp;
    }
}
