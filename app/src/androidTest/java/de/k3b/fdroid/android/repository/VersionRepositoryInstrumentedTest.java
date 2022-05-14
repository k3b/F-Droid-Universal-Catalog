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
package de.k3b.fdroid.android.repository;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.interfaces.VersionRepository;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class VersionRepositoryInstrumentedTest {
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";

    private VersionRepository versionRepository;

    private Repo repo;
    private Repo repo2;
    private App app;

    private FDroidDatabaseFactory factory = null;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        factory = FDroidDatabase.getINSTANCE(context, true);
        versionRepository = factory.versionRepository();


        RoomTestHelper h = new RoomTestHelper(factory);
        repo = h.createRepo();
        repo2 = h.createRepo();
        app = h.createApp();
        h.createVersion(app, repo, 7, 7, 7, null); // not found maxSdk
        h.createVersion(app, repo, 9, 9, 0, null); // not found minSdk

        h.createVersion(app, repo2, 8, 8, 0, null); // found
        h.createVersion(app, repo, 8, 8, 0, "arm64"); // found
    }

    @After
    public void finish() {
        factory.appRepository().delete(app);
        factory.repoRepository().delete(repo);
        factory.repoRepository().delete(repo2);
        // version deleted through cascade delete
    }


    @Test
    public void findBestBySdkVersion_noNativeCode() {
        List<Version> versions = versionRepository.findBestBySdkAndNative(8, null);
        Assert.assertTrue(versions.size() == 2);
    }

    @Test
    public void findBestBySdkVersion_withNativeCode() {
        List<Version> versions = versionRepository.findBestBySdkAndNative(8, "%arm7%");
        Assert.assertTrue(versions.size() == 1);
    }

    @Test
    public void findByAppId() {
        List<Version> versions = versionRepository.findByAppId(app.getId());
        Assert.assertTrue(versions.size() == 4);
    }

}