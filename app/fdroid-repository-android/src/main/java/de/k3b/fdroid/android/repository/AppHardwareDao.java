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
import androidx.room.Update;

import java.util.List;

import de.k3b.fdroid.domain.entity.AppHardware;
import de.k3b.fdroid.domain.repository.AppHardwareRepository;

@Dao
public interface AppHardwareDao extends AppHardwareRepository {
    default void insert(AppHardware appHardware) {
        int result = (int) insertEx(appHardware);
        appHardware.setId(result);
    }

    @Insert
    long insertEx(AppHardware appHardware);

    @Update
    void update(AppHardware appHardware);

    @Delete
    void delete(AppHardware appHardware);

    @Query("SELECT * FROM AppHardware WHERE AppHardware.appId = :appId")
    List<AppHardware> findByAppId(int appId);
}
