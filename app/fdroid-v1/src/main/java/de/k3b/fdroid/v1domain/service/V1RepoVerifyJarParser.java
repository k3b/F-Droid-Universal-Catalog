/*
 * Copyright (c) 2022-2023 by k3b.
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

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.jar.JarEntry;

import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.v1domain.entity.V1App;
import de.k3b.fdroid.v1domain.entity.V1Repo;
import de.k3b.fdroid.v1domain.entity.V1Version;
import de.k3b.fdroid.v1domain.util.JarUtilities;

/**
 * Reads and verfies the jar.
 * throws {@link V1JarException} if something goes wrong.
 */
public class V1RepoVerifyJarParser extends FDroidCatalogJsonStreamParserBase implements ProgressObservable {
    @NonNull
    private final Repo repoInDatabase;
    private V1Repo repoInJar = null;
    private int lastAppCount = 0;
    private int lastVersionCount = 0;
    private ProgressObserver progressObserver;


    public V1RepoVerifyJarParser(@NonNull Repo repoInDatabase) {
        if (repoInDatabase == null) throw new NullPointerException();

        this.repoInDatabase = repoInDatabase;
    }

    public void setProgressObserver(@Nullable ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }

    @Override
    protected void onRepo(V1Repo v1Repo) {
        this.repoInJar = v1Repo;
        lastAppCount = 0;
        lastVersionCount = 0;
    }

    @Override
    protected void afterJsonJarRead(JarEntry jarEntry) {
        super.afterJsonJarRead(jarEntry);
        if (this.repoInJar == null) {
            throw new V1JarException(repoInDatabase, "Missing Repo-Json-Entry in downloaded " + RepoCommon.V1_JAR_NAME);
        }
        if (repoInDatabase.getTimestamp() != 0 && repoInJar.getTimestamp() < repoInDatabase.getTimestamp()) {
            throw new V1JarException("Downloaded " + RepoCommon.V1_JAR_NAME +
                    " is older than current database index! "
                    + Repo.asDateString(repoInJar.getTimestamp()) + " < " + Repo.asDateString(repoInDatabase.getTimestamp()));
        }
        X509Certificate certificate = JarUtilities.getSigningCertFromJar(repoInDatabase, jarEntry);
        repoInDatabase.setLastAppCount(lastAppCount);
        repoInDatabase.setLastVersionCount(lastVersionCount);
        RepoCommon.copyCommon(repoInDatabase, repoInJar);
        if (certificate == null && progressObserver != null) {
            progressObserver.log("Warning: " + repoInDatabase.getLastUsedDownloadMirror() + ": Jar is not signed.");
        }
        JarUtilities.verifyAndUpdateSigningCertificate(repoInDatabase, certificate);
    }

    @Override
    protected V1App appFromJson(Gson gson, JsonReader reader) throws IOException {
        // save memory: we are not interested in this
        skipJsonValue(reader);
        return null;
    }

    @Override
    protected V1Version versionFromJson(Gson gson, JsonReader reader) throws IOException {
        // save memory: we are not interested in this
        skipJsonValue(reader);
        return null;
    }

    @Override
    protected void onApp(V1App v1App) {
        lastAppCount++;
    }

    @Override
    protected void onVersion(String name, V1Version v1Version) {
        lastVersionCount++;
    }

    @Override
    protected String log(String s) {
        return null;
    }
}
