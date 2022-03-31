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

import de.k3b.fdroid.android.model.App;
import de.k3b.fdroid.android.model.Repo;

@Dao
public abstract class RepoDao {
    @Insert
    public abstract void insertAll(Repo... repos);

    @Update
    public abstract void updateAll(Repo... repos);

    @Delete
    public abstract void delete(Repo repo);

    @Query("SELECT * FROM Repo WHERE Repo.id = :repoId")
    public abstract Repo findById(Integer repoId);

    @Query("SELECT * FROM Repo WHERE Repo.address = :address")
    public abstract Repo findByAddress(String address);

    public Repo findByApp(App app) {
        return findById(app.repoId);
    }

    @Query("SELECT * FROM Repo")
    public abstract Repo[] findAll();
}
