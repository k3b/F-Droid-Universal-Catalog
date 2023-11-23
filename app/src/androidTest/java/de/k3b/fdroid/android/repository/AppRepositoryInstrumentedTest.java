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
import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppCategory;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.util.TestHelper;

/**
 * Database Repository Instrumented test, which will execute on an Android device.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AppRepositoryInstrumentedTest {
    public static final int SDK = 8;
    // testdata
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    TestHelper testHelper;
    private int appId;
    private int categoryId;
    // JPA specific
    private AppRepository repo;

    private void setupAndroid() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        FDroidDatabaseFactory factory = FDroidDatabase.getINSTANCE(context, true);
        repo = factory.appRepository();

        testHelper = new TestHelper(new RoomFDroidDatabaseFacade(factory));
    }

    @Before
    public void setUp() {
        setupAndroid();
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