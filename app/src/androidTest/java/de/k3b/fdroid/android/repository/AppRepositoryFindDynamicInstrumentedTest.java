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
package de.k3b.fdroid.android.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppAntiFeature;
import de.k3b.fdroid.domain.entity.AppCategory;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.AntiFeatureRepository;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.CategoryRepository;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.util.TestHelper;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AppRepositoryFindDynamicInstrumentedTest {
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    public static final int SDK = 8;
    Repo repo = null;
    private AppRepository appRepository;

    private App app = null;
    private RepoRepository repoRepository;
    private TestHelper testHelper;
    private int categoryId;
    private int antiFeatureId;
    private CategoryRepository categoryRepository;
    private AntiFeatureRepository antiFeatureRepository;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        FDroidDatabaseFactory factory = FDroidDatabase.getINSTANCE(context, true);
        appRepository = factory.appRepository();
        repoRepository = factory.repoRepository();
        categoryRepository = factory.categoryRepository();
        antiFeatureRepository = factory.antiFeatureRepository();

        testHelper = new TestHelper(new RoomFDroidDatabaseFacade(factory));
        app = testHelper.createApp(MY_PACKAGE_NAME, MY_ICON);
        repo = testHelper.createRepo();
        testHelper.createVersion(app, repo, SDK, SDK, 0, null);
        AppCategory appCategory = testHelper.createAppCategory(app, null);
        categoryId = appCategory.getCategoryId();
        AppAntiFeature appAntiFeature = testHelper.createAppAntiFeature(app, null);
        antiFeatureId = appAntiFeature.getAntiFeatureId();

    }

    @After
    public void finish() {
        appRepository.delete(app);
        repoRepository.delete(repo);
        categoryRepository.deleteById(categoryId);
        antiFeatureRepository.deleteById(antiFeatureId);
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