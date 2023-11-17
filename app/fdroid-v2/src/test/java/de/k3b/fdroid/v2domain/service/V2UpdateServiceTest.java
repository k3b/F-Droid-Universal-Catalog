/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of org.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.service.AppCategoryUpdateService;
import de.k3b.fdroid.domain.service.CategoryService;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.util.Java8Util;
import de.k3b.fdroid.v2domain.entity.packagev2.V2PackageVersion;

/**
 * This test uses json data form
 * ...\F-Droid-Universal-Catalog\app\fdroid-v2\src\test\resources\exampledata\V2TestData-index-v2.json
 */
public class V2UpdateServiceTest {
    private Repo repo;
    private App app;

    private Version version;

    private V2TestData testData;

    @Before
    public void setup() {
        testData = new V2TestData();
        repo = new Repo("unittest", "https://www.fdroid.org/testrepo", null);
        repo.setId(4712);
        app = new App("my.test.app");
        app.setId(4711);
        version = new Version(app.getAppId(), repo.getId());
        version.setId(4713);
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
        sut.update(app, testData.appInfo, testData.version);

        // assert: v2import and v2import shout create the same result
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
        sut.update(repo.getId(), app.getPackageName(), testData.app);

        // assert: v2import and v2import shoud create the same result
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
        sut.update(app, localizedMap, testData.appInfo, testData.version, exceptionContext);

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

    @Test
    public void updateVersion() {
        // arrange
        V2VersionUpdateService sut = new V2VersionUpdateService(
                null,
                null,
                null).init();

        // act
        // List<Version> roomVersionList = new ArrayList<>(Arrays.asList(version));
        List<Version> roomVersionList = new ArrayList<>();

        List<V2PackageVersion> v2VersionList = Collections.singletonList(testData.version);
        sut.update(repo.getId(), app, roomVersionList, v2VersionList);
        version = roomVersionList.get(0);

        // assert: v2import and v2import shout create the same result
        String expected;
        expected = "Version[appId=4711,repoId=4712,apkName=/my.test.app_47.apk,added=2020-03-14" +
                ",versionCode=123" +
                ",versionName=1.2.3,size=1493080" +
                ",minSdkVersion=14,targetSdkVersion=21" +
                ",maxSdkVersion=32,srcname=my.test.app_10401_src.tar.gz,hash=77bf8dd...4179" +
                ",hashType=sha256" +
                // ",sig=c6c0dcf...c4cf" + // not found in v2 data
                ",signer=666d4e0...31a5" +
                "]";
        assertEquals(expected, version.toString());

        expected = "App[id=4711,packageName=my.test.app" +
                ",searchVersion=1.2.3(123)" +
                ",searchSdk=[14,21,32]" +
                ",searchSigner=666d4e0...1a5 " +
                "]";
        assertEquals(expected, app.toString());

    }

    @Test
    public void updateRepo() {
        // arrange
        V2RepoUpdateService sut = new V2RepoUpdateService(null).init();

        // act
        sut.update(repo, testData.repo);

        // assert: v1import and v2import shout create the same result
        String expected;
        expected = "Repo[id=4712,name=repo-test-name,timestamp=2022-02-06" +
                // ",version=20001,maxage=14" + // not in v2
                ",icon=repo-icon.png" +
                ",address=https://f-droid.org/test" +
                ",description=repo-test-description" +
                ",mirrors=https://f-droid.org/test1,https://f-droid.org/test2" +
                "]";
        assertEquals(expected, repo.toString());
    }
}