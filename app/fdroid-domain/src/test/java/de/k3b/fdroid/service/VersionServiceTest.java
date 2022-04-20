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
package de.k3b.fdroid.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import de.k3b.fdroid.domain.Version;

public class VersionServiceTest {
    private static final int SDK15 = 15;
    private static final int SDK16 = 16;
    private final VersionService versionService = new VersionService();

    @Test
    public void fixMaxSdk_withNativecode() {
        String nativeCode1 = "cpu1"; // vxx1
        Version v141 = createVersion(141, 0, nativeCode1);
        Version v151 = createVersion(151, 0, nativeCode1);
        Version v161 = createVersion(161, 0, nativeCode1);
        Version v171 = createVersion(171, 0, nativeCode1);

        String nativeCode2 = "cpu2"; // vxx2
        Version v142 = createVersion(142, 0, nativeCode2);
        Version v152 = createVersion(152, 0, nativeCode2);
        Version v162 = createVersion(162, 0, nativeCode2);
        Version v172 = createVersion(172, 0, nativeCode2);

        String nativeCode3 = "cpu3"; // not affected by other maxsdk
        Version v143 = createVersion(143, 0, nativeCode3);

        String nativeCode4 = null; // no native-code. Affected by first maxsdk
        Version v144 = createVersion(144, 0, nativeCode4);

        Version v159_15 = createVersion(159, SDK15, nativeCode1);
        Version v169_16 = createVersion(169, SDK16, nativeCode2);

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
        Version v141 = createVersion(141, 0, nativeCode);

        Version v151 = createVersion(151, 0, nativeCode);
        Version v159 = createVersion(159, SDK15, nativeCode);

        Version v161 = createVersion(161, 0, nativeCode);
        Version v169 = createVersion(169, SDK16, nativeCode);

        Version v171 = createVersion(171, 0, nativeCode);

        versionService.fixMaxSdk(Arrays.asList(v151, v171, v159, v141, v161, v169));
        Assert.assertEquals("v171 unmodified", 0, v171.getMaxSdkVersion());
        Assert.assertEquals("v161 fixed", SDK16, v161.getMaxSdkVersion());
        Assert.assertEquals("v151 fixed", SDK15, v151.getMaxSdkVersion());
        Assert.assertEquals("v141 fixed", SDK15, v141.getMaxSdkVersion());
    }


    private Version createVersion(int versionCode, int maxSdk, String nativeCode) {
        Version version = new Version();
        version.setVersionCode(versionCode);
        version.setNativecode(nativeCode);
        version.setMaxSdkVersion(maxSdk);
        return version;
    }
}