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
package de.k3b.fdroid.jpa.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.domain.entity.Locale;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.service.LanguageService;

@DataJpaTest
public class LocalizedRepositoryTest {
    private static final String MY_Summary = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    private int appId;
    private int localeId;

    @Autowired
    JpaTestHelper jpaTestHelper;

    @Autowired
    private LocalizedRepository repo;

    @BeforeEach
    public void init() {
        appId = jpaTestHelper.createApp().getId();

        Locale locale = jpaTestHelper.createLocale( "@+");
        locale.setLanguagePriority(5);
        localeId = jpaTestHelper.save(locale).getId();

        Localized localized = new Localized(appId, localeId);
        localized.setSummary(MY_Summary);
        localized.setName("@+");
        localized.setIcon(MY_ICON);
        repo.insert(localized);
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(repo, "repo");
        Assert.notNull(jpaTestHelper, "jpaTestHelper");
    }

    @Test
    public void findByAppId() {
        List<Localized> localized = repo.findByAppId(appId);
        Assert.isTrue(localized.size() == 1, "found 1");
    }

    @Test
    public void findByAppIdAndLocaleIds_found() {
        List<Localized> localized = repo.findByAppIdAndLocaleIds(appId, Collections.singletonList(localeId));
        Assert.isTrue(localized.size() == 1, "found 1");
    }

    @Test
    public void findByAppIdAndLocaleIds_notfound() {
        List<Localized> localized = repo.findByAppIdAndLocaleIds(appId, Collections.singletonList(localeId - 1000));
        Assert.isTrue(localized.size() == 0, "found 0");
    }

    @Test
    public void findNonHiddenByAppIds() {
        int a2 = jpaTestHelper.createApp().getId();

        Locale l2 = jpaTestHelper.createLocale("@-");
        l2.setLanguagePriority(LanguageService.LANGUAGE_PRIORITY_HIDDEN);
        jpaTestHelper.save(l2);

        Localized al2 = new Localized(a2, l2.getId());
        al2.setName("@-");
        jpaTestHelper.save(al2);

        List<Localized> localized = repo.findNonHiddenByAppIds(Arrays.asList(appId, a2));
        Assert.isTrue(localized.size() == 1, "found 1");
        Assert.isTrue(localized.get(0).getName().equals("@+"), "#1 ok");
    }
}