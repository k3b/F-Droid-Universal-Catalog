/*
 * Copyright (c) 2023 by k3b.
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

package de.k3b.fdroid.v1domain.service;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;

import de.k3b.fdroid.v1domain.entity.App;
import de.k3b.fdroid.v1domain.entity.IndexV1;
import de.k3b.fdroid.v1domain.entity.Version;

public class V1TestData {
    public static final IndexV1 indexV1;
    public static final App app;
    public static final Version version;

    static {
        try (InputStreamReader is = new InputStreamReader(V1TestData.class.getClassLoader().getResourceAsStream("exampledata/index-v1.small.json"))) {
            Gson gson = new Gson();
            indexV1 = gson.fromJson(is, IndexV1.class);
            app = indexV1.getApps().get(0);
            version = indexV1.getPackages().get(app.getPackageName()).get(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
