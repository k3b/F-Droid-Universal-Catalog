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
package de.k3b.fdroid.domain.service;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.k3b.fdroid.domain.entity.Version;

public class VersionServiceTest {
    private static final int SDK15 = 15;
    private static final int SDK16 = 16;
    private final VersionService versionService = new VersionService();

    @Test
    public void fixMaxSdk_withNativecode() {
        String nativeCode1 = "cpu1"; // vxx1
        Version v141 = createVersion(nativeCode1, 141, 0);
        Version v151 = createVersion(nativeCode1, 151, 0);
        Version v161 = createVersion(nativeCode1, 161, 0);
        Version v171 = createVersion(nativeCode1, 171, 0);

        String nativeCode2 = "cpu2"; // vxx2
        Version v142 = createVersion(nativeCode2, 142, 0);
        Version v152 = createVersion(nativeCode2, 152, 0);
        Version v162 = createVersion(nativeCode2, 162, 0);
        Version v172 = createVersion(nativeCode2, 172, 0);

        String nativeCode3 = "cpu3"; // not affected by other maxsdk
        Version v143 = createVersion(nativeCode3, 143, 0);

        String nativeCode4 = null; // no native-code. Affected by first maxsdk
        Version v144 = createVersion(nativeCode4, 144, 0);

        Version v159_15 = createVersion(nativeCode1, 159, SDK15);
        Version v169_16 = createVersion(nativeCode2, 169, SDK16);

        versionService.fixMaxSdk(Arrays.asList(v141, v142, v143, v144, v151, v152, v159_15, v161, v162, v169_16, v171, v172));

        { // vxx1 affected by nativeCode1
            Assert.assertEquals("v171 unmodified", 0, v171.getMaxSdkVersion());
            Assert.assertEquals("v161 not fixed", 0, v161.getMaxSdkVersion());

            // updated by v159_15
            Assert.assertEquals("v151 fixed", SDK15, v151.getMaxSdkVersion());
            Assert.assertEquals("v141 fixed", SDK15, v141.getMaxSdkVersion());
        }

        { // vxx2 affected by nativeCode2
            Assert.assertEquals("v172 unmodified", 0, v172.getMaxSdkVersion());

            // updated by v169_16
            Assert.assertEquals("v162 fixed", SDK16, v162.getMaxSdkVersion());
            Assert.assertEquals("v152 fixed", SDK16, v152.getMaxSdkVersion());
            Assert.assertEquals("v142 fixed", SDK16, v142.getMaxSdkVersion());
        }

        Assert.assertEquals("v143 not fixed (no max for cpu)", 0, v143.getMaxSdkVersion());
        Assert.assertEquals("v144 fixed (first max because nativeCode==null)", SDK16, v144.getMaxSdkVersion());
    }

    @Test
    public void fixMaxSdk_noNativecode() {
        String nativeCode = null;
        Version v141 = createVersion(nativeCode, 141, 0);

        Version v151 = createVersion(nativeCode, 151, 0);
        Version v159 = createVersion(nativeCode, 159, SDK15);

        Version v161 = createVersion(nativeCode, 161, 0);
        Version v169 = createVersion(nativeCode, 169, SDK16);

        Version v171 = createVersion(nativeCode, 171, 0);

        versionService.fixMaxSdk(Arrays.asList(v151, v171, v159, v141, v161, v169));
        Assert.assertEquals("v171 unmodified", 0, v171.getMaxSdkVersion());
        Assert.assertEquals("v161 fixed", SDK16, v161.getMaxSdkVersion());
        Assert.assertEquals("v151 fixed", SDK15, v151.getMaxSdkVersion());
        Assert.assertEquals("v141 fixed", SDK15, v141.getMaxSdkVersion());
    }


    @Test
    public void removeInterimVersions() {
        Version vNaC5 = createVersion("a",5);
        Version vNaC1 = createVersion("a",1);
        Version vN_C9 = createVersion(null,9);
        Version vN_C3 = createVersion(null,3);

        List<Version> versionList = new ArrayList<>(Arrays.asList(vN_C9, vNaC1, vNaC5, vN_C3));

        List<Version> removed = versionService.removeInterimVersions(versionList, 0);
        assertArrayEquals("removed (order: sort)", new Version[]{vNaC1, vN_C3},
                removed.toArray(new Version[0]));
        assertArrayEquals("versionList (order: original list)", new Version[]{vN_C9, vNaC5},
                versionList.toArray(new Version[0]));
    }

    @Test
    public void sortedByNativeAndCodeDecending() {
        Version vNaC5 = createVersion("a",5);
        Version vNaC1 = createVersion("a",1);
        Version vN_C9 = createVersion(null,9);
        Version vN_C3 = createVersion(null,3);

        List<Version> versionList = Arrays.asList(vN_C9, vNaC1, vNaC5, vN_C3);
        Version[] result = versionService.sortedByNativeAndCodeDecending(versionList);
        assertArrayEquals(new Version[]{vNaC5, vNaC1, vN_C9, vN_C3}, result);
    }

    private Version createVersion(String nativeCode, int versionCode) {
        return createVersion(nativeCode, versionCode, 0);
    }

    private Version createVersion(String nativeCode, int versionCode, int maxSdk) {
        Version version = new Version();
        version.setVersionCode(versionCode);
        version.setNativecode(nativeCode);
        version.setMaxSdkVersion(maxSdk);
        return version;
    }
}