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
package de.k3b.fdroid.android.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import de.k3b.fdroid.room.db.RepoRepository;
import de.k3b.fdroid.room.model.Repo;

@Dao
public interface RepoDao extends RepoRepository {
    @Insert
    void insertAll(Repo... repos);

    @Update
    void updateAll(Repo... repos);

    @Delete
    void delete(Repo repo);

    @Query("SELECT * FROM Repo WHERE Repo.id = :repoId")
    Repo findById(Integer repoId);

    @Query("SELECT * FROM Repo WHERE Repo.address = :address")
    Repo findByAddress(String address);

    @Query("SELECT * FROM Repo")
    Repo[] findAll();
}
