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
package de.k3b.fdroid.jpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.domain.service.TranslationService;

/**
 * Database Repository Instrumented test, which will execute in Spring/JPA.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 */

@RunWith(SpringRunner.class)
@DataJpaTest
public class TranslationRepositoryTest {
    // testdata
    private static final String MY_TYP = TranslationService.TYP_REPOSITORY_NAME;
    private static final String OTHER_TYP = MY_TYP + "_";
    private static final String[] SEARCH_TYPS = {"aa", MY_TYP, "bb"};
    private static final String MY_LOCALE = "zz";
    private static final String OTHER_LOCALE = "zq";
    private static final String[] SEARCH_LOCALES = {"QQ", MY_LOCALE, "RR"};

    private static final int MY_ID = 42;
    private static final int OTHER_ID = MY_ID + 1;

    // JPA specific
    @Autowired
    JpaTestHelper testHelper;

    @Autowired
    private TranslationRepository repo;

    @BeforeEach
    public void setUp() {
        // JPA Test specific

        // common code
        testHelper.createLocale(MY_LOCALE).getId();
        testHelper.createLocale(OTHER_LOCALE).getId();

        // exact match
        testHelper.createTranslation(MY_TYP, MY_ID, MY_LOCALE);

        // one vale differ from exact match
        testHelper.createTranslation(OTHER_TYP, MY_ID, MY_LOCALE);
        testHelper.createTranslation(MY_TYP, OTHER_ID, MY_LOCALE);
        testHelper.createTranslation(MY_TYP, MY_ID, OTHER_LOCALE);
    }

    @Test
    public void findByTyp() {
        assertEquals(3, repo.findByTyp(MY_TYP).size());
    }

    @Test
    public void findByTypAndId() {
        assertEquals(2, repo.findByTypAndId(MY_TYP, MY_ID).size());
    }

    @Test
    public void findByTypAndIdAndLocales() {
        assertEquals(1, repo.findByTypAndIdAndLocales(MY_TYP, MY_ID, MY_LOCALE).size());
    }

    @Test
    public void findByTypsAndLocaleIds() {
        assertEquals(2, repo.findByTypsAndLocales(SEARCH_TYPS, SEARCH_LOCALES).size());
    }

    @Test
    public void findall() {
        assertEquals(4, repo.findAll().size());
    }

    @Test
    public void deleteByTyp() {
        repo.deleteByTyp(MY_TYP);
        assertEquals(1, repo.findAll().size());
    }

    @Test
    public void deleteByTypAndId() {
        repo.deleteByTypAndId(MY_TYP, MY_ID);
        assertEquals(2, repo.findAll().size());
    }

    @Test
    public void deleteByTypAndIdAndLocale() {
        repo.deleteByTypAndIdAndLocale(MY_TYP, MY_ID, MY_LOCALE);
        assertEquals(3, repo.findAll().size());
    }

    @Test
    public void deleteTypAndLocale() {
        repo.deleteByTypAndLocale(MY_TYP, MY_LOCALE);
        assertEquals(2, repo.findAll().size());
    }
}