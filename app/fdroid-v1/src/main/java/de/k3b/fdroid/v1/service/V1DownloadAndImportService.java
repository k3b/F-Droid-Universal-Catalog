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

import static de.k3b.fdroid.util.StringUtil.replaceEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.util.StringUtil;

public class V1DownloadAndImportService implements ProgressObservable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);
    private final RepoRepository repoRepository;
    private final HttpV1JarDownloadService downloadService;
    private final V1UpdateService v1UpdateService;

    public V1DownloadAndImportService(
            RepoRepository repoRepository, HttpV1JarDownloadService downloadService, V1UpdateService v1UpdateService) {
        this.repoRepository = repoRepository;
        this.downloadService = downloadService;
        this.v1UpdateService = v1UpdateService;
    }

    /**
     * @throws V1JarException if security relevant properties differ
     */
    public static Repo copy(Repo dest, Repo src) throws V1JarException {
        dest.setJarSigningCertificate(replaceEmptySecure(
                src.getJarSigningCertificate(), dest.getJarSigningCertificate(), "jarSigningCertificate"));
        dest.setJarSigningCertificateFingerprint(replaceEmptySecure(
                src.getJarSigningCertificateFingerprint(), dest.getJarSigningCertificateFingerprint(), "jarSigningCertificateFingerprint"));
        dest.setName(replaceEmptySecure(
                src.getName(), dest.getName(), "name"));
        dest.setAddress(replaceEmptySecure(
                src.getAddress(), dest.getAddress(), "address"));

        dest.setLastUsedDownloadMirror(replaceEmpty(src.getLastUsedDownloadMirror(), dest.getLastUsedDownloadMirror()));
        // dest.setLastErrorMessage(replaceEmpty(src.getLastErrorMessage(), dest.getLastErrorMessage()));
        dest.setLastErrorMessage(src.getLastErrorMessage());
        dest.setLastUsedDownloadDateTimeUtc(replaceEmpty(src.getLastUsedDownloadDateTimeUtc(), dest.getLastUsedDownloadDateTimeUtc()));
        dest.setAutoDownloadEnabled(replaceEmpty(src.isAutoDownloadEnabled(), dest.isAutoDownloadEnabled()));

        RepoCommon.copyCommon(dest, src);
        return dest;
    }

    /**
     * @param downloadUrl                            where data comes from
     * @param jarSigningCertificateFingerprintOrNull optionalan fingerprint
     * @param taskId                                 optional info about task currently downloading.
     * @return info about the downloaded repo data. unsaved, (if something goes wrong)
     * @throws V1JarException if something went wrong.
     */
    public Repo download(String downloadUrl, String jarSigningCertificateFingerprintOrNull, String taskId) throws V1JarException {
        String context = "downloading";
        Repo repo = new Repo("NewRepository", downloadUrl);
        repo.setLastUsedDownloadMirror(downloadUrl);
        repo.setJarSigningCertificateFingerprint(jarSigningCertificateFingerprintOrNull);
        repo.setDownloadTaskId(taskId);
        try {
            File file = downloadService.download(downloadUrl, 0, repo);

            context = "importing";
            if (file != null) {
                return importRepo(repo, file);
            }
        } catch (Throwable exception) {
            return onException(repo, exception, context);
        }
        return null;
    }

    public Repo download(int repoId, String taskId) throws V1JarException {
        Repo repo = repoRepository.findById(repoId);
        if (repo == null)
            throw new V1JarException("download(repoId=" + repoId + "): Repo not found");
        repo.setDownloadTaskId(taskId);
        return download(repo);
    }

    /**
     * @param repo containing infos what to download.
     * @return null if nothing changed since last download else updated/saved repo data.exception = {DataIntegrityViolationException@9551} "org.springframework.dao.DataIntegrityViolationException: could not execute statement; SQL [n/a]; nested exception is org.hibernate.exception.DataException: could not execute statement"
     * @throws V1JarException if something went wrong.
     */
    public Repo download(@NonNull Repo repo) throws V1JarException {
        String context = "downloading";
        // assume no error yet
        repo.setLastErrorMessage(null);
        try {
            File file = downloadService.download(repo.getV1Url(), repo.getLastUsedDownloadDateTimeUtc(), repo);
            context = "importing";
            if (file != null) {
                return importRepo(repo, file);
            }
        } catch (Throwable exception) {
            onException(repo, exception, context);
        } finally {
            // always save
            v1UpdateService.save(repo);
        }
        return null;
    }

    private Repo fixRepo(Repo repoFromImport) {
        if (repoFromImport.getId() == 0) {
            // new Repo not saved yet. Try to find existing db-repo to be used instead
            Repo repoFromDatabase = repoRepository.findCorrespondigRepo(repoFromImport);
            if (repoFromDatabase != null) {
                return copy(repoFromDatabase, repoFromImport);
            }
        }
        return repoFromImport;
    }

    private Repo importRepo(Repo repo, File file) throws IOException {
        repo = fixRepo(repo);

        if (file != null) {
            v1UpdateService.readFromJar(new FileInputStream(file), repo);
            repo.setLastUsedDownloadDateTimeUtc(System.currentTimeMillis());
            repo.setDownloadTaskId(null);
            repoRepository.save(repo);
        } else {
            // no error: there was no update since last download
            repo.setLastUsedDownloadDateTimeUtc(System.currentTimeMillis());
            repo.setDownloadTaskId(null);
            repoRepository.save(repo);
            return null;
        }
        return repo;
    }

    /**
     * same as {@link StringUtil#replaceEmpty(String, String)} but throws {@link V1JarException}
     * if both have values that are not the same.
     * @param context ie "jarSigningCertificate" meaning that the certificate must be the same
     * @throws V1JarException if security relevant properties differ
     */
    private static String replaceEmptySecure(String newValue, String emptyValueReplacement, String context) throws V1JarException {
        if (StringUtil.isEmpty(newValue)) return emptyValueReplacement;

        // dest not empty
        if (!StringUtil.isEmpty(emptyValueReplacement) && 0 != newValue.compareToIgnoreCase(emptyValueReplacement)) {
            throw new V1JarException(context + " values are not the same: " +
                    newValue + " != " + emptyValueReplacement);
        }
        return newValue;
    }

    private Repo onException(Repo repo, Throwable exception, String context) {
        repo.setLastErrorMessage(Repo.asDateString(System.currentTimeMillis()) + ": " + exception.getMessage());

        String message = "Error " + context + " " + repo.getV1Url();
        LOGGER.error(message + " (" + repo + ")", exception);
        throw new V1JarException(message, exception);
    }


    @Override
    public void setProgressObserver(ProgressObserver progressObserver) {
        downloadService.setProgressObserver(progressObserver);
        v1UpdateService.setProgressObserver(progressObserver);
    }
}
