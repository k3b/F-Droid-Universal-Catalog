/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid project.
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

package de.k3b.fdroid.android;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.android.v1.service.V1UpdateServiceAndroid;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.v1domain.service.V1RepoVerifyJarParser;
import de.k3b.fdroid.v1domain.service.V1UpdateService;

@RunWith(AndroidJUnit4.class)
public class V1ImportIntegrationTest {
    private FDroidDatabase db;
    private V1UpdateService importer;
    private RepoRepository repoRepository;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FDroidDatabase.class).build();

        importer = V1UpdateServiceAndroid.create(db);
        repoRepository = db.repoRepository();
    }

    @After
    public void closeDb() {
        db.close();
    }

    @Test
    public void importV1Jar() throws Exception {
        // create
        importer.readFromJar(getDemoInStream(), null);

        // update existing
        importer.readFromJar(getDemoInStream(), null);

        List<Repo> repoList = repoRepository.findAll();
        assertThat(repoList.size(), equalTo(1));
    }

    @Test
    public void verifyJar() throws Exception {

        Repo repo = new Repo();
        V1RepoVerifyJarParser verifyJarParser = new V1RepoVerifyJarParser(repo);
        verifyJarParser.readFromJar(getDemoInStream());

        // connectedDebugAndroidTest fails on android-4.2 tablet and android-4.4 phone
        // works on android-7 tablet and on android-10 phone
        assertThat(repo.getJarSigningCertificate(), is(notNullValue()));
    }

    private InputStream getDemoInStream() {
        return V1ImportIntegrationTest.class.getResourceAsStream("/c_geo_nightlies-index-v1.jar");
    }
}
