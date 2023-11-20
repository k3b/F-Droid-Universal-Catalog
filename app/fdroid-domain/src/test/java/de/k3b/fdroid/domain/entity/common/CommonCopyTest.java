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
package de.k3b.fdroid.domain.entity.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import de.k3b.fdroid.domain.util.TestDataGenerator;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * tests that XxxCommon.toString() == CopyOf(XxxCommon).toString().
 */
@RunWith(JUnitParamsRunner.class)
public class CommonCopyTest {
    private static final LocalizedCommon LOCALIZED_COMMON_EMPTY = new LocalizedCommon();
    private static final LocalizedCommon LOCALIZED_COMMON_EMPTY_COPY = new LocalizedCommon(LOCALIZED_COMMON_EMPTY);
    private static final LocalizedCommon LOCALIZED_COMMON_FILLED = TestDataGenerator.fill(new LocalizedCommon(), 4);
    private static final LocalizedCommon LOCALIZED_COMMON_FILLED_COPY = new LocalizedCommon(LOCALIZED_COMMON_FILLED);

    private static final VersionCommon VERSION_COMMON_EMPTY = new VersionCommon();
    private static final VersionCommon VERSION_COMMON_EMPTY_COPY = new VersionCommon(VERSION_COMMON_EMPTY);
    private static final VersionCommon VERSION_COMMON_FILLED = TestDataGenerator.fill(new VersionCommon(), 4);
    private static final VersionCommon VERSION_COMMON_FILLED_COPY = new VersionCommon(VERSION_COMMON_FILLED);

    private static final AppCommon APP_COMMON_EMPTY = new AppCommon();
    private static final AppCommon APP_COMMON_EMPTY_COPY = new AppCommon(APP_COMMON_EMPTY);
    private static final AppCommon APP_COMMON_FILLED = TestDataGenerator.fill(new AppCommon(), 4);
    private static final AppCommon APP_COMMON_FILLED_COPY = new AppCommon(APP_COMMON_FILLED);

    private static final RepoCommon REPO_COMMON_EMPTY = new RepoCommon();
    private static final RepoCommon REPO_COMMON_EMPTY_COPY = new RepoCommon(REPO_COMMON_EMPTY);
    private static final RepoCommon REPO_COMMON_FILLED = TestDataGenerator.fill(new RepoCommon(), 4);
    private static final RepoCommon REPO_COMMON_FILLED_COPY = new RepoCommon(REPO_COMMON_FILLED);

    @SuppressWarnings("unused")
    private Object[] getExamples() {
        return new Object[]{
                new Object[]{"LOCALIZED_COMMON_EMPTY", LOCALIZED_COMMON_EMPTY, LOCALIZED_COMMON_EMPTY_COPY},
                new Object[]{"LOCALIZED_COMMON_FILLED", LOCALIZED_COMMON_FILLED, LOCALIZED_COMMON_FILLED_COPY},

                new Object[]{"VERSION_COMMON_EMPTY", VERSION_COMMON_EMPTY, VERSION_COMMON_EMPTY_COPY},
                new Object[]{"VERSION_COMMON_FILLED", VERSION_COMMON_FILLED, VERSION_COMMON_FILLED_COPY},

                new Object[]{"APP_COMMON_EMPTY", APP_COMMON_EMPTY, APP_COMMON_EMPTY_COPY},
                new Object[]{"APP_COMMON_FILLED", APP_COMMON_FILLED, APP_COMMON_FILLED_COPY},

                new Object[]{"REPO_COMMON_EMPTY", REPO_COMMON_EMPTY, REPO_COMMON_EMPTY_COPY},
                new Object[]{"REPO_COMMON_FILLED", REPO_COMMON_FILLED, REPO_COMMON_FILLED_COPY},

        };
    }

    @Test
    @Parameters(method = "getExamples")
    @SuppressWarnings("JUNIT")
    public void localized(String comment, Object common, Object copy) {
        assertEquals(comment, common.toString(), copy.toString());
    }
}
