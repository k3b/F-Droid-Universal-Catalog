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
package de.k3b.fdroid.catalog.v2domain.service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.k3b.fdroid.catalog.v2domain.entity.packagev2.V2Screenshots;
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2File;

public class V2FixPhoneScreenshotService {
    public void fix(V2Screenshots v2Screenshots) {
        if (v2Screenshots != null) {
            Map<String, String> phoneDir = v2Screenshots.getPhoneDir();
            Map<String, List<V2File>> screenshotsPhone = v2Screenshots.getPhone();
            if (phoneDir == null && screenshotsPhone != null && !screenshotsPhone.isEmpty()) {
                phoneDir = new TreeMap<>();
                for (Map.Entry<String, List<V2File>> entry : screenshotsPhone.entrySet()) {
                    String language = entry.getKey();
                    String dir = fixFiles(entry.getValue());
                    if (dir != null) phoneDir.put(language, dir);
                }
                v2Screenshots.setPhoneDir(phoneDir);
            }
        }
    }

    private String fixFiles(List<V2File> v2FileList) {
        if (v2FileList != null && !v2FileList.isEmpty()) {
            // i.e. /a/b/c/d.png => /a/b/c/
            String path = v2FileList.get(0).getName();
            int pos = path.lastIndexOf('/');
            if (pos < 1) return null; // no path info, nothing to do

            path = path.substring(0, pos + 1);
            if (allNameStartWith(v2FileList, path)) {
                for (V2File f : v2FileList) {
                    f.setName(f.getName().substring(pos + 1));
                }
                return path.substring(1);
            }
        }
        return null;
    }

    protected boolean allNameStartWith(List<V2File> v2FileList, String path) {
        for (V2File f : v2FileList) {
            if (!f.getName().startsWith(path)) return false;
        }
        return true;
    }

    public void init() {
    }
}
