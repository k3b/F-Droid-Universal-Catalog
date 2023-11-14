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

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.util.TestDataGenerator;
import de.k3b.fdroid.html.util.MustacheEx;

public class ResourceBundleMustacheContextTest {
    Mustache.CustomContext translator;

    @Before
    public void setup() {
        translator = new ResourceBundleMustacheContext(Locale.US);
        MustacheEx.addFixedProperty("t", translator);
    }

    @Test
    public void translateContextString() {
        Map<String, Object> value = new HashMap<>();
        value.put("name", "World");
        String format = format("Hello '{{name}}' from {{t.app_name}}", value);
        MatcherAssert.assertThat(format, CoreMatchers.equalTo("Hello 'World' from FDroid Universal Catalog"));
    }

    @Test
    public void repoText() throws Exception {
        MustacheEx.addFixedProperty("t", translator);
        Repo repo = TestDataGenerator.fill(new Repo(), 4);
        FormatService formatService = new FormatService(
                "list_repo", Repo.class, translator);

        String format = formatService.format(repo);
        System.out.println(format);
    }

    private String format(String template, Object values) {
        FormatService formatService = new FormatService(
                template, translator);
        return formatService.format(values);
    }
}
