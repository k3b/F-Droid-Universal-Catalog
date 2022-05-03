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
package de.k3b.fdroid.android.v1.service;

import android.util.Log;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.android.repository.FDroidDatabaseFactory;
import de.k3b.fdroid.domain.interfaces.AppCategoryRepository;
import de.k3b.fdroid.domain.interfaces.AppHardwareRepository;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.HardwareProfileRepository;
import de.k3b.fdroid.domain.interfaces.LocaleRepository;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.domain.interfaces.VersionRepository;
import de.k3b.fdroid.service.CategoryService;
import de.k3b.fdroid.service.LanguageService;
import de.k3b.fdroid.v1.service.V1UpdateService;

public class V1UpdateServiceAndroid extends V1UpdateService {

    public static final String TAG = Global.LOG_TAG_IMPORT;

    public V1UpdateServiceAndroid(
            RepoRepository repoRepository, AppRepository appRepository,
            CategoryService categoryService, AppCategoryRepository appCategoryRepository,
            VersionRepository versionRepository, LocalizedRepository localizedRepository,
            LocaleRepository localeRepository, HardwareProfileRepository hardwareProfileRepository,
            AppHardwareRepository appHardwareRepository, LanguageService languageService) {
        super(repoRepository, appRepository, categoryService, appCategoryRepository,
                versionRepository, localizedRepository, localeRepository, hardwareProfileRepository,
                appHardwareRepository, languageService);
    }

    public static V1UpdateServiceAndroid create(FDroidDatabaseFactory db) {
        CategoryService categoryService = new CategoryService(db.categoryRepository());
        LanguageService languageService = new LanguageService(db.localeRepository());
        return new V1UpdateServiceAndroid(db.repoRepository(), db.appRepository(), categoryService,
                db.appCategoryRepository(), db.versionRepository(), db.localizedRepository(), db.localeRepository(),
                db.hardwareProfileRepository(), db.appHardwareRepository(), languageService) {

        };

    }
    @Override
    protected String log(String s) {
        Log.d(TAG, s);
        return s;
    }
}
