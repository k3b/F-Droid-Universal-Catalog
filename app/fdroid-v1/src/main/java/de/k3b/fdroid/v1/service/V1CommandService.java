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

import org.springframework.lang.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.AppSearchParameter;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.common.EntityCommon;
import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.service.AppWithDetailsPagerService;
import de.k3b.fdroid.service.adapter.AppRepositoryAdapterImpl;
import de.k3b.fdroid.util.StringUtil;

/**
 * Commandline interpreter to dispatch service method calls.
 */
public class V1CommandService {
    private final RepoRepository repoRepository;
    private final AppRepository appRepository;
    private final LocalizedRepository localizedRepository;
    private final HttpV1JarDownloadService downloadService;
    private final V1UpdateService v1UpdateService;
    private String downloadPath = System.getProperty("user.home") + "/.fdroid/downloads";

    public V1CommandService(RepoRepository repoRepository, AppRepository appRepository,
                            LocalizedRepository localizedRepository, HttpV1JarDownloadService downloadService, V1UpdateService v1UpdateService,
                            String downloadPath) {
        this.repoRepository = repoRepository;
        this.appRepository = appRepository;
        this.localizedRepository = localizedRepository;
        this.downloadService = downloadService;
        this.v1UpdateService = v1UpdateService;
        if (downloadPath != null) this.downloadPath = downloadPath.replace("~", System.getProperty("user.home"));
    }

    public static CharSequence getHelp() {
        StringBuilder s = new StringBuilder();
        s.append("\nusage java -jar fdroid-universal-cli.jar [options]\n\n");
        s.append("-f(ind App) {expression} \n");
        s.append("-r(eload reload database from downloaded jars)\n");
        s.append("-d(ir show downloaded jars)\n");

        s.append("http(s)://url download repository from url\n");

        return s;
    }

    public void exec(String... args) throws IOException {
        int i = 0;
        File f;
        while (i < args.length) {
            String arg =  args[i];
            if (arg.startsWith("-f") && i < args.length - 1) {
                System.out.println(execFind(args[i+1]));
                i++;
            } else if (arg.startsWith("http")) {
                System.out.println(execDownload(arg));
            } else if (arg.startsWith("-r")) {
                execReloadDbFromDownload();
            } else if (arg.startsWith("-d")) {
                System.out.println(execDir(new StringBuilder()));
            } else if ((f = new File(new File(downloadPath), arg)).exists()) {
                execImportLocalFile(f.getAbsolutePath());
            } else {

                System.out.println(getHelp());
                System.exit(-1);

            }
            i++;
        }
    }

    private String execDownload(String downloadUrl) {
        V1DownloadAndImportService downloadService = new V1DownloadAndImportService(repoRepository, this.downloadService, v1UpdateService);
        Repo repo = downloadService.download(downloadUrl, null, null);
        return "";
    }

    private String execFind(String search) {
        List<Integer> appIdList = appRepository.findDynamic(new AppSearchParameter().text(search));

        AppWithDetailsPagerService details = new AppWithDetailsPagerService(
                new AppRepositoryAdapterImpl(appRepository),
                null, // new LocalizedRepositoryAdapterImpl(localizedRepository),
                null, null);

        details.init(appIdList, 10);

        StringBuilder result = new StringBuilder()
                .append("# Search for '").append(search).append("':\n")
                .append("PackageName\tName\tSdk\tVersion\tLastUpdated\n");
        int max = Math.min(details.size(), 40);
        for (int i = 0; i < max; i++) {
            App app = details.getAppByOffset(i);
            String name = app.getSearchName().split("\\|")[0]; // details.getName(i);
            add(result, app, name);
        }
        return result.toString();
    }

    private void add(StringBuilder result, App app, String name) {
        Object[] cols = new Object[]{
                app.getPackageName(), name, app.getSearchSdk(),
                app.getSearchVersion(), EntityCommon.asDateString(app.getLastUpdated())};
        result.append(StringUtil.toCsvStringOrNull(Arrays.asList(cols), "\t"))
                .append("\n");
    }

    public  CharSequence execDir(StringBuilder s) {
        s.append("Files in ").append(downloadPath).append("\n\n");

        for (File f : listFiles()) {
            String name = f.getName();
            if (!name.startsWith(".")) {
                s.append("  ").append(name).append("\n");
            }
        }
        return s;
    }

    @NonNull
    private File[] listFiles() {
        File dir = new File(downloadPath);
        File[] files = dir.listFiles();
        return files == null ? new File[0] : files;
    }

    private void execReloadDbFromDownload() throws IOException {
        for (File f : listFiles()) {
            String absolutePath = f.getAbsolutePath();
            System.out.println();
            System.out.println("reload " + absolutePath);
            if (absolutePath.endsWith(RepoCommon.V1_JAR_NAME)) {
                execImportLocalFile(absolutePath);
            }
        }
    }

    private Repo execDownloadV1Jar(String url, boolean execImport) throws IOException {
        if (repoRepository == null) throw new NullPointerException("Missing repoRepository");

        HttpV1JarDownloadService parser = getHttpV1JarDownloadService();
        Repo repoDownloaded = new Repo();
        repoDownloaded.setLastUsedDownloadMirror(url);
        long downloadDate = System.currentTimeMillis();
        File jarFile = parser.downloadHttps(url, 0, repoDownloaded);

        Repo repoFound = repoRepository.findByAddress(repoDownloaded.getAddress());
        if (repoFound != null) {
            copy(repoFound, repoDownloaded, downloadDate);
            repoRepository.update(repoFound);
        } else {
            repoDownloaded.setLastUsedDownloadDateTimeUtc(downloadDate);
            repoRepository.insert(repoDownloaded);
            repoFound = repoDownloaded;
        }
        return repoFound;
    }

    private void execImportLocalFile(String inputPath) throws IOException {
        if (v1UpdateService == null) throw new NullPointerException("missing v1UpdateService");

        InputStream is = new FileInputStream(inputPath);
        if (inputPath.toLowerCase().endsWith(".jar")) {
            v1UpdateService.readFromJar(is, null);
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
        dest.setDownloadTaskId(src.getDownloadTaskId());
        if (src.getJarSigningCertificate() != null) dest.setJarSigningCertificate(src.getJarSigningCertificate());
        if (src.getJarSigningCertificateFingerprint() != null) dest.setJarSigningCertificateFingerprint(src.getJarSigningCertificateFingerprint());
    }
}
