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


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import java.util.List;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.RepoRepository;

@DataJpaTest
public class V1RepoRepositoryTest {
    private static String myAddress = "my.package.name";

    @Autowired
    JpaTestHelper jpaTestHelper;

    @Autowired
    private RepoRepository repo;

    @BeforeEach
    public void init() {
        Repo r = jpaTestHelper.createRepo();
        myAddress = r.getAddress();
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(repo, "repo");
        Assert.notNull(jpaTestHelper, "jpaTestHelper");
    }

    @Test
    public void findByAddress() {
        Repo r = repo.findByAddress(myAddress);
        Assert.notNull(r, "found");
    }

    @Test
    public void findByBusy() {
        jpaTestHelper.createRepo();
        Repo r = jpaTestHelper.createRepo();
        r.setDownloadTaskId("notEmpty");
        jpaTestHelper.save(r);

        List<Repo> repoList = repo.findByBusy();
        assertThat(repoList.size(), is(1));
    }

    @Test
    public void findByAppId() {
        App app = jpaTestHelper.createApp();
        Repo r = jpaTestHelper.createRepo();
        jpaTestHelper.createVersion(app, r);

        Repo found = repo.findFirstByAppId(app.getId());
        assertThat(found, is(r));
    }
}