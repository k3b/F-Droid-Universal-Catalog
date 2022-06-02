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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.repository.RepoRepository;

/**
 * Download and import a https:.../index-v1.jar in one step:
 * <p>
 * Import direcly from the https-stream without a seperate download file.
 * Certificate check is done while importing.
 * <p>
 * Disadvantage: If certificate checks fails the database content is modified allowing vandalism and Denail-Of-Service-Attacks.
 * Advantage: Faster processing: decoding jar/json has to be done only once. No additional temp-file required.
 */
public class HttpV1JarImportService extends HttpV1JarDownloadService
        implements V1DownloadAndImportServiceInterface {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    private final RepoRepository repoRepository;
    private final V1UpdateService v1UpdateService;

    public HttpV1JarImportService(RepoRepository repoRepository, V1UpdateService v1UpdateService) {
        super("");
        this.repoRepository = repoRepository;
        this.v1UpdateService = v1UpdateService;
    }

    @Override
    protected FDroidCatalogJsonStreamParserBase createParser() {
        return v1UpdateService.init();
    }

    @Override
    public void setProgressObserver(ProgressObserver progressObserver) {
        super.setProgressObserver(progressObserver);
        v1UpdateService.setProgressObserver(progressObserver);
    }

    @Override // Implements V1DownloadAndImportServiceInterface
    public Repo download(String downloadUrl, String jarSigningCertificateFingerprintOrNull, String taskId) throws V1JarException {
        Repo repo = new Repo("NewRepository", downloadUrl);
        repo.setLastUsedDownloadMirror(downloadUrl);

        repo.setDownloadTaskId(taskId);
        try {
            File file = super.downloadHttps(downloadUrl, 0, repo);
        } catch (Throwable exception) {
            return onException(repo, exception);
        }
        return this.repoInDatabase;
    }

    @Override  // Implements V1DownloadAndImportServiceInterface
    public Repo download(int repoId, String taskId) throws V1JarException {
        Repo repo = repoRepository.findById(repoId);
        if (repo == null) {
            throw new V1JarException("download(repoId=" + repoId + "): Repo not found");
        }
        repo.setDownloadTaskId(taskId);
        repo.setLastErrorMessage(null);

        this.repoInDatabase = repo;

        try {
            super.downloadHttps(repo.getV1Url(), repo.getLastUsedDownloadDateTimeUtc(), repo);
        } catch (Throwable exception) {
            return onException(repo, exception);
        }
        return this.repoInDatabase;
    }

    @Override  // Implements V1DownloadAndImportServiceInterface
    public Repo getLastRepo() {
        return repoInDatabase;
    }

    @Override
    protected File downloadInputStream(String name, InputStream inputStream) throws IOException {
        log("Downloading... ");
        parseAndDownload(inputStream, null); // null means do not create a download-file
        return null; // no file created
    }

    private Repo onException(Repo repo, Throwable exception) {
        String context = "downloading";
        repo.setLastErrorMessage(Repo.asDateString(System.currentTimeMillis()) + ": " + exception.getMessage());

        String message = "Error " + context + " " + repo.getV1Url();
        LOGGER.error(message + " (" + repo + ")", exception);
        throw new V1JarException(message, exception);
    }

}
