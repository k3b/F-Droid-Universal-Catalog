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
package de.k3b.fdroid.jpa.view;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import org.aspectj.lang.annotation.Before;
import org.junit.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.service.FormatService;
import de.k3b.fdroid.service.adapter.ValueAndStringTranslations;
import de.k3b.fdroid.util.TestDataGenerator;

public class JpaStringResourceMustacheContextTest {
    JpaStringResourceMustacheContext translator;

    @BeforeEach
    public void setup() {
        translator = new JpaStringResourceMustacheContext(Locale.US);
    }

    @Test
    public void translateContextString() {
        ValueAndStringTranslations vt = new ValueAndStringTranslations("World", translator);

        FormatService formatService = new FormatService(
                "Hello '{{v}}' from {{t.app_name}}");
        String format = formatService.format(vt);
        // R.string.app_name exists
        assertThat(format, equalTo("Hello 'World' from FDroid Universal Catalog"));
    }

    @Test
    public void repoText() throws Exception {
        Repo repo = TestDataGenerator.fill(new Repo(),4);
        ValueAndStringTranslations vt = new ValueAndStringTranslations(repo, translator);

        String template = (String) translator.get("list_repo");
        FormatService formatService = new FormatService(template);
        String format = formatService.format(vt);
    }
}