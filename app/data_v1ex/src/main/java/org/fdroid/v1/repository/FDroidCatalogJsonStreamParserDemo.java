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

import org.fdroid.v1.model.App;
import org.fdroid.v1.model.Localized;
import org.fdroid.v1.model.LocalizedStatistics;
import org.fdroid.v1.model.Repo;
import org.fdroid.v1.model.Version;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

/** json stream praser demo für FDroid.org index v1 format that logs found data to the console. */
public class FDroidCatalogJsonStreamParserDemo extends FDroidCatalogJsonStreamParserBase {
    LocalizedStatistics statistics = new LocalizedStatistics();
    @Override
    protected String log(String s) {
        System.out.println(s);
        return s;
    }

    @Override
    protected void onRepo(Repo repo) {
        log("onRepo(" + repo.getName() + ")");
    }

    @Override
    protected void onApp(App app) {
        StringBuilder nameWithLocales = new StringBuilder();
        String lastUpdated = asDateString(app.getLastUpdated());
        if (lastUpdated != null) {
            nameWithLocales.append(lastUpdated).append(" ");
        }

        nameWithLocales.append(app.getPackageName());

        Map<String, Localized> locales = app.getLocalized();
        if (locales != null && !locales.isEmpty()) {
            Set<String> localeNames = locales.keySet();
            for (String locale : localeNames) {
                nameWithLocales.append(" ").append(locale);
                statistics.addStatistics(locale, locales.get(locale));
            }
        }
        log("onApp(" + nameWithLocales + ")");
    }

    @Override
    protected void onVersion(String name, Version version) {
        log("onVersion(" + name +
                "," + version.getVersionName() + "," + asDateString(version.getAdded()) +
                ")");
    }

    public void readJsonStream(InputStream in) throws IOException {
        super.readJsonStream(in);
        log(statistics.toString());
    }
}
