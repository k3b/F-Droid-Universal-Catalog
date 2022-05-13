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
package de.k3b.fdroid.android.repository;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.AppRepositoryFindDynamic;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AppRepositoryFindDynamicInstrumentedTest {
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";

    private AppRepository repo;
    private App app = null;

    @Before
    public void setUp() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        FDroidDatabaseFactory factory = FDroidDatabase.getINSTANCE(context);
        repo = factory.appRepository();

        app = new App(MY_PACKAGE_NAME);
        app.setIcon(MY_ICON);

        repo.insert(app);
    }

    @After
    public void finish() {
        repo.delete(app);
    }

    @Test
    public void findDynamic_search() {
        List<Integer> apps = repo.findDynamic(new AppRepositoryFindDynamic.AppSearchParameter().text("acka my"));
        assertThat(apps.size(), equalTo(1));
    }
}