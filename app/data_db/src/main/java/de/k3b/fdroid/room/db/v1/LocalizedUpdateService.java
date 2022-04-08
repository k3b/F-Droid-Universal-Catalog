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

package de.k3b.fdroid.room.db.v1;

import org.fdroid.model.common.LocalizedCommon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.room.db.LocaleRepository;
import de.k3b.fdroid.room.db.LocalizedRepository;
import de.k3b.fdroid.room.model.Locale;
import de.k3b.fdroid.room.model.Localized;

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
        id2Locale.put(locale.id, locale);
        code2Locale.put(locale.code, locale);
    }

    private String getLocaleCode(int localeId) {
        Locale locale = (localeId == 0) ? null : id2Locale.get(localeId);
        return (locale == null) ? null : locale.code;
    }

    private int getOrCreateLocaleId(String localeCode) {
        if (localeCode != null) {
            Locale locale = code2Locale.get(localeCode);
            if (locale == null) {
                // create on demand
                locale = new Locale();
                locale.code = localeCode;
                localeRepository.insert(locale);
                init(locale);
            }
            return locale.id;
        }
        return 0;
    }

    public List<Localized> update(int appId, Map<String, org.fdroid.model.v1.Localized> v1LocalizedMap) {
        List<Localized> roomLocalizedList = localizedRepository.findByAppId(appId);

        deleteRemoved(roomLocalizedList, v1LocalizedMap);
        for (Map.Entry<String, org.fdroid.model.v1.Localized> v1Entry : v1LocalizedMap.entrySet()) {
            int localeId = getOrCreateLocaleId(v1Entry.getKey());
            org.fdroid.model.v1.Localized v1Localized = v1Entry.getValue();
            Localized roomLocalized = findByLocaleId(roomLocalizedList, localeId);
            if (roomLocalized == null) {
                roomLocalized = new Localized();
                roomLocalized.appId = appId;
                roomLocalized.localeId = localeId;
                LocalizedCommon.copyCommon(roomLocalized, v1Localized);
                localizedRepository.insert(roomLocalized);
                roomLocalizedList.add(roomLocalized);
            } else {
                LocalizedCommon.copyCommon(roomLocalized, v1Localized);
                localizedRepository.update(roomLocalized);
            }
        }
        return roomLocalizedList;

    }

    private Localized findByLocaleId(List<Localized> roomLocalizedList, int localeId) {
        for (Localized l : roomLocalizedList) {
            if (l.localeId == localeId) return l;
        }
        return null;
    }

    private void deleteRemoved(List<Localized> roomLocalizedList, Map<String, org.fdroid.model.v1.Localized> v1LocalizedMap) {
        for (int i = roomLocalizedList.size() - 1; i >= 0; i--) {
            Localized roomLocalized = roomLocalizedList.get(i);
            String localeCode = getLocaleCode(roomLocalized.localeId);
            if (localeCode != null && !v1LocalizedMap.containsKey(localeCode)) {
                localizedRepository.delete(roomLocalized);
                roomLocalizedList.remove(i);
            }
        }
    }
}
