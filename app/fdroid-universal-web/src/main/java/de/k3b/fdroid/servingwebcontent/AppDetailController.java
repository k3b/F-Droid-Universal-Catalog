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
import de.k3b.fdroid.domain.adapter.LocalizedRepositoryAdapterImpl;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppWithDetails;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.AppDetailRepository;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.repository.VersionRepository;
import de.k3b.fdroid.domain.repository.VersionRepositoryWithMinSdkFilter;
import de.k3b.fdroid.domain.service.AppWithDetailsPagerService;
import de.k3b.fdroid.domain.service.CacheServiceInteger;
import de.k3b.fdroid.domain.service.LocalizedImageService;
import de.k3b.fdroid.domain.util.StringUtil;
import de.k3b.fdroid.html.service.GetUrlMustacheLamdaService;

@Controller
public class AppDetailController {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_HTML);

    private final VersionRepositoryWithMinSdkFilter versionRepositoryWithMinSdkFilter;
    private final AppWithDetailsPagerService appWithDetailsPagerService;
    private final Mustache.Lambda getUrl;
    private final AppRepository appRepository;
    private final LocalizedImageService localizedImageService;

    public AppDetailController(
            @Value("${de.k3b.fdroid.downloads.images}") String imageDir,
            RepoRepository repoRepository, AppRepository appRepository,
            LocalizedRepository localizedRepository,
            VersionRepository versionRepository) {
        this.appRepository = appRepository;
        AppDetailRepository<App> appAppDetailRepository = new AppRepositoryAdapterImpl(appRepository);
        AppDetailRepository<Localized> localizedRepositoryAdapter = new LocalizedRepositoryAdapterImpl(localizedRepository);
        versionRepositoryWithMinSdkFilter = new VersionRepositoryWithMinSdkFilter(versionRepository);

        appWithDetailsPagerService = new AppWithDetailsPagerService(
                appAppDetailRepository, localizedRepositoryAdapter, versionRepositoryWithMinSdkFilter,
                null);

        CacheServiceInteger<Repo> repoCacheService = new CacheServiceInteger<>(repoRepository.findAll());
        getUrl = new GetUrlMustacheLamdaService(repoCacheService);

        localizedImageService = new LocalizedImageService(imageDir, repoCacheService, appRepository);
    }

    @GetMapping(HTML_APP_ROOT + "/{idOrPackageName}")
    public String appDetailHtml(
            @PathVariable String idOrPackageName,
            @RequestParam(name = "minSdk", required = false, defaultValue = "0") String minVersionSdkText,
            @RequestParam(name = "back", required = false, defaultValue = "") String back,
            Model model) {
        AppWithDetailsPagerService.AppItemAtOffset[] item = appDetail(idOrPackageName, minVersionSdkText);

        model.addAttribute("item", item);
        model.addAttribute("getUrl", getUrl);
        model.addAttribute("back", back);
        return "App/app_detail";
    }

    @ResponseBody
    @GetMapping(value = WebConfig.API_ROOT + "/app/{idOrPackageName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppWithDetails appDetailRest(
            @PathVariable String idOrPackageName,
            @RequestParam(name = "minSdk", required = false, defaultValue = "0") String minVersionSdkText) {
        AppWithDetails appWithDetails = appDetail(idOrPackageName, minVersionSdkText)[0].getAppWithDetails();
        if (appWithDetails.getVersionList().isEmpty()) {
            LOGGER.info("appDetailRest(idOrPackageName='{}', minSdk='{}') : no Version found for {}",
                    idOrPackageName, minVersionSdkText, appWithDetails);
            return null;
        }
        return appWithDetails;
    }

    private AppWithDetailsPagerService.AppItemAtOffset[] appDetail(String idOrPackageName, String minVersionSdkText) {
        int minVersionSdk = StringUtil.parseInt(minVersionSdkText, 0);
        int id = 0;
        try {
            id = Integer.parseInt(idOrPackageName);
        } catch (NumberFormatException ex) {
            App app = appRepository.findByPackageName(idOrPackageName);
            if (app != null) id = app.getId();
        }
        versionRepositoryWithMinSdkFilter.setMinSdk(minVersionSdk);
        appWithDetailsPagerService.init(Collections.singletonList(id), 1);

        AppWithDetailsPagerService.AppItemAtOffset[] item = appWithDetailsPagerService.itemAtOffset(0, 1);
        return item;
    }

    @GetMapping(value = HTML_APP_ROOT + "/{packageName}/{locale}/phoneScreenshots/{name}", produces = "image/*")
    public @ResponseBody
    byte[] localizedPhoneScreenshots(@PathVariable String packageName, @PathVariable String locale, @PathVariable String name) {
        String path = packageName + "/" + locale + "/phoneScreenshots/" + name;
        File file = localizedImageService.getOrDownloadLocalImageFile(packageName, path);
        if (file != null) {
            try (InputStream in = new FileInputStream(file)) {
                return IOUtils.toByteArray(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}