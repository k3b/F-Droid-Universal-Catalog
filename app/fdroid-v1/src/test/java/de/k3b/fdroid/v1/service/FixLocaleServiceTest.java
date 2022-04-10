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

import java.util.Map;

import de.k3b.fdroid.v1.domain.App;
import de.k3b.fdroid.v1.domain.Localized;

public class FixLocaleServiceTest {

    @Test
    public void fix() {
        App app = new App();
        addLocalized(app.getLocalized(),
                "ar-SA", "zh-TW",
                "de", "en-us", "de-DE", "de-rDE");

        new FixLocaleService().fix(app);

        String expected = "App[localized={ar:Localized[name=ar-SA]" +
                ",de:Localized[name=de-rDE],en:Localized[name=en-us]" +
                ",zh-TW:Localized[name=zh-TW]}]";
        Assert.assertEquals(expected, app.toString());
    }

    private void addLocalized(Map<String, Localized> localized, String... locales) {
        for(String locale : locales) {
            Localized l = new Localized();
            l.setName(locale);
            localized.put(locale, l);
        }
    }
}