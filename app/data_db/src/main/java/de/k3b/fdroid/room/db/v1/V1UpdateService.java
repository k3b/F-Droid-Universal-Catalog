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

package de.k3b.fdroid.room.db.v1;

import org.fdroid.model.v1.App;
import org.fdroid.model.v1.Repo;
import org.fdroid.model.v1.Version;
import org.fdroid.service.v1.FDroidCatalogJsonStreamParserBase;
import org.fdroid.service.v1.FixLocaleService;

import java.io.IOException;
import java.io.InputStream;

import de.k3b.fdroid.room.db.AppCategoryRepository;
import de.k3b.fdroid.room.db.AppRepository;
import de.k3b.fdroid.room.db.CategoryRepository;
import de.k3b.fdroid.room.db.LocaleRepository;
import de.k3b.fdroid.room.db.LocalizedRepository;
import de.k3b.fdroid.room.db.RepoRepository;
import de.k3b.fdroid.room.db.VersionRepository;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public abstract class V1UpdateService {
    JsonStreamParser jsonStreamParser = new JsonStreamParser();
    RepoUpdateService repoUpdateService;

    AppUpdateService appUpdateService;
    VersionUpdateService versionUpdateService;

    FixLocaleService fixLocaleService = new FixLocaleService();

    private int currentRepoId = 0;

    /*
    public V1UpdateService(FDroidDatabase fDroidDatabase) {

        this(
                fDroidDatabase.repoDao(),
                fDroidDatabase.appDao(),
                fDroidDatabase.categoryDao(),
                fDroidDatabase.appCategoryDao(),
                fDroidDatabase.versionDao(),
                fDroidDatabase.localizedDao(),
                fDroidDatabase.localeDao());
    }
     */

    public V1UpdateService(RepoRepository repoRepository, AppRepository appRepository, CategoryRepository categoryRepository, AppCategoryRepository appCategoryRepository, VersionRepository versionRepository, LocalizedRepository localizedRepository, LocaleRepository localeRepository) {
        repoUpdateService = new RepoUpdateService(repoRepository);

        AppCategoryUpdateService appCategoryUpdateService = new AppCategoryUpdateService(
                categoryRepository, appCategoryRepository);
        LocalizedUpdateService localizedUpdateService = new LocalizedUpdateService(
                localizedRepository, localeRepository);
        appUpdateService = new AppUpdateService(appRepository, appCategoryUpdateService, localizedUpdateService);

        versionUpdateService = new VersionUpdateService(appRepository, versionRepository);
    }

    public void readFromJar(InputStream jarInputStream) throws IOException {
        jsonStreamParser.readFromJar(jarInputStream);
    }

    abstract protected String log(String s);

    class JsonStreamParser extends FDroidCatalogJsonStreamParserBase {

        /**
         * Stream event, when something has to be logged
         *
         * @param s
         */
        @Override
        protected String log(String s) {
            return V1UpdateService.this.log(s);
        }

        /**
         * Stream event, when a {@link Repo} was read
         *
         * @param v1Repo
         */
        @Override
        protected void onRepo(Repo v1Repo) {
            de.k3b.fdroid.domain.Repo roomRepo = repoUpdateService.update(v1Repo);
            currentRepoId = roomRepo.id;
        }

        /**
         * Stream event, when a {@link App} was read
         *
         * @param v1App
         */
        @Override
        protected void onApp(App v1App) {
            fixLocaleService.fix(v1App);
            appUpdateService.update(currentRepoId, v1App);

        }

        /**
         * Stream event, when a {@link Version} was read
         *
         * @param packageName
         * @param v1Version
         */
        @Override
        protected void onVersion(String packageName, Version v1Version) {
            versionUpdateService.update(currentRepoId, packageName, v1Version);
        }
    }
}
