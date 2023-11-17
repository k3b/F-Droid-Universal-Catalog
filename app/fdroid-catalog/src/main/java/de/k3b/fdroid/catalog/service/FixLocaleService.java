/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid project.
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

package de.k3b.fdroid.catalog.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.k3b.fdroid.domain.entity.common.LocalizedCommon;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.service.PropertyMerger;

/**
 * Consolidate Locales so that for examle en, en_US, en_GB become one locale "en"
 */
public class FixLocaleService {
    public PropertyMerger merger = new PropertyMerger();

    public <L extends LocalizedCommon> Map<String, L> fix(Map<String, L> localesOld) {
        String[] keys = localesOld.keySet().toArray(new String[0]);
        Integer[] changes = LanguageService.getCanonicalLocaleChangeIndex(keys);
        Arrays.sort(keys, 0, keys.length, String.CASE_INSENSITIVE_ORDER);

        Map<String, L> localesNew = new TreeMap<>();

        List<L> sameLocale = new ArrayList<>();
        for (int i = 1; i < changes.length; i++) {
            String canonical = LanguageService.getCanonicalLocale(keys[changes[i - 1]]);
            sameLocale.clear();
            for (int j = changes[i - 1]; j < changes[i]; j++) {
                sameLocale.add(localesOld.get(keys[j]));
            }

            if (LanguageService.isValidCanonical(canonical)) {
                localesNew.put(canonical, merger.merge(sameLocale));
            }
        }
        return localesNew;
    }

    protected <L extends LocalizedCommon> void addEnLocaleIfneccessary(Map<String, L> localesNew, String summary, String description, Class<L> localizedClass) {
        if (!localesNew.containsKey("en") && (summary != null || description != null)) {
            L enLocale;
            try {
                enLocale = localizedClass.getConstructor().newInstance();
            } catch (Exception ex) {
                throw new IllegalArgumentException("Cannot create " + localizedClass.getName() +
                        ": no parameterless constructor found.");
            }
            enLocale.setSummary(summary);
            enLocale.setDescription(description);
            localesNew.put("en", enLocale);
        }
    }
}
