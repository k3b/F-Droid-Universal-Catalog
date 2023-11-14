/*
 * Copyright (c) 2022-2023 by k3b.
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

import junit.framework.TestCase;

import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.util.TestDataGenerator;

public class FormatServiceTest extends TestCase {

    public void testFormat_allValuesPresentWithHtmlEscape() {
        FormatService formatService = new FormatService(
                "Repo: '{{name}}({{lastAppCount}})' {{timestampDate}}", null);
        Repo repo = TestDataGenerator.fill(new Repo(), 4);
        repo.setName("name with <html/>");

        assertEquals("Repo: 'name with &lt;html/&gt;(4)' 1970-01-01", formatService.format(repo));
    }
    public void testFormat_allValuesNull() {
        FormatService formatService = new FormatService(
                "Repo: '{{name}}({{lastAppCount}})' {{timestampDate}}", null);
        Repo repo = new Repo();

        assertEquals("Repo: '(0)' ", formatService.format(repo));
    }

    public void testFormat_conditionalValue() {
        FormatService formatService = new FormatService(
                "<h3 {{#autoDownloadEnabled}}style='background-color:lime;'{{/autoDownloadEnabled}}>{{name}}</h3>", null);
        Repo repo = new Repo("My Name", "");

        repo.setAutoDownloadEnabled(true);
        assertEquals("<h3 style='background-color:lime;'>My Name</h3>", formatService.format(repo));

        repo.setAutoDownloadEnabled(false);
        assertEquals("<h3 >My Name</h3>", formatService.format(repo));
    }

    public void testFormatCustom() {

        class CustomType {
            final String a = "hello";
            final CustomTypeMitContext s = new CustomTypeMitContext();

            class CustomTypeMitContext implements Mustache.CustomContext {
                @Override
                public Object get(String name) throws Exception {
                    return name;
                }
            }
        }

        FormatService formatService = new FormatService(
                "a = {{a}} s.world = {{s.world}}", null);
        CustomType customType = new CustomType();

        assertEquals("a = hello s.world = world", formatService.format(customType));
    }

}