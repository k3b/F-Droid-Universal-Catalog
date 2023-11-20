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
package de.k3b.fdroid.html.service;

import com.samskivert.mustache.Mustache;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Locale;

import de.k3b.fdroid.html.util.FormatUtil;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/**
 * iterates over all mustache templates *.hbs and executes it-s formatter.
 * Assumed file structure
 * <p>
 * * .../FDroidUniversal/app/fdroid-html/src/test/java/de/k3b/fdroid/html/service
 * * .../FDroidUniversal/app/fdroid-html/build/resources/main/html/Repo/list_repo.hbs
 */
@RunWith(JUnitParamsRunner.class)
public class GenericTemplateTest {
    private static Mustache.CustomContext translator;

    // @Parameterized.Parameters
    @SuppressWarnings("unused")
    public static Object[] getExamples() throws Exception {
        translator = new ResourceBundleMustacheContext(Locale.US);
        return FormatUtil.getTestCaseArray();
    }

    @Test
    @Parameters(method = "getExamples")
    @SuppressWarnings("JUNIT")
    public void domainTemplateTest(Object testParamExampleItem, String testParamTemplateId) {
        System.out.println("<!-- running template " + testParamExampleItem.getClass().getSimpleName() + "/" + testParamTemplateId + " -->");
        FormatService formatService = new FormatService(
                testParamTemplateId, testParamExampleItem.getClass(), translator);
        String format = formatService.format(testParamExampleItem);
        System.out.println(format);
    }
}
