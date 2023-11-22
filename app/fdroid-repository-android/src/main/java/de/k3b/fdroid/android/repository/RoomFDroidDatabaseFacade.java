/*
 * Copyright (c) 2023 by k3b.
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
package de.k3b.fdroid.android.repository;

import de.k3b.fdroid.domain.repository.AntiFeatureRepository;
import de.k3b.fdroid.domain.repository.AppAntiFeatureRepository;
import de.k3b.fdroid.domain.repository.AppCategoryRepository;
import de.k3b.fdroid.domain.repository.AppHardwareRepository;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.CategoryRepository;
import de.k3b.fdroid.domain.repository.HardwareProfileRepository;
import de.k3b.fdroid.domain.repository.IFDroidDatabase;
import de.k3b.fdroid.domain.repository.LocaleRepository;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.domain.repository.VersionRepository;

public class RoomFDroidDatabaseFacade implements IFDroidDatabase {
    private final FDroidDatabaseFactory content;

    public RoomFDroidDatabaseFacade(FDroidDatabaseFactory content) {
        this.content = content;
    }

    @Override
    public AppRepository appRepository() {
        return content.appRepository();
    }

    @Override
    public AppCategoryRepository appCategoryRepository() {
        return content.appCategoryRepository();
    }

    @Override
    public CategoryRepository categoryRepository() {
        return content.categoryRepository();
    }

    @Override
    public TranslationRepository translationRepository() {
        return content.translationRepository();
    }

    @Override
    public AppAntiFeatureRepository appAntiFeatureRepository() {
        return content.appAntiFeatureRepository();
    }

    @Override
    public AntiFeatureRepository antiFeatureRepository() {
        return content.antiFeatureRepository();
    }

    @Override
    public LocaleRepository localeRepository() {
        return content.localeRepository();
    }

    @Override
    public LocalizedRepository localizedRepository() {
        return content.localizedRepository();
    }

    @Override
    public RepoRepository repoRepository() {
        return content.repoRepository();
    }

    @Override
    public VersionRepository versionRepository() {
        return content.versionRepository();
    }

    @Override
    public AppHardwareRepository appHardwareRepository() {
        return content.appHardwareRepository();
    }

    @Override
    public HardwareProfileRepository hardwareProfileRepository() {
        return content.hardwareProfileRepository();
    }
}
