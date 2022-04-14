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
import java.util.List;
import java.util.Optional;

import de.k3b.fdroid.domain.Locale;
import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.service.LanguageService;

@DataJpaTest
public class LocalizedRepositoryTest {
    private static final String MY_Summary = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    private static final int MY_APP_ID = 47110815;
    private static final int MY_LOCALE_ID = 47110816;

    @Autowired
    private LocalizedRepositoryJpa jpa;
    @Autowired
    private LocalizedRepository repo;
    @Autowired
    private LocaleRepositoryJpa localeJpa;

    private int id;

    @BeforeEach
    public void init() {
        jpa.deleteAll();
        Localized localized = new Localized();
        localized.setAppId(MY_APP_ID);
        localized.setLocaleId(MY_LOCALE_ID);
        localized.setSummary(MY_Summary);
        localized.setIcon(MY_ICON);
        repo.insert(localized);
        id = localized.getId();
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(jpa, "jpa");
        Assert.notNull(repo, "repo");
        Assert.notNull(localeJpa, "localeJpa");
    }

    @Test
    public void findById() {
        Optional<Localized> found = jpa.findById(this.id);
        Assert.isTrue(found.isPresent(), "found 1");
    }

    @Test
    public void findByAppId() {
        List<Localized> localized = repo.findByAppId(MY_APP_ID);
        Assert.isTrue(localized.size() == 1, "found 1");
    }

    @Test
    public void findByAppIdAndLocaleIds_found() {
        List<Localized> localized = repo.findByAppIdAndLocaleIds(MY_APP_ID, Arrays.asList(MY_LOCALE_ID));
        Assert.isTrue(localized.size() == 1, "found 1");
    }

    @Test
    public void findByAppIdAndLocaleIds_notfound() {
        List<Localized> localized = repo.findByAppIdAndLocaleIds(MY_APP_ID, Arrays.asList(MY_LOCALE_ID - 1000));
        Assert.isTrue(localized.size() == 0, "found 0");
    }

    @Test
    public void findNonHiddenByAppIds() {
        Locale l1 = new Locale();
        Localized al1 = new Localized();
        Locale l2 = new Locale();
        Localized al2 = new Localized();

        try {
            l1.setCode("@+");
            l1.setLanguagePriority(5);
            localeJpa.save(l1);
            al1.setAppId(MY_APP_ID);
            al1.setLocaleId(l1.getId());
            al1.setName("@+");
            repo.insert(al1);

            l2.setCode("@-");
            l2.setLanguagePriority(LanguageService.LANGUAGE_PRIORITY_HIDDEN);
            localeJpa.save(l2);
            al2.setAppId(MY_APP_ID + 1);
            al2.setLocaleId(l2.getId());
            al2.setName("@-");
            repo.insert(al2);

            List<Localized> localized = repo.findNonHiddenByAppIds(Arrays.asList(MY_APP_ID, MY_APP_ID + 1));
            Assert.isTrue(localized.size() == 1, "found 1");
            Assert.isTrue(localized.get(0).getName().equals("@+"), "#1 ok");
        } finally {
            repo.delete(al2);
            localeJpa.delete(l2);
            repo.delete(al1);
            localeJpa.delete(l1);
        }
    }
}