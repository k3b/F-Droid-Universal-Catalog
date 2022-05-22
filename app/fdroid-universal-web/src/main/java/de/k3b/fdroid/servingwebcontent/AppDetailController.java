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
package de.k3b.fdroid.servingwebcontent;

import com.samskivert.mustache.Mustache;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collections;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.AppDetailRepository;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.domain.interfaces.VersionRepository;
import de.k3b.fdroid.html.service.GetUrlMustacheLamdaService;
import de.k3b.fdroid.service.AppWithDetailsPagerService;
import de.k3b.fdroid.service.CacheService;
import de.k3b.fdroid.service.LocalizedImageService;
import de.k3b.fdroid.service.adapter.AppRepositoryAdapterImpl;
import de.k3b.fdroid.service.adapter.LocalizedRepositoryAdapterImpl;

@Controller
public class AppDetailController {
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

        appWithDetailsPagerService = new AppWithDetailsPagerService(
                appAppDetailRepository, localizedRepositoryAdapter, versionRepository, null);

        CacheService<Repo> repoCacheService = new CacheService<>(repoRepository.findAll());
        getUrl = new GetUrlMustacheLamdaService(repoCacheService);

        localizedImageService = new LocalizedImageService(imageDir, repoCacheService, appRepository);
    }

    @GetMapping("/App/app/{idOrPackageName}")
    public String appList(
            @PathVariable String idOrPackageName,
            Model model) {
        int id = 0;
        try {
            id = Integer.parseInt(idOrPackageName);
        } catch (NumberFormatException ex) {
            App app = appRepository.findByPackageName(idOrPackageName);
            if (app != null) id = app.getId();
        }
        appWithDetailsPagerService.init(Collections.singletonList(id), 1);
        AppWithDetailsPagerService.ItemAtOffset[] item = appWithDetailsPagerService.itemAtOffset(0, 1);
        model.addAttribute("app", item);
        model.addAttribute("getUrl", getUrl);
        return "App/app_detail";
    }

    @GetMapping(value = "/App/app/{packageName}/{locale}/phoneScreenshots/{name}", produces = "image/*")
    public @ResponseBody
    byte[] localizedPhoneScreenshots(@PathVariable String packageName, @PathVariable String locale, @PathVariable String name) {
        String path = packageName + "/" + locale + "/phoneScreenshots/" + name;
        File file = localizedImageService.getOrDownloadLocalImageFile(packageName, path);
        if (file != null) {
            try (InputStream in = new FileInputStream(file)) {
                return IOUtils.toByteArray(in);
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        }
        return null;
    }

}