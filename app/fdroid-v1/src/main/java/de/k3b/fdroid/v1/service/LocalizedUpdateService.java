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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.AppForSearch;
import de.k3b.fdroid.domain.Locale;
import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.domain.common.LocalizedCommon;
import de.k3b.fdroid.domain.common.PojoCommon;
import de.k3b.fdroid.domain.interfaces.LocaleRepository;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class LocalizedUpdateService {
    private final LocalizedRepository localizedRepository;
    private final LocaleRepository localeRepository;

    Map<Integer, Locale> id2Locale = null;
    Map<String, Locale> code2Locale = null;

    public LocalizedUpdateService(LocalizedRepository localizedRepository, LocaleRepository localeRepository) {
        this.localizedRepository = localizedRepository;
        this.localeRepository = localeRepository;
    }

    public LocalizedUpdateService init() {
        List<Locale> locales = localeRepository.findAll();
        id2Locale = new HashMap<>();
        code2Locale = new HashMap<>();

        for (Locale locale : locales) {
            init(locale);
        }

        return this;
    }

    private void init(Locale locale) {
        id2Locale.put(locale.getId(), locale);
        code2Locale.put(locale.getCode(), locale);
    }

    private String getLocaleCode(int localeId) {
        Locale locale = (localeId == 0) ? null : id2Locale.get(localeId);
        return (locale == null) ? null : locale.getCode();
    }

    private int getOrCreateLocaleId(String localeCode) {
        if (localeCode != null) {
            Locale locale = code2Locale.get(localeCode);
            if (locale == null) {
                // create on demand
                locale = new Locale();
                locale.setCode(localeCode);
                localeRepository.insert(locale);
                init(locale);
            }
            return locale.getId();
        }
        return 0;
    }

    public List<Localized> update(int appId, AppForSearch roomApp, Map<String, de.k3b.fdroid.v1.domain.Localized> v1LocalizedMap) {
        List<Localized> roomLocalizedList = localizedRepository.findByAppId(appId);

        StringBuilder name = new StringBuilder();
        StringBuilder summary = new StringBuilder();
        StringBuilder description = new StringBuilder();
        StringBuilder whatsNew = new StringBuilder();

        deleteRemoved(roomLocalizedList, v1LocalizedMap);
        for (Map.Entry<String, de.k3b.fdroid.v1.domain.Localized> v1Entry : v1LocalizedMap.entrySet()) {
            String language = v1Entry.getKey();
            int localeId = getOrCreateLocaleId(language);
            de.k3b.fdroid.v1.domain.Localized v1Localized = v1Entry.getValue();
            Localized roomLocalized = findByLocaleId(roomLocalizedList, localeId);
            if (roomLocalized == null) {
                roomLocalized = new Localized();
                roomLocalized.setAppId(appId);
                roomLocalized.setLocaleId(localeId);
                LocalizedCommon.copyCommon(roomLocalized, v1Localized);
                localizedRepository.insert(roomLocalized);
                roomLocalizedList.add(roomLocalized);
            } else {
                LocalizedCommon.copyCommon(roomLocalized, v1Localized);
                localizedRepository.update(roomLocalized);
            }

            if (roomApp != null) {
                add("name", PojoCommon.MAX_LEN_AGGREGATED, roomApp, name, language, v1Localized.getName(), " | ");
                add("summary", PojoCommon.MAX_LEN_AGGREGATED, roomApp, summary, language, v1Localized.getSummary(), "\n");
                add("description", PojoCommon.MAX_LEN_AGGREGATED_DESCRIPTION, roomApp, description, language, v1Localized.getDescription(), "\n\n------------\n\n");
                add("whatsNew", PojoCommon.MAX_LEN_AGGREGATED, roomApp, whatsNew, language, v1Localized.getWhatsNew(), "\n\n------------\n\n");
            }
        }
        if (roomApp != null) {
            roomApp.setSearchName(name.toString());
            roomApp.setSearchSummary(summary.toString());
            roomApp.setSearchDescription(description.toString());
            roomApp.setSearchWhatsNew(whatsNew.toString());
        }
        return roomLocalizedList;

    }

    private void add(String fieldName, int maxLen, App roomApp, StringBuilder dest, String language, String src, String seperator) {
        if (src != null && src.length() > 0 && !dest.toString().contains(src)) {
            if (dest.length() > 0) {
                dest.append(seperator);
            }
            dest.append(language).append(": ").append(src);
            if (dest.length() > maxLen) {
                throw new IllegalArgumentException(fieldName + " > " + maxLen +
                        " in " + roomApp.getPackageName() +
                        ": ('" + dest + "')\n\t" + roomApp);
            }
        }
    }

    private Localized findByLocaleId(List<Localized> roomLocalizedList, int localeId) {
        for (Localized l : roomLocalizedList) {
            if (l.getLocaleId() == localeId) return l;
        }
        return null;
    }

    private void deleteRemoved(List<Localized> roomLocalizedList, Map<String, de.k3b.fdroid.v1.domain.Localized> v1LocalizedMap) {
        for (int i = roomLocalizedList.size() - 1; i >= 0; i--) {
            Localized roomLocalized = roomLocalizedList.get(i);
            String localeCode = getLocaleCode(roomLocalized.getLocaleId());
            if (localeCode != null && !v1LocalizedMap.containsKey(localeCode)) {
                localizedRepository.delete(roomLocalized);
                roomLocalizedList.remove(i);
            }
        }
    }
}
