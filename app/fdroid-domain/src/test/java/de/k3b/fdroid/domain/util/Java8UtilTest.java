/*
 * Copyright (c) 2023 by k3b.
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

package de.k3b.fdroid.domain.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import de.k3b.fdroid.domain.entity.App;

public class Java8UtilTest {

    @Test
    public void getKeyValueMap() {
        HashMap<String, App> map = new HashMap<>();
        map.put("de", new App("some Value"));
        Map<String, String> result = Java8Util.getKeyValueMap(map, a -> a.getPackageName());

        assertEquals("some Value", result.get("de"));
    }
}