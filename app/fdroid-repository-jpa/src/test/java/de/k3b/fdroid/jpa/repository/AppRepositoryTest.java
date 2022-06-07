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

import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppCategory;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.VersionRepository;

@DataJpaTest
public class AppRepositoryTest {
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    public static final int SDK = 8;

    @Autowired
    JpaTestHelper testHelper;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private VersionRepository versionRepository;

    private int appId;
    private int categoryId;

    @BeforeEach
    public void init() {
        App app = testHelper.createApp(MY_PACKAGE_NAME, MY_ICON);
        appId = app.getId();

        Repo repo = testHelper.createRepo();
        testHelper.createVersion(
                app, repo, SDK, SDK, 0, null);

        AppCategory appCategory = testHelper.createAppCategory(app, null);
        categoryId = appCategory.getCategoryId();
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(appRepository, "repo");
        Assert.notNull(testHelper, "jpaTestHelper");
    }

    @Test
    public void findByRepoIdAndPackageName() {
        App app = appRepository.findByPackageName(MY_PACKAGE_NAME);
        Assert.notNull(app, "found");
    }

    @Test
    public void findByIds() {
        List<App> appIdList = appRepository.findByIds(Collections.singletonList(appId));
        Assert.notNull(appIdList, "found");
        Assert.isTrue(appIdList.size() == 1, "found 1");
    }

    @Test
    public void findDynamic_search() {
        AppSearchParameter searchParameter = new AppSearchParameter()
                .searchText("acka my");
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        Assert.notNull(appIdList, "found");
        Assert.isTrue(appIdList.size() == 1, "found 1");
    }

    @Test
    public void findDynamic_version() {
        // additional version not found
        testHelper.createVersion(
                null, null, SDK - 1, SDK - 1, SDK - 1, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .versionSdk(SDK);
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        Assert.notNull(appIdList, "found");
        Assert.isTrue(appIdList.size() == 1, "found 1");
    }

    @Test
    public void findDynamic_category() {
        // additional app/category not found
        testHelper.createAppCategory(null, null);

        AppSearchParameter searchParameter = new AppSearchParameter()
                .categoryId(categoryId);
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        Assert.notNull(appIdList, "found");
        Assert.isTrue(appIdList.size() == 1, "found 1");
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
        List<Integer> appIdList = appRepository.findDynamic(searchParameter);
        Assert.notNull(appIdList, "found");
        Assert.isTrue(appIdList.size() == 1, "found 1");
    }

    @Test
    public void findDynamic_default() {
        List<Integer> appIdList = appRepository.findDynamic(new AppSearchParameter());
        Assert.notNull(appIdList, "found");
        Assert.isTrue(appIdList.size() == 1, "found 1");
    }
}