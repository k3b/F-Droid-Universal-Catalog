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

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.repository.VersionRepository;
import de.k3b.fdroid.domain.util.TestHelper;

/**
 * Database Repository Instrumented test, which will execute on an Android device.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class VersionRepositoryInstrumentedTest {
    // testdata
    private static final String MY_NAME = "my.name";
    private final int MY_VERSION_CODE = 2075;
    private final int MY_SDK = 8;
    // Android Room Test specific
    TestHelper testHelper;
    private int appId;
    private int repoId;
    private VersionRepository repo;

    private void setupAndroid() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        FDroidDatabaseFactory factory = FDroidDatabase.getINSTANCE(context, true);
        repo = factory.versionRepository();

        testHelper = new TestHelper(new RoomFDroidDatabaseFacade(factory));
    }

    @Before
    public void setUp() {
        setupAndroid();
        repoId = testHelper.createRepo().getId();
        appId = testHelper.createApp().getId();

        Version version = new Version(appId, repoId);
        version.setVersionCode(MY_VERSION_CODE);
        version.setApkName(MY_NAME);
        version.setSrcname("my source name");
        version.setNativecode("helloWorldCpu");
        version.setSdk(MY_SDK, MY_SDK, MY_SDK);
        this.repo.insert(version);
    }

    @Test
    public void findByAppId() {
        List<Version> versionList = repo.findByAppId(appId);
        assertEquals(1, versionList.size());
    }

    @Test
    public void findByAppIds() {
        List<Version> versionList = repo.findByAppIds(Collections.singletonList(appId));
        assertEquals(1, versionList.size());
    }

    @Test
    public void findByMinSdkAndAppIds() {
        List<Version> versionList = repo.findByMinSdkAndAppIds(MY_SDK, Collections.singletonList(appId));
        assertEquals(1, versionList.size());
    }

    @Test
    public void findBestBySdkVersion_noVersionAndNoNativeCode() {
        List<Version> versionList = repo.findBestBySdkAndNative(0, null);
        assertEquals(1, versionList.size());
    }

    @Test
    public void findBestBySdkVersion_noNativeCode() {
        List<Version> versionList = repo.findBestBySdkAndNative(8, null);
        assertEquals(versionList.size(), 1);
    }

    @Test
    public void findBestBySdkVersion_withNativeCode() {
        List<Version> versionList = repo.findBestBySdkAndNative(8, "%arm7%");
        assertEquals(versionList.size(), 0);
    }
}
