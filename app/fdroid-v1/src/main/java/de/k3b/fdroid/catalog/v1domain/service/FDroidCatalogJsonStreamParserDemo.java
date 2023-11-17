/*
 * Copyright (c) 2022-2023 by k3b.
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

package de.k3b.fdroid.catalog.v1domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.catalog.v1domain.entity.V1App;
import de.k3b.fdroid.catalog.v1domain.entity.V1Localized;
import de.k3b.fdroid.catalog.v1domain.entity.V1Repo;
import de.k3b.fdroid.catalog.v1domain.entity.V1Version;
import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.util.LocalizedStatistics;

/**
 * json stream praser demo für FDroid.org index v1 format that logs found data to the console.
 */
public class FDroidCatalogJsonStreamParserDemo extends FDroidCatalogJsonStreamParserBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    V1FixLocaleService v1FixLocaleService = new V1FixLocaleService();

    private final StringBuilder appWithNoLocale = new StringBuilder();
    private final StringBuilder appWithNoEnLocale = new StringBuilder();
    // statistics
    LocalizedStatistics statistics = new LocalizedStatistics();
    private int numberOfApps = 0;

    @Override
    protected String log(String s) {
        System.out.println(s);
        LOGGER.info(s);
        return s;
    }

    @Override
    protected void onRepo(V1Repo v1Repo) {
        log("onRepo(" + v1Repo.getName() + ")");
    }

    @Override
    protected void onApp(V1App v1App) {
        if (v1App != null) {
            numberOfApps++;
            // Map<String, V1Localized> oldLocales = v1App.getLocalized();
            v1FixLocaleService.fix(v1App);
            StringBuilder nameWithLocales = new StringBuilder();
            String lastUpdated = EntityCommon.asDateString(v1App.getLastUpdated());
            if (lastUpdated != null) {
                nameWithLocales.append(lastUpdated).append(" ");
            }

            nameWithLocales.append(v1App.getPackageName());

            Map<String, V1Localized> locales = v1App.getLocalized();
            if (locales != null && !locales.isEmpty()) {
                Set<String> localeNames = locales.keySet();
                for (String locale : localeNames) {
                    nameWithLocales.append(" ").append(locale);
                    statistics.addStatistics(locale, locales.get(locale));
                }
                if (!locales.containsKey("en")) {
                    appWithNoEnLocale.append(v1App.getPackageName()).append("\n\t").append(v1App).append("\n");
                }
            } else {
                appWithNoLocale.append(v1App.getPackageName()).append("\n\t").append(v1App).append("\n");
            }
            log("onApp(" + nameWithLocales + ")");
        }
    }

    @Override
    protected void onVersion(String name, V1Version v1Version) {
        if (v1Version != null) {
            log("onVersion(" + name +
                    "," + v1Version.getVersionName() + "," + EntityCommon.asDateString(v1Version.getAdded()) +
                    ")");
        }
    }

    public void readJsonStream(InputStream jsonInputStream) throws IOException {
        super.readJsonStream(jsonInputStream);
        log(statistics.toString());
        log("appWithNoLocale  : " + appWithNoLocale);
        log("appWithNoEnLocale: " + appWithNoEnLocale);
        log("Apps " + numberOfApps);
    }
}
