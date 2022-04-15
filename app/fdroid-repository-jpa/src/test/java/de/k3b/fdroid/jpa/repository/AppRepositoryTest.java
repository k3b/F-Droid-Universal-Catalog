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

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.interfaces.AppRepository;

@DataJpaTest
public class AppRepositoryTest {
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    @Autowired
    JpaTestHelper jpaTestHelper;
    private int repoId;
    private int appId;

    @Autowired
    private AppRepository repo;

    @BeforeEach
    public void init() {
        repoId = jpaTestHelper.createRepo().getId();
        App app = new App(repoId);
        app.setPackageName(MY_PACKAGE_NAME);
        app.setIcon(MY_ICON);
        repo.insert(app);
        appId = app.getId();
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(repo, "repo");
        Assert.notNull(jpaTestHelper, "jpaTestHelper");
    }

    @Test
    public void findByRepoIdAndPackageName() {
        App app = repo.findByRepoIdAndPackageName(repoId, MY_PACKAGE_NAME);
        Assert.notNull(app, "found");
    }

    @Test
    public void findByIds() {
        List<App> apps = repo.findByIds(Arrays.asList(appId));
        Assert.notNull(apps, "found");
        Assert.isTrue(apps.size() == 1, "found 1");
    }


    @Test
    public void findIdsByExpression() {
        List<Integer> apps = repo.findIdsByExpression("acka my");
        Assert.notNull(apps, "found");
        Assert.isTrue(apps.size() == 1, "found 1");
    }
}