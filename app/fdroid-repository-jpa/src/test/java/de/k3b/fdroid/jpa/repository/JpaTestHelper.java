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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Category;
import de.k3b.fdroid.domain.HardwareProfile;
import de.k3b.fdroid.domain.Locale;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.Version;

@Service
public class JpaTestHelper {
    @Autowired
    EntityManager entityManager;

    private int nextNo = 1;

    public App createApp() {
        App app = new App();
        app.setPackageName("test.app." + nextNo++);
        return save(app);
    }

    public <T> T save(T entity) {
        entityManager.persist(entity);
        return entity;
    }

    public Repo createRepo() {
        Repo repo = new Repo();
        repo.setName("test-repo-" + nextNo++);
        repo.setAddress("testrepo.org." + nextNo++);
        return save(repo);
    }

    public Version createVersion(App app, Repo repo) {
        if (repo == null) repo = createRepo();
        if (app == null) app = createApp();
        return save(new Version(app.getId(), repo.getId()));
    }

    public Locale createLocale(String code) {
        Locale locale = new Locale();
        locale.setCode(code);
        return save(locale);
    }

    public Category createCategory() {
        Category category = new Category();
        category.setName("name" + nextNo++);
        return save(category);
    }

    public HardwareProfile createHardwareProfile() {
        HardwareProfile hardwareProfile = new HardwareProfile();
        hardwareProfile.setName("name" + nextNo++);
        return save(hardwareProfile);
    }
}
