/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of org.fdroid.v2domain the fdroid json catalog-format-v1 parser.
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
import org.mockito.Mockito;

import java.util.Map;
import java.util.TreeMap;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.service.AppCategoryUpdateService;
import de.k3b.fdroid.domain.service.CategoryService;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.util.Java8Util;

/**
 * This test uses json data form
 * ...\F-Droid-Universal-Catalog\app\fdroid-v2\src\test\resources\exampledata\V2TestData-index-v2.json
 */
public class V2UpdateServiceTest {
    Repo repo;
    App app;

    @Before
    public void setup() {
        repo = new Repo("unittest", "https://www.fdroid.org/testrepo", null);
        repo.setId(4712);
        app = new App("my.test.app");
        app.setId(4711);

    }

    private static V2LocalizedUpdateService createLocalizedUpdateService() {
        return new V2LocalizedUpdateService(
                null,
                new LanguageService(null),
                new V2FixPhoneScreenshotService());
    }

    @Test
    public void updateApp() {
        // arrange
        V2AppUpdateService sut = new V2AppUpdateService(
                null,
                null,
                null
        );

        // act
        sut.update(app, V2TestData.metadata, V2TestData.versionV2);

        // assert: v1import and v2import shout create the same result
        String expected = "App[id=4711,packageName=my.test.app,changelog=my-changelog," +
                "suggestedVersionName=1.2.3,suggestedVersionCode=123," +
                "issueTracker=my-issueTracker,license=my-license,sourceCode=my-sourceCode,webSite=my-webSite," +
                "added=2020-05-09," + "lastUpdated=2020-03-14" +
                ",icon=my-en-icon-name.png" +
                ",searchCategory=my-cat1,my-cat2" +
                "]";
        // todo ?translation,?donate,preferredSigner

        assertEquals(expected, app.toString());
    }

    @Test
    public void updateAppWithDetailsIntegrationsTest() {
        AppRepository appRepository = Mockito.mock(AppRepository.class);
        Mockito.when(appRepository.findByPackageName(app.getPackageName())).thenReturn(app);

        V2AppUpdateService sut = new V2AppUpdateService(
                appRepository,
                createLocalizedUpdateService(),
                new AppCategoryUpdateService(
                        new CategoryService(null),
                        null)
        ).init();

        // act
        sut.update(repo.getId(), app.getPackageName(), V2TestData.packageV2);

        // assert: v1import and v2import shoud create the same result
        String expected = "App[id=4711,resourceRepoId=4712,packageName=my.test.app," +
                "changelog=my-changelog,suggestedVersionName=1.2.3,suggestedVersionCode=123," +
                "issueTracker=my-issueTracker,license=my-license,sourceCode=my-sourceCode," +
                "webSite=my-webSite,added=2020-05-09,lastUpdated=2020-03-14," +
                "icon=my-en-icon-name.png,searchCategory=my-cat1,my-cat2," +
                "searchName=:de: my-de...ame-app," +
                "searchSummary=:de: my-de...ary-app," +
                "searchDescription=:de: my-de...ion-app,searchWhatsNew=:en: my-en-whatsNew" +
                "]";

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

        V2LocalizedUpdateService sut = createLocalizedUpdateService().init();

        // act
        Java8Util.OutParam<Localized> exceptionContext = new Java8Util.OutParam<>(null);
        sut.update(app, localizedMap, V2TestData.metadata, V2TestData.versionV2, exceptionContext);

        // assert
        String expectedDe = "Localized[appId=4711,localeId=de,name=my-de-name-app,summary=my-de-summary-app,description=my-de-description-app]";
        assertEquals(expectedDe, lde.toString());
        String expectedEn = "Localized[appId=4711,localeId=en,name=my-en-name-app,summary=my-en-summary-app,description=my-en-description-app" +
                ",icon=my-en-icon-name.png" +
                ",video=my-en-video" +
                ",whatsNew=my-en-whatsNew" +
                ",phoneScreenshotDir=my.test.app/en-US/phoneScreenshots/" +
                ",phoneScreenshots=my-en-phone1-name.pn...n-phone2-name.png" +
                "]";
        assertEquals(expectedEn, len.toString());
    }
}