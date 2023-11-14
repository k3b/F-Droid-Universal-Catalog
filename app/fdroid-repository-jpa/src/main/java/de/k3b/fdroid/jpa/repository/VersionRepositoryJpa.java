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

    @Query(value = "SELECT v FROM AppVersion v " +
            "WHERE v.appId IN (?1) " +
            "ORDER BY v.versionCode desc")
    List<Version> findByAppIds(List<Integer> appIds);

    @Query(value = "SELECT v FROM AppVersion v " +
            "WHERE" +
            " ?1 >= v.minSdkVersion AND (v.maxSdkVersion = 0 OR v.maxSdkVersion >= ?1) " +
            " AND v.appId IN (?2) " +
            "ORDER BY v.versionCode desc")
    List<Version> findByMinSdkAndAppIds(int sdk, List<Integer> appIds);

    @Query(value = "SELECT MAX(v.id) AS id, " +
            " v.appId, v.repoId, " +
            " min(v.minSdkVersion) AS minSdkVersion, " +
            " max(v.maxSdkVersion) AS maxSdkVersion, " +
            " max(v.targetSdkVersion) AS targetSdkVersion, " +
            " max(v.versionCode) AS versionCode, " +
            " max(v.added) AS added, " +
            " max(v.nativecode) AS nativecode, " +
            " max(v.size) AS size " +
            "FROM AppVersion AS v " +
            "WHERE ((?1 = 0) OR (v.minSdkVersion <= ?1 AND " +
            " ((v.maxSdkVersion IS NULL) OR (v.maxSdkVersion = 0) OR (v.maxSdkVersion >= ?1)))) AND " +
            " (v.nativecode IS NULL OR ?2 IS NULL OR v.nativecode like ?2) " +
            "GROUP BY v.appId, v.repoId " +
            "ORDER BY added DESC, id desc ", nativeQuery = true)
    List<Version> findBestBySdkAndNative(int sdkversion, String nativeCode);
}

