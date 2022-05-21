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
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.util.StringUtil;

/**
 * get the repo-icon from local Cache or download from repository
 */
public class RepoIconService extends ImageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_HTML);

    public RepoIconService(String imageCacheDir) {
        super(imageCacheDir);
    }

    /**
     * @return null, if there is no icon or download fails
     */
    public File getOrDownloadLocalImageFile(Repo repo) {
        File localIconFile = getLocalImageFile(repo);

        if (localIconFile != null && !localIconFile.exists()) {
            String repoIconUrl = repo.getRepoIconUrl();
            if (!StringUtil.isEmpty(repoIconUrl)) {
                if (download(localIconFile, repoIconUrl)) {
                    return localIconFile;
                }
            }
            return null;
        }
        return localIconFile;
    }

    public File getLocalImageFile(Repo repo) {
        File result = null;
        if (repo != null && !StringUtil.isEmpty(repo.getIcon())) {
            result = getLocalImageFile("repo_" + repo.getId());
        }
        return result;
    }
}
