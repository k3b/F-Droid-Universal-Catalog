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

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import de.k3b.fdroid.domain.Locale;
import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.service.LanguageService;

public class LocalizedUpdateServiceTest {

    @Test
    public void sortByPrioDesc() {
        LanguageService languageService = new LanguageService(null);

        // assume that id==prio
        languageService.init(Arrays.asList(createLocale(1,"en"), createLocale(99,"de")));
        LocalizedUpdateService sut = new LocalizedUpdateService(null, languageService);

        List<Localized> localizedList = Arrays.asList(createLocalized(1, "en"), createLocalized(99, "de"));

        Localized[] sorted = sut.sortByPrioDesc(localizedList);
        assertTrue(sorted[0].getId() > sorted[1].getId());
    }

    private Locale createLocale(int id, String code) {
        Locale l = new Locale();
        l.setId(id);
        l.setLanguagePriority(id);
        l.setCode(code);
        return l;
    }


    private Localized createLocalized(int id, String code) {
        Localized l = new Localized();
        l.setId(id);
        l.setLocaleId(id);
        return l;
    }
}