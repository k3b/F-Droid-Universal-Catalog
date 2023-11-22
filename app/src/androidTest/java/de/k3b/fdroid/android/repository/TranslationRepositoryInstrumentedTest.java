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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.domain.service.TranslationService;
import de.k3b.fdroid.domain.util.TestHelper;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class TranslationRepositoryInstrumentedTest {
    public static final String TYP = TranslationService.TYP_REPOSITORY_NAME;
    TestHelper testHelper;
    private String localeId;
    private int appId;
    private TranslationRepository translationRepository;

    private FDroidDatabaseFactory factory = null;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        factory = FDroidDatabase.getINSTANCE(context, true);
        translationRepository = factory.translationRepository();

        testHelper = new TestHelper(new RoomFDroidDatabaseFacade(factory));
        this.localeId = testHelper.createLocale("zz").getId();
        this.appId = testHelper.createApp().getAppId();

        // found
        testHelper.createTranslation(TYP, appId, localeId);

        // other-non-found
        testHelper.createTranslation(TYP + "_", appId, localeId);
        testHelper.createTranslation(TYP, appId + 1, localeId);
        testHelper.createTranslation(TYP, appId, localeId + '_');
    }

    @After
    public void finish() {
    }

    @Test
    public void findByTyp() {
        assertEquals(3, translationRepository.findByTyp(TYP).size());
    }

    @Test
    public void findByTypAndId() {
        assertEquals(2, translationRepository.findByTypAndId(TYP, appId).size());
    }

    @Test
    public void findByTypAndIdAndLocalesId() {
        assertEquals(1, translationRepository.findByTypAndIdAndLocales(TYP, appId, localeId).size());
    }

    @Test
    public void findByTypsAndLocaleIds() {

        String[] typs = {"aa", TYP, "bb"};
        assertEquals(2, translationRepository.findByTypsAndLocales(typs, "QQ", localeId, "RR").size());
    }
}