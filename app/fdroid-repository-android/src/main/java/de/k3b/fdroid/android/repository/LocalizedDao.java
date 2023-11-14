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
package de.k3b.fdroid.android.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.repository.LocalizedRepository;

@Dao
public interface LocalizedDao extends LocalizedRepository {
    default void insert(Localized localized) {
        int result = (int) insertEx(localized);
        localized.setId(result);
    }

    @Insert
    long insertEx(Localized localized);

    @Update
    void update(Localized localized);

    @Delete
    void delete(Localized localized);

    @Query("SELECT * FROM Localized WHERE Localized.appId = :appId ")
    List<Localized> findByAppId(int appId);

    @Query("SELECT * FROM Localized WHERE Localized.appId = :appId and Localized.localeId in (:localeIds) ")
    List<Localized> findByAppIdAndLocaleIds(int appId, List<String> localeIds);

    @Query(value = "select al.* from Localized al " +
            "inner join Locale l on al.localeId = l.id " +
            "where al.appId in (:appIds) and l.languagePriority <> -1 " +
            "order by al.appId, l.languagePriority desc")
    List<Localized> findNonHiddenByAppIds(List<Integer> appIds);

    @Query(value = "select al.* from Localized al " +
            "where al.appId in (:appIds) and al.localeId in (:localeIds) " +
            "order by al.appId")
    List<Localized> findByAppIdsAndLocaleIds(List<Integer> appIds, String[] localeIds);
}
