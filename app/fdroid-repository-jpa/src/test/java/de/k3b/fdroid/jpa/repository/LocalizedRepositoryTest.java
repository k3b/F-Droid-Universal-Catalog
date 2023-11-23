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
package de.k3b.fdroid.jpa.repository;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.domain.entity.Locale;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.service.LanguageService;

/**
 * Database Repository Instrumented test, which will execute in Spring/JPA.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class LocalizedRepositoryTest {
    // testdata
    private static final String MY_Summary = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";

    private int appId;
    private String localeId;

    // JPA specific
    @Autowired
    JpaTestHelper testHelper;

    @Autowired
    private LocalizedRepository repo;

    @BeforeEach
    public void setUp() {
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