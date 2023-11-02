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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import de.k3b.fdroid.domain.adapter.AppRepositoryAdapterImpl;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.entity.AppWithDetails;
import de.k3b.fdroid.domain.entity.Category;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.AppDetailRepository;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.CategoryRepository;
import de.k3b.fdroid.domain.repository.LocaleRepository;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.service.AppIconService;
import de.k3b.fdroid.domain.service.AppWithDetailsPagerService;
import de.k3b.fdroid.domain.service.CacheServiceInteger;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.util.AndroidVersionName;
import de.k3b.fdroid.domain.util.StringUtil;

@Controller
@SuppressWarnings("unused")
public class AppController {
    private static final int PAGESIZE_DEFAULT = 5;
    private static final int PAGESIZE_MAX = 25;
    private final AppRepository appRepository;
    private final LocaleRepository localeRepository;
    private final AppWithDetailsPagerService appWithDetailsPagerService;
    private final AppIconService iconService;
    private final ArrayList<Category> categoryList;
    private final CacheServiceInteger<Category> categoryCache;

    public AppController(@Value("${de.k3b.fdroid.downloads.icons}") String iconsDir,
                         RepoRepository repoRepository, AppRepository appRepository,
                         CategoryRepository categoryRepository,
                         LocaleRepository localeRepository) {
        this.appRepository = appRepository;

        AppDetailRepository<App> appAppDetailRepository = new AppRepositoryAdapterImpl(appRepository);
        appWithDetailsPagerService = new AppWithDetailsPagerService(
                appAppDetailRepository, null, null, null);
        CacheServiceInteger<Repo> cache = new CacheServiceInteger<>(repoRepository.findAll());
        iconService = new AppIconService(iconsDir, cache, appRepository);

        ArrayList<Category> categoryList = new ArrayList<>(categoryRepository.findAll());
        categoryList.add(new Category(0, ""));
        categoryList.sort(Category.COMPARE_BY_NAME);
        categoryCache = new CacheServiceInteger<>(categoryList);
        this.categoryList = categoryList;
        this.localeRepository = localeRepository;
    }

    @ResponseBody
    @GetMapping(value = WebConfig.API_ROOT + "/app/", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppWithDetails[] appListRest(
            @RequestParam(name = "q", required = false, defaultValue = "") String query,
            @RequestParam(name = "minSdk", required = false, defaultValue = "0") String minVersionSdkText,
            @RequestParam(name = "c", required = false, defaultValue = "0") String categoryIdText,
            @RequestParam(name = "s", required = false, defaultValue = "") String sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") String page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "" + PAGESIZE_DEFAULT) String pageSize,
            @RequestParam(name = "locales", required = false, defaultValue = "") String locales
    ) {
        int pageNumber = appList(query, minVersionSdkText, categoryIdText, sort, page, pageSize, locales, null);
        return appWithDetailsPagerService.getAppWithDetailsArray(pageNumber);
    }

    @GetMapping(WebConfig.HTML_APP_ROOT)
    public String appListHtml(
            @RequestParam(name = "q", required = false, defaultValue = "") String query,
            @RequestParam(name = "minSdk", required = false, defaultValue = "0") String minVersionSdkText,
            @RequestParam(name = "c", required = false, defaultValue = "0") String categoryIdText,
            @RequestParam(name = "s", required = false, defaultValue = "") String sort,
            @RequestParam(name = "page", required = false, defaultValue = "0") String page,
            @RequestParam(name = "pageSize", required = false, defaultValue = "" + PAGESIZE_DEFAULT) String pageSize,
            @RequestParam(name = "locales", required = false, defaultValue = "") String locales,
            Model model) {
        appList(query, minVersionSdkText, categoryIdText, sort, page, pageSize, locales, model);

        return "App/app_overview";
        // return "greeting";
    }

    private int appList(String query, String minVersionSdkText, String categoryIdText, String sort,
                        String pageText, String pageSizeText, String locales, Model model) {
        int minVersionSdk = StringUtil.parseInt(minVersionSdkText, 0);
        int page = StringUtil.parseInt(pageText, 0);
        int pageSize = StringUtil.parseInt(pageSizeText, PAGESIZE_DEFAULT);
        int categoryId = StringUtil.parseInt(categoryIdText, 0);

        if (pageSize < 1) pageSize = 1;
        if (pageSize > PAGESIZE_MAX) pageSize = PAGESIZE_MAX;

        AppSearchParameter appSearchParameter = new AppSearchParameter()
                .searchText(query)
                .versionSdk(minVersionSdk)
                .categoryId(categoryId)
                .orderBy(sort)
                .locales(LanguageService.getCanonicalLocalesArray(locales));
        List<Integer> appIdList = appRepository.findDynamic(appSearchParameter);
        appWithDetailsPagerService.init(appIdList, pageSize, appSearchParameter);

        int maxPage = appIdList.size() / pageSize;
        if (page >= maxPage) {
            page = maxPage - 1;
        }
        int from = Math.max(page, 0) * pageSize;

        AppWithDetailsPagerService.AppItemAtOffset[] items = appWithDetailsPagerService.itemAtOffset(from, from + pageSize);

        if (model != null) {
            StringBuilder params = new StringBuilder();
            if (!StringUtil.isEmpty(query)) params.append("&q=").append(query);
            if (minVersionSdk > 0) params.append("&minSdk=").append(minVersionSdk);
            if (categoryId > 0) params.append("&c=").append(categoryId);
            if (!StringUtil.isEmpty(sort)) params.append("&s=").append(sort);
            if (!StringUtil.isEmpty(locales)) params.append("&locales=").append(locales);
            if (pageSize != PAGESIZE_DEFAULT) params.append("&pageSize=").append(pageSize);

            model.addAttribute("item", items);
            model.addAttribute("query", query);
            model.addAttribute("minSdk", minVersionSdk);
            model.addAttribute("minSdkName", AndroidVersionName.getName(minVersionSdk, null));
            model.addAttribute("sort", sort);
            model.addAttribute("androidVersion", AndroidVersionName.getMap().entrySet());
            model.addAttribute("params", params.toString());
            model.addAttribute("back", urlencode("?page=" + page + params));
            model.addAttribute("categories", categoryList);
            model.addAttribute("category", categoryCache.getItemById(categoryId));
            model.addAttribute("locales", locales);
            model.addAttribute("localeList", localeRepository.findAll());

            if (page > 0) model.addAttribute("prev", page - 1);
            if (page + 1 < maxPage) model.addAttribute("next", page + 1);
        }
        return page;
    }

    private String urlencode(String q) {
        return URLEncoder.encode(q, StandardCharsets.UTF_8);
    }

    @GetMapping(value = WebConfig.HTML_APP_ROOT + "/icons/{packageName}.png", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] appIcon(@PathVariable String packageName) {
        File file = iconService.getOrDownloadLocalImageFile(packageName);
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