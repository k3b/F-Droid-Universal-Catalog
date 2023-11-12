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

package de.k3b.fdroid.v2domain.service;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.TreeMap;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Localized;

/**
 * create testdata for import tests from
 * ...\F-Droid-Universal-Catalog\app\fdroid-v2\src\test\resources\exampledata\example-index-v2-formatted.json
 */
public class V2UpdateServiceTest {
    App app;

    @Before
    public void setup() {
        app = new App("my.test.app");
        app.setId(4711);

    }

    @Test
    public void updateApp() {
        // arrange
        V2AppUpdateService sut = new V2AppUpdateService(null, null);

        // act
        sut.update(app, V2TestData.metadata, V2TestData.versionV2);

        // assert
        String expected = "App[id=4711,packageName=my.test.app,changelog=my-changelog," +
                "suggestedVersionName=1.2.3,suggestedVersionCode=123," +
                "issueTracker=my-issueTracker,license=my-license,sourceCode=my-sourceCode,webSite=my-webSite," +
                "added=2020-05-09," + "lastUpdated=2020-03-14" +
                ",icon=my-en-icon-name.png" +
                // ",searchCategory=my-cat1,my-cat2" + // !! new
                "]";
        // todo ?translation,categories,?donate,preferredSigner

        assertEquals(expected, app.toString());
    }

    @Test
    public void updateLocalized() {
        // arrange
        Localized lde = new Localized(app.getAppId(), "de");
        Localized len = new Localized(app.getAppId(), "en");

        Map<String, Localized> localizedMap = new TreeMap<>();
        localizedMap.put(lde.getLocaleId(), lde);
        localizedMap.put(len.getLocaleId(), len);

        V2LocalizedUpdateService sut = new V2LocalizedUpdateService(null);

        // act
        sut.update(app, localizedMap, V2TestData.metadata, V2TestData.versionV2);

        // assert
        String expectedDe = "Localized[appId=4711,localeId=de,name=my-de-name-app,summary=my-de-summary-app,description=my-de-description-app]";
        assertEquals(expectedDe, lde.toString());
        String expectedEn = "Localized[appId=4711,localeId=en,name=my-en-name-app,summary=my-en-summary-app,description=my-en-description-app" +
                ",icon=my-en-icon-name.png,whatsNew=my-en-whatsNew,phoneScreenshots=my-en-phon...e2-name]";
        assertEquals(expectedEn, len.toString());
    }
}