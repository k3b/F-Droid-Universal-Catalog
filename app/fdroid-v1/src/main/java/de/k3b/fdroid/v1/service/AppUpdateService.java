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

import de.k3b.fdroid.domain.common.AppCommon;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.util.StringUtil;
import de.k3b.fdroid.v1.domain.UpdateService;

/**
 * {@link UpdateService} that updates {@link de.k3b.fdroid.domain.App}
 * from {@link de.k3b.fdroid.v1.domain.App} using a {@link AppRepository}
 */
public class AppUpdateService implements UpdateService, ProgressObservable {
    private static final int PROGRESS_INTERVALL = 100;

    private final AppRepository appRepository;
    private final AppCategoryUpdateService appCategoryUpdateService;
    private final LocalizedUpdateService localizedUpdateService;

    private ProgressObserver progressObserver = null;
    private int progressCounter = 0;
    private int progressCountdown = 0;

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
        progressCounter = 0;
        progressCountdown = 0;
    }

    public de.k3b.fdroid.domain.App update(int repoId, de.k3b.fdroid.v1.domain.App v1App) {
        de.k3b.fdroid.domain.App roomApp = appRepository.findByPackageName(v1App.getPackageName());
        String progressChar = ".";
        if (roomApp == null) {
            progressChar = "+";
            roomApp = new de.k3b.fdroid.domain.App();
        }
        AppCommon.copyCommon(roomApp, v1App);
        roomApp.setSearchCategory(StringUtil.toCsvStringOrNull(v1App.getCategories()));
        appRepository.save(roomApp);

        progressCounter++;
        if (progressObserver != null && (--progressCountdown) <= 0) {
            progressObserver.onProgress(progressCounter, progressChar, roomApp.getPackageName());
            progressCountdown = PROGRESS_INTERVALL;
        }

        if (appCategoryUpdateService != null)
            appCategoryUpdateService.update(roomApp.getId(), v1App.getCategories());
        if (localizedUpdateService != null) {
            localizedUpdateService.update(repoId, roomApp.getId(), roomApp, v1App.getLocalized());
            appRepository.update(roomApp);
        }
        return roomApp;
    }

    @Override
    public void setProgressObserver(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }
}
