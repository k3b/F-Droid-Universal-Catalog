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
package de.k3b.fdroid.android.db;

import de.k3b.fdroid.android.repository.AppCategoryDao;
import de.k3b.fdroid.android.repository.AppHardwareDao;
import de.k3b.fdroid.android.repository.CategoryDao;
import de.k3b.fdroid.android.repository.HardwareProfileDao;
import de.k3b.fdroid.android.repository.LocaleDao;
import de.k3b.fdroid.android.repository.LocalizedDao;
import de.k3b.fdroid.android.repository.RepoDao;
import de.k3b.fdroid.android.repository.VersionDao;
import de.k3b.fdroid.domain.interfaces.AppRepository;

public interface FDroidDatabaseFactory {
    AppRepository appRepository();

    AppCategoryDao appCategoryRepository();

    CategoryDao categoryRepository();

    LocaleDao localeRepository();

    LocalizedDao localizedRepository();

    RepoDao repoRepository();

    VersionDao versionRepository();

    AppHardwareDao appHardwareRepository();

    HardwareProfileDao hardwareProfileRepository();
}
