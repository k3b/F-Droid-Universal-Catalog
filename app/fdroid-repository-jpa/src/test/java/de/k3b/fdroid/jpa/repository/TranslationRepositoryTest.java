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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.domain.service.TranslationService;

@DataJpaTest
public class TranslationRepositoryTest {
    public static final String TYP = TranslationService.TYP_REPOSITORY_NAME;
    @Autowired
    JpaTestHelper testHelper;
    private String localeId;
    private int appId;
    @Autowired
    private TranslationRepository translationRepository;

    @BeforeEach
    public void init() {
        this.localeId = testHelper.createLocale("zz").getId();
        this.appId = testHelper.createApp().getAppId();

        // found
        testHelper.createTranslation(TYP, appId, localeId);

        // other-non-found
        testHelper.createTranslation(TYP + "_", appId, localeId);
        testHelper.createTranslation(TYP, appId + 1, localeId);
        testHelper.createTranslation(TYP, appId, localeId + '_');
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
    public void findByTypAndIdAndLocaleId() {
        assertEquals(1, translationRepository.findByTypAndIdAndLocales(TYP, appId, localeId).size());
    }

    @Test
    public void findByTypsAndLocaleIds() {

        String[] typs = {"aa", TYP, "bb"};
        assertEquals(2, translationRepository.findByTypsAndLocales(typs, "QQ", localeId, "RR").size());
    }
}