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
package de.k3b.fdroid.jpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import de.k3b.fdroid.domain.entity.AppAntiFeature;
import de.k3b.fdroid.domain.repository.AppAntiFeatureRepository;

/**
 * Database Repository Instrumented test, which will execute in Spring/JPA.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AppAntiFeatureRepositoryTest {
    @Autowired
    JpaTestHelper testHelper;
    // testdata
    private int appId;
    private int antiFeatureId;
    // JPA specific
    @Autowired
    private AppAntiFeatureRepository repo;

    @BeforeEach
    public void setUp() {
        appId = testHelper.createApp().getId();
        antiFeatureId = testHelper.createAntiFeature().getId();

        AppAntiFeature appAntiFeature = new AppAntiFeature(appId, antiFeatureId);
        repo.insert(appAntiFeature);
    }

    @Test
    public void findByAppId() {
        List<AppAntiFeature> list = repo.findByAppId(appId);
        assertEquals(1, list.size());
    }
}