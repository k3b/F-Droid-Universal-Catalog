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

package de.k3b.fdroid.v1domain.service;

import org.springframework.lang.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.X509Certificate;
import java.util.jar.JarEntry;

import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.repository.AppCategoryRepository;
import de.k3b.fdroid.domain.repository.AppHardwareRepository;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.HardwareProfileRepository;
import de.k3b.fdroid.domain.repository.LocaleRepository;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.repository.VersionRepository;
import de.k3b.fdroid.domain.service.CategoryService;
import de.k3b.fdroid.domain.service.HardwareProfileService;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.v1domain.entity.App;
import de.k3b.fdroid.v1domain.entity.Repo;
import de.k3b.fdroid.v1domain.entity.UpdateService;
import de.k3b.fdroid.v1domain.entity.Version;
import de.k3b.fdroid.v1domain.util.JarUtilities;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public abstract class V1UpdateService implements UpdateService, ProgressObservable {
    private final RepoRepository repoRepository;
    private final HardwareProfileService hardwareProfileService;
    JsonStreamParser jsonStreamParser;
    RepoUpdateService repoUpdateService;
    private ProgressObserver progressObserver;

    AppUpdateService appUpdateService;
    VersionUpdateService versionUpdateService;

    FixLocaleService fixLocaleService = new FixLocaleService();

    private de.k3b.fdroid.domain.entity.Repo roomRepo;
    private int currentRepoId = 0;

    public V1UpdateService(RepoRepository repoRepository, AppRepository appRepository,
                           CategoryService categoryService,
                           AppCategoryRepository appCategoryRepository,
                           VersionRepository versionRepository,
                           LocalizedRepository localizedRepository,
                           LocaleRepository localeRepository,
                           HardwareProfileRepository hardwareProfileRepository,
                           AppHardwareRepository appHardwareRepository,
                           LanguageService languageService) {
        repoUpdateService = new RepoUpdateService(repoRepository);

        AppCategoryUpdateService appCategoryUpdateService = new AppCategoryUpdateService(
                categoryService, appCategoryRepository);
        LocalizedUpdateService localizedUpdateService = new LocalizedUpdateService(
                localizedRepository, languageService);
        appUpdateService = new AppUpdateService(appRepository, appCategoryUpdateService, localizedUpdateService);

        hardwareProfileService = new HardwareProfileService(appRepository, hardwareProfileRepository, appHardwareRepository);
        versionUpdateService = new VersionUpdateService(appRepository, versionRepository, hardwareProfileService);
        this.repoRepository = repoRepository;
    }

    public void readFromJar(InputStream jarInputStream, de.k3b.fdroid.domain.entity.Repo existingRepoOrNull) throws IOException {
        roomRepo = existingRepoOrNull;
        init();
        jsonStreamParser.readFromJar(jarInputStream);
    }

    public void readJsonStream(InputStream jsonInputStream) throws IOException {
        init();
        jsonStreamParser.readJsonStream(jsonInputStream);
    }

    public JsonStreamParser init() {
        appUpdateService.init();
        versionUpdateService.init();
        jsonStreamParser = new JsonStreamParser();
        return jsonStreamParser;
    }

    public void save(de.k3b.fdroid.domain.entity.Repo repo) {
        if (repo.getId() == 0) {
            repoRepository.insert(repo);
        } else {
            repoRepository.update(repo);
        }
    }

    abstract protected String log(String s);

    public void setProgressObserver(@Nullable ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
        this.appUpdateService.setProgressObserver(progressObserver);
        this.versionUpdateService.setProgressObserver(progressObserver);
        this.hardwareProfileService.setProgressObserver(progressObserver);
    }

    class JsonStreamParser extends FDroidCatalogJsonStreamParserBase {
        private int lastAppCount = 0;
        private int lastVersionCount = 0;

        /**
         * Stream event, when something has to be logged
         */
        @Override
        protected String log(String s) {
            return V1UpdateService.this.log(s);
        }

        /**
         * Stream event, when a {@link Repo} was read
         */
        @Override
        protected void onRepo(Repo v1Repo) {
            roomRepo = repoUpdateService.update(v1Repo, roomRepo);
            currentRepoId = roomRepo.getId();
            lastAppCount=0;
            lastVersionCount=0;
        }

        /**
         * Stream event, when a {@link App} was read
         */
        @Override
        protected void onApp(App v1App) {
            if (v1App != null) {
                if (lastAppCount == 0 && progressObserver != null) {
                    progressObserver.setProgressContext("🗃 " + roomRepo.getName() + " : ", " / " + roomRepo.getLastAppCount());
                }

                lastAppCount++;

                fixLocaleService.fix(v1App);
                appUpdateService.update(currentRepoId, v1App);
            }
        }

        /**
         * Stream event, when a {@link Version} was read
         */
        @Override
        protected void onVersion(String packageName, Version v1Version) {
            if (lastVersionCount == 0 && progressObserver != null) {
                progressObserver.setProgressContext("🏬 " + roomRepo.getName() + " : ", " / " + roomRepo.getLastAppCount());
            }
            lastVersionCount++;
            versionUpdateService.updateCollectVersions(currentRepoId, packageName, v1Version);
        }

        @Override
        protected void afterJsonJarRead(JarEntry jarEntry) {
            super.afterJsonJarRead(jarEntry);
            roomRepo.setLastAppCount(lastAppCount);
            roomRepo.setLastVersionCount(lastVersionCount);
            X509Certificate certificate = JarUtilities.getSigningCertFromJar(roomRepo, jarEntry);
            JarUtilities.verifyAndUpdateSigningCertificate(roomRepo, certificate);
            roomRepo.setDownloadTaskId(null);
            save(roomRepo);
        }

    }
}
