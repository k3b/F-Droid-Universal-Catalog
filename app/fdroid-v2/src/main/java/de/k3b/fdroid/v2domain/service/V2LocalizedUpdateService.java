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

package de.k3b.fdroid.v2domain.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.service.LocalizedService;
import de.k3b.fdroid.domain.util.ExceptionUtils;
import de.k3b.fdroid.domain.util.Java8Util;
import de.k3b.fdroid.domain.util.StringUtil;
import de.k3b.fdroid.v2domain.entity.packagev2.MetadataV2;
import de.k3b.fdroid.v2domain.entity.packagev2.PackageVersionV2;
import de.k3b.fdroid.v2domain.entity.packagev2.Screenshots;
import de.k3b.fdroid.v2domain.entity.packagev2.V2App;
import de.k3b.fdroid.v2domain.entity.repo.FileV2;

public class V2LocalizedUpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);
    public static final String FALLBACK_LOCALE = LanguageService.FALLBACK_LOCALE;
    @Nullable
    private final LocalizedRepository localizedRepository;
    @NotNull
    private final LanguageService languageService;
    @NotNull
    private final LocalizedService localizedService;

    public V2LocalizedUpdateService(@Nullable LocalizedRepository localizedRepository,
                                    @NotNull LanguageService languageService) {
        this.localizedRepository = localizedRepository;
        this.languageService = languageService;
        this.localizedService = new LocalizedService(languageService);
    }

    // List<FileV2>, List<String>
    private static Map<String, String> getFileNameListMap(Map<String, List<FileV2>> map) {
        if (map == null) return null;
        Map<String, String> result = new TreeMap<>();
        for (Map.Entry<String, List<FileV2>> entry : map.entrySet()) {
            result.put(entry.getKey(), StringUtil.toCsvStringOrNull(Java8Util.reduce(entry.getValue(),
                    FileV2::getName)));
        }
        return result;
    }

    public V2LocalizedUpdateService init() {
        languageService.init();
        return this;
    }

    private static Map<String, Localized> toLocalizedMap(List<Localized> localizedList) {
        Map<String, Localized> localizedMap = new TreeMap<>();
        if (localizedList != null) {
            for (Localized localized : localizedList) {
                localizedMap.put(localized.getLocaleId(), localized);
            }
        }
        return localizedMap;
    }

    public List<Localized> update(
            int repoId, App roomApp, V2App v2App) {
        return update(repoId, roomApp, v2App.getMetadata(), getLastVersion(v2App.getVersions()));
    }

    private PackageVersionV2 getLastVersion(Map<String, PackageVersionV2> versions) {
        if (versions == null || versions.isEmpty()) return null;
        return versions.entrySet().iterator().next().getValue();
    }

    private List<Localized> update(
            int repoId,
            @NotNull App roomApp, @NotNull MetadataV2 metadata,
            @Nullable PackageVersionV2 lastVersion) {
        Java8Util.OutParam<Localized> exceptionContext = new Java8Util.OutParam<>(null);

        String packageName = null;
        int appId = 0;
        if (roomApp != null) {
            packageName = roomApp.getPackageName();
            appId = roomApp.getAppId();
        }

        try {
            List<Localized> roomLocalizedList = (localizedRepository == null)
                    ? new ArrayList<>()
                    : localizedRepository.findByAppId(roomApp.getAppId());

            roomLocalizedList = update(roomApp, toLocalizedMap(roomLocalizedList), metadata, lastVersion, exceptionContext);

            List<Localized> deleted = localizedService.deleteHidden(roomLocalizedList);
            if (roomApp != null) {
                if (repoId != 0 && (roomApp.getResourceRepoId() == null)) {
                    roomApp.setResourceRepoId(repoId);
                }
                localizedService.recalculateSearchFields(repoId, roomApp, roomLocalizedList);
            }
            if (localizedRepository != null) localizedRepository.deleteAll(deleted);
            return roomLocalizedList;
        } catch (Exception ex) {
            Localized roomLocalized = exceptionContext.getValue();

            // thrown by j2se hibernate database problem
            // hibernate DataIntegrityViolationException -> NestedRuntimeException
            // hibernate org.hibernate.exception.DataException inherits from PersistenceException
            StringBuilder message = new StringBuilder();
            message.append("Exception in ").append(getClass().getSimpleName())
                    .append(".update(repo=").append(repoId)
                    .append(", app(").append(appId).append(")=").append(packageName);
            if (roomLocalized != null) {
                message.append(", localized(")
                        .append(roomLocalized.getId()).append(",")
                        .append(roomLocalized.getLocaleId()).append(")");
            }
            message.append(") ").append(ExceptionUtils.getParentCauseMessage(ex, PersistenceException.class));

            LOGGER.error(message + "\n\tMetadataV2=" + metadata
                    + ",\n\tPackageVersionV2=" + lastVersion, ex);
            throw new PersistenceException(message.toString(), ex);
        }
    }

    // Entrypoint for unittest
    protected List<Localized> update(App roomApp, Map<String, Localized> localizedMap,
                                     MetadataV2 metadata, PackageVersionV2 lastVersion,
                                     Java8Util.OutParam<Localized> exceptionContext) {
        Converter converter = new Converter(localizedMap, roomApp, exceptionContext);
        converter.convert(metadata.getName(), Localized::getName, Localized::setName);
        converter.convert(metadata.getDescription(), Localized::getDescription, Localized::setDescription);
        converter.convert(metadata.getSummary(), Localized::getSummary, Localized::setSummary);

        converter.convert(metadata.getVideo(), Localized::getVideo, Localized::setVideo);

        converter.convert(Java8Util.getKeyValueMap(metadata.getIcon(), f -> MetadataV2.getIconName(f)),
                Localized::getIcon, Localized::setIcon);

        Screenshots screenshots = metadata.getScreenshots();
        if (screenshots != null) {
            Map<String, List<FileV2>> phoneScreenshots = screenshots.getPhone();
            if (phoneScreenshots != null) {
                converter.convert(getFileNameListMap(phoneScreenshots)
                        , Localized::getPhoneScreenshots, Localized::setPhoneScreenshots);
            }
        }

        if (lastVersion != null) {
            converter.convert(lastVersion.getWhatsNew(), Localized::getWhatsNew, Localized::setWhatsNew);
        }
        return new ArrayList<>(localizedMap.values());
    }

    private Localized getOrCreateLocalized(
            String locale, Map<String, Localized> localizedMap, App roomApp) {
        Localized result = localizedMap.get(locale);
        if (result == null) {
            languageService.getOrCreateLocaleByCode(locale);
            if (!languageService.isHidden(locale)) {
                result = new Localized(roomApp.getAppId(), locale);
                localizedMap.put(result.getLocaleId(), result);
            }
        }
        return result;
    }

    private class Converter {
        private final Map<String, Localized> localizedMap;
        private final App app;
        private final Java8Util.OutParam<Localized> exceptionContext;
        private final Localized fallbackLocalized;

        public Converter(Map<String, Localized> localizedMap,
                         @NotNull App app, Java8Util.OutParam<Localized> exceptionContext) {
            this.localizedMap = localizedMap;
            this.app = app;
            this.exceptionContext = exceptionContext;
            this.fallbackLocalized = getOrCreateLocalized(FALLBACK_LOCALE, localizedMap, app);
        }

        public void convert(Map<String, String> localeMap,
                            Java8Util.Getter<Localized, String> getter, Java8Util.Setter<Localized, String> setter) {
            localeMap = LanguageService.getCanonicalLocale(localeMap);
            if (localeMap != null) {
                for (Map.Entry<String, String> entry : localeMap.entrySet()) {
                    Localized localized = getOrCreateLocalized(entry.getKey(), localizedMap, app);
                    exceptionContext.setValue(localized);
                    if (localized != null) {
                        String value = entry.getValue();
                        if (value != null && isNotFallbackValue(value, getter)) {
                            setter.set(localized, value);
                        }
                    }
                }
            }
        }

        private boolean isNotFallbackValue(String value, Java8Util.Getter<Localized, String> getter) {
            String fallbackValue = getter.get(fallbackLocalized);
            return (fallbackValue == null || !fallbackValue.equalsIgnoreCase(value));
        }
    }
}
