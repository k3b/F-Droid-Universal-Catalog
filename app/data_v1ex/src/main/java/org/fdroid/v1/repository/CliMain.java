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

package org.fdroid.v1.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/** a tiny j2se cli app to read the repository */
public class CliMain {
    public static void main(String[] args)  {
        try {
            FDroidCatalogJsonStreamParserBase repo = new FDroidCatalogJsonStreamParserDemo();

            // InputStream is = new FileInputStream("/home/EVE/StudioProjects/FDroid/app/data_v1ex/src/main/java/org/fdroid/v1/repository/index-v1.small.json");
            // InputStream is = new FileInputStream("/home/EVE/StudioProjects/FDroid/app/data_v1ex/src/main/java/org/fdroid/v1/repository/index-v1.full.json");
            //index-v1ex.small.json");

            // repo.readJsonStream(is);

            InputStream is = new FileInputStream("/home/EVE/StudioProjects/FDroid/app/data_v1ex/src/main/java/org/fdroid/v1/repository/index-v1.jar");
            repo.readFromJar(is);
            is.close();
            System.exit(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
