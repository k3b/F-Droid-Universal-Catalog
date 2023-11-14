/*
 * Copyright (c) 2022 by k3b.
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

package de.k3b.fdroid.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import de.k3b.fdroid.domain.entity.AppHardware;
import de.k3b.fdroid.domain.entity.HardwareProfile;
import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.util.StringUtil;
import de.k3b.fdroid.domain.util.TestDataGenerator;

public class HardwareProfileServiceTest {

    @Test
    public void isCompatible() {
        HardwareProfile profile = new HardwareProfile("test profile", 14, "8080,z80");

        assertTrue("all ok", HardwareProfileService.isCompatible(profile,
                new Version(10, 15, 16, "6502,z80")));
        assertFalse("wrong nativecode", HardwareProfileService.isCompatible(profile,
                new Version(10, 15, 16, "6502,8ß85")));
        assertFalse("wrong sdk", HardwareProfileService.isCompatible(profile,
                new Version(15, 15, 16, "6502,8ß85")));
    }

    @Test
    public void isCompatibleSdk() {
        assertTrue("00", HardwareProfileService.isCompatibleSdk(15, 0, 0));
        assertTrue("all same", HardwareProfileService.isCompatibleSdk(15, 15, 15));
        assertFalse("<", HardwareProfileService.isCompatibleSdk(15, 16, 0));
        assertFalse(">", HardwareProfileService.isCompatibleSdk(15, 0, 14));
    }

    @Test
    public void isCompatibleNativecode() {
        assertTrue("null null", HardwareProfileService.isCompatibleNativecode(null, null));
        assertTrue("z80 null", HardwareProfileService.isCompatibleNativecode(
                StringUtil.toStringArray("8080,z80"), null));
        assertTrue("z80 match", HardwareProfileService.isCompatibleNativecode(
                StringUtil.toStringArray("8080,z80"), StringUtil.toStringArray("6502,z80")));
        assertFalse("z80 mis-match", HardwareProfileService.isCompatibleNativecode(
                StringUtil.toStringArray("8080,z80"), StringUtil.toStringArray("6502,8085")));
    }

    @Test
    public void isCompatibleNativecode_armeCpu_compatible_armAppVersion() {
        HardwareProfile profile = new HardwareProfile();
        profile.setNativecode("armeabi-v7a");
        assertTrue("armeabi-v7a device is compatible with armabi-v7a app", HardwareProfileService.isCompatibleNativecode(
                StringUtil.toStringArray("armabi-v7a"), profile.getNativecodeArray()));
    }

    @Test
    public void calculateAppHardware_found() {
        String nativecode = "8080,z80";
        HardwareProfile profile = new HardwareProfile("test profile", 14, nativecode);

        List<Version> versions = new ArrayList<>();
        addVersions(versions, 4, 5, nativecode, 4); // sdk-min not matching
        addVersions(versions, 6, 7, "6502", 14); //  nativecode not matching
        addVersions(versions, 8, 9, nativecode, 14); //  all matching
        addVersions(versions, 10, 11, null, 14); //  matching (no nativecode)
        addVersions(versions, 12, 13, nativecode, 30); // sdk-max not matching

        AppHardware result = new AppHardware();
        HardwareProfileService.calculateAppHardware(result, profile, versions);
        assertEquals("min", 8, result.getMin().getVersionCode());
        assertEquals("max", 11, result.getMax().getVersionCode());
    }

    @Test
    public void calculateAppHardware_notFound() {
        String nativecode = "8080,z80";
        HardwareProfile profile = new HardwareProfile("test profile", 14, nativecode);

        List<Version> versions = new ArrayList<>();
        addVersions(versions, 4, 5, nativecode, 4); // sdk-min not matching
        addVersions(versions, 6, 7, "6502", 14); //  nativecode not matching
        addVersions(versions, 12, 13, nativecode, 30); // sdk-max not matching

        AppHardware result = new AppHardware();
        HardwareProfileService.calculateAppHardware(result, profile, versions);
        assertEquals("min", 0, result.getMin().getVersionCode());
        assertEquals("max", 0, result.getMax().getVersionCode());
    }

    private List<Version> addVersions(List<Version> result, int min, int max, String nativecode, int sdk) {
        for (int i = min; i <= max; i++) {
            Version version = TestDataGenerator.fill(new Version(), i);
            version.setSdk(sdk, sdk, sdk);
            version.setNativecode(nativecode);
            result.add(version);
        }
        return result;
    }

}