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
package de.k3b.fdroid.jpa.repository.testcase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import java.util.List;

import de.k3b.fdroid.jpa.repository.JpaTestHelper;

@DataJpaTest
public class TestEntityRepositoryTest {
    private static final String MY_NAME = "name peter";
    private static final String MY_FAMILY_NAME = "familyname smith";
    @Autowired
    private TestRepositoryJpa jpa;

    @Autowired
    JpaTestHelper jpaTestHelper;

    @BeforeEach
    public void init() {
        TestEntity testEntity = new TestEntity();
        testEntity.name = MY_NAME;
        testEntity.familyName = MY_FAMILY_NAME;
        jpaTestHelper.save(testEntity);
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(jpa, "jpa");
    }

    @Test
    public void findByName() {
        TestEntity test = jpa.findByName(MY_NAME);
        Assert.notNull(test, "found");
    }

    @Test
    public void findByFamilyName() {
        List<TestEntity> test = jpa.findByFamilyName(MY_FAMILY_NAME);
        Assert.notEmpty(test, "found");
    }

    @Test
    public void findByAVeryComplicatedQuery() {
        List<Integer> result = jpa.findByAVeryComplicatedQuery("ame");
    }
}
