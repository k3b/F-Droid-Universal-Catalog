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
package de.k3b.fdroid.android.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import java.util.List;

import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.repository.VersionRepository;

@Dao
public interface VersionDao extends VersionRepository {
    default void insert(Version version) {
        int result = (int) insertEx(version);
        version.setId(result);
    }

    @Insert
    long insertEx(Version version);

    @Update
    void update(Version version);

    @Delete
    void delete(Version version);

    @Query("SELECT * FROM AppVersion WHERE AppVersion.appId = :appId")
    List<Version> findByAppId(int appId);

    @Query(value = "select v.* from AppVersion v " +
            "where v.appId in (:appIds) " +
            "order by v.versionCode desc")
    List<Version> findByAppIds(List<Integer> appIds);

    @Query("SELECT v.* FROM AppVersion AS v WHERE" +
            " :sdk >= v.minSdkVersion AND (v.maxSdkVersion = 0 OR v.maxSdkVersion >= :sdk) AND " +
            " v.appId in (:appIds) " +
            "order by v.versionCode desc")
    List<Version> findByMinSdkAndAppIds(int sdk, List<Integer> appIds);

    @Query("SELECT MAX(v.id) AS id, " +
            " v.appId, v.repoId, " +
            " min(v.minSdkVersion) AS minSdkVersion, " +
            " max(v.maxSdkVersion) AS maxSdkVersion, " +
            " max(v.targetSdkVersion) AS targetSdkVersion, " +
            " max(v.versionCode) AS versionCode, " +
            " max(v.added) AS added, " +
            " max(v.nativecode) AS nativecode, " +
            " max(v.size) AS size " +
            "FROM AppVersion AS v " +
            "WHERE ((:sdkversion = 0) OR (v.minSdkVersion <= :sdkversion AND " +
            " ((v.maxSdkVersion IS NULL) OR (v.maxSdkVersion = 0) OR (v.maxSdkVersion >= :sdkversion)))) AND " +
            " (v.nativecode IS NULL OR :nativeCode IS NULL OR v.nativecode like :nativeCode) " +
            "GROUP BY v.appId, v.repoId " +
            "ORDER BY added DESC, id desc ")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    List<Version> findBestBySdkAndNative(int sdkversion, String nativeCode);
}
