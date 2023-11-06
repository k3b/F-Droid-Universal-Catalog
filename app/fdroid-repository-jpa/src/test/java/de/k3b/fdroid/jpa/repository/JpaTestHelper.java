/*
 * Copyright (c) 2022-2023 by k3b.
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

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppCategory;
import de.k3b.fdroid.domain.entity.Category;
import de.k3b.fdroid.domain.entity.HardwareProfile;
import de.k3b.fdroid.domain.entity.Locale;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.Version;

@Service
public class JpaTestHelper {
    @Autowired // working with SPRING_BOOT_VERSION = '5.3.29' with Java8
    // with 6.0.0 not working any more
    // maybe @PersistenceUnit
    EntityManager entityManager;

    private int nextNo = 1;

    public App createApp() {
        return createApp("test.app." + nextNo++, null);
    }

    public App createApp(String packageName, String icon) {
        App app = new App();
        app.setPackageName(packageName);
        app.setIcon(icon);
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

    public Version createVersion(App app, Repo repo, int minSdk, int targetSdk, int maxSdk, String nativecode) {
        if (repo == null) repo = createRepo();
        if (app == null) app = createApp();
        Version version = new Version(app.getId(), repo.getId());
        version.setMaxSdkVersion(maxSdk);
        version.setMinSdkVersion(minSdk);
        version.setTargetSdkVersion(targetSdk);
        version.setNativecode(nativecode);

        return save(version);
    }

    public Version createVersion(App app, Repo repo) {
        return createVersion(app, repo, 0, 0, 0, null);
    }

    public Locale createLocale(String code) {
        Locale locale = new Locale();
        locale.setId(code);
        return save(locale);
    }

    public Category createCategory() {
        Category category = new Category();
        category.setName("name" + nextNo++);
        return save(category);
    }

    public AppCategory createAppCategory(App app, Category category) {
        if (category == null) category = createCategory();
        if (app == null) app = createApp();
        AppCategory appCategory = new AppCategory(app.getId(), category.getId());

        return save(appCategory);
    }

    public HardwareProfile createHardwareProfile() {
        HardwareProfile hardwareProfile = new HardwareProfile();
        hardwareProfile.setName("name" + nextNo++);
        return save(hardwareProfile);
    }
}
