/*
 * Copyright (c) 2023 by k3b.
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

package de.k3b.fdroid.v2domain.service;

import static de.k3b.fdroid.domain.entity.common.EntityCommon.ifNotNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.common.AppCommon;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.v2domain.entity.packagev2.ManifestV2;
import de.k3b.fdroid.v2domain.entity.packagev2.MetadataV2;
import de.k3b.fdroid.v2domain.entity.packagev2.PackageV2;
import de.k3b.fdroid.v2domain.entity.packagev2.PackageVersionV2;
import de.k3b.fdroid.v2domain.entity.repo.FileV2;

public class V2AppUpdateService {
    private final AppRepository appRepository;
    @Nullable
    private final V2LocalizedUpdateService v2LocalizedUpdateService;

    public V2AppUpdateService(AppRepository appRepository, @Nullable V2LocalizedUpdateService v2LocalizedUpdateService) {
        this.appRepository = appRepository;
        this.v2LocalizedUpdateService = v2LocalizedUpdateService;
    }

    private static String getIconName(MetadataV2 metadata, String locale) {
        Map<String, FileV2> iconMap = LanguageService.getCanonicalLocale(metadata.getIcon());
        FileV2 icon = (iconMap == null) ? null : iconMap.get(LanguageService.FALLBACK_LOCALE);
        return MetadataV2.getIconName(icon);
    }

    public App update(@NotNull String packageName, @NotNull PackageV2 packageV2) {
        App app = getOrCreateApp(packageName);
        update(app, packageV2);
        appRepository.save(app);

        return app;
        // if (v2LocalizedUpdateService != null) v2LocalizedUpdateService.
    }

    private App update(App app, PackageV2 packageV2) {
        Map<String, PackageVersionV2> versions = packageV2.getVersions();
        PackageVersionV2 version = (versions == null || versions.isEmpty()) ? null : versions.entrySet().iterator().next().getValue();
        update(app, packageV2.getMetadata(), version);
        if (v2LocalizedUpdateService != null) v2LocalizedUpdateService.update(app, packageV2);
        return app;
    }

    protected App update(App app, MetadataV2 metadata, PackageVersionV2 version) {
        if (metadata != null) {
            AppCommon.copyCommon(app, metadata, null);

            String iconName = getIconName(metadata, LanguageService.FALLBACK_LOCALE);
            if (iconName != null) {
                app.setIcon(iconName);
            }
        }

        ManifestV2 versionManifest = (version == null) ? null : version.getManifest();
        if (versionManifest != null) {
            app.setSuggestedVersionName(ifNotNull(versionManifest.getVersionName(), app.getSuggestedVersionName()));

            long versionCode = versionManifest.getVersionCode();
            if (versionCode != 0) app.setSuggestedVersionCode("" + versionCode);
        }
        return app;
    }

    private App getOrCreateApp(String packageName) {
        App app = appRepository.findByPackageName(packageName);
        if (app == null) {
            app = new App(packageName);
            appRepository.insert(app);
            if (app.getAppId() == 0)
                throw new RuntimeException("appRepository.insert(app) did not generate appId");
        }
        return app;
    }
}
