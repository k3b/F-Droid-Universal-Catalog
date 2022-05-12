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
package de.k3b.fdroid.android.domain;

import de.k3b.fdroid.domain.interfaces.Enitity;

/**
 * Android-Room specific pseudeo Enitity used to create a DatabaseView
 */
@androidx.room.DatabaseView("SELECT id, packageName, packageName search, 1000 score FROM App UNION\n" +
        "    SELECT id, packageName, searchName search, 1000 score FROM App UNION\n" +
        "    SELECT id, packageName, searchSummary search, 100 score FROM App UNION\n" +
        "    SELECT id, packageName, searchWhatsNew search, 10 score FROM App UNION\n" +
        "    SELECT id, packageName, searchCategory search, 50 score FROM App UNION\n" +
        "    SELECT id, packageName, searchDescription search, 1 score FROM App")
public class AppSearch implements Enitity {
}
