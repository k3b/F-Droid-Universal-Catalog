/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of de.k3b.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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

package de.k3b.fdroid.catalog.v2domain.service;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import de.k3b.fdroid.catalog.v2domain.entity.packagev2.V2App;
import de.k3b.fdroid.catalog.v2domain.entity.packagev2.V2AppCatalog;
import de.k3b.fdroid.catalog.v2domain.entity.packagev2.V2AppInfo;
import de.k3b.fdroid.catalog.v2domain.entity.packagev2.V2PackageVersion;
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2Category;
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2Repo;

public class V2TestData {
    public static final String UNITTEST_TEST_DATA = "exampledata/V2TestData-index-v2.json";

    public final V2AppCatalog appCatalog;
    public final V2App app;
    public final V2AppInfo appInfo;
    public final V2PackageVersion version;
    public final V2Repo repo;
    public final V2Category category;

    public V2TestData() {
        try (InputStream resourceAsStream = V2TestData.class.getClassLoader().getResourceAsStream(UNITTEST_TEST_DATA);
             InputStreamReader is = new InputStreamReader(Objects.requireNonNull(resourceAsStream, "Cannot read json from " + UNITTEST_TEST_DATA))) {
            Gson gson = new Gson();
            appCatalog = gson.fromJson(is, V2AppCatalog.class);
            repo = appCatalog.getRepo();
            if (repo != null && repo.getCategories() != null) {
                category = repo.getCategories().entrySet().iterator().next().getValue();
            }
            if (appCatalog != null && appCatalog.getPackages() != null) {
                app = appCatalog.getPackages().entrySet().iterator().next().getValue();
                if (app != null) {
                    appInfo = app.getMetadata();
                    version = app.getVersions().entrySet().iterator().next().getValue();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
