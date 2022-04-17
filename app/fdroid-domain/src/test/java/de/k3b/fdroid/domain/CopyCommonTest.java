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

package de.k3b.fdroid.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.k3b.fdroid.domain.common.AppCommon;
import de.k3b.fdroid.domain.common.LocalizedCommon;
import de.k3b.fdroid.domain.common.ProfileCommon;
import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.domain.common.VersionCommon;
import de.k3b.fdroid.util.TestDataGenerator;

/**
 * verifies that all vales from TestDataGenerator.fill(src) are copied via Xxx.copyCommon
 */
public class CopyCommonTest {

    @Test
    public void appCommon() {
        AppCommon src = TestDataGenerator.fill(new AppCommon(), 4);
        AppCommon dest = new AppCommon();
        AppCommon.copyCommon(dest, src);

        assertEquals(dest.toString(), src.toString());
    }

    @Test
    public void localizedCommon() {
        LocalizedCommon src = TestDataGenerator.fill(new LocalizedCommon(), 4);
        LocalizedCommon dest = new LocalizedCommon();
        LocalizedCommon.copyCommon(dest, src);

        assertEquals(dest.toString(), src.toString());
    }

    @Test
    public void profileCommon() {
        ProfileCommon src = TestDataGenerator.fill(new ProfileCommon(), 4);
        ProfileCommon dest = new ProfileCommon();
        ProfileCommon.copyCommon(dest, src);

        assertEquals(dest.toString(), src.toString());
    }

    @Test
    public void repoCommon() {
        RepoCommon src = TestDataGenerator.fill(new RepoCommon(), 4);
        RepoCommon dest = new RepoCommon();
        RepoCommon.copyCommon(dest, src);

        assertEquals(dest.toString(), src.toString());
    }

    @Test
    public void versionCommon() {
        VersionCommon src = TestDataGenerator.fill(new VersionCommon(), 4);
        VersionCommon dest = new VersionCommon();
        VersionCommon.copyCommon(dest, src);

        assertEquals(dest.toString(), src.toString());
    }
}