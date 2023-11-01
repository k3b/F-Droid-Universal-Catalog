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

package de.k3b.fdroid.domain.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

public class StringUtilTest {

    @Test
    public void isEmptyArray() {
        assertTrue("null", StringUtil.isEmpty((String[]) null));
        assertTrue("[0]", StringUtil.isEmpty(StringUtil.EMPTY_ARRAY));

        assertFalse("[1]", StringUtil.isEmpty(new String[]{""}));
    }

    @Test
    public void testReplaceEmptyString() {
        assertNull("null", StringUtil.replaceEmpty(null, null));
        assertEquals("replaced", "replaced", StringUtil.replaceEmpty(null, "replaced"));
        assertEquals("not-replaced", "not-replaced", StringUtil.replaceEmpty("not-replaced","replaced"));
    }

    @Test
    public void testReplaceEmptyLong() {
        assertEquals("replaced", 22, StringUtil.replaceEmpty(0,22));
        assertEquals("not-replaced", 11, StringUtil.replaceEmpty(11,22));
    }

    @Test
    public void isEmptyString() {
        assertTrue("null", StringUtil.isEmpty((String) null));
        assertTrue("", StringUtil.isEmpty(""));

        assertFalse("something", StringUtil.isEmpty("something"));
    }

    @Test
    public void isEmptyLong() {
        assertTrue("o", StringUtil.isEmpty(0));

        assertFalse("1", StringUtil.isEmpty(1));
    }

    @Test
    public void contains() {
        assertTrue("found", StringUtil.contains("something", new String[]{"something"}));
        assertFalse("not found", StringUtil.contains("something", new String[]{"other"}));
        assertFalse("empty array", StringUtil.contains("something", StringUtil.EMPTY_ARRAY));

    }

    @Test
    public void toCsvStringOrNull() {
        assertNull("null", StringUtil.toCsvStringOrNull(null));
        assertNull("empty", StringUtil.toCsvStringOrNull(Collections.emptyList()));
        assertEquals("1", "1", StringUtil.toCsvStringOrNull(Collections.singletonList("1")));
        assertEquals("1,2", "1,2", StringUtil.toCsvStringOrNull(Arrays.asList("1", "2")));
    }

    @Test
    public void toStringArray() {
        assertArrayEquals("null", StringUtil.EMPTY_ARRAY, StringUtil.toStringArray(null));
        assertArrayEquals("empty", StringUtil.EMPTY_ARRAY, StringUtil.toStringArray(""));
        assertArrayEquals("1", new String[]{"1"}, StringUtil.toStringArray("1"));
        assertArrayEquals("1,2", new String[]{"1", "2"}, StringUtil.toStringArray("1,2"));
    }

    @Test
    public void getFirst() {
        String result = StringUtil.getFirst("a, b, c", ", ", null);
        assertEquals("a", result);
    }

    @Test
    public void getLast() {
        String result = StringUtil.getLast("a, b, c", ", ", null);
        assertEquals("c", result);
    }

    @Test
    public void getFirstWithPrefix() {
        String combinedValue = "x:a, y:b, z:c";
        assertEquals("x:a", "a",
                StringUtil.getFirstWithPrefix(combinedValue, "x:", ", ", null));
        assertEquals("y:b", "b",
                StringUtil.getFirstWithPrefix(combinedValue, "y:", ", ", null));
        assertEquals("z:c", "c",
                StringUtil.getFirstWithPrefix(combinedValue, "z:", ", ", null));
        assertNull("not q:", StringUtil.getFirstWithPrefix(combinedValue, "q:", ", ", null));
    }
}