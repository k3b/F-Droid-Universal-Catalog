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

package de.k3b.fdroid.util;

import java.util.List;

public class StringUtil {
    public static final String[] EMPTY_ARRAY = new String[0];
    public static final String CSV_FIELD_DELIMITER = ",";

    public static <T> boolean isEmpty(T[] s) {
        return s == null || s.length == 0;
    }

    public static boolean isEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean isEmpty(long s) {
        return s == 0;
    }

    public static boolean contains(String search, String[] itemArray) {
        for (String item : itemArray) {
            if (search.equalsIgnoreCase(item)) return true;
        }
        return false;
    }

    public static <T> String toCsvStringOrNull(List<T> list, String delimiter) {
        StringBuilder result = new StringBuilder();
        if (list != null) {
            for (T s : list) {
                if (s != null) {
                    if (result.length() > 0) result.append(delimiter);
                    result.append(s);
                }
            }
        }

        return result.length() == 0 ? null : result.toString();
    }

    public static <T> String toCsvStringOrNull(List<T> list) {
        return toCsvStringOrNull(list, CSV_FIELD_DELIMITER);
    }

    public static String[] toStringArray(String csvString) {
        if (StringUtil.isEmpty(csvString)) {
            return StringUtil.EMPTY_ARRAY;
        } else {
            return csvString.split(CSV_FIELD_DELIMITER);
        }
    }

}
