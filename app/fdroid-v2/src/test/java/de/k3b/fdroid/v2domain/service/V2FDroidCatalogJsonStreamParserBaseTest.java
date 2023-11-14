/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of de.k3b.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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
package de.k3b.fdroid.v2domain.service;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;

import de.k3b.fdroid.v2domain.entity.packagev2.V2App;
import de.k3b.fdroid.v2domain.entity.repo.V2Repo;

public class V2FDroidCatalogJsonStreamParserBaseTest {
    @Test
    public void readJsonStream() throws IOException {
        String path;
        path = "C:\\Users\\eve\\fdroid\\downloads\\examples\\fdroid-20231109\\example-index-v2-formatted.json";

        Parser parser = new Parser();
        try (FileInputStream is = new FileInputStream(path)) {
            parser.readJsonStream(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class Parser extends V2FDroidCatalogJsonStreamParserBase {

        @Override
        protected String log(String s) {
            System.out.println(s);
            return null;
        }

        @Override
        protected void onRepo(V2Repo repo) {
            System.out.println(repo.getAddress());

        }

        @Override
        protected void onPackage(String name, V2App v2App) {
            System.out.println(name);
        }
    }
}