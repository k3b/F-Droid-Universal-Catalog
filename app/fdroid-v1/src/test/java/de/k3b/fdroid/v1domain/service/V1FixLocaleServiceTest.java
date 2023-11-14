/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid.v1domain the fdroid json catalog-format-v1 parser.
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

package de.k3b.fdroid.v1domain.service;

import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import de.k3b.fdroid.v1domain.entity.V1App;
import de.k3b.fdroid.v1domain.entity.V1Localized;

public class V1FixLocaleServiceTest {

    @Test
    public void fix() {
        V1App v1App = new V1App();
        addLocalized(v1App.getLocalized(),
                "ar-SA", "zh-TW",
                "de", "en-us", "de-DE", "de-rDE");

        new V1FixLocaleService().fix(v1App);

        String expected = "V1App[localized={" +
                "ar:V1Localized[name=ar-SA]," +
                "de:V1Localized[name=de-rDE]," +
                "en:V1Localized[name=en-us]," +
                "zh-TW:V1Localized[name=zh-TW]}]";
        Assert.assertEquals(expected, v1App.toString());
    }

    private void addLocalized(Map<String, V1Localized> localized, String... locales) {
        for (String locale : locales) {
            V1Localized l = new V1Localized();
            l.setName(locale);
            localized.put(locale, l);
        }
    }
}