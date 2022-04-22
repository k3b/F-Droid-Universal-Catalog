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

import java.security.cert.X509Certificate;
import java.util.jar.JarEntry;

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.v1.domain.App;
import de.k3b.fdroid.v1.domain.Version;
import de.k3b.fdroid.v1.service.util.JarUtilities;

/**
 * Reads and verfies the jar.
 * throws {@link V1JarException} if something goes wrong.
 */
public class V1RepoVerifyJarParser extends FDroidCatalogJsonStreamParserBase {
    private final Repo repoInDatabase;
    private de.k3b.fdroid.v1.domain.Repo repoInJar = null;

    public V1RepoVerifyJarParser(Repo repoInDatabase) {
        this.repoInDatabase = repoInDatabase;
    }

    @Override
    protected void onRepo(de.k3b.fdroid.v1.domain.Repo repo) {
        this.repoInJar = repo;
    }

    @Override
    protected void afterJsonJarRead(JarEntry jarEntry) {
        super.afterJsonJarRead(jarEntry);
        if (this.repoInJar == null) {
            throw new V1JarException(repoInDatabase, "Missing Repo-Json-Entry in downloaded " + RepoCommon.V1_JAR_NAME);
        }
        if (repoInDatabase != null && repoInDatabase.getTimestamp() != 0 && repoInJar.getTimestamp() < repoInDatabase.getTimestamp()) {
            throw new V1JarException("Downloaded " + RepoCommon.V1_JAR_NAME +
                    " is older than current database index! "
                    + Repo.asDateString(repoInJar.getTimestamp()) + " < " + Repo.asDateString(repoInDatabase.getTimestamp()));
        }
        X509Certificate certificate = JarUtilities.getSigningCertFromJar(jarEntry);
        RepoCommon.copyCommon(repoInDatabase, repoInJar);
        JarUtilities.verifySigningCertificate(repoInDatabase, certificate);
    }

    @Override
    protected void onApp(App app) {
    }

    @Override
    protected void onVersion(String name, Version version) {
    }

    @Override
    protected String log(String s) {
        return null;
    }
}
