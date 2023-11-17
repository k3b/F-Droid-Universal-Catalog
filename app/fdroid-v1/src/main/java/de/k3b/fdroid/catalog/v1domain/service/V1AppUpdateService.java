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

import java.util.Objects;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.catalog.service.AppCategoryUpdateService;
import de.k3b.fdroid.catalog.v1domain.entity.IV1UpdateService;
import de.k3b.fdroid.catalog.v1domain.entity.V1App;
import de.k3b.fdroid.domain.entity.common.AppCommon;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.util.ExceptionUtils;
import de.k3b.fdroid.domain.util.StringUtil;

/**
 * {@link IV1UpdateService} that updates {@link de.k3b.fdroid.domain.entity.App}
 * from {@link V1App} using a {@link AppRepository}
 */
public class V1AppUpdateService implements IV1UpdateService, ProgressObservable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    private static final int PROGRESS_INTERVALL = 100;

    @Nullable
    private final AppRepository appRepository;
    @Nullable
    private final AppCategoryUpdateService appCategoryUpdateService;
    @Nullable
    private final V1LocalizedUpdateService v1LocalizedUpdateService;
    @Nullable
    private final V1FixLocaleService v1FixLocaleService;

    private ProgressObserver progressObserver = null;
    private int progressCounter = 0;
    private int progressCountdown = 0;

    public V1AppUpdateService(
            @Nullable AppRepository appRepository,
            @Nullable V1LocalizedUpdateService v1LocalizedUpdateService,
            @Nullable AppCategoryUpdateService appCategoryUpdateService,
            @Nullable V1FixLocaleService v1FixLocaleService) {
        this.appRepository = appRepository;
        this.appCategoryUpdateService = appCategoryUpdateService;
        this.v1LocalizedUpdateService = v1LocalizedUpdateService;
        this.v1FixLocaleService = v1FixLocaleService;
    }

    public V1AppUpdateService init() {
        if (appCategoryUpdateService != null) this.appCategoryUpdateService.init();
        if (v1LocalizedUpdateService != null) this.v1LocalizedUpdateService.init();
        progressCounter = 0;
        progressCountdown = 0;
        return this;
    }

    public de.k3b.fdroid.domain.entity.App update(int repoId, V1App v1App)
            throws PersistenceException {
        Objects.requireNonNull(appRepository, "appRepository is not initialized");
        String packageName = v1App.getPackageName();
        de.k3b.fdroid.domain.entity.App roomApp = null;
        try {
            roomApp = appRepository.findByPackageName(packageName);
            String progressChar = ".";
            if (roomApp == null) {
                progressChar = "+";
                roomApp = new de.k3b.fdroid.domain.entity.App();
            }
            if (v1FixLocaleService != null) {
                v1App = v1FixLocaleService.fix(v1App);
            }
            update(roomApp, v1App);
            appRepository.save(roomApp);

            updateDetails(repoId, roomApp, v1App, progressChar);
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

    // Entrypoint for unittest
    protected void update(de.k3b.fdroid.domain.entity.App roomApp, V1App v1App) {
        AppCommon.copyCommon(roomApp, v1App, v1App);
        String searchCategory = StringUtil.toCsvStringOrNull(v1App.getCategories());
        if (searchCategory != null) {
            roomApp.setSearchCategory(searchCategory);
        }
    }

    protected void updateDetails(int repoId, de.k3b.fdroid.domain.entity.App roomApp, V1App v1App, String progressChar) {
        progressCounter++;
        if (progressObserver != null && (--progressCountdown) <= 0) {
            progressObserver.onProgress(progressCounter, progressChar, roomApp.getPackageName());
            progressCountdown = PROGRESS_INTERVALL;
        }

        if (appCategoryUpdateService != null) {
            appCategoryUpdateService.update(roomApp.getId(), v1App.getCategories());
        }
        if (v1LocalizedUpdateService != null) {
            v1LocalizedUpdateService.update(repoId, roomApp.getId(), roomApp, v1App.getLocalized());
        }
        if (appRepository != null) {
            appRepository.update(roomApp);
        }
    }

    @Override
    public void setProgressObserver(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }
}
