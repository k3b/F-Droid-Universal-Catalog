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

import static org.junit.Assert.assertNotNull;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.entity.HardwareProfile;
import de.k3b.fdroid.domain.repository.HardwareProfileRepository;
import de.k3b.fdroid.domain.util.TestHelper;

/**
 * Database Repository Instrumented test, which will execute on an Android device.
 * <p>
 * Note: ...android.repository.XxxRepositoryInstrumentedTest should do the same as ...jpa.repository.XxxRepositoryTest
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HardwareProfileRepositoryInstrumentedTest {
    // testdata
    private static final String MY_NAME = "my-test-profile";

    // Android Room Test specific
    TestHelper testHelper;
    private HardwareProfileRepository repo;

    private void setupAndroid() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        FDroidDatabaseFactory factory = FDroidDatabase.getINSTANCE(context, true);
        repo = factory.hardwareProfileRepository();

        testHelper = new TestHelper(new RoomFDroidDatabaseFacade(factory));
    }

    @Before
    public void setUp() {
        setupAndroid();
        HardwareProfile hardwareProfile = new HardwareProfile();
        hardwareProfile.setName(MY_NAME);
        repo.insert(hardwareProfile);
    }

    @Test
    public void findByName() {
        HardwareProfile hardwareProfile = repo.findByName(MY_NAME);
        assertNotNull("found", hardwareProfile);
    }
}