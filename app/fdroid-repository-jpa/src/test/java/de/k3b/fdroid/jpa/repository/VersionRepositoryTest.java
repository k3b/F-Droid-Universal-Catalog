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
package de.k3b.fdroid.jpa.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.repository.VersionRepository;

/**
 * Database Repository Instrumented test, which will execute in Spring/JPA.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class VersionRepositoryTest {
    // testdata
    private static final String MY_NAME = "my.name";
    private final int MY_VERSION_CODE = 2075;
    private final int MY_SDK = 8;

    private int appId;
    private int repoId;

    // JPA specific
    @Autowired
    JpaTestHelper testHelper;
    @Autowired
    private VersionRepository repo;

    @BeforeEach
    public void setup() {
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
