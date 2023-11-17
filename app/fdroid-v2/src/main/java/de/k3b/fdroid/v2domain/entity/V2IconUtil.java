/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of de.k3b.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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

package de.k3b.fdroid.v2domain.entity;

import de.k3b.fdroid.v2domain.entity.repo.V2File;

public class V2IconUtil {
    public static String getIconName(V2File icon) {
        String iconName = (icon == null) ? null : icon.getName();
        if (iconName != null) {
            //  && iconName.startsWith("/"))
            int lastSeperator = iconName.lastIndexOf("/");
            iconName = iconName.substring(lastSeperator + 1);
        }
        return iconName;
    }
}
