/*
 * Copyright (c) 2022-2023 by k3b.
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
package de.k3b.fdroid.servingwebcontent;

import static de.k3b.fdroid.servingwebcontent.WebConfig.HTML_APP_ROOT;

import com.samskivert.mustache.Mustache;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.adapter.AppRepositoryAdapterImpl;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.entity.AppWithDetails;
import de.k3b.fdroid.domain.entity.LocalizedLocalesSorter;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.common.ExtDoc;
import de.k3b.fdroid.domain.repository.AppDetailRepository;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.repository.LocalizedRepositoryWithLocaleFilter;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.repository.VersionRepository;
import de.k3b.fdroid.domain.repository.VersionRepositoryWithMinSdkFilter;
import de.k3b.fdroid.domain.service.AppIconService;
import de.k3b.fdroid.domain.service.AppWithDetailsPagerService;
import de.k3b.fdroid.domain.service.CacheServiceInteger;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.service.LocalizedImageService;
import de.k3b.fdroid.domain.util.StringUtil;
import de.k3b.fdroid.html.service.GetUrlMustacheLamdaService;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
@Tag(name = "App", description = "Get infos for an Android App",
        externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "App"))
@SuppressWarnings("unused")
public class AppDetailController {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_HTML);

    // url parameter
    private static final String PARAM_PACKAGENAME_DESCRIPTION = "packageName";
    private static final String PARAM_PACKAGENAME_EXAMPLE = "de.k3b.android.androFotoFinder";
    private static final String PARAM_ID_PACKAGENAME_DESCRIPTION = "appId or " + PARAM_PACKAGENAME_DESCRIPTION;
    private static final String PARAM_ID_PACKAGENAME_EXAMPLE = "131 or " + PARAM_PACKAGENAME_EXAMPLE;
    private static final String PARAM_BACK_DESCRIPTION = "Relative url to return to search page";

    // injected Services
    private final VersionRepositoryWithMinSdkFilter versionRepositoryWithMinSdkFilter;
    private final LocalizedRepositoryWithLocaleFilter localizedRepositoryAdapter;
    private final AppWithDetailsPagerService appWithDetailsPagerService;
    private final Mustache.Lambda getUrl;
    private final AppRepository appRepository;
    private final AppIconService iconService;
    private final LocalizedImageService localizedImageService;

    public AppDetailController(
            @Value("${de.k3b.fdroid.downloads.images}") String imageDir,
            @Value("${de.k3b.fdroid.downloads.icons}") String iconsDir,
            RepoRepository repoRepository, AppRepository appRepository,
            LocalizedRepository localizedRepository,
            VersionRepository versionRepository) {
        this.appRepository = appRepository;
        AppDetailRepository<App> appAppDetailRepository = new AppRepositoryAdapterImpl(appRepository);
        localizedRepositoryAdapter = new LocalizedRepositoryWithLocaleFilter(localizedRepository);
        versionRepositoryWithMinSdkFilter = new VersionRepositoryWithMinSdkFilter(versionRepository);

        appWithDetailsPagerService = new AppWithDetailsPagerService(
                appAppDetailRepository, localizedRepositoryAdapter, versionRepositoryWithMinSdkFilter,
                null);

        CacheServiceInteger<Repo> repoCacheService = new CacheServiceInteger<>(repoRepository.findAll());
        getUrl = new GetUrlMustacheLamdaService(repoCacheService);

        localizedImageService = new LocalizedImageService(imageDir, repoCacheService, appRepository);

        CacheServiceInteger<Repo> cache = new CacheServiceInteger<>(repoRepository.findAll());
        iconService = new AppIconService(iconsDir, cache, appRepository);
    }

    private static byte[] readFile(File file) {
        if (file != null && file.exists() && file.canRead()) {
            try (InputStream in = new FileInputStream(file)) {
                return IOUtils.toByteArray(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @GetMapping(HTML_APP_ROOT + "/{idOrPackageName}")
    public String appDetailHtml(
            @PathVariable
            @Schema(description = PARAM_ID_PACKAGENAME_DESCRIPTION, example = PARAM_ID_PACKAGENAME_EXAMPLE)
            String idOrPackageName,

            @RequestParam(name = "minSdk", required = false, defaultValue = "0")
            @Schema(description = ExtDoc.PARAM_MINSDK_DESCRIPTION, example = "14",
                    externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "MinSdk"))
            String minSdkText,

            @RequestParam(name = "back", required = false, defaultValue = "")
            @Schema(description = PARAM_BACK_DESCRIPTION)
            String back,

            @RequestParam(name = "locales", required = false, defaultValue = "")
            @Schema(description = ExtDoc.PARAM_LOKALES_DESCRIPTION, example = "de,en",
                    externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Locale"))
            String locales,

            Model model) {
        AppWithDetails item = appDetail(
                idOrPackageName, minSdkText, locales);

        model.addAttribute("item", item);
        model.addAttribute("getUrl", getUrl);
        model.addAttribute("back", back);
        return "App/app_detail";
    }

    @ResponseBody
    @GetMapping(value = WebConfig.API_ROOT + "/app/{idOrPackageName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppWithDetails appDetailRest(
            @PathVariable
            @Schema(description = PARAM_ID_PACKAGENAME_DESCRIPTION, example = PARAM_ID_PACKAGENAME_EXAMPLE)
            String idOrPackageName,

            @RequestParam(name = "minSdk", required = false, defaultValue = "0")
            @Schema(description = ExtDoc.PARAM_MINSDK_DESCRIPTION, example = "14",
                    externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "MinSdk"))
            String minSdkText,

            @RequestParam(name = "back", required = false, defaultValue = "")
            @Schema(description = PARAM_BACK_DESCRIPTION)
            String back,

            @RequestParam(name = "locales", required = false, defaultValue = "")
            @Schema(description = ExtDoc.PARAM_LOKALES_DESCRIPTION, example = "de,en",
                    externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Locale"))
            String locales) {
        AppWithDetails appWithDetails = appDetail(idOrPackageName, minSdkText, locales);
        if (appWithDetails.getVersionList().isEmpty()) {
            LOGGER.info("appDetailRest(idOrPackageName='{}', minSdk='{}') : no Version found for {}",
                    idOrPackageName, minSdkText, appWithDetails);
            return null;
        }
        return appWithDetails;
    }

    private AppWithDetails appDetail(
            String idOrPackageName, String minSdkText, String locales) {
        int minVersionSdk = StringUtil.parseInt(minSdkText, 0);
        int id = 0;
        try {
            id = Integer.parseInt(idOrPackageName);
        } catch (NumberFormatException ex) {
            App app = appRepository.findByPackageName(idOrPackageName);
            if (app != null) id = app.getId();
        }

        String[] canonicalLocalesArray = LanguageService.getCanonicalLocalesArray(locales);
        AppSearchParameter appSearchParameter = (StringUtil.isEmpty(locales))
                ? null
                : new AppSearchParameter()
                .locales(canonicalLocalesArray);

        versionRepositoryWithMinSdkFilter.setMinSdk(minVersionSdk);
        localizedRepositoryAdapter.setLocales(canonicalLocalesArray);
        appWithDetailsPagerService.init(Collections.singletonList(id), 1, appSearchParameter);

        AppWithDetails item = appWithDetailsPagerService.itemAtOffset(0, 1)[0].getAppWithDetails();
        if (canonicalLocalesArray != null && item.getLocalizedList() != null) {
            item.getLocalizedList().sort(new LocalizedLocalesSorter<>(canonicalLocalesArray));
        }
        return item;
    }

    @GetMapping(value = HTML_APP_ROOT + "/{packageName}/{locale}/phoneScreenshots/{phoneScreenshotNameWithoutPath}", produces = "image/*")
    public @ResponseBody
    byte[] localizedPhoneScreenshots(
            @PathVariable
            @Schema(description = PARAM_PACKAGENAME_DESCRIPTION, example = PARAM_PACKAGENAME_EXAMPLE)
            String packageName,

            @PathVariable
            @Schema(description = "Locale or language of the result", example = "de",
                    externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Locale"))
            String locale,

            @PathVariable
            @Schema(example = "1-Gallery.png")
            String phoneScreenshotNameWithoutPath) {
        String path = packageName + "/" + locale + "/phoneScreenshots/" + phoneScreenshotNameWithoutPath;
        return readFile(localizedImageService.getOrDownloadLocalImageFile(packageName, path));
    }

    @GetMapping(value = WebConfig.HTML_APP_ROOT + "/icon/{packageName}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] appIcon(@PathVariable
                   @Schema(description = PARAM_PACKAGENAME_DESCRIPTION, example = PARAM_PACKAGENAME_EXAMPLE)
                   String packageName) {
        return readFile(iconService.getOrDownloadLocalImageFile(packageName));
    }
}