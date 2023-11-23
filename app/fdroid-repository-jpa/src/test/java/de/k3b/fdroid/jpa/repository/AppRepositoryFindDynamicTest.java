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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppAntiFeature;
import de.k3b.fdroid.domain.entity.AppCategory;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.AppRepository;

/**
 * Database Repository Instrumented test, which will execute in Spring/JPA.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AppRepositoryFindDynamicTest {
    public static final int SDK = 8;
    // testdata
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    @Autowired
    JpaTestHelper testHelper;
    private Repo repo = null;
    private App app = null;
    private int categoryId;
    private int antiFeatureId;
    // Android Room Test specific
    @Autowired
    private AppRepository appRepository;

    @BeforeEach
    public void setUp() {
        Repo r = testHelper.createRepo();

        app = testHelper.createApp(MY_PACKAGE_NAME, MY_ICON);
        repo = testHelper.createRepo();
        testHelper.createVersion(app, repo, SDK, SDK, 0, null);
        AppCategory appCategory = testHelper.createAppCategory(app, null);
        categoryId = appCategory.getCategoryId();
        AppAntiFeature appAntiFeature = testHelper.createAppAntiFeature(app, null);
        antiFeatureId = appAntiFeature.getAntiFeatureId();

    }

    @Test
    public void findDynamic_text() {
        AppSearchParameter searchParameter = new AppSearchParameter()
                .searchText("acka my");
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        assertThat(appIdList.size(), equalTo(1));
    }

    @Test
    public void findDynamic_version() {
        // additional version not found
        testHelper.createVersion(
                null, null, SDK - 1, SDK - 1, SDK - 1, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .versionSdk(SDK);
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        assertThat(appIdList.size(), equalTo(1));
    }

    @Test
    public void findDynamic_category() {
        // additional app/category not found
        testHelper.createAppCategory(app, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .categoryId(categoryId);
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        assertThat(appIdList.size(), equalTo(1));
    }

    @Test
    public void findDynamic_antiFeature() {
        // additional app/antiFeature not found
        testHelper.createAppAntiFeature(app, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .antiFeatureId(antiFeatureId);
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        assertThat(appIdList.size(), equalTo(1));
    }

    @Test
    public void findDynamic_textPlusVersionPlusCategory() {
        // additional version not found
        testHelper.createVersion(
                null, null, SDK - 1, SDK - 1, SDK - 1, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .searchText("acka my")
                .versionSdk(SDK)
                .categoryId(categoryId);
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        assertThat(appIdList.size(), equalTo(1));
    }

    @Test
    public void findDynamic_textPlusVersionPlusAntiFeature() {
        // additional version not found
        testHelper.createVersion(
                null, null, SDK - 1, SDK - 1, SDK - 1, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .searchText("acka my")
                .versionSdk(SDK)
                .antiFeatureId(antiFeatureId);
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        assertThat(appIdList.size(), equalTo(1));
    }

    @Test
    public void findDynamic_noCondition() {
        AppSearchParameter searchParameter = new AppSearchParameter();
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        assertThat(appIdList.size(), equalTo(1));
    }
}
