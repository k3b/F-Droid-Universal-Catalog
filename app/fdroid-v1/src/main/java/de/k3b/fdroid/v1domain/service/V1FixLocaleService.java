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

import de.k3b.fdroid.v1domain.entity.V1App;
import de.k3b.fdroid.v1domain.entity.V1Localized;

public class V1FixLocaleService extends de.k3b.fdroid.domain.service.FixLocaleService {
    public V1App fix(V1App v1App) {
        Map<String, V1Localized> localesOld = (v1App == null) ? null : v1App.getLocalized();
        if (localesOld != null) {
            convertIcons(v1App.getPackageName(), localesOld);
            Map<String, V1Localized> localesNew = fix(localesOld);

            addEnLocaleIfneccessary(localesNew, v1App.getSummary(), v1App.getDescription(), V1Localized.class);
            v1App.setLocalized(localesNew);
        }

        return v1App;
    }

    private void convertIcons(String packageName, Map<String, V1Localized> localesOld) {
        for (Map.Entry<String, V1Localized> entry : localesOld.entrySet()) {
            String locale = entry.getKey();
            V1Localized v1Localized = entry.getValue();
            String icon = v1Localized.getIcon();
            if (icon != null && icon.startsWith("icon_") && icon.endsWith("=.png")) {
                v1Localized.setIcon("../" + packageName + "/" + locale + "/icon.png");
            }

            if (v1Localized.getPhoneScreenshotCount() > 0) {
                v1Localized.setPhoneScreenshotDir(packageName + "/" + locale + "/phoneScreenshots/");
            }
        }
    }

}
