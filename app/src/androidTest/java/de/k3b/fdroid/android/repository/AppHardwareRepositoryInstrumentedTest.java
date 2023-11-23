/*
 * Copyright (c) 2023 by k3b.
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
package de.k3b.fdroid.android.repository;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.entity.AppHardware;
import de.k3b.fdroid.domain.repository.AppHardwareRepository;
import de.k3b.fdroid.domain.util.TestHelper;

/**
 * Database Repository Instrumented test, which will execute on an Android device.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AppHardwareRepositoryInstrumentedTest {
    TestHelper testHelper;
    // testdata
    private int appId;
    private int hardwareProfileId;
    // JPA specific
    private AppHardwareRepository repo;

    private void setupAndroid() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        FDroidDatabaseFactory factory = FDroidDatabase.getINSTANCE(context, true);
        repo = factory.appHardwareRepository();

        testHelper = new TestHelper(new RoomFDroidDatabaseFacade(factory));
    }

    @Before
    public void setUp() {
        setupAndroid();
        appId = testHelper.createApp().getId();
        hardwareProfileId = testHelper.createHardwareProfile().getId();

        AppHardware appHardware = new AppHardware(appId, hardwareProfileId);
        appHardware.getMin().setVersionCode(1);
        appHardware.getMax().setVersionCode(99);
        repo.insert(appHardware);
    }

    @Test
    public void findByAppId() {
        List<AppHardware> list = repo.findByAppId(appId);
        assertEquals(1, list.size());
    }
}