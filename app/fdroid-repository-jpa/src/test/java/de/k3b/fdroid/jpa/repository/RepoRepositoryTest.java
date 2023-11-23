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
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.RepoRepository;

/**
 * Database Repository Instrumented test, which will execute in Spring/JPA.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class RepoRepositoryTest {
    // testdata
    private static String myAddress;
    // i.e: "my.package.name";

    // JPA specific
    @Autowired
    JpaTestHelper testHelper;

    @Autowired
    private RepoRepository repo;

    @BeforeEach
    public void setUp() {
        Repo r = testHelper.createRepo();
        myAddress = r.getAddress();
    }

    @Test
    public void findByAddress() {
        Repo r = repo.findByAddress(myAddress);
        assertThat(r, is(notNullValue()));
    }

    @Test
    public void findByBusy() {
        testHelper.createRepo();
        Repo r = testHelper.createRepo();
        r.setDownloadTaskId("notEmpty");
        testHelper.save(r);

        List<Repo> repoList = repo.findByBusy();
        assertThat(repoList.size(), is(1));
    }

    @Test
    public void findByAppId() {
        App app = testHelper.createApp();
        Repo r = testHelper.createRepo();
        testHelper.createVersion(app, r);

        Repo found = repo.findFirstByAppId(app.getId());
        assertThat(found.getId(), is(r.getId()));
    }
}