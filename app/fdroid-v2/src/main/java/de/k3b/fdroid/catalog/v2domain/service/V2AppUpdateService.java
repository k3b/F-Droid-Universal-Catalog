/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of de.k3b.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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

package de.k3b.fdroid.catalog.v2domain.service;

import static de.k3b.fdroid.domain.entity.common.EntityCommon.ifNotNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.catalog.service.AppCategoryUpdateService;
import de.k3b.fdroid.catalog.v2domain.entity.V2IconUtil;
import de.k3b.fdroid.catalog.v2domain.entity.packagev2.V2App;
import de.k3b.fdroid.catalog.v2domain.entity.packagev2.V2AppInfo;
import de.k3b.fdroid.catalog.v2domain.entity.packagev2.V2Manifest;
import de.k3b.fdroid.catalog.v2domain.entity.packagev2.V2PackageVersion;
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2File;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.common.AppCommon;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.util.ExceptionUtils;
import de.k3b.fdroid.domain.util.StringUtil;

public class V2AppUpdateService implements ProgressObservable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    private static final int PROGRESS_INTERVALL = 100;

    @Nullable
    private final AppRepository appRepository;
    @Nullable
    private final V2LocalizedUpdateService v2LocalizedUpdateService;
    @Nullable
    private final AppCategoryUpdateService appCategoryUpdateService;

    private ProgressObserver progressObserver = null;
    private int progressCounter = 0;
    private int progressCountdown = 0;

    public V2AppUpdateService(
            @Nullable AppRepository appRepository,
            @Nullable V2LocalizedUpdateService v2LocalizedUpdateService,
            @Nullable AppCategoryUpdateService appCategoryUpdateService) {
        this.appRepository = appRepository;
        this.v2LocalizedUpdateService = v2LocalizedUpdateService;
        this.appCategoryUpdateService = appCategoryUpdateService;

    }

    private static String getIconName(V2AppInfo metadata, String locale) {
        Map<String, V2File> iconMap = LanguageService.getCanonicalLocale(metadata.getIconMap());
        V2File icon = (iconMap == null) ? null : iconMap.get(locale);
        return V2IconUtil.getIconName(icon);
    }

    public V2AppUpdateService init() {
        if (v2LocalizedUpdateService != null) this.v2LocalizedUpdateService.init();
        if (appCategoryUpdateService != null) this.appCategoryUpdateService.init();
        // if (localizedUpdateService != null) this.localizedUpdateService.init();
        progressCounter = 0;
        progressCountdown = 0;
        return this;
    }

    public App update(int repoId, @NotNull String packageName, @NotNull V2App v2App) {
        Objects.requireNonNull(appRepository, "appRepository is not initialized");
        App roomApp = null;
        try {
            App roomApp1 = appRepository.findByPackageName(packageName);
            String progressChar = ".";
            if (roomApp1 == null) {
                progressChar = "+";
                roomApp1 = new App(packageName);
                appRepository.insert(roomApp1);
                if (roomApp1.getAppId() == 0)
                    throw new RuntimeException("appRepository.insert(roomApp) did not generate appId");
            }
            roomApp = roomApp1;
            update(roomApp, v2App);
            appRepository.save(roomApp);
            updateDetails(repoId, roomApp, v2App, progressChar);

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
            LOGGER.error(message + "\n\tV2App=" + v2App, ex);
            throw new PersistenceException(message, ex);
        }
    }

    protected App update(App roomApp, V2App v2App) {
        Map<String, V2PackageVersion> versions = v2App.getVersions();
        V2PackageVersion version = (versions == null || versions.isEmpty()) ? null : versions.entrySet().iterator().next().getValue();
        update(roomApp, v2App.getMetadata(), version);
        return roomApp;
    }

    // Entrypoint for unittest
    protected App update(App roomApp, V2AppInfo metadata, V2PackageVersion version) {
        if (metadata != null) {
            AppCommon.copyCommon(roomApp, metadata, null);

            String iconName = getIconName(metadata, LanguageService.FALLBACK_LOCALE);
            if (iconName != null) {
                roomApp.setIcon(iconName);
            }
            roomApp.setSearchCategory(StringUtil.toCsvStringOrNull(metadata.getCategories()));
        }

        V2Manifest versionManifest = (version == null) ? null : version.getManifest();
        if (versionManifest != null) {
            roomApp.setSuggestedVersionName(ifNotNull(versionManifest.getVersionName(), roomApp.getSuggestedVersionName()));

            long versionCode = versionManifest.getVersionCode();
            if (versionCode != 0) roomApp.setSuggestedVersionCode("" + versionCode);
        }
        return roomApp;
    }

    protected void updateDetails(int repoId, App roomApp, V2App v2App, String progressChar) {
        progressCounter++;
        if (progressObserver != null && (--progressCountdown) <= 0) {
            progressObserver.onProgress(progressCounter, progressChar, roomApp.getPackageName());
            progressCountdown = PROGRESS_INTERVALL;
        }

        if (v2LocalizedUpdateService != null)
            v2LocalizedUpdateService.update(repoId, roomApp, v2App);
    }


    @Override
    public void setProgressObserver(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }
}
