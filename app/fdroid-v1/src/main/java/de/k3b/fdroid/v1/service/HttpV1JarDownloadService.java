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

package de.k3b.fdroid.v1.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.ProgressListener;
import de.k3b.fdroid.util.CopyInputStream;
import de.k3b.fdroid.v1.service.util.DateUtils;

/* download v1-jar while simultaniously checking/updating signature */
@Service
public class HttpV1JarDownloadService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);
    public static final String HTTP_LAST_MODIFIED = "Last-Modified";
    public static final String HTTP_IF_MODIFIED_SINCE = "If-Modified-Since";

    @NonNull
    private final String downloadPath;
    @Nullable
    private final ProgressListener progressListener;
    @NonNull
    private Repo repoInDatabase;

    public HttpV1JarDownloadService(
            @Value("${de.k3b.fdroid.downloads:~/.fdroid/downloads}") @NonNull String downloadPath,
            @Nullable ProgressListener progressListener) {
        this.progressListener = progressListener;
        if (downloadPath == null) throw new NullPointerException();

        this.downloadPath = downloadPath.replace("~", System.getProperty("user.home"));
    }

    private static String getName(Repo repo) {
        String name = (repo != null) ? repo.getName() : null;
        if (name == null) {
            name = "download";
        }
        return name;
    }

    public File download(@NonNull Repo repo) throws IOException {
        return download(repo.getV1Url(), repo.getLastUsedDownloadDateTimeUtc(), repo);
    }

    public File download(String downloadUrl, long lastModified, @NonNull Repo repo) throws IOException {
        if (repo == null || downloadUrl == null) throw new NullPointerException();

        downloadUrl = Repo.getV1Url(downloadUrl);
        this.repoInDatabase = repo;
        String name = getName(repoInDatabase);

        log("Downloading " + downloadUrl);

        HttpURLConnection connection = (HttpURLConnection) new URL(downloadUrl).openConnection();
        if (lastModified != 0) {
            connection.setRequestProperty(HTTP_IF_MODIFIED_SINCE, DateUtils.formatDate(new Date(lastModified)));
        }

        String jarLastModified = connection.getRequestProperty(HTTP_LAST_MODIFIED);
        if (repoInDatabase != null && jarLastModified != null) {
            repoInDatabase.setLastUsedDownloadDateTimeUtc(DateUtils.parseDate(jarLastModified).getTime());
        }
        log("https result =" + connection.getResponseMessage() +
                "(" + connection.getResponseCode() + "," + HTTP_LAST_MODIFIED +
                "='" + jarLastModified + "')");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_NOT_MODIFIED) {
            // 304
            return null;
        }
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream response = connection.getInputStream();

            if (response != null) {
                File jarfileDownload = getJarfile(name + ".tmp");
                log("Downloading to " + jarfileDownload.getAbsolutePath());
                download(response, new FileOutputStream(jarfileDownload));
                File jarfileFinal = getJarfile(getName(repoInDatabase));
                if (jarfileFinal.exists()) jarfileFinal.delete();
                log("Renaming to " + jarfileFinal.getAbsolutePath());
                jarfileDownload.renameTo(jarfileFinal);
                return jarfileFinal;
            }
        }
        //??? error handling
        return null;
    }

    private void download(InputStream inputStream, OutputStream downloadFileOut) throws IOException {
        V1RepoVerifyJarParser repoParser = new V1RepoVerifyJarParser(repoInDatabase);
        try (InputStream in = open(inputStream, downloadFileOut)) {
            repoParser.readFromJar(in);
        }
    }

    public File getJarfile(String name) {
        File downloadDir = new File(downloadPath);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }

        return new File(downloadDir, RepoCommon.getV1JarFileName(name));
    }

    private InputStream open(InputStream inputStream, OutputStream downloadFileOut) throws IOException {
        if (downloadFileOut != null) {
            inputStream = new CopyInputStream(inputStream, downloadFileOut);
        }
        return new BufferedInputStream(inputStream);
    }

    protected void log(String message) {
        LOGGER.debug(message);
        System.out.println(message);
        if (progressListener != null) {
            progressListener.log(message);
        }
    }
}
