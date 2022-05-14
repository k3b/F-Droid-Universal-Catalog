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
package de.k3b.fdroid.android.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.RoomWarnings;
import androidx.room.Update;

import java.util.List;

import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.interfaces.VersionRepository;

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

    @Query(value = "select al.* from AppVersion al " +
            "where al.appId in (:appIds) " +
            "order by al.versionCode desc")
    List<Version> findByAppIds(List<Integer> appIds);

    @Query("SELECT MAX(av.id) AS id, " +
            " av.appId, av.repoId, " +
            " min(av.minSdkVersion) AS minSdkVersion, " +
            " max(av.maxSdkVersion) AS maxSdkVersion, " +
            " max(av.targetSdkVersion) AS targetSdkVersion, " +
            " max(av.versionCode) AS versionCode, " +
            " max(av.added) AS added, " +
            " max(av.nativecode) AS nativecode, " +
            " max(av.size) AS size " +
            "FROM AppVersion AS av " +
            "WHERE av.minSdkVersion <= :sdkversion AND " +
            "((av.maxSdkVersion IS NULL) OR (av.maxSdkVersion = 0) OR (av.maxSdkVersion >= :sdkversion)) AND " +
            "(av.nativecode IS NULL OR :nativeCode IS NULL OR av.nativecode like :nativeCode) " +
            "GROUP BY av.appId, av.repoId ")
    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    List<Version> findBestBySdkAndNative(int sdkversion, String nativeCode);
}
