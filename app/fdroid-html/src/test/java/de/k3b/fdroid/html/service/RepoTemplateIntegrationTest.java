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

import static org.junit.Assert.assertEquals;

import com.samskivert.mustache.Mustache;

import org.junit.Test;

import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.html.util.HtmlUtil;

public class RepoTemplateIntegrationTest {
    private final Mustache.CustomContext dummy = new DummyMustacheContext();

    @Test
    public void listRepo_containsStateError() {
        Repo repo = new Repo();
        repo.setLastErrorMessage("some error");

        FormatService formatService = new FormatService(
                "list_repo", Repo.class, dummy);

        String html = formatService.format(repo);
        String cssClass = HtmlUtil.getHtmlCssClassState(html);
        assertEquals("error", cssClass);
    }
}
