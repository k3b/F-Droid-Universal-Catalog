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

package de.k3b.fdroid.v1.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import de.k3b.fdroid.v1.domain.Localized;

public class PropertyMergerTest {
    private final PropertyMerger sut = new PropertyMerger();

    @Test
    public void merge() {
        Localized result = new Localized();
        result.setName("name not overwritten");// (1)
        // summery null (2)
        result.setDescription("must overwrite");// (3)
        result.setPhoneScreenshots(Arrays.asList("replaced")); // (4)

        // (1) shorter than existing
        Localized ni = new Localized();ni.setName("ignore");

        // (2) overwrite null or empty
        Localized s = new Localized();s.setSummary("Summary overwrite null");

        // (3) new value is longer than old
        Localized d = new Localized();d.setDescription("Description longer than existing");

        // (4) longer list replaces existing shorter list
        Localized r = new Localized();r.setPhoneScreenshots(Arrays.asList("phoneShots1","phoneShots2"));

        result = sut.merge(Arrays.asList(result, ni, d, s, r));

        Assert.assertEquals("name not overwritten", result.getName());
        Assert.assertEquals("Summary overwrite null", result.getSummary());
        Assert.assertEquals("Description longer than existing", result.getDescription());
        Assert.assertEquals("PhoneScreenshots().size()", 2, result.getPhoneScreenshots().size());
    }
}