/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either translation 3 of the License, or
 * (at your option) any later translation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package de.k3b.fdroid.android.repository;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.domain.service.TranslationService;
import de.k3b.fdroid.domain.util.TestHelper;

/**
 * Database Repository Instrumented test, which will execute on an Android device.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TranslationRepositoryInstrumentedTest {
    // testdata
    private static final String MY_TYP = TranslationService.TYP_REPOSITORY_NAME;
    private static final String OTHER_TYP = MY_TYP + "_";
    private static final String[] SEARCH_TYPS = {"aa", MY_TYP, "bb"};
    private static final String MY_LOCALE = "zz";
    private static final String OTHER_LOCALE = "zq";
    private static final String[] SEARCH_LOCALES = {"QQ", MY_LOCALE, "RR"};

    private static final int MY_ID = 42;
    private static final int OTHER_ID = MY_ID + 1;

    // Android Room Test specific
    TestHelper testHelper;
    private TranslationRepository repo;

    private FDroidDatabaseFactory factory = null;

    private void setupAndroid() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        factory = FDroidDatabase.getINSTANCE(context, true);
        repo = factory.translationRepository();
        testHelper = new TestHelper(new RoomFDroidDatabaseFacade(factory));
    }

    @Before
    public void setUp() {
        setupAndroid();

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
    public void deleteByIdAndTyps() {
        repo.deleteByTypAndId(MY_TYP, MY_ID);
        assertEquals(2, repo.findAll().size());
    }

    @Test
    public void deleteByIdAndTypsAndLocales() {
        repo.deleteByTypAndIdAndLocale(MY_TYP, MY_ID, MY_LOCALE);
        assertEquals(3, repo.findAll().size());
    }

    @Test
    public void deleteTypsAndLocales() {
        repo.deleteByTypAndLocale(MY_TYP, MY_LOCALE);
        assertEquals(2, repo.findAll().size());
    }
}