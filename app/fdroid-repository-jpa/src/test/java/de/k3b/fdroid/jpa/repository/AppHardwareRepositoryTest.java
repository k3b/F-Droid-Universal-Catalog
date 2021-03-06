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

import java.util.List;

import de.k3b.fdroid.domain.entity.AppHardware;
import de.k3b.fdroid.domain.repository.AppHardwareRepository;


@DataJpaTest
public class AppHardwareRepositoryTest {
    @Autowired
    JpaTestHelper jpaTestHelper;

    @Autowired
    private AppHardwareRepository repo;

    private int appId;
    private int hardwareProfileId;

    @BeforeEach
    public void init() {
        appId = jpaTestHelper.createApp().getId();
        hardwareProfileId = jpaTestHelper.createHardwareProfile().getId();

        AppHardware appHardware = new AppHardware(appId, hardwareProfileId);
        appHardware.getMin().setVersionCode(1);
        appHardware.getMax().setVersionCode(99);
        repo.insert(appHardware);
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(jpaTestHelper, "jpaTestHelper");
        Assert.notNull(repo, "repo");
    }


    @Test
    public void findByAppId() {
        List<AppHardware> list = repo.findByAppId(appId);
        Assert.isTrue(list.size() == 1, "1 found");
    }
}