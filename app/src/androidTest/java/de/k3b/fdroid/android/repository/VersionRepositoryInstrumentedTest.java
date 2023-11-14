/*
 * Copyright (c) 2022 by k3b.
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

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.entity.common.VersionCommon;
import de.k3b.fdroid.domain.repository.VersionRepository;

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
    public void findBestBySdkVersion_noVersionAndNoNativeCode() {
        List<Version> versionList = versionRepository.findBestBySdkAndNative(0, null);
        assertEquals(2, versionList.size());

        String actual = new VersionCommon(versionList.get(0)).toString();
        // all sdk-versionList 7..9
        String expected = "VersionCommon[minSdkVersion=7,targetSdkVersion=9,maxSdkVersion=7]";
        assertEquals(expected, actual);
    }

    @Test
    public void findBestBySdkVersion_noNativeCode() {
        List<Version> versionList = versionRepository.findBestBySdkAndNative(8, null);
        assertEquals(2, versionList.size());

        String actual = new VersionCommon(versionList.get(0)).toString();

        String expected = "VersionCommon[minSdkVersion=8,targetSdkVersion=8]";
        assertEquals(expected, actual);
    }

    @Test
    public void findBestBySdkVersion_withNativeCode() {
        List<Version> versionList = versionRepository.findBestBySdkAndNative(8, "%arm7%");
        assertEquals(1, versionList.size());

        String actual = new VersionCommon(versionList.get(0)).toString();
        String expected = "VersionCommon[minSdkVersion=8,targetSdkVersion=8]";
        assertEquals(expected, actual);
    }

    @Test
    public void findByAppId() {
        List<Version> versionList = versionRepository.findByAppId(app.getId());
        assertEquals(4, versionList.size());
    }

    @Test
    public void findByMinSdkAndAppIds() {
        List<Version> versionList = versionRepository.findByMinSdkAndAppIds(8, Collections.singletonList(app.getId()));
        assertEquals(2, versionList.size());
    }
}