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
package de.k3b.fdroid.jpa.repository;

import org.springframework.stereotype.Service;

import java.util.List;

import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.repository.VersionRepository;
import de.k3b.fdroid.jpa.repository.base.RepositoryAdapterBase;

/**
 * Spring-Boot-Jpa (Non-Android) specific Database-Repository implementation:
 * Entity-Pojo-s are transfered from/to database using Spring-Boot-Jpa.
 * XxxxRepositoryJpa implements the Database transfer.
 * XxxxRepositoryAdapter makes XxxxRepositoryJpa compatible with XxxxRepository.
 */
@Service
public class VersionRepositoryAdapter extends RepositoryAdapterBase<Integer, Version, VersionRepositoryJpa> implements VersionRepository {
    public VersionRepositoryAdapter(VersionRepositoryJpa jpa) {
        super(jpa);
    }

    @Override
    public List<Version> findByAppId(int appId) {
        return jpa.findByAppId(appId);
    }

    @Override
    public List<Version> findBestBySdkAndNative(int sdkversion, String nativeCode) {
        return jpa.findBestBySdkAndNative(sdkversion, nativeCode);
    }

    @Override
    public List<Version> findByAppIds(List<Integer> appIds) {
        return jpa.findByAppIds(appIds);
    }

    @Override
    public List<Version> findByMinSdkAndAppIds(int minSdk, List<Integer> appIds) {
        return jpa.findByMinSdkAndAppIds(minSdk, appIds);
    }
}
