/*
 * Copyright (c) 2023 by k3b.
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
package de.k3b.fdroid.android.repository;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.entity.Locale;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.util.TestHelper;

/**
 * Database Repository Instrumented test, which will execute on an Android device.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LocalizedRepositoryInstrumentedTest {
    // testdata
    private static final String MY_Summary = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    // JPA specific
    TestHelper testHelper;
    private int appId;
    private String localeId;
    private LocalizedRepository repo;

    private void setupAndroid() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        FDroidDatabaseFactory factory = FDroidDatabase.getINSTANCE(context, true);
        repo = factory.localizedRepository();

        testHelper = new TestHelper(new RoomFDroidDatabaseFacade(factory));
    }

    @Before
    public void setUp() {
        setupAndroid();

        appId = testHelper.createApp().getId();

        Locale locale = testHelper.createLocale("@+");
        locale.setLanguagePriority(5);
        localeId = testHelper.save(locale).getId();

        Localized localized = new Localized(appId, localeId);
        localized.setSummary(MY_Summary);
        localized.setName("@+");
        localized.setIcon(MY_ICON);
        repo.insert(localized);
    }

    @Test
    public void findByAppId() {
        List<Localized> localized = repo.findByAppId(appId);
        assertEquals(1, localized.size());
    }

    @Test
    public void findByAppIdAndLocaleIds_found() {
        List<Localized> localized = repo.findByAppIdAndLocaleIds(appId, Collections.singletonList(localeId));
        assertEquals(1, localized.size());
    }

    @Test
    public void findByAppIdAndLocaleIds_notfound() {
        List<Localized> localized = repo.findByAppIdAndLocaleIds(appId, Collections.singletonList(""));
        assertEquals(0, localized.size());
    }

    @Test
    public void findNonHiddenByAppIds() {
        int a2 = testHelper.createApp().getId();

        Locale l2 = testHelper.createLocale("@-");
        l2.setLanguagePriority(LanguageService.LANGUAGE_PRIORITY_HIDDEN);
        testHelper.save(l2);

        Localized al2 = new Localized(a2, l2.getId());
        al2.setName("@-");
        testHelper.save(al2);

        List<Localized> localized = repo.findNonHiddenByAppIds(Arrays.asList(appId, a2));
        assertEquals("found 1", 1, localized.size());
        assertEquals("#1 ok", "@+", localized.get(0).getName());
    }
}