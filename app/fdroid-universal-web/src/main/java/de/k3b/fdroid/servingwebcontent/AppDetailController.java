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

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collections;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.domain.interfaces.AppDetailRepository;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.domain.interfaces.VersionRepository;
import de.k3b.fdroid.html.service.GetUrlMustacheLamdaService;
import de.k3b.fdroid.service.AppWithDetailsPagerService;
import de.k3b.fdroid.service.CacheService;
import de.k3b.fdroid.service.adapter.AppRepositoryAdapterImpl;
import de.k3b.fdroid.service.adapter.LocalizedRepositoryAdapterImpl;

@Controller
public class AppDetailController {
    private final AppWithDetailsPagerService appWithDetailsPagerService;
    private final Mustache.Lambda getUrl;
    private final AppRepository appRepository;

    public AppDetailController(RepoRepository repoRepository, AppRepository appRepository,
                               LocalizedRepository localizedRepository,
                               VersionRepository versionRepository) {
        this.appRepository = appRepository;
        AppDetailRepository<App> appAppDetailRepository = new AppRepositoryAdapterImpl(appRepository);
        AppDetailRepository<Localized> localizedRepositoryAdapter = new LocalizedRepositoryAdapterImpl(localizedRepository);

        appWithDetailsPagerService = new AppWithDetailsPagerService(
                appAppDetailRepository, localizedRepositoryAdapter, versionRepository, null);

        getUrl = new GetUrlMustacheLamdaService(new CacheService<>(repoRepository.findAll()));
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
}