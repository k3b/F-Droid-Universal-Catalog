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
package de.k3b.fdroid.html.service;

import com.samskivert.mustache.Mustache;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.util.TestDataGenerator;

public class ResourceBundleMustacheContextTest {
    Mustache.CustomContext translator;

    @Before
    public void setup() {
        translator = new ResourceBundleMustacheContext(Locale.US);
    }

    @Test
    public void translateContextString() {
        String format = format("Hello '{{v}}' from {{t.app_name}}", "World");
        MatcherAssert.assertThat(format, CoreMatchers.equalTo("Hello 'World' from FDroid Universal Catalog"));
    }

    @Test
    public void repoText() throws Exception {
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
