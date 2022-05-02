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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.util.TestDataGenerator;

/**
 * iterates over all mustache templates *.hbs and executes it-s formatter.
 * Assumed file structure
 * <p>
 * * .../FDroidUniversal/app/fdroid-html/src/test/java/de/k3b/fdroid/html/service
 * * .../FDroidUniversal/app/fdroid-html/build/resources/main/html/Repo/list_repo.hbs
 */
@RunWith(Parameterized.class)
public class GenericTemplateTest {
    private static File htmlDir;
    private static String namespace;
    private static Mustache.CustomContext translator;

    private final Object item;
    private final String templateId;

    public GenericTemplateTest(Object item, String templateId) {
        super();
        this.item = item;
        this.templateId = templateId;
    }

    // @BeforeClass
    private static void init() throws IOException {
        File buildDir = getBuildDir();
        htmlDir = new File(buildDir, "resources/main/html");
        String name = Repo.class.getName();
        int len = name.lastIndexOf('.');
        namespace = name.substring(0, len + 1);

        translator = new ResourceBundleMustacheContext(Locale.US);
    }

    private static File getBuildDir() throws IOException {
        File pwd = new File(".").getCanonicalFile();
        File buildDir = new File(pwd, "build");
        if (buildDir.isDirectory() && buildDir.exists()) return buildDir;

        buildDir = pwd;
        while (!buildDir.getName().equals("build")) {
            buildDir = buildDir.getParentFile();
            if (buildDir == null) return null;
        }
        return buildDir;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> input() throws Exception {
        init();
        List<Object[]> result = new ArrayList<>();

        for (File dir : listFiles(htmlDir)) {
            if (!dir.getName().startsWith(".") && dir.isDirectory()) {
                Class<?> itemClass = Class.forName(namespace + dir.getName());
                Object item = TestDataGenerator.fill(itemClass.getConstructor().newInstance(), 4);

                inputTemplates(result, dir, item);
            }
        }
        return result;
    }

    // to make lint happy about null pointer exceptions
    private static File[] listFiles(File htmlDir) {
        File[] files = (htmlDir == null) ? null : htmlDir.listFiles();
        return files == null ? new File[0] : files;
    }

    private static void inputTemplates(List<Object[]> result, File dir, Object item) {
        if (dir != null) {
            for (File template : listFiles(dir)) {
                String name = template.getName();
                if (name.endsWith(".hbs")) {
                    int len = name.lastIndexOf('.');
                    String templateId = name.substring(0, len);

                    result.add(new Object[]{item, templateId});
                }
            }
        }
    }

    @Test
    public void domainTemplateTest() {
        System.out.println("<!-- running template " + item.getClass().getSimpleName() + "/" + templateId + " -->");
        FormatService formatService = new FormatService(
                templateId, item.getClass(), translator);
        String format = formatService.format(item);
        System.out.println(format);
    }
}
