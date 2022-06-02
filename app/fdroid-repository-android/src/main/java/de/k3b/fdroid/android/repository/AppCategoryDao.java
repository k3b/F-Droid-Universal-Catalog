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
import androidx.room.Update;

import java.util.List;

import de.k3b.fdroid.domain.entity.AppCategory;
import de.k3b.fdroid.domain.repository.AppCategoryRepository;

@Dao
public interface AppCategoryDao extends AppCategoryRepository {
    default void insert(AppCategory appCategory) {
        int result = (int) insertEx(appCategory);
        appCategory.setId(result);
    }

    @Insert
    long insertEx(AppCategory appCategory);

    @Update
    void update(AppCategory appCategory);

    @Delete
    void delete(AppCategory appCategory);

    @Query("SELECT * FROM AppCategory WHERE AppCategory.appId = :appId")
    List<AppCategory> findByAppId(int appId);
}
