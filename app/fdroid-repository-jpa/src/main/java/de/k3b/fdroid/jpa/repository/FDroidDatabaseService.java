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

package de.k3b.fdroid.jpa.repository;

import org.springframework.stereotype.Service;

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

@Service
public class FDroidDatabaseService implements IFDroidDatabase {
    private final RepoRepository repoRepository;
    private final AppRepository appRepository;
    private final LocaleRepository localeRepository;
    private final LocalizedRepository localizedRepository;
    private final VersionRepository versionRepository;
    private final AppCategoryRepository appCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final TranslationRepository translationRepository;
    private final AppAntiFeatureRepository appAntiFeatureRepository;
    private final AntiFeatureRepository antiFeatureRepository;
    private final AppHardwareRepository appHardwareRepository;
    private final HardwareProfileRepository hardwareProfileRepository;

    public FDroidDatabaseService(
            RepoRepository repoRepository,
            AppRepository appRepository,
            LocaleRepository localeRepository, LocalizedRepository localizedRepository,
            VersionRepository versionRepository,
            AppCategoryRepository appCategoryRepository, CategoryRepository categoryRepository,
            TranslationRepository translationRepository,
            AppAntiFeatureRepository appAntiFeatureRepository, AntiFeatureRepository antiFeatureRepository,
            AppHardwareRepository appHardwareRepository, HardwareProfileRepository hardwareProfileRepository) {
        this.repoRepository = repoRepository;
        this.appRepository = appRepository;
        this.localeRepository = localeRepository;
        this.localizedRepository = localizedRepository;
        this.versionRepository = versionRepository;
        this.appCategoryRepository = appCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.translationRepository = translationRepository;
        this.appAntiFeatureRepository = appAntiFeatureRepository;
        this.antiFeatureRepository = antiFeatureRepository;
        this.appHardwareRepository = appHardwareRepository;
        this.hardwareProfileRepository = hardwareProfileRepository;
    }

    @Override
    public AppRepository appRepository() {
        return this.appRepository;
    }

    @Override
    public AppCategoryRepository appCategoryRepository() {
        return this.appCategoryRepository;
    }

    @Override
    public CategoryRepository categoryRepository() {
        return this.categoryRepository;
    }

    @Override
    public TranslationRepository translationRepository() {
        return this.translationRepository;
    }

    @Override
    public AppAntiFeatureRepository appAntiFeatureRepository() {
        return this.appAntiFeatureRepository;
    }

    @Override
    public AntiFeatureRepository antiFeatureRepository() {
        return this.antiFeatureRepository;
    }

    @Override
    public LocaleRepository localeRepository() {
        return this.localeRepository;
    }

    @Override
    public LocalizedRepository localizedRepository() {
        return this.localizedRepository;
    }

    @Override
    public RepoRepository repoRepository() {
        return this.repoRepository;
    }

    @Override
    public VersionRepository versionRepository() {
        return this.versionRepository;
    }

    @Override
    public AppHardwareRepository appHardwareRepository() {
        return this.appHardwareRepository;
    }

    @Override
    public HardwareProfileRepository hardwareProfileRepository() {
        return this.hardwareProfileRepository;
    }
}
