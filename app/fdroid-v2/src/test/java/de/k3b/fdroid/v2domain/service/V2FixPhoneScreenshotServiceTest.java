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
package de.k3b.fdroid.v2domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.TreeMap;

import de.k3b.fdroid.v2domain.entity.packagev2.V2Screenshots;
import de.k3b.fdroid.v2domain.entity.repo.V2File;

public class V2FixPhoneScreenshotServiceTest {
    @Test
    public void fix() {
        V2Screenshots v2Screenshots = new V2Screenshots();
        TreeMap<String, List<V2File>> files = new TreeMap<>();
        files.put("en", Arrays.asList(
                new V2File("/a/b/c/test.png"),
                new V2File("/a/b/c/test2.png"),
                new V2File("/a/b/c/test3.png")));
        v2Screenshots.setPhone(files);
        V2FixPhoneScreenshotService sut = new V2FixPhoneScreenshotService();

        sut.fix(v2Screenshots);
        assertEquals("dir ", "a/b/c/", v2Screenshots.getPhoneDir().get("en"));
        assertTrue("prefix ", sut.allNameStartWith(files.get("en"), "test"));
    }
}