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

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class CanicalLocaleTest {

    @Test
    public void getCanonicalLocale() {
        testCanonical("en", "en-US");

        testCanonical("de", "de");
        testCanonical("de", "de-DE");
        testCanonical("de", "de_DE");
        testCanonical("de", "de-rDE");
        testCanonical("de", "de-AT");

        // special case chineese
        testCanonical("zh-TW", "zh-rTW");

        // special case non-2-letter-language (plattdütsch, ancient german)
        testCanonical("nds-DE", "nds-DE");

        // error non-locale
        testCanonical(null, null);
        testCanonical("", "");
        testCanonical("heLLo World", "heLLo World");
    }

    private void testCanonical(String expected, String fdroidLocale) {
        assertEquals("CanicalLocale.getCanonicalLocale(" + fdroidLocale + ")", expected, CanicalLocale.getCanonicalLocale(fdroidLocale));
    }

    @Test
    public void getCanonicalLocaleChangeIndex() {
        String[] keys = new String[]{
                "de", "zh-TW", "de-DE", "ar-SA","de-rDE","en-us"};
        Integer[] index = CanicalLocale.getCanonicalLocaleChangeIndex(keys);
        Assert.assertArrayEquals("sorted keys", new String[]{
                "ar-SA","de", "de-DE", "de-rDE","en-us", "zh-TW"}, keys);
        Assert.assertArrayEquals("index", new Integer[]{0,1,4,5,6}, index);
    }
}