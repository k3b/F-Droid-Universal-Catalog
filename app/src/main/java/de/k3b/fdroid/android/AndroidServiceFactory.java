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

package de.k3b.fdroid.android;

import android.content.Context;

import java.io.File;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.android.db.V1UpdateServiceAndroid;
import de.k3b.fdroid.v1.service.HttpV1JarDownloadService;
import de.k3b.fdroid.v1.service.V1DownloadAndImportService;
import de.k3b.fdroid.v1.service.V1UpdateService;

public class AndroidServiceFactory {
    private static AndroidServiceFactory INSTANCE = null;
    private final Context context;
    private final FDroidDatabase database;

    private V1DownloadAndImportService v1DownloadAndImportService = null;

    private AndroidServiceFactory(Context context) {
        this.context = context;
        this.database = FDroidDatabase.getINSTANCE(context);
    }

    public static AndroidServiceFactory getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new AndroidServiceFactory(context);
        }
        return INSTANCE;
    }

    private File getTempDir(String subDirName) {
        // https://stackoverflow.com/questions/3425906/creating-temporary-files-in-android
        File outputDir = context.getCacheDir();
        if (subDirName != null) outputDir = new File(outputDir, subDirName);
        outputDir.mkdirs();
        return outputDir;
    }

    public V1DownloadAndImportService getV1DownloadAndImportService() {
        if (v1DownloadAndImportService == null) {
            v1DownloadAndImportService = new V1DownloadAndImportService(
                    database.repoRepository(), getHttpV1JarDownloadService(), getV1UpdateService());
        }
        return v1DownloadAndImportService;
    }

    private V1UpdateService getV1UpdateService() {
        return V1UpdateServiceAndroid.create(database);
    }

    private HttpV1JarDownloadService getHttpV1JarDownloadService() {
        return new HttpV1JarDownloadService(getTempDir("download").getAbsolutePath(), null);
    }
}
