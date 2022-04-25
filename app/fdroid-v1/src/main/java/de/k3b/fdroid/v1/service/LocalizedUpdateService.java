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

import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.domain.common.LocalizedCommon;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.service.LanguageService;
import de.k3b.fdroid.service.LocalizedService;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class LocalizedUpdateService {
    private final LocalizedRepository localizedRepository;
    private final LanguageService languageService;
    private final LocalizedService localizedService;

    public LocalizedUpdateService(LocalizedRepository localizedRepository,
                                  LanguageService languageService) {
        this.localizedRepository = localizedRepository;
        this.languageService = languageService;
        this.localizedService = new LocalizedService(languageService);
    }

    public LocalizedUpdateService init() {
        languageService.init();
        return this;
    }

    public List<Localized> update(int appId, de.k3b.fdroid.domain.App roomApp, Map<String, de.k3b.fdroid.v1.domain.Localized> v1LocalizedMap) {
        List<Localized> roomLocalizedList = localizedRepository.findByAppId(appId);
        List<Localized> deleted = localizedService.deleteHidden(roomLocalizedList);
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
            localizedService.recalculateSearchFields(roomApp, roomLocalizedList);
        }
        deleteAll(deleted);
        return roomLocalizedList;

    }

    private void deleteAll(List<Localized> deleted) {
        for (Localized l : deleted) {
            if(l.getId() != 0) {
                localizedRepository.delete(l);
            }
        }
    }

    private String getLocaleCodeByLocalized(Localized roomLocalized) {
        return roomLocalized == null ? null : languageService.getLocaleCodeById(roomLocalized.getLocaleId());
    }
}
