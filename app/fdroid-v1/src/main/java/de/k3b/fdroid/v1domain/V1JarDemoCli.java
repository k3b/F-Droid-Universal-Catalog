/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid.v1domain the fdroid json catalog-format-v1 parser.
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

package de.k3b.fdroid.v1domain;

import java.io.FileInputStream;
import java.io.InputStream;

import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.v1domain.service.FDroidCatalogJsonStreamParserBase;
import de.k3b.fdroid.v1domain.service.FDroidCatalogJsonStreamParserDemo;
import de.k3b.fdroid.v1domain.service.HttpV1JarDownloadService;
import de.k3b.fdroid.v1domain.service.V1RepoVerifyJarParser;

/**
 * a tiny j2se cli app to read the repository from a local v1-jar file
 */
public class V1JarDemoCli {
    public static void main(String[] args) {
        String path;
        path = "/home/EVE/StudioProjects/FDroid/app/fdroid-v1/src/test/java/de/k3b/fdroid/v1/exampledata/index-v1.jar";

        // path = "/home/EVE/StudioProjects/FDroid/app/fdroid-v1/src/test/java/de/k3b/fdroid/v1/exampledata/index-v1.small.json";
        // path = "/home/EVE/StudioProjects/FDroid/app/fdroid-v1/src/test/java/de/k3b/fdroid/v1/exampledata/index-v1.full.json";
        // index-v1ex.small.json");

        // testParserWithConsoleOutput(path);

        // testV1RepoVerifyJarParser(path);
        testDownbload();
    }

    private static void testDownbload() {
        HttpV1JarDownloadService parser = new HttpV1JarDownloadService("~/.fdroid/downloads");

        // repo.setLastUsedDownloadDateTimeUtc(DateUtils.parseDate("Thu, 21 Apr 2022 17:36:30 GMT").getTime());

        String[] urls = new String[]{
                "https://apt.izzysoft.de/fdroid/repo/index-v1.jar",
                "https://f-droid.org/archive/index-v1.jar",
                "https://guardianproject.info/fdroid/repo/index-v1.jar",
                "https://f-droid.org/repo/index-v1.jar",
                "https://fdroid.cgeo.org/nightly/index-v1.jar",
        };

        for (String url : urls) {
            Repo repo = new Repo();
            try {
                parser.downloadHttps(url, repo.getLastUsedDownloadDateTimeUtc(), repo);

            } catch (Exception ex) {
                System.out.println(repo + ":" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    private static void testV1RepoVerifyJarParser(String path) {
        Repo repo = new Repo();
        // wrong fingerprint
        repo.setJarSigningCertificateFingerprint("0cb2814380117cd5621064c1d7512b32e3cb8c8cb2b1f20016f6da763598d738");
        try {
            V1RepoVerifyJarParser parser = new V1RepoVerifyJarParser(repo);
            InputStream is = new FileInputStream(path);
            parser.readFromJar(is);
            is.close();
            System.exit(0);

        } catch (Exception ex) {
            System.out.println(repo + ":" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private static void testParserWithConsoleOutput(String path) {
        try {
            FDroidCatalogJsonStreamParserBase repo = new FDroidCatalogJsonStreamParserDemo();


            // repo.readJsonStream(is);

            InputStream is = new FileInputStream(path);
            repo.readFromJar(is);
            is.close();
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
