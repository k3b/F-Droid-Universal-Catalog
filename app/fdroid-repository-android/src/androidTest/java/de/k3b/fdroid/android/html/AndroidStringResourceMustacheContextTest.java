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
package de.k3b.fdroid.android.html;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.html.service.FormatService;
import de.k3b.fdroid.html.util.MustacheEx;
import de.k3b.fdroid.util.TestDataGenerator;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class AndroidStringResourceMustacheContextTest {
    private Locale oldDefault;
    AndroidStringResourceMustacheContext translator;
    Context appContext;

    @Before
    public void setup() {
        setLocale("en", "US");
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        translator = new AndroidStringResourceMustacheContext(appContext);
        MustacheEx.addFixedProperty("t", translator);


    }
    @After
    public void teardown() {
        //  restore the Locale in the tear down method, to avoid running into issues
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Locale.setDefault(Locale.Category.DISPLAY, oldDefault);
        } else {
            Locale.setDefault(oldDefault);
        }
    }

    @Test
    public void translateContextString() {
        FormatService formatService = new FormatService(
                "Hello '{{v}}' from {{t.test_label}}", translator);
        Map<String, Object> map = new HashMap<>();
        map.put("v", "World");
        String format = formatService.format(map);

        // R.strings.test_label=AndroidTest
        assertThat(format, equalTo("Hello 'World' from AndroidTest"));
    }

    @Test
    public void repoText() throws Exception {
        Repo repo = TestDataGenerator.fill(new Repo(), 4);
        FormatService formatService = new FormatService(
                "list_repo", repo.getClass(), translator);
        String format = formatService.format(repo);
        Log.i(Global.LOG_TAG_HTML, format);
    }

    // Locale during unit test on Android see https://stackoverflow.com/questions/16760194/locale-during-unit-test-on-android/21810126
    private void setLocale(String language, String country) {
        Locale locale = new Locale(language, country);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            oldDefault = Locale.getDefault(Locale.Category.DISPLAY);
            // here we update locale for date formatters
            Locale.setDefault(Locale.Category.DISPLAY, locale);
        } else {
            oldDefault = Locale.getDefault();
            Locale.setDefault(locale);
        }
        // update locale for app resources
        Resources res = InstrumentationRegistry.getInstrumentation().getTargetContext().getResources();
        Configuration config = res.getConfiguration();
        config.locale = locale;
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

}