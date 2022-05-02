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
package de.k3b.fdroid.html.util;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.util.TestDataGenerator;

public class FormatUtil {
    public static File[] getResourceFolderFiles(String folder) {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        URL url = loader.getResource(folder);
        if (url != null) {
            String path = url.getPath();
            return new File(path).listFiles();
        }
        return new File[0];
    }

    private static String getNamespace(Class<Repo> baseClass) {
        String name = baseClass.getName();
        int len = name.lastIndexOf('.');
        return name.substring(0, len + 1);
    }

    /**
     * Helper for Parameterized-tests: return list with exampleItem and templateId
     */
    public static Collection<Object[]> getTestCases() throws Exception {
        List<Object[]> result = new ArrayList<>();
        String namespace = namespace = getNamespace(Repo.class);
        for (File dir : getResourceFolderFiles("html")) {
            if (!dir.getName().startsWith(".") && dir.isDirectory()) {
                Class<?> itemClass = Class.forName(namespace + dir.getName());
                Object exampleItem = TestDataGenerator.fill(itemClass.getConstructor().newInstance(), 4);

                collectTemplates(result, dir, exampleItem);
            }
        }
        return result;
    }

    // to make lint happy about null pointer exceptions
    private static File[] listFiles(File htmlDir) {
        File[] files = (htmlDir == null) ? null : htmlDir.listFiles();
        return files == null ? new File[0] : files;
    }

    private static void collectTemplates(List<Object[]> result, File dir, Object exampleItem) {
        if (dir != null) {
            for (File template : listFiles(dir)) {
                String name = template.getName();
                if (name.endsWith(".hbs")) {
                    int len = name.lastIndexOf('.');
                    String templateId = name.substring(0, len);

                    result.add(new Object[]{exampleItem, templateId});
                }
            }
        }
    }
}
