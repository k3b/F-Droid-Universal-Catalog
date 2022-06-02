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
package de.k3b.fdroid.jpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import de.k3b.fdroid.domain.entity.Version;

/**
 * Spring-Boot-Jpa (Non-Android) specific Database-Repository implementation:
 * Entity-Pojo-s are transfered from/to database using Spring-Boot-Jpa.
 * XxxxRepositoryJpa implements the Database transfer.
 * XxxxRepositoryAdapter makes XxxxRepositoryJpa compatible with XxxxRepository.
 */
@Repository
public interface VersionRepositoryJpa extends CrudRepository<Version, Integer> {
    List<Version> findByAppId(int appId);

    @Query(value = "select al from AppVersion al " +
            "where al.appId in (?1) " +
            "order by al.versionCode desc")
    List<Version> findByAppIds(List<Integer> appIds);

    @Query(value = "SELECT MAX(av.id) AS id, " +
            " av.appId, av.repoId, " +
            " min(av.minSdkVersion) AS minSdkVersion, " +
            " max(av.maxSdkVersion) AS maxSdkVersion, " +
            " max(av.targetSdkVersion) AS targetSdkVersion, " +
            " max(av.versionCode) AS versionCode, " +
            " max(av.added) AS added, " +
            " max(av.nativecode) AS nativecode, " +
            " max(av.size) AS size " +
            "FROM AppVersion AS av " +
            "WHERE ((:sdkversion = 0) OR (av.minSdkVersion <= :sdkversion AND " +
            " ((av.maxSdkVersion IS NULL) OR (av.maxSdkVersion = 0) OR (av.maxSdkVersion >= :sdkversion)))) AND " +
            " (av.nativecode IS NULL OR :nativeCode IS NULL OR av.nativecode like :nativeCode) " +
            "GROUP BY av.appId, av.repoId " +
            "ORDER BY added DESC, id desc ", nativeQuery = true)
    List<Version> findBestBySdkAndNative(int sdkversion, String nativeCode);
}

