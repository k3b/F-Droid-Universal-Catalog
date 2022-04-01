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

package org.fdroid.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Localisation language info from org.fdroid.v1 is deliverd in a non standard way
 * resulting in different representations of the same language.
 *
 * This App uses only one locale per language so that
 * en, en-AU, en-GB, en-US, en-rUS, en-us and en_US will all be mapped to "en"
 *
 * Exception: chinese mainland zh-CN and taiwan zh-TW use different symbols: no canonical
 *
 */
public class CanicalLocale {
    /** create a canonical Locale :
     * en, en-AU, en-GB, en-US, en-rUS, en-us and en_US will all be mapped to "en" */
    public static String getCanonicalLocale(String fdroidLocale) {
        String normalized = fdroidLocale;
        if (normalized != null) {
            if (normalized.compareToIgnoreCase("zh") == 0) return "zh-CN";
            if (normalized.length() <= 2) {
                return normalized.toLowerCase(Locale.ROOT);
            }

            if (normalized.length() == "de-rDE".length() && normalized.contains("-r")) {
                // i.e. "de-rDE" => "de-DE"
                normalized = normalized.replace("-r", "-");
            }

            // i.e. "de_DE" => "de-DE"
            normalized = normalized.replace("_", "-");

            String normalizedLowerCase = normalized.toLowerCase(Locale.ROOT);

            if (normalized.charAt(2) == '-' && !normalizedLowerCase.startsWith("zh-")) {
                // chinese mainland zh-CN and taiwan zh-TW use different symbols: no canonical
                // ie "en-US" => "en"
                return normalizedLowerCase.substring(0, 2);
            }
        }
        return normalized;
    }

    /**
     * true if eigher 2 char locale or 5 char zh-xx
     */
    public static boolean isValidCanonical(String canonical) {
        int length = canonical == null ? 0 : canonical.length();
        return ((length == 2) || (length == 5 && canonical.startsWith("zh-")));
    }

    /**
     * return array of keys-offsets where canonical locale changes. keys are sorted
     */
    public static Integer[] getCanonicalLocaleChangeIndex(String[] keys) {
        Arrays.sort(keys, 0, keys.length, String.CASE_INSENSITIVE_ORDER);

        List<Integer> result = new ArrayList<>();
        String last = "";
        int i = 0;
        while (i < keys.length) {
            String canonical = CanicalLocale.getCanonicalLocale(keys[i]);
            if (!canonical.equals(last)) {
                result.add(i);
                last = canonical;
            }
            i++;
        }
        result.add(i);
        return result.toArray(new Integer[0]);
    }
}
