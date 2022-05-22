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
package de.k3b.fdroid.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.util.StringUtil;

/**
 * get the appicon from local repoCache or download from repository
 */
public class AppIconService extends ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_HTML);

    private final AppRepository appRepository;
    private final CacheService<Repo> repoCache;

    public AppIconService(String imageCacheDir, CacheService<Repo> repoCache, AppRepository appRepository) {
        super(imageCacheDir);
        this.repoCache = repoCache;

        this.appRepository = appRepository;
    }

    /**
     * @return null, if there is no icon or download fails
     */
    public File getOrDownloadLocalImageFile(App app) {
        File iconFile = getLocalImageFile(app);

        if (!error(iconFile)) {
            // (icon download ok) -> nothing to do
            return iconFile;
        }
        if (iconFile == null || iconFile.exists()) {
            // (no icon defined) || (error download) -> nothing to do
            return null;
        }

        // for download need repo to calculate url
        Integer repoId = app.getResourceRepoId();
        if (repoId != null) {
            Repo repo = repoCache.getItemById(repoId);
            if (repo != null) {
                String appIconUrl = repo.getAppIconUrl(app.getIcon());
                if (!StringUtil.isEmpty(appIconUrl)) {
                    if (download(iconFile, appIconUrl)) {
                        return iconFile;
                    }
                }
            }
        }
        return null;
    }

    /**
     * @return null, if there is no icon or download fails
     */
    public File getOrDownloadLocalImageFile(String packageName) {
        File iconFile = getLocalImageFile(packageName);

        if (!error(iconFile)) {
            // (icon download ok) -> nothing to do
            return iconFile;
        }
        if (iconFile == null || iconFile.exists()) {
            // (no icon defined) || (error download) -> nothing to do
            return null;
        }

        // for download need app to calculate url
        String packageNameWithoutIconSuffix = packageName.endsWith(IMAGE_SUFFIX)
                ? packageName.substring(0, packageName.length() - IMAGE_SUFFIX.length())
                : packageName;
        return getOrDownloadLocalImageFile(appRepository.findByPackageName(packageNameWithoutIconSuffix));

    }

    public File getLocalImageFile(App app) {
        File result = null;
        if (app != null && !StringUtil.isEmpty(app.getIcon())) {
            result = getLocalImageFile(app.getPackageName());
        }
        return result;

    }

}
