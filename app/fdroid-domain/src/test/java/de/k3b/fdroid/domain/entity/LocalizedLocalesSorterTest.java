/*
 * Copyright (c) 2023 by k3b.
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

package de.k3b.fdroid.domain.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class LocalizedLocalesSorterTest {
    private static final String[] locales = "es,de,en".split(",");
    private static final LocalizedLocalesSorter<Localized> sut = new LocalizedLocalesSorter<>(locales);

    private static final Localized es = createLocalized("es");
    private static final Localized de = createLocalized("de");
    private static final Localized en = createLocalized("en");

    private static final Localized pt = createLocalized("pt");

    private static Localized createLocalized(String locale) {
        Localized l = new Localized();
        l.setLocaleId(locale);
        return l;
    }

    @Test
    @SuppressWarnings("unused")
    public void testCompare() {
        assertTrue(sut.compare(es, de) < 0);
        assertEquals(0, sut.compare(es, es));
        assertTrue(sut.compare(de, es) > 0);
        assertTrue(sut.compare(en, pt) < 0);
    }

    @Test
    public void testSort() {
        List<Localized> localizedList = Arrays.asList(en, pt, de, es);
        localizedList.sort(sut);
        assertEquals("[Localized[localeId=es], Localized[localeId=de], Localized[localeId=en], Localized[localeId=pt]]", localizedList.toString());
    }

}