/*
 * Copyright (c) 2022-2023 by k3b.
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

package de.k3b.fdroid.v1domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.entity.common.AppCommon;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.util.ExceptionUtils;
import de.k3b.fdroid.domain.util.StringUtil;
import de.k3b.fdroid.v1domain.entity.App;
import de.k3b.fdroid.v1domain.entity.UpdateService;

/**
 * {@link UpdateService} that updates {@link de.k3b.fdroid.domain.entity.App}
 * from {@link App} using a {@link AppRepository}
 */
public class AppUpdateService implements UpdateService, ProgressObservable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

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

    public de.k3b.fdroid.domain.entity.App update(int repoId, App v1App)
            throws PersistenceException {
        String packageName = v1App.getPackageName();
        de.k3b.fdroid.domain.entity.App roomApp = null;
        try {
            roomApp = appRepository.findByPackageName(packageName);
            String progressChar = ".";
            if (roomApp == null) {
                progressChar = "+";
                roomApp = new de.k3b.fdroid.domain.entity.App();
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
        } catch (Exception ex) {
            // thrown by j2se hibernate database problem
            // hibernate DataIntegrityViolationException -> NestedRuntimeException
            // hibernate org.hibernate.exception.DataException inherits from PersistenceException
            String message = "PersistenceException in " + getClass().getSimpleName() + ".update(repo="
                    + repoId + ", app("
                    + (roomApp == null ? "?" : roomApp.getAppId())
                    + ")=" + packageName + ") "
                    + ExceptionUtils.getParentCauseMessage(ex, PersistenceException.class);
            LOGGER.error(message + "\n\tv1App=" + v1App, ex);
            throw new PersistenceException(message, ex);
        }

    }

    @Override
    public void setProgressObserver(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }
}
