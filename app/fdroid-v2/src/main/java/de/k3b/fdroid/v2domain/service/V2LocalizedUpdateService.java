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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.util.Java8Util;
import de.k3b.fdroid.domain.util.StringUtil;
import de.k3b.fdroid.v2domain.entity.packagev2.MetadataV2;
import de.k3b.fdroid.v2domain.entity.packagev2.PackageV2;
import de.k3b.fdroid.v2domain.entity.packagev2.PackageVersionV2;
import de.k3b.fdroid.v2domain.entity.packagev2.Screenshots;
import de.k3b.fdroid.v2domain.entity.repo.FileV2;

public class V2LocalizedUpdateService {
    public static final String FALLBACK_LOCALE = LanguageService.FALLBACK_LOCALE;
    private final LocalizedRepository localizedRepository;

    public V2LocalizedUpdateService(LocalizedRepository localizedRepository) {
        this.localizedRepository = localizedRepository;
    }

    // List<FileV2>, List<String>
    private static Map<String, String> getFileNameListMap(Map<String, List<FileV2>> map) {
        if (map == null) return null;
        Map<String, String> result = new TreeMap<>();
        for (Map.Entry<String, List<FileV2>> entry : map.entrySet()) {
            result.put(entry.getKey(), StringUtil.toCsvStringOrNull(Java8Util.reduce(entry.getValue(), f -> f.getName())));
        }
        return result;
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

    public void update(App app, PackageV2 packageV2) {
        update(app, packageV2.getMetadata(), getLastVersion(packageV2.getVersions()));
    }

    private PackageVersionV2 getLastVersion(Map<String, PackageVersionV2> versions) {
        if (versions == null || versions.isEmpty()) return null;
        return versions.entrySet().iterator().next().getValue();
    }

    private void update(@NotNull App app, @NotNull MetadataV2 metadata,
                        @Nullable PackageVersionV2 lastVersion) {
        List<Localized> localizedList = localizedRepository.findByAppId(app.getAppId());
        update(app, toLocalizedMap(localizedList), metadata, lastVersion);
    }

    protected void update(App app, Map<String, Localized> localizedMap,
                          MetadataV2 metadata, PackageVersionV2 lastVersion) {
        Converter converter = new Converter(localizedMap, app);
        converter.convert(metadata.getName(), l -> l.getName(), (l, v) -> l.setName(v));
        converter.convert(metadata.getDescription(), l -> l.getDescription(), (l, v) -> l.setDescription(v));
        converter.convert(metadata.getSummary(), l -> l.getSummary(), (l, v) -> l.setSummary(v));

        converter.convert(metadata.getVideo(), l -> l.getVideo(), (l, v) -> l.setVideo(v));

        converter.convert(Java8Util.getKeyValueMap(metadata.getIcon(), f -> MetadataV2.getIconName(f)),
                l -> l.getIcon(), (l, v) -> l.setIcon(v));

        Screenshots screenshots = metadata.getScreenshots();
        if (screenshots != null) {
            Map<String, List<FileV2>> phoneScreenshots = screenshots.getPhone();
            if (phoneScreenshots != null) {
                converter.convert(getFileNameListMap(phoneScreenshots)
                        , l -> l.getPhoneScreenshots(), (l, v) -> l.setPhoneScreenshots(v));
            }
        }

        if (lastVersion != null) {
            converter.convert(lastVersion.getWhatsNew(), l -> l.getWhatsNew(), (l, v) -> l.setWhatsNew(v));
        }
    }

    private Localized getOrCreateLocalized(
            String locale, Map<String, Localized> localizedMap, App app) {
        Localized result = localizedMap.get(locale);
        if (result == null) {
            result = new Localized(app.getAppId(), locale);
            localizedMap.put(result.getLocaleId(), result);
        }
        return result;
    }

    private class Converter {
        private final Map<String, Localized> localizedMap;
        private final App app;
        private final Localized fallbackLocalized;

        public Converter(Map<String, Localized> localizedMap,
                         @NotNull App app) {
            this.localizedMap = localizedMap;
            this.app = app;
            this.fallbackLocalized = getOrCreateLocalized(FALLBACK_LOCALE, localizedMap, app);
        }

        public void convert(Map<String, String> localeMap,
                            Java8Util.Getter<Localized, String> getter, Java8Util.Setter<Localized, String> setter) {
            localeMap = LanguageService.getCanonicalLocale(localeMap);
            if (localeMap != null) {
                for (Map.Entry<String, String> entry : localeMap.entrySet()) {
                    Localized localized = getOrCreateLocalized(entry.getKey(), localizedMap, app);

                    String value = entry.getValue();
                    if (value != null && isNotFallbackValue(value, getter)) {
                        setter.set(localized, value);
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
