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

import java.util.Arrays;
import java.util.List;

import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.interfaces.VersionRepository;

@DataJpaTest
public class VersionRepositoryTest {
    private static final String MY_NAME = "my.name";
    private static final String MY_PACKAGE_NAME = "my.package.name";
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
        List<Version> version = versionRepository.findByAppIds(Arrays.asList(appId));
        Assert.isTrue(version.size() == 1, "found 1");
    }
}
