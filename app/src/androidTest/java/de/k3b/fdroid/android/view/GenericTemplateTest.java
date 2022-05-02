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
package de.k3b.fdroid.android.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.test.platform.app.InstrumentationRegistry;

import com.samskivert.mustache.Mustache;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Locale;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.html.service.FormatService;
import de.k3b.fdroid.html.service.ResourceBundleMustacheContext;
import de.k3b.fdroid.html.util.FormatUtil;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(Parameterized.class)
public class GenericTemplateTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);
    private static Mustache.CustomContext translator;
    private final Object testParamExampleItem;
    private final String testParamTemplateId;
    Context appContext;
    private Locale oldDefault;

    public GenericTemplateTest(Object exampleItem, String templateId) {
        super();
        this.testParamExampleItem = exampleItem;
        this.testParamTemplateId = templateId;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> input() throws Exception {
        translator = new ResourceBundleMustacheContext(Locale.US);
        return FormatUtil.getTestCases();
    }

    @Before
    public void setup() {
        setLocale("en", "US");
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        translator = new AndroidStringResourceMustacheContext(appContext);

    }

    @After
    public void teardown() {
        //  restore the Locale in the tear down method, to avoid running into issues
        Locale.setDefault(Locale.Category.DISPLAY, oldDefault);
    }

    @Test // iterate over resources does not work on android :-(
    public void domainTemplateTest() {
        System.out.println("<!-- running template " + testParamExampleItem.getClass().getSimpleName() + "/" + testParamTemplateId + " -->");
        FormatService formatService = new FormatService(
                testParamTemplateId, testParamExampleItem.getClass(), translator);
        String format = formatService.format(testParamExampleItem);
        LOGGER.info(format);
    }

    // Locale during unit test on Android see https://stackoverflow.com/questions/16760194/locale-during-unit-test-on-android/21810126
    private void setLocale(String language, String country) {
        oldDefault = Locale.getDefault(Locale.Category.DISPLAY);
        Locale locale = new Locale(language, country);
        // here we update locale for date formatters
        Locale.setDefault(Locale.Category.DISPLAY, locale);
        // update locale for app resources
        Resources res = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }
}