/*
 * Copyright (c) 2022 by k3b.
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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.util.IOUtils;
import de.k3b.fdroid.domain.util.StringUtil;

/**
 * get the appicon from local repoCache or download from repository
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public abstract class ImageService {
    protected static final String IMAGE_SUFFIX = ".png";

    /**
     * One day measured in Milliseconds
     */
    private static final long DAY_IN_MILLISECS = 1000 * 60 * 60 * 24;

    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_HTML);
    protected final File imageCacheDir;

    public ImageService(String imageCacheDir) {
        if (imageCacheDir == null) throw new NullPointerException();
        this.imageCacheDir = new File(imageCacheDir);
        this.imageCacheDir.mkdirs();
    }

    protected boolean download(File iconFile, String appIconUrl) {
        InputStream response = null;
        FileOutputStream outputStream = null;
        try {
            URL url = new URL(appIconUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                response = connection.getInputStream();

                if (response != null) {
                    File tempFile = new File(iconFile.getParentFile(), iconFile.getName() + ".tmp");
                    if (tempFile.exists()) tempFile.delete();
                    outputStream = new FileOutputStream(tempFile, false);
                    IOUtils.copy(response, outputStream);
                    outputStream.close();
                    if (iconFile.exists()) iconFile.delete();
                    tempFile.renameTo(iconFile);

                    LOGGER.info("downladed-icon('{}' <- '{}')", iconFile, appIconUrl);
                    return true;
                }
            }

            LOGGER.warn("downladed-icon('{}' <- '{}') : {} - {}", iconFile, appIconUrl,
                    connection.getResponseCode(), connection.getResponseMessage());

            // create 1 byte file as error marker
            outputStream = new FileOutputStream(iconFile, false);
            outputStream.write(1);
            outputStream.flush();
            outputStream.close();

        } catch (Exception e) {
            LOGGER.error("downladed-icon('{}' <- '{}') exception {}", iconFile, appIconUrl, e.getMessage());
            LOGGER.error("downladed-icon", e);
        } finally {
            IOUtils.closeQuietly(outputStream, response);
        }
        return false;
    }

    public boolean error(File file) {
        if (file == null || !file.exists()) return true;
        if (file.length() > 10) return false;
        if (System.currentTimeMillis() - file.lastModified() > DAY_IN_MILLISECS) {
            // Download-Error-File older than 1 Day. Delete to trigger re-Download.
            file.delete();
        }
        return true;
    }

    public File getExistingLocalImageFileOrNull(String packageName) {
        File result = getLocalImageFile(packageName);
        if (!error(result)) return result;
        return null;
    }

    public File getLocalImageFile(String packageName) {
        File result = null;
        if (!StringUtil.isEmpty(packageName)) {
            String localFileName = (packageName.endsWith(IMAGE_SUFFIX))
                    ? packageName
                    : (packageName + IMAGE_SUFFIX);
            result = new File(this.imageCacheDir, localFileName);
        }
        return result;
    }
}
