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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Locale;
import de.k3b.fdroid.domain.Localized;

public class LocalizedServiceTest {
    private LocalizedService localizedService;
    private LanguageService languageService;

    private Localized localizedEn;
    private Localized localizedDe;

    @Before
    public void init() {
        languageService = new LanguageService(null).init(Arrays.asList());
        localizedService = new LocalizedService(languageService);

        localizedEn = createLocalized(1, "en");
        localizedDe = createLocalized(99, "de");
    }

    @Test
    public void sortByPrioDesc() {
        Localized[] sorted = localizedService.sortByPrioDesc(Arrays.asList(
                localizedEn, localizedDe));

        assertTrue(sorted[0].getId() > sorted[1].getId());
    }


    @Test
    public void recalculateSearchFields_searchNamePrio0_withPrefix() {
        App app = new App();

        Localized pl = createLocalized(2, "pl");
        languageService.getItemById(2).setLanguagePriority(0);

        localizedService.recalculateSearchFields(0, app, Arrays.asList(
                localizedEn, localizedDe, pl));
        assertEquals("Name-de | Name-en | pl: Name-pl", app.getSearchName());
    }
    //--------------

    private Locale createLocale(int id, String code) {
        Locale l = new Locale();
        l.setId(id);
        l.setLanguagePriority(id);
        l.setCode(code);
        l.setNameEnglish("NameEnglish-"+code);

        languageService.init(l);
        return l;
    }


    private Localized createLocalized(int id, String code) {
        createLocale(id, code);

        Localized l = new Localized();
        l.setId(id);
        l.setLocaleId(id);
        l.setName("Name-" + code);
        l.setSummary("Summary-" + code);
        l.setDescription("Description-" + code);
        l.setWhatsNew("WhatsNew-" + code);
        return l;
    }

}