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

package de.k3b.fdroid.v1domain.service;

import java.util.Map;

import de.k3b.fdroid.v1domain.entity.Localized;
import de.k3b.fdroid.v1domain.entity.V1App;

public class V1FixLocaleService extends de.k3b.fdroid.domain.service.FixLocaleService {
    public V1App fix(V1App v1App) {
        Map<String, Localized> localesOld = (v1App == null) ? null : v1App.getLocalized();
        if (localesOld != null) {
            convertIcons(v1App.getPackageName(), localesOld);
            Map<String, Localized> localesNew = fix(localesOld);

            addEnLocaleIfneccessary(localesNew, v1App.getSummary(), v1App.getDescription(), Localized.class);
            v1App.setLocalized(localesNew);
        }

        return v1App;
    }

    private void convertIcons(String packageName, Map<String, Localized> localesOld) {
        for (Map.Entry<String, Localized> entry : localesOld.entrySet()) {
            String locale = entry.getKey();
            Localized localized = entry.getValue();
            String icon = localized.getIcon();
            if (icon != null && icon.startsWith("icon_") && icon.endsWith("=.png")) {
                localized.setIcon("../" + packageName + "/" + locale + "/icon.png");
            }

            if (localized.getPhoneScreenshotCount() > 0) {
                localized.setPhoneScreenshotDir(packageName + "/" + locale + "/phoneScreenshots/");
            }
        }
    }

}
