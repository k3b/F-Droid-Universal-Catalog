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

package de.k3b.fdroid.domain.repository;

import java.util.List;

import de.k3b.fdroid.domain.entity.Version;

/**
 * Same as default implementation but adds a filter for minSdkVersion.
 */
public class VersionRepositoryWithMinSdkFilter implements AppDetailRepository<Version> {
    private final VersionRepository versionRepository;
    private int minSdk;

    public VersionRepositoryWithMinSdkFilter(VersionRepository versionRepository) {
        this.versionRepository = versionRepository;
    }

    public void setMinSdk(int minSdk) {
        this.minSdk = minSdk;
    }

    @Override
    public List<Version> findByAppIds(List<Integer> appIds) {
        if (minSdk <= 0) {
            return versionRepository.findByAppIds(appIds);
        } else {
            return versionRepository.findByMinSdkAndAppIds(minSdk, appIds);
        }
    }
}
