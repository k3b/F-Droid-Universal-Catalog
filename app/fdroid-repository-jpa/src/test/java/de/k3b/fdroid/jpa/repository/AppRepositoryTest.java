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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.interfaces.AppRepository;

@DataJpaTest
public class AppRepositoryTest {
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    private static final int MY_REPO_ID = 47110815;

    @Autowired
    private AppRepositoryJpa jpa;
    @Autowired
    private AppRepository repo;

    private int id = 0;

    @BeforeEach
    public void init() {
        App app = new App();
        app.repoId = MY_REPO_ID;
        app.setPackageName(MY_PACKAGE_NAME);
        app.setIcon(MY_ICON);
        repo.insert(app);
        id = app.id;
    }

    @AfterEach
    public void finish() {
        jpa.deleteById(id);
        id = 0;
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(jpa, "jpa");
        Assert.notNull(repo, "repo");
    }

    @Test
    public void findByRepoIdAndPackageName() {
        App app = repo.findByRepoIdAndPackageName(MY_REPO_ID, MY_PACKAGE_NAME);
        Assert.notNull(app, "found");
    }
}