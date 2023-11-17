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

package de.k3b.fdroid.android.v1.service;

import android.app.Application;

import java.io.File;

import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.android.repository.FDroidDatabaseFactory;
import de.k3b.fdroid.android.repository.RepoDao;
import de.k3b.fdroid.catalog.v1domain.service.HttpV1JarDownloadService;
import de.k3b.fdroid.catalog.v1domain.service.HttpV1JarImportService;
import de.k3b.fdroid.catalog.v1domain.service.V1DownloadAndImportService;
import de.k3b.fdroid.catalog.v1domain.service.V1DownloadAndImportServiceInterface;
import de.k3b.fdroid.catalog.v1domain.service.V1UpdateService;
import de.k3b.fdroid.domain.service.AppIconService;
import de.k3b.fdroid.domain.service.CacheServiceInteger;
import de.k3b.fdroid.domain.service.RepoIconService;

public class AndroidServiceFactory {
    private final Application context;
    private final FDroidDatabaseFactory database;

    private V1DownloadAndImportServiceInterface v1DownloadAndImportService = null;
    private final File myRoot;
    private AppIconService appIconService = null;
    private RepoIconService repoIconService = null;

    public AndroidServiceFactory(Application context, FDroidDatabaseFactory database) {
        this.context = context;
        this.database = database;
        this.myRoot = getTempDir("download");
    }

    /**
     * to be called when database has changed (i.e. after download/import
     */
    public void clearCache() {
        appIconService = null;
        repoIconService = null;
    }

    private File getTempDir(String subDirName) {
        // https://stackoverflow.com/questions/3425906/creating-temporary-files-in-android
        File outputDir = context.getCacheDir();
        if (subDirName != null) outputDir = new File(outputDir, subDirName);
        outputDir.mkdirs();
        return outputDir;
    }

    public V1DownloadAndImportServiceInterface getV1DownloadAndImportService() {
        if (v1DownloadAndImportService == null) {
            if (Global.USE_VERIFYING_DOWNLOAD) {
                v1DownloadAndImportService = new V1DownloadAndImportService(
                        getRepoRepository(), getHttpV1JarDownloadService(), getV1UpdateService());
            } else {
                v1DownloadAndImportService = new HttpV1JarImportService(database.repoRepository(), getV1UpdateService());
            }
        }
        return v1DownloadAndImportService;
    }

    public RepoDao getRepoRepository() {
        return database.repoRepository();
    }

    public AppIconService getAppIconService() {
        if (appIconService == null) {
            appIconService = new AppIconService(new File(myRoot, "icons").getAbsolutePath(),
                    new CacheServiceInteger<>(getRepoRepository().findAll()), database.appRepository());
        }
        return appIconService;
    }

    public RepoIconService getRepoIconService() {
        if (repoIconService == null) {
            repoIconService = new RepoIconService(new File(myRoot, "icons").getAbsolutePath());
        }
        return repoIconService;
    }

    private V1UpdateService getV1UpdateService() {
        return V1UpdateServiceAndroid.create(database);
    }

    private HttpV1JarDownloadService getHttpV1JarDownloadService() {
        return new HttpV1JarDownloadService(myRoot.getAbsolutePath());
    }
}
