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

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.RepoRepository;

@Dao
public interface RepoDao extends RepoRepository {
    default void insert(Repo repo) {
        int result = (int) insertEx(repo);
        repo.setId(result);
    }

    @Insert
    long insertEx(Repo category);

    @Update
    void update(Repo repo);

    @Delete
    void delete(Repo repo);

    @Query("SELECT * FROM Repo WHERE Repo.id = :repoId")
    Repo findById(Integer repoId);

    @Query("SELECT * FROM Repo WHERE Repo.name = :name")
    Repo findByName(String name);

    @Query("SELECT * FROM Repo WHERE Repo.address = :address")
    Repo findByAddress(String address);

    @Query("SELECT * FROM Repo WHERE Repo.downloadTaskId is not null")
    List<Repo> findByBusy();

    /**
     * where the app can be downloaded from
     */
    @Query("SELECT r.* FROM Repo r " +
            " WHERE EXISTS(SELECT av.repoId FROM APPVERSION av" +
            " WHERE av.repoId = r.id and av.appId = :appId) " +
            "ORDER BY r.lastUsedDownloadDateTimeUtc desc")
    List<Repo> findListByAppId(int appId);

    @Query("SELECT * FROM Repo")
    List<Repo> findAllEx();

    default List<Repo> findAll() {
        List<Repo> result = findAllEx();
        if (result.size() == 0) {
            // should be done with Room-Migratoins but the migratons-sql was never called
            // https://developer.android.com/training/data-storage/room/migrating-db-versions
            Repo fdroid = new Repo("f-droid.org", "https://f-droid.org/repo", "s");
            fdroid.setAutoDownloadEnabled(true);
            add(result,
                    fdroid,
                    new Repo("apt.izzysoft.de", "https://apt.izzysoft.de/fdroid/repo", "n"),
                    new Repo("f-droid.org/archive", "https://f-droid.org/archive", "a"),
                    new Repo("guardianproject.info", "https://guardianproject.info/fdroid/repo", "s")
            );
        }
        return result;
    }

    default void add(List<Repo> result, Repo... repos) {
        for(Repo repo : repos) {
            result.add(repo);
            insert(repo);
        }
    }

}
