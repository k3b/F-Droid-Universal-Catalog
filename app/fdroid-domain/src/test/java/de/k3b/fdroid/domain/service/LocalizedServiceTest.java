/*
 * Copyright (c) 2022-2023 by k3b.
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
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Locale;
import de.k3b.fdroid.domain.entity.Localized;

public class LocalizedServiceTest {
    private LocalizedService localizedService;
    private LanguageService languageService;

    private Localized localizedEn;
    private Localized localizedDe;

    @Before
    public void init() {
        languageService = new LanguageService(null);
        languageService.init(Collections.emptyList());
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
        languageService.getItemById("pl").setLanguagePriority(0);

        localizedService.recalculateSearchFields(0, app, Arrays.asList(
                localizedEn, localizedDe, pl));
        assertEquals(":de: Name-de | :en: Name-en | :pl: Name-pl", app.getSearchName());
    }

    @Test
    public void createLocalePrefix() {
        assertEquals(":de: ", LocalizedService.createLocalePrefix("de"));
    }

    @Test
    public void removeLocalePrefix() {
        assertEquals("Hello World!", LocalizedService.removeLocalePrefix("Hello World!"));
        assertEquals("Hello World!", LocalizedService.removeLocalePrefix(LocalizedService.createLocalePrefix("de") + "Hello World!"));
    }

    //--------------

    private Locale createLocale(int id, String code) {
        Locale l = new Locale();
        l.setId(code);
        l.setLanguagePriority(id);
        l.setNameEnglish("NameEnglish-" + code);

        languageService.init(l);
        return l;
    }


    private Localized createLocalized(int id, String code) {
        createLocale(id, code);

        Localized l = new Localized();
        l.setId(id);
        l.setLocaleId(code);
        l.setName("Name-" + code);
        l.setSummary("Summary-" + code);
        l.setDescription("Description-" + code);
        l.setWhatsNew("WhatsNew-" + code);
        return l;
    }

}