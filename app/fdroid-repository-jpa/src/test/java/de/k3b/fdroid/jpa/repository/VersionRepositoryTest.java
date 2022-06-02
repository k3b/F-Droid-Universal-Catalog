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

import java.util.Collections;
import java.util.List;

import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.repository.VersionRepository;

@DataJpaTest
public class VersionRepositoryTest {
    private static final String MY_NAME = "my.name";
    private final int MY_VERSION_CODE = 2075;
    @Autowired
    JpaTestHelper jpaTestHelper;
    private int appId;
    private int repoId;

    @Autowired
    private VersionRepository versionRepository;

    @BeforeEach
    public void init() {
        repoId = jpaTestHelper.createRepo().getId();
        appId = jpaTestHelper.createApp().getId();

        Version version = new Version(appId,repoId);
        version.setVersionCode(MY_VERSION_CODE);
        version.setApkName(MY_NAME);
        version.setSrcname("my source name");
        version.setNativecode("helloWorldCpu");
        this.versionRepository.insert(version);
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(versionRepository, "repo");
        Assert.notNull(jpaTestHelper, "jpaTestHelper");
    }

    @Test
    public void findByAppId() {
        List<Version> version = versionRepository.findByAppId(appId);
        Assert.isTrue(version.size() == 1, "found 1");
    }

    @Test
    public void findByAppIds() {
        List<Version> version = versionRepository.findByAppIds(Collections.singletonList(appId));
        Assert.isTrue(version.size() == 1, "found 1");
    }

    @Test
    public void findBestBySdkVersion_noVersionAndNoNativeCode() {
        List<Version> versions = versionRepository.findBestBySdkAndNative(0, null);
        Assert.isTrue(versions.size() == 1);
    }

    @Test
    public void findBestBySdkVersion_noNativeCode() {
        List<Version> versions = versionRepository.findBestBySdkAndNative(8, null);
        Assert.isTrue(versions.size() == 1);
    }

    @Test
    public void findBestBySdkVersion_withNativeCode() {
        List<Version> versions = versionRepository.findBestBySdkAndNative(8, "%arm7%");
        Assert.isTrue(versions.size() == 0);
    }
}
