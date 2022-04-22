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
import org.springframework.lang.Nullable;
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
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.util.CopyInputStream;

/* download v1-jar while simultaniously checking/updating signature */
@Service
public class HttpV1JarDownloadService {
    @Nullable
    private final RepoRepository repoRepository;
    private final String downloadPath;
    private Repo repoInDatabase;

    public HttpV1JarDownloadService(@Nullable RepoRepository repoRepository, @Value("de.k3b.fdroid.downloads:~/.fdroid/downloads") String downloadPath) {
        this.repoRepository = repoRepository;

        this.downloadPath = downloadPath.replace("~", System.getProperty("user.home"));
    }

    protected static String getHeaderValueOrNull(HttpResponse response, String name) {
        Header firstHeader = response.getFirstHeader(name);
        String value = (firstHeader == null) ? null : firstHeader.getValue();
        return value;
    }

    public static String getName(Repo repo) {
        String name = (repo != null) ? repo.getName() : null;
        if (name == null) {
            name = "download";
        }
        return name;
    }

    public void download(Repo repo) throws IOException {
        setRepoInDatabase(repo);
        download(repo.getV1Url(), repo.getLastUsedDownloadDateTimeUtc());
    }

    public void download(String downloadUrl, long lastModified) throws IOException {
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
        }
    }

    protected void log(String message) {
        System.out.println(message);
    }

    public void download(InputStream inputStream, OutputStream downloadFileOut) throws IOException {
        V1RepoVerifyJarParser repoParser = new V1RepoVerifyJarParser(repoInDatabase);
        try (InputStream in = open(inputStream, downloadFileOut)) {
            repoParser.readFromJar(in);

            // JarInputStream jarInputStream = new JarInputStream(in, true);
        }
    }

    public File getJarfile(String name) {
        File downloadDir = new File(downloadPath);
        if (!downloadDir.exists()) {
            downloadDir.mkdirs();
        }
        File jarfile = new File(downloadDir, name.replace(' ', '_') + "-" + RepoCommon.V1_JAR_NAME);
        return jarfile;
    }

    private InputStream open(InputStream inputStream, OutputStream downloadFileOut) throws IOException {
        InputStream in = new BufferedInputStream(inputStream);
        if (downloadFileOut != null) {
            in = new CopyInputStream(in, downloadFileOut);
        }
        return in;
    }

    private void t() {
    }

    public HttpV1JarDownloadService setRepoInDatabase(@NonNull Repo repoInDatabase) {
        this.repoInDatabase = repoInDatabase;
        return this;
    }
}
