/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid.v1domain the fdroid json catalog-format-v1 parser.
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

package de.k3b.fdroid.catalog.v1domain.service;

import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;

public interface V1DownloadAndImportServiceInterface extends ProgressObservable {
    /**
     * @param downloadUrl                            where data comes from
     * @param jarSigningCertificateFingerprintOrNull optional a fingerprint
     * @param taskId                                 optional info about task currently downloading.
     * @return info about the downloaded repo data. unsaved, (if something goes wrong)
     * @throws V1JarException if something went wrong.
     */
    Repo download(String downloadUrl, String jarSigningCertificateFingerprintOrNull, String taskId) throws V1JarException;

    Repo download(int repoId, String taskId) throws V1JarException;

    Repo getLastRepo();
}
