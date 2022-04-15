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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.RepoRepository;

@DataJpaTest
public class RepoRepositoryTest {
    private static final String MY_ADDRESS = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    private static final int MY_REPO_ID = 47110815;

    @Autowired
    private RepoRepositoryJpa jpa;
    @Autowired
    private RepoRepository repo;

    private int id = 0;

    @BeforeEach
    public void init() {
        Repo r = new Repo();
        r.setIcon(MY_ICON);
        r.setAddress(MY_ADDRESS);
        // r.setId(4711);
        repo.insert(r);
    }

    @AfterEach
    public void finish() {
        jpa.deleteById(id);
        id = 0;
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(jpa, "jpa");
        Assert.notNull(repo, "repo");
    }

    @Test
    public void findByAddress() {
        Repo r = repo.findByAddress(MY_ADDRESS);
        Assert.notNull(r, "found");
    }
}