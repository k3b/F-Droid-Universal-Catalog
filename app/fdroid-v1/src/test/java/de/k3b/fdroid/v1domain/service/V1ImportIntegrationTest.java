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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.RepoRepository;

public class V1ImportIntegrationTest {
    private V1UpdateService importer;
    private RepoRepository repoRepository;

    /*
    @Test
    public void importV1Jar() throws Exception {
        // create
        importer.readFromJar(getDemoInStream(), null);

        // update existing
        importer.readFromJar(getDemoInStream(), null);

        List<Repo> repoList = repoRepository.findAll();
        assertThat(repoList.size(), equalTo(1));
    }

     */

    /// TODO fix me
    @Ignore("failed since 2023-10-27. Why??")
    @Test
    public void verifyJar() throws IOException {

        Repo repo = new Repo();
        V1RepoVerifyJarParser verifyJarParser = new V1RepoVerifyJarParser(repo);
        verifyJarParser.readFromJar(getDemoInStream());
        assertThat(repo.getJarSigningCertificate(), is(notNullValue()));
    }

    private InputStream getDemoInStream() {
        return V1ImportIntegrationTest.class.getResourceAsStream("/c_geo_nightlies-index-v1.jar");
    }
}