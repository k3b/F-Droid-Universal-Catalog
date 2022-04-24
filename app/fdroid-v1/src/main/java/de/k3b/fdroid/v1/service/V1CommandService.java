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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.common.PojoCommon;
import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.service.AppWithDetailsPagerService;
import de.k3b.fdroid.service.adapter.AppRepositoryAdapterImpl;
import de.k3b.fdroid.service.adapter.LocalizedRepositoryAdapterImpl;
import de.k3b.fdroid.util.StringUtil;

public class V1CommandService {
    private final RepoRepository repoRepository;
    private final AppRepository appRepository;
    private final LocalizedRepository localizedRepository;
    private final V1UpdateService v1UpdateService;
    private String downloadPath = System.getProperty("user.home") + "/.fdroid/downloads";

    public V1CommandService(RepoRepository repoRepository, AppRepository appRepository,
                            LocalizedRepository localizedRepository, V1UpdateService v1UpdateService,
                            String downloadPath) {
        this.repoRepository = repoRepository;
        this.appRepository = appRepository;
        this.localizedRepository = localizedRepository;
        this.v1UpdateService = v1UpdateService;
        if (downloadPath != null) this.downloadPath = downloadPath.replace("~", System.getProperty("user.home"));
    }

    public static CharSequence getHelp() {
        StringBuilder s = new StringBuilder();
        s.append("\nusage java -jar demo.jar [options]\n\n");
        s.append("-f {expression} find app");
        s.append("-r reload database from downloaded jars\n");
        s.append("-d dir show downloaded jars\n");

        return s;
    }

    public void exec(String... args) throws IOException {
        int i = 0;
        while (i < args.length) {
            String arg =  args[i];
            if (arg.startsWith("-f") && i < args.length - 1) {
                System.out.println(execFind(args[i+1]));
                i++;
            } else if (arg.startsWith("-r")) {
                execReloadDbFromDownload();
            } else if (arg.startsWith("-d")) {
                System.out.println(execDir(new StringBuilder()));
            } else {
                System.out.println(getHelp());
                System.exit(-1);

            }
            i++;
        }
    }

    private String execFind(String search) {
        List<Integer> appIdList = appRepository.findIdsByExpressionSortByScore(search);

        AppWithDetailsPagerService details = new AppWithDetailsPagerService(
                new AppRepositoryAdapterImpl(appRepository),
                new LocalizedRepositoryAdapterImpl(localizedRepository), null, null);

        details.init(appIdList, 10);

        StringBuilder result = new StringBuilder()
                .append("# Search for '").append(search).append("':\n")
                .append("PackageName\tName\tSdk\tVersion\tLastUpdated\n");
        int max = Math.min(appIdList.size(), 40);
        for (int i = 0; i < max; i++) {
            add(result, details.getAppByOffset(i), details.getName(i));
        }
        return result.toString();
    }

    private void add(StringBuilder result, App app, String name) {
        Object[] cols = new Object[]{
                app.getPackageName(), name, app.getSearchSdk(),
                app.getSearchVersion(), PojoCommon.asDateString(app.getLastUpdated())};
        result.append(StringUtil.toCsvStringOrNull(Arrays.asList(cols), "\t"))
        .append("\n");
    }

    public  CharSequence execDir(StringBuilder s) {
        s.append("Files in ").append(downloadPath).append("\n\n");

        File dir = new File(downloadPath);
        for (File f : dir.listFiles()) {
            String name = f.getName();
            if (!name.startsWith(".")) {
                s.append("  ").append(name).append("\n");
            }
        }
        return s;
    }

    private void execReloadDbFromDownload() throws IOException {
        File dir = new File(downloadPath);
        for (File f : dir.listFiles()) {
            String absolutePath = f.getAbsolutePath();
            if (absolutePath.endsWith(RepoCommon.V1_JAR_NAME)) {
                execImportLocalFile(absolutePath);
            }
        }
    }
    private void execDownloadV1Jar(String url, boolean execImport) throws IOException {
        if (repoRepository == null) throw new NullPointerException("Missing repoRepository");

        HttpV1JarDownloadService parser = getHttpV1JarDownloadService();
        Repo repoDownloaded = new Repo();
        repoDownloaded.setLastUsedDownloadMirror(url);
        long downloadDate = System.currentTimeMillis();
        File jarFile = parser.download(url, 0, repoDownloaded);

        Repo repoFound = repoRepository.findByAddress(repoDownloaded.getAddress());
        if (repoFound != null) {
            copy(repoFound, repoDownloaded, downloadDate);
            repoRepository.update(repoFound);
        } else {
            repoDownloaded.setLastUsedDownloadDateTimeUtc(downloadDate);
            repoRepository.insert(repoDownloaded);
            repoFound = repoDownloaded;
        }
    }

    private void execImportLocalFile(String inputPath) throws IOException {
        if (v1UpdateService == null) throw new NullPointerException("missing v1UpdateService");

        InputStream is = new FileInputStream(inputPath);
        if (inputPath.toLowerCase().endsWith(".jar")) {
            v1UpdateService.readFromJar(is);
        } else {
            v1UpdateService.readJsonStream(is);
        }
    }

    private HttpV1JarDownloadService getHttpV1JarDownloadService() {
        return new HttpV1JarDownloadService(this.downloadPath);
    }

    private void copy(Repo dest, Repo src, long downloadDate) {
        RepoCommon.copyCommon(dest, src);
        dest.setLastUsedDownloadDateTimeUtc(downloadDate);
        dest.setLastUsedDownloadMirror(src.getLastUsedDownloadMirror());
        if (src.getJarSigningCertificate() != null) dest.setJarSigningCertificate(src.getJarSigningCertificate());
        if (src.getJarSigningCertificateFingerprint() != null) dest.setJarSigningCertificateFingerprint(src.getJarSigningCertificateFingerprint());
    }
}
