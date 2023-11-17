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
import de.k3b.fdroid.v2domain.entity.V2IconUtil;
import de.k3b.fdroid.v2domain.entity.packagev2.V2App;
import de.k3b.fdroid.v2domain.entity.packagev2.V2AppInfo;
import de.k3b.fdroid.v2domain.entity.packagev2.V2PackageVersion;
import de.k3b.fdroid.v2domain.entity.packagev2.V2Screenshots;
import de.k3b.fdroid.v2domain.entity.repo.V2File;

public class V2LocalizedUpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);
    public static final String FALLBACK_LOCALE = LanguageService.FALLBACK_LOCALE;
    @Nullable
    private final LocalizedRepository localizedRepository;
    @NotNull
    private final LanguageService languageService;
    @NotNull
    private final LocalizedService localizedService;
    @NotNull
    private final V2FixPhoneScreenshotService v2FixPhoneScreenshotService;

    public V2LocalizedUpdateService(@Nullable LocalizedRepository localizedRepository,
                                    @NotNull LanguageService languageService,
                                    @NotNull V2FixPhoneScreenshotService v2FixPhoneScreenshotService) {
        this.localizedRepository = localizedRepository;
        this.languageService = languageService;
        this.localizedService = new LocalizedService(languageService);
        this.v2FixPhoneScreenshotService = v2FixPhoneScreenshotService;
    }

    // List<V2File>, List<String>
    private static Map<String, String> getFileNameListMap(Map<String, List<V2File>> map) {
        if (map == null) return null;
        Map<String, String> result = new TreeMap<>();
        for (Map.Entry<String, List<V2File>> entry : map.entrySet()) {
            result.put(entry.getKey(), StringUtil.toCsvStringOrNull(Java8Util.reduce(entry.getValue(),
                    V2File::getName)));
        }
        return result;
    }

    public V2LocalizedUpdateService init() {
        languageService.init();
        v2FixPhoneScreenshotService.init();
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

    private V2PackageVersion getLastVersion(Map<String, V2PackageVersion> versions) {
        if (versions == null || versions.isEmpty()) return null;
        return versions.entrySet().iterator().next().getValue();
    }

    private List<Localized> update(
            int repoId,
            @NotNull App roomApp, @NotNull V2AppInfo v2AppInfo,
            @Nullable V2PackageVersion lastVersion) {
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

            roomLocalizedList = update(roomApp, toLocalizedMap(roomLocalizedList), v2AppInfo, lastVersion, exceptionContext);

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

            LOGGER.error(message + "\n\tV2AppInfo=" + v2AppInfo
                    + ",\n\tV2PackageVersion=" + lastVersion, ex);
            throw new PersistenceException(message.toString(), ex);
        }
    }

    // Entrypoint for unittest
    protected List<Localized> update(App roomApp, Map<String, Localized> localizedMap,
                                     V2AppInfo v2AppInfo, V2PackageVersion lastVersion,
                                     Java8Util.OutParam<Localized> exceptionContext) {
        Converter converter = new Converter(localizedMap, roomApp, exceptionContext);
        converter.convert(v2AppInfo.getName(), Localized::getName, Localized::setName);
        converter.convert(v2AppInfo.getDescription(), Localized::getDescription, Localized::setDescription);
        converter.convert(v2AppInfo.getSummary(), Localized::getSummary, Localized::setSummary);

        converter.convert(v2AppInfo.getVideo(), Localized::getVideo, Localized::setVideo);

        converter.convert(Java8Util.getKeyValueMap(v2AppInfo.getIcon(), V2IconUtil::getIconName),
                Localized::getIcon, Localized::setIcon);

        V2Screenshots screenshots = v2AppInfo.getScreenshots();
        if (screenshots != null) {
            Map<String, List<V2File>> phoneScreenshots = screenshots.getPhone();
            if (phoneScreenshots != null) {
                v2FixPhoneScreenshotService.fix(screenshots);
                converter.convert(getFileNameListMap(phoneScreenshots)
                        , Localized::getPhoneScreenshots, Localized::setPhoneScreenshots);
                converter.convert(screenshots.getPhoneDir()
                        , Localized::getPhoneScreenshotDir, Localized::setPhoneScreenshotDir);
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
