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
package de.k3b.fdroid.domain.util;

import org.jetbrains.annotations.NotNull;

import de.k3b.fdroid.domain.entity.AntiFeature;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppAntiFeature;
import de.k3b.fdroid.domain.entity.AppCategory;
import de.k3b.fdroid.domain.entity.Category;
import de.k3b.fdroid.domain.entity.HardwareProfile;
import de.k3b.fdroid.domain.entity.Locale;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.Translation;
import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.repository.IFDroidDatabase;

/**
 * helper to create database entries for automated tests.
 */
public class TestHelper {
    private final IFDroidDatabase db;
    private int nextNo = 1;

    public TestHelper(IFDroidDatabase db) {
        this.db = db;
    }

    public App createApp() {
        return createApp("test.app." + nextNo++, null);
    }

    @NotNull
    public App createApp(String packageName, String icon) {
        App app = new App();
        app.setPackageName(packageName);
        app.setIcon(icon);

        db.appRepository().insert(app);
        return app;
    }

    public Repo createRepo() {
        Repo repo = new Repo();
        repo.setName("test-repo-" + nextNo++);
        repo.setAddress("testrepo.org." + nextNo++);
        db.repoRepository().insert(repo);
        return repo;
    }

    public Version createVersion(App app, Repo repo) {
        return createVersion(app, repo, 0, 0, 0, null);
    }

    public Version createVersion(App app, Repo repo, int minSdk, int targetSdk, int maxSdk, String nativecode) {
        if (repo == null) repo = createRepo();
        if (app == null) app = createApp();
        Version version = new Version(app.getId(), repo.getId());
        version.setMaxSdkVersion(maxSdk);
        version.setMinSdkVersion(minSdk);
        version.setTargetSdkVersion(targetSdk);
        version.setNativecode(nativecode);

        db.versionRepository().insert(version);
        return version;
    }

    public Locale createLocale(String code) {
        Locale locale = new Locale(code);
        db.localeRepository().insert(locale);
        return locale;
    }

    public Category createCategory() {
        Category category = new Category();
        category.setName("name" + nextNo++);
        db.categoryRepository().insert(category);
        return category;
    }

    public AppCategory createAppCategory(App app, Category category) {
        if (category == null) category = createCategory();
        if (app == null) app = createApp();
        AppCategory appCategory = new AppCategory(app.getId(), category.getId());
        db.appCategoryRepository().insert(appCategory);
        return appCategory;
    }

    public Translation createTranslation(String typ, int id, String localeId) {
        Translation translation = new Translation(typ, id, localeId, "name" + nextNo++);
        db.translationRepository().insert(translation);
        return translation;
    }

    public AntiFeature createAntiFeature() {
        AntiFeature antiFeature = new AntiFeature();
        antiFeature.setName("name" + nextNo++);
        db.antiFeatureRepository().insert(antiFeature);
        return antiFeature;
    }

    public AppAntiFeature createAppAntiFeature(App app, AntiFeature antiFeature) {
        if (antiFeature == null) antiFeature = createAntiFeature();
        if (app == null) app = createApp();
        AppAntiFeature appAntiFeature = new AppAntiFeature(app.getId(), antiFeature.getId());
        db.appAntiFeatureRepository().insert(appAntiFeature);
        return appAntiFeature;
    }

    public HardwareProfile createHardwareProfile() {
        HardwareProfile hardwareProfile = new HardwareProfile();
        hardwareProfile.setName("name" + nextNo++);
        db.hardwareProfileRepository().insert(hardwareProfile);
        return hardwareProfile;
    }

    public Locale save(Locale o) {
        db.localeRepository().save(o);
        return o;
    }

    public Localized save(Localized o) {
        db.localizedRepository().save(o);
        return o;
    }

    public Repo save(Repo o) {
        db.repoRepository().save(o);
        return o;
    }

    public Category save(Category o) {
        db.categoryRepository().save(o);
        return o;
    }

    public AntiFeature save(AntiFeature o) {
        db.antiFeatureRepository().save(o);
        return o;
    }

    public Translation save(Translation o) {
        db.translationRepository().save(o);
        return o;
    }

    public Version save(Version o) {
        db.versionRepository().save(o);
        return o;
    }
}
