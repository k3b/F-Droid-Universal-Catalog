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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.domain.entity.App;
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
public class AppRepositoryTest {
    // testdata
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    public static final int SDK = 8;
    // JPA specific
    @Autowired
    JpaTestHelper testHelper;
    private int appId;
    private int categoryId;
    @Autowired
    private AppRepository repo;

    @BeforeEach
    public void setUp() {
        App app = testHelper.createApp(MY_PACKAGE_NAME, MY_ICON);
        appId = app.getId();

        Repo repo = testHelper.createRepo();
        testHelper.createVersion(
                app, repo, SDK, SDK, 0, null);

        AppCategory appCategory = testHelper.createAppCategory(app, null);
        categoryId = appCategory.getCategoryId();
    }

    @Test
    public void findByRepoIdAndPackageName() {
        App app = repo.findByPackageName(MY_PACKAGE_NAME);
        assertNotNull(app);
    }

    @Test
    public void findByIds() {
        List<App> appIdList = repo.findByIds(Collections.singletonList(appId));
        assertNotNull(appIdList);
        assertEquals(1, appIdList.size());
    }

    @Test
    public void findDynamic_search() {
        AppSearchParameter searchParameter = new AppSearchParameter()
                .searchText("acka my");
        List<Integer> appIdList = repo.findDynamic(searchParameter);
        assertNotNull(appIdList);
        assertEquals(1, appIdList.size());
    }

    @Test
    public void findDynamic_version() {
        // additional version not found
        testHelper.createVersion(
                null, null, SDK - 1, SDK - 1, SDK - 1, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .versionSdk(SDK);
        List<Integer> appIdList = repo.findDynamic(searchParameter);
        assertNotNull(appIdList);
        assertEquals(1, appIdList.size());
    }

    @Test
    public void findDynamic_category() {
        // additional app/category not found
        testHelper.createAppCategory(null, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .categoryId(categoryId);
        List<Integer> appIdList = repo.findDynamic(searchParameter);
        assertNotNull(appIdList);
        assertEquals(1, appIdList.size());
    }

    @Test
    public void findDynamic_searchPlusVersionPlusCategory() {
        // additional version not found
        testHelper.createVersion(
                null, null, SDK - 1, SDK - 1, SDK - 1, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .searchText("acka my")
                .categoryId(categoryId)
                .versionSdk(SDK);
        List<Integer> appIdList = repo.findDynamic(searchParameter);
        assertNotNull(appIdList);
        assertEquals(1, appIdList.size());
    }

    @Test
    public void findDynamic_default() {
        List<Integer> appIdList = repo.findDynamic(new AppSearchParameter());
        assertNotNull(appIdList);
        assertEquals(1, appIdList.size());
    }
}