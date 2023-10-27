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
package de.k3b.fdroid.domain.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Locale;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.util.StringUtil;

public class LocalizedService {
    public static final String SEPERATOR_NAME = " | ";
    public static final String SEPERATOR_SUMMARY = "\n";
    public static final String SEPERATOR_DESCRIPTION = "\n\n------------\n\n";
    public static final String SEPERATOR_WHATS_NEW = "\n\n------------\n\n";
    private final LanguageService languageService;

    public LocalizedService(LanguageService languageService) {
        this.languageService = languageService;
    }

    protected Localized[] sortByPrioDesc(List<Localized> roomLocalizedList) {
        // :-( : List<>.sort() requieres android api 24. not compatible with api 14
        Localized[] result = roomLocalizedList.toArray(new Localized[0]);
        Arrays.sort(result, this::compareByPrio);
        return result;
    }

    private int compareByPrio(Localized x, Localized y) {
        int px = languageService.getItemById(x.getLocaleId()).getLanguagePriority();
        int py = languageService.getItemById(y.getLocaleId()).getLanguagePriority();
        return -Integer.compare(px, py);
    }

    private void add(StringBuilder dest, String src, String fieldName, int maxLen, App roomApp,
                     String languagePrefix, String seperator) {
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
    public List<Localized>  deleteHidden(List<Localized> roomLocalizedList) {
        List<Localized> deletedList = new ArrayList<>();
        for (int i = roomLocalizedList.size() - 1; i >= 0; i--) {
            Localized roomLocalized = roomLocalizedList.get(i);
            if (roomLocalized != null && isHidden(roomLocalized)) {
                roomLocalizedList.remove(i);
                deletedList.add(roomLocalized);
            }
        }
        return deletedList;
    }

    public boolean isHidden(Localized roomLocalized) {
        if (roomLocalized == null) return true;

        Locale locale = languageService.getItemById(roomLocalized.getLocaleId());
        return (LanguageService.isHidden(locale));
    }

    /**
     * App.searchXxx calculated from detail Localized-s
     */
    public void recalculateSearchFields(int repoId, App roomApp, List<Localized> roomLocalizedList) {
        StringBuilder name = new StringBuilder();
        StringBuilder summary = new StringBuilder();
        StringBuilder description = new StringBuilder();
        StringBuilder whatsNew = new StringBuilder();
        String roomIcon = roomApp.getIcon();
        String icon = roomIcon;

        Localized[] roomLocalizedListSortByPrio = sortByPrioDesc(roomLocalizedList);

        for (Localized loc : roomLocalizedListSortByPrio) {

            Locale locale = languageService.getItemById(loc.getLocaleId());
            String languagePrefix = "";
            if (languageService.getOrCreateLocaleByCode(locale.getId()).getLanguagePriority() < 1) {
                // only visible if it is not a prefered language
                languagePrefix = locale.getId() + ": ";
            }

            add(name, loc.getName(), "name", EntityCommon.MAX_LEN_AGGREGATED, roomApp, languagePrefix, SEPERATOR_NAME);
            add(summary, loc.getSummary(), "summary", EntityCommon.MAX_LEN_AGGREGATED, roomApp, languagePrefix, SEPERATOR_SUMMARY);
            add(description, loc.getDescription(), "description", EntityCommon.MAX_LEN_AGGREGATED_DESCRIPTION, roomApp, languagePrefix, SEPERATOR_DESCRIPTION);
            add(whatsNew, loc.getWhatsNew(), "whatsNew", EntityCommon.MAX_LEN_AGGREGATED, roomApp, languagePrefix, SEPERATOR_WHATS_NEW);
            if (icon == null) {
                String locIcon = loc.getIcon();
                if (locIcon != null && !locIcon.endsWith("=.png")) {
                    icon = StringUtil.emptyAsNull(locIcon);
                }
            }
        }
        roomApp.setSearchName(name.toString());
        roomApp.setSearchSummary(summary.toString());
        roomApp.setSearchDescription(description.toString());
        roomApp.setSearchWhatsNew(whatsNew.toString());
        if (roomIcon == null && icon != null && repoId != 0) {
            roomApp.setIcon(icon);
            roomApp.setResourceRepoId(repoId);
        }
    }
}
