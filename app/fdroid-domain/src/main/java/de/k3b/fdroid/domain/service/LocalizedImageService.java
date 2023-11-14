/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid project.
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
package de.k3b.fdroid.domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.util.StringUtil;

/**
 * get an app-image from local Cache or download from repository
 */
public class LocalizedImageService extends ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_HTML);
    private final CacheServiceInteger<Repo> repoCache;
    private final AppRepository appRepository;

    public LocalizedImageService(String imageCacheDir, CacheServiceInteger<Repo> repoCache, AppRepository appRepository) {
        super(imageCacheDir);
        this.repoCache = repoCache;
        this.appRepository = appRepository;
    }

    /**
     * @return null, if there is no icon or download fails
     */
    public File getOrDownloadLocalImageFile(String packageName, String path) {
        File iconFile = getLocalImageFile(path);

        if (!error(iconFile)) {
            // (icon download ok) -> nothing to do
            LOGGER.debug("getOrDownloadLocalImageFile(packageName='{}', path='{}') returned cached file '{}'",
                    packageName, path, iconFile);
            return iconFile;
        }
        if (iconFile == null || iconFile.exists()) {
            // (no icon defined) || (error download) -> nothing to do
            LOGGER.debug("getOrDownloadLocalImageFile(packageName='{}', path='{}') returned null: No icon or error download ",
                    packageName, path);
            return null;
        }

        iconFile = getOrDownloadLocalImageFile(appRepository.findByPackageName(packageName), path);
        LOGGER.debug("getOrDownloadLocalImageFile(packageName='{}', path='{}') returned downloaded file '{}'",
                packageName, path, iconFile);
        return iconFile;
    }

    /**
     * @return null, if there is no icon or download fails
     */
    public File getOrDownloadLocalImageFile(App app, String path) {
        File iconFile = getLocalImageFile(path);

        if (!error(iconFile)) {
            // (icon download ok) -> nothing to do
            LOGGER.debug("getOrDownloadLocalImageFile(app='{}', path='{}') returned cached file '{}'",
                    app, path, iconFile);
            return iconFile;
        }
        if (iconFile == null || iconFile.exists() || app == null) {
            // (no icon defined) || (error download) -> nothing to do
            LOGGER.debug("getOrDownloadLocalImageFile(app='{}', path='{}') returned null: No icon or error download ",
                    app, path);
            return null;
        }

        // for download need repo to calculate url
        Integer repoId = app.getResourceRepoId();
        if (repoId != null) {
            Repo repo = repoCache.getItemById(repoId);
            if (repo != null) {
                String appIconUrl = repo.getUrl(path);
                if (!StringUtil.isEmpty(appIconUrl)) {
                    iconFile.getParentFile().mkdirs();
                    if (download(iconFile, appIconUrl)) {
                        LOGGER.debug("getOrDownloadLocalImageFile(app='{}', path='{}') returned downloaded file '{}'",
                                app, path, iconFile);
                        return iconFile;
                    }
                }
            }
        }
        LOGGER.debug("getOrDownloadLocalImageFile(app='{}', path='{}') returned null: No icon or error download ",
                app, path);
        return null;
    }

    public File getLocalImageFile(String path) {
        if (StringUtil.isEmpty(path)) return null;
        return new File(this.imageCacheDir, path);
    }
}
