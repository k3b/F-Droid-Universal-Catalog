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

package org.fdroid.service.v1;

import org.fdroid.model.v1.App;
import org.fdroid.model.v1.Localized;
import org.fdroid.util.CanicalLocale;
import org.fdroid.util.PropertyMerger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FixLocaleService {
    public PropertyMerger merger = new PropertyMerger();
    public App fix(App app) {
        Map<String, Localized> localesOld = (app == null) ? null : app.getLocalized();
        if (localesOld != null) {
            String[] keys = localesOld.keySet().toArray(new String[0]);
            Integer[] changes = CanicalLocale.getCanonicalLocaleChangeIndex(keys);
            Arrays.sort(keys, 0, keys.length, String.CASE_INSENSITIVE_ORDER);

            Map<String, Localized> localesNew = new TreeMap();

            List<Localized> sameLocale = new ArrayList<>();
            for (int i = 1; i < changes.length; i++) {
                String canonical = CanicalLocale.getCanonicalLocale(keys[changes[i - 1]]);
                sameLocale.clear();
                for (int j = changes[i - 1]; j < changes[i]; j++) {
                    sameLocale.add(localesOld.get(keys[j]));
                }

                if (CanicalLocale.isValidCanonical(canonical)) {
                    localesNew.put(canonical, merger.merge(sameLocale));
                }
            }

            addEnLocaleIfneccessary(localesNew, app.getSummary(), app.getDescription());
            app.setLocalized(localesNew);
        }

        return app;
    }

    private void addEnLocaleIfneccessary(Map<String, Localized> localesNew, String summary, String description) {
        if (!localesNew.containsKey("en") && (summary != null || description != null)) {
            Localized enLocale = new Localized();
            enLocale.setSummary(summary);
            enLocale.setDescription(description);
            localesNew.put("en", enLocale);
        }
    }
}
