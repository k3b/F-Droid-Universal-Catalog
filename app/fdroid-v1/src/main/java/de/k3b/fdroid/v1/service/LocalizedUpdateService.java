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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Locale;
import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.domain.common.LocalizedCommon;
import de.k3b.fdroid.domain.common.PojoCommon;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.service.LanguageService;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class LocalizedUpdateService {
    private final LocalizedRepository localizedRepository;
    private final LanguageService languageService;

    public LocalizedUpdateService(LocalizedRepository localizedRepository,
                                  LanguageService languageService) {
        this.localizedRepository = localizedRepository;
        this.languageService = languageService;
    }

    public LocalizedUpdateService init() {
        languageService.init();
        return this;
    }

    public List<Localized> update(int appId, de.k3b.fdroid.domain.App roomApp, Map<String, de.k3b.fdroid.v1.domain.Localized> v1LocalizedMap) {
        List<Localized> roomLocalizedList = localizedRepository.findByAppId(appId);
        deleteHidden(roomLocalizedList);
        // deleteRemoved(roomLocalizedList, v1LocalizedMap);

        for (Map.Entry<String, de.k3b.fdroid.v1.domain.Localized> v1Entry : v1LocalizedMap.entrySet()) {
            String language = v1Entry.getKey();
            int localeId = languageService.getOrCreateLocaleIdByCode(language);
            if (!LanguageService.isHidden(localeId)) {
                de.k3b.fdroid.v1.domain.Localized v1Localized = v1Entry.getValue();
                Localized roomLocalized = LanguageService.findByLocaleId(roomLocalizedList, localeId);
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

            } // if not hidden
        } // for each v1 language

        if (roomApp != null) {
            recalculateSearchFields(roomApp, roomLocalizedList);
        }
        return roomLocalizedList;

    }

    /** App.searchXxx calculated from detail Localized-s */
    private void recalculateSearchFields(App roomApp, List<Localized> roomLocalizedList) {
        StringBuilder name = new StringBuilder();
        StringBuilder summary = new StringBuilder();
        StringBuilder description = new StringBuilder();
        StringBuilder whatsNew = new StringBuilder();

        Localized[] roomLocalizedListSortByPrio = sortByPrioDesc(roomLocalizedList);

        for (Localized loc : roomLocalizedListSortByPrio) {

            Locale locale = languageService.getLocaleById(loc.getLocaleId());
            String languagePrefix = "";
            if (languageService.getOrCreateLocaleByCode(locale.getCode()).getLanguagePriority() < 1) {
                // only visible if it is not a prefered language
                languagePrefix = locale.getCode() + ": ";
            }

            add("name", PojoCommon.MAX_LEN_AGGREGATED, roomApp, name, languagePrefix, loc.getName(), " | ");
            add("summary", PojoCommon.MAX_LEN_AGGREGATED, roomApp, summary, languagePrefix, loc.getSummary(), "\n");
            add("description", PojoCommon.MAX_LEN_AGGREGATED_DESCRIPTION, roomApp, description, languagePrefix, loc.getDescription(), "\n\n------------\n\n");
            add("whatsNew", PojoCommon.MAX_LEN_AGGREGATED, roomApp, whatsNew, languagePrefix, loc.getWhatsNew(), "\n\n------------\n\n");

        }
        roomApp.setSearchName(name.toString());
        roomApp.setSearchSummary(summary.toString());
        roomApp.setSearchDescription(description.toString());
        roomApp.setSearchWhatsNew(whatsNew.toString());
    }

    protected Localized[] sortByPrioDesc(List<Localized> roomLocalizedList) {
        // :-( : List<>.sort() requieres android api 24. not compatible with api 14
        Localized[] result = roomLocalizedList.toArray(new Localized[0]);
        Arrays.sort(result, (x, y) -> compareByPrio(x,y));
        return result;
    }

    private int compareByPrio(Localized x, Localized y) {
        int px = languageService.getLocaleById(x.getLocaleId()).getLanguagePriority();
        int py = languageService.getLocaleById(y.getLocaleId()).getLanguagePriority();
        return -Integer.compare(px,py);
    }

    private void add(String fieldName, int maxLen, App roomApp, StringBuilder dest,
                     String languagePrefix, String src, String seperator) {
        if (src != null && src.length() > 0 && !dest.toString().contains(src)) {
            if (dest.length() > 0) {
                dest.append(seperator);
            }

            dest.append(languagePrefix).append(src);
            if (dest.length() > maxLen) {
                throw new IllegalArgumentException(fieldName + " > " + maxLen +
                        " in " + roomApp.getPackageName() +
                        ": ('" + dest + "')\n\t" + roomApp);
            }
        }
    }

    /**
     * delete all from list that should be hidden
     */
    public void deleteHidden(List<Localized> roomLocalizedList) {
        for (int i = roomLocalizedList.size() - 1; i >= 0; i--) {
            Localized roomLocalized = roomLocalizedList.get(i);
            if (roomLocalized != null && isHidden(roomLocalized)) {
                localizedRepository.delete(roomLocalized);
                roomLocalizedList.remove(i);
            }
        }
    }

    public boolean isHidden(Localized roomLocalized) {
        if (roomLocalized == null) return true;

        Locale locale = languageService.getLocaleById(roomLocalized.getLocaleId());
        return (locale != null && LanguageService.isHidden(locale));
    }

    private String getLocaleCodeByLocalized(Localized roomLocalized) {
        return roomLocalized == null ? null : languageService.getLocaleCodeById(roomLocalized.getLocaleId());
    }
}
