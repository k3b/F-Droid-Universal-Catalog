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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.k3b.fdroid.domain.HardwareProfile;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.util.StringUtil;

public class HardwareProfileServiceTest {

    @Test
    public void isCompatible_happyDay() {
        HardwareProfile profile = new HardwareProfile("test profile");
        profile.setSdkVersion(14);
        profile.setNativecode("8080,z80");

        Version version = new Version();
        version.setSdk(10, 15, 16);
        version.setNativecode("6502,z80");

        assertEquals(true, HardwareProfileService.isCompatible(profile, version));
    }

    @Test
    public void isCompatibleSdk() {
        assertEquals("00", true, HardwareProfileService.isCompatibleSdk(15, 0, 0));
        assertEquals("all same", true, HardwareProfileService.isCompatibleSdk(15, 15, 15));
        assertEquals("<", false, HardwareProfileService.isCompatibleSdk(15, 16, 0));
        assertEquals(">", false, HardwareProfileService.isCompatibleSdk(15, 0, 14));
    }

    @Test
    public void isCompatibleNativecode() {
        assertEquals("null null", true, HardwareProfileService.isCompatibleNativecode(null, null));
        assertEquals("z80 null", true, HardwareProfileService.isCompatibleNativecode(
                StringUtil.toStringArray("8080,z80"), null));
        assertEquals("z80 match", true, HardwareProfileService.isCompatibleNativecode(
                StringUtil.toStringArray("8080,z80"), StringUtil.toStringArray("6502,z80")));
        assertEquals("z80 mis-match", false, HardwareProfileService.isCompatibleNativecode(
                StringUtil.toStringArray("8080,z80"), StringUtil.toStringArray("6502,8085")));
    }

}