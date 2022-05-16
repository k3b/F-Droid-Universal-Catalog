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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.CachedDownloadImageService;
import de.k3b.fdroid.util.IOUtils;
import de.k3b.fdroid.util.StringUtil;

/**
 * get the appicon from local repoCache or download from repository
 */
public class AppIconService implements CachedDownloadImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_HTML);
    public static final String ICON_SUFFIX = ".png";

    // iconCacheDir @Value("${de.k3b.fdroid.downloads.icons:~/.fdroid/downloads/icons}")
    private final File iconCacheDir;

    private final AppRepository appRepository;
    private final CacheService<Repo> repoCache;

    public AppIconService(String iconCacheDir, CacheService<Repo> repoCache, AppRepository appRepository) {
        this.repoCache = repoCache;
        if (iconCacheDir == null) throw new NullPointerException();

        this.appRepository = appRepository;
        this.iconCacheDir = new File(iconCacheDir);
        this.iconCacheDir.mkdirs();
    }

    /**
     * @return null, if there is no icon or download fails
     */
    public File getOrDownloadLocalIconFile(App app) {
        File localIconFile = getLocalIconFile(app);

        if (localIconFile != null && !localIconFile.exists()) {
            Integer repoId = app.getResourceRepoId();
            if (repoId != null) {
                Repo repo = repoCache.getItemById(repoId);
                if (repo != null) {
                    String appIconUrl = repo.getAppIconUrl(app.getIcon());
                    if (!StringUtil.isEmpty(appIconUrl)) {
                        if (download(localIconFile, appIconUrl)) {
                            return localIconFile;
                        }
                    }
                }
            }
            return null;
        }
        return localIconFile;
    }

    /**
     * @return null, if there is no icon or download fails
     */
    @Override
    public File getOrDownloadLocalIconFile(String packageName) {
        File localIconFile = getLocalIconFile(packageName);

        if (localIconFile != null && !localIconFile.exists()) {
            String packageNameWithoutIconSuffix = packageName.endsWith(ICON_SUFFIX)
                    ? packageName.substring(0, packageName.length() - ICON_SUFFIX.length())
                    : packageName;
            return getOrDownloadLocalIconFile(appRepository.findByPackageName(packageNameWithoutIconSuffix));
        }
        return localIconFile;
    }

    private boolean download(File localIconFile, String appIconUrl) {

        InputStream response = null;
        FileOutputStream outputStream = null;
        try {
            URL url = new URL(appIconUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = connection.getInputStream();

                if (response != null) {

                    File tempFile = new File(localIconFile.getParentFile(), localIconFile.getName() + ".tmp");
                    if (tempFile.exists()) tempFile.delete();
                    outputStream = new FileOutputStream(tempFile, false);
                    IOUtils.copy(response, outputStream);
                    outputStream.close();
                    if (localIconFile.exists()) localIconFile.delete();
                    tempFile.renameTo(localIconFile);

                    LOGGER.debug("getLocalIconFile-Downladed('{}' <- '{}')", localIconFile, appIconUrl);
                    return true;
                }
            }

        } catch (Exception e) {
            LOGGER.error("getLocalIconFile-Downladed('{}' <- '{}') exception {}", localIconFile, appIconUrl, e.getMessage());
            LOGGER.error("getLocalIconFile-Downladed", e);
        } finally {
            IOUtils.closeQuietly(outputStream, response);
        }
        return false;
    }

    private File getLocalIconFile(App app) {
        File result = null;
        if (app != null && !StringUtil.isEmpty(app.getIcon())) {
            result = getLocalIconFile(app.getPackageName());
        }
        return result;

    }

    @Override
    public File getExistingLocalIconFileOrNull(String packageName) {
        File result = getLocalIconFile(packageName);
        if (result == null || !result.exists()) return null;
        return result;
    }

    private File getLocalIconFile(String packageName) {
        File result = null;
        if (!StringUtil.isEmpty(packageName)) {
            String localFileName = (packageName.endsWith(ICON_SUFFIX))
                    ? packageName
                    : (packageName + ICON_SUFFIX);
            result = new File(this.iconCacheDir, localFileName);
        }
        return result;
    }
}