/*
 * Copyright (c) 2023 by k3b.
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

import de.k3b.fdroid.domain.entity.AntiFeature;
import de.k3b.fdroid.domain.repository.AntiFeatureRepository;

@Dao
public interface AntiFeatureDao extends AntiFeatureRepository {
    default void insert(AntiFeature antiFeature) {
        int result = (int) insertEx(antiFeature);
        antiFeature.setId(result);
    }

    @Insert
    long insertEx(AntiFeature antiFeature);

    @Update
    void update(AntiFeature antiFeature);

    @Delete
    void delete(AntiFeature antiFeature);

    @Query("DELETE FROM AntiFeature where AntiFeature.id = :id")
    void deleteById(Integer id);

    @Query("SELECT * FROM AntiFeature")
    List<AntiFeature> findAll();

    @Query("SELECT * FROM AntiFeature where AntiFeature.name = :name")
    AntiFeature findByName(String name);
}
