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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.util.TestHelper;

/**
 * Database Repository Instrumented test, which will execute on an Android device.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RepoRepositoryInstrumentedTest {
    // testdata
    private static String myAddress;
    // i.e: "my.package.name";

    // JPA specific
    TestHelper testHelper;
    private RepoRepository repo;

    private void setupAndroid() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        FDroidDatabaseFactory factory = FDroidDatabase.getINSTANCE(context, true);
        repo = factory.repoRepository();

        testHelper = new TestHelper(new RoomFDroidDatabaseFacade(factory));
    }

    @Before
    public void setUp() {
        setupAndroid();
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