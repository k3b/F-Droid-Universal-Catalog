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


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import java.util.List;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
@DataJpaTest
public class RepoRepositoryTest {
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
        Version version = jpaTestHelper.createVersion(app, r);

        Repo found = repo.findFirstByAppId(app.getId());
        assertThat(found, is(r));
    }
}