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

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.DateUtils;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.util.CopyInputStream;

/* download v1-jar while simultaniously checking/updating signature */
@Service
public class HttpV1JarDownloadService {
    @NonNull
    private final String downloadPath;
    @NonNull
    private Repo repoInDatabase;

    public HttpV1JarDownloadService(@Value("de.k3b.fdroid.downloads:~/.fdroid/downloads") @NonNull String downloadPath) {
        if (downloadPath == null) throw new NullPointerException();

        this.downloadPath = downloadPath.replace("~", System.getProperty("user.home"));
    }

    protected static String getHeaderValueOrNull(HttpResponse response, String name) {
        Header firstHeader = response.getFirstHeader(name);
        String value = (firstHeader == null) ? null : firstHeader.getValue();
        return value;
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
        if (repo == null) throw new NullPointerException();

        this.repoInDatabase = repo;
        String name = getName(repoInDatabase);
        HttpClient client = HttpClients.custom().build();
        RequestBuilder request = RequestBuilder.get().setUri(downloadUrl);
        if (lastModified != 0) {
            request.setHeader(HttpHeaders.IF_MODIFIED_SINCE, DateUtils.formatDate(new Date(lastModified)));
        }
        log("Downloading " + downloadUrl);
        HttpResponse response = client.execute(request.build());
        log("https result =" + response.getStatusLine().getReasonPhrase() +
                "(" + response.getStatusLine().getStatusCode() + ")");

        HttpEntity responseEntity = response.getEntity();
        // Last-Modified: Thu, 21 Apr 2022 17:36:30 GMT
        // ETag: "bf396-5dd2d8ce6f6f7"
        String jarLastModified = getHeaderValueOrNull(response, HttpHeaders.LAST_MODIFIED);
        if (repoInDatabase != null && jarLastModified != null) {
            repoInDatabase.setLastUsedDownloadDateTimeUtc(DateUtils.parseDate(jarLastModified).getTime());
        }
        if (responseEntity != null) {
            File jarfileDownload = getJarfile(name + ".tmp");
            log("Downloading to " + jarfileDownload.getAbsolutePath());
            download(responseEntity.getContent(), new FileOutputStream(jarfileDownload));
            File jarfileFinal = getJarfile(getName(repoInDatabase));
            if (jarfileFinal.exists()) jarfileFinal.delete();
            log("Renaming to " + jarfileFinal.getAbsolutePath());
            jarfileDownload.renameTo(jarfileFinal);
            return jarfileFinal;
        }
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

        File jarfile = new File(downloadDir, RepoCommon.getV1JarFileName(name));
        return jarfile;
    }

    private InputStream open(InputStream inputStream, OutputStream downloadFileOut) throws IOException {
        if (downloadFileOut != null) {
            inputStream = new CopyInputStream(inputStream, downloadFileOut);
        }
        InputStream in = new BufferedInputStream(inputStream);
        return in;
    }

    protected void log(String message) {
        System.out.println(message);
    }

}
