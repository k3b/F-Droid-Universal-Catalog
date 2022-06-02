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

import org.apache.commons.io.IOUtils;
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
import java.util.List;

import de.k3b.fdroid.domain.adapter.AppRepositoryAdapterImpl;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.AppDetailRepository;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.service.AppIconService;
import de.k3b.fdroid.domain.service.AppWithDetailsPagerService;
import de.k3b.fdroid.domain.service.CacheService;

@Controller
public class AppController {
    private static final int PAGESIZE = 5;
    private final AppRepository appRepository;
    private final AppWithDetailsPagerService appWithDetailsPagerService;
    private final AppIconService iconService;

    public AppController(@Value("${de.k3b.fdroid.downloads.icons}") String iconsDir,
                         RepoRepository repoRepository, AppRepository appRepository) {
        this.appRepository = appRepository;

        AppDetailRepository<App> appAppDetailRepository = new AppRepositoryAdapterImpl(appRepository);
        appWithDetailsPagerService = new AppWithDetailsPagerService(
                appAppDetailRepository, null, null, null);
        CacheService<Repo> cache = new CacheService<>(repoRepository.findAll());
        iconService = new AppIconService(iconsDir, cache, appRepository);
    }

    @GetMapping("/App/app")
    public String appList(
            @RequestParam(name = "q", required = false, defaultValue = "") String query,
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            Model model) {
        int from = Math.max(page, 0) * PAGESIZE;
        List<Integer> appIdList = appRepository.findDynamic(new AppSearchParameter().text(query));
        appWithDetailsPagerService.init(appIdList, PAGESIZE);
        int maxPage = appIdList.size() / PAGESIZE;
        model.addAttribute("app", appWithDetailsPagerService.itemAtOffset(from, from + PAGESIZE - 1));
        model.addAttribute("query", query);
        if (page > 0) model.addAttribute("prev", page - 1);
        if (page + 1 < maxPage) model.addAttribute("next", page + 1);
        return "App/app_overview";
        // return "greeting";
    }

    @GetMapping(value = "/App/app/icons/{packageName}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] appIcon(@PathVariable String packageName) {
        File file = iconService.getOrDownloadLocalImageFile(packageName);
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