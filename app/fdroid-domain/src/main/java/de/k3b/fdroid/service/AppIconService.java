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

import java.io.File;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.util.StringUtil;

/**
 * get the appicon from local cache or download from repository
 */
public class AppIconService {
    // // @Value("${de.k3b.fdroid.downloads:~/.fdroid/downloads/appIcons}")
    private final File iconCacheDir;
    private final RepoRepository repoRepository;

    public AppIconService(String iconCacheDir, RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
        if (iconCacheDir == null) throw new NullPointerException();
        this.iconCacheDir = new File(iconCacheDir);
        this.iconCacheDir.mkdirs();
    }

    /**
     * return null, if there is no icon or the icon is not downloaded yet
     */
    public File getLocalIconFile(App app) {
        if (app == null) throw new NullPointerException();

        String iconFileName = app.getIcon();
        if (!StringUtil.isEmpty(iconFileName)) {
            File localIcon = new File(iconCacheDir, iconFileName);
            if (localIcon.exists()) return localIcon;

            Repo repo = repoRepository.findFirstByAppId(app.getId());

            String iconUrl = (repo == null) ? null : repo.getAppIconUrl(app.getIcon());

        }
        return null;
    }
}
