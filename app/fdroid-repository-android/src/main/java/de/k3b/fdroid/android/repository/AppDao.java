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
import androidx.room.RawQuery;
import androidx.room.Update;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.List;

import de.k3b.fdroid.domain.App;

// NOTE: AppDao cannot be Generic, AppDao<App> is not possible :-(
@Dao
public interface AppDao { // extends AppRepository {
    default void insert(App app) {
        int result = (int) insertEx(app);
        app.setId(result);
    }

    @Insert
    long insertEx(App app);

    @Update
    void update(App roomApp);

    @Delete
    void delete(App app);

    @Query("SELECT * FROM App WHERE App.packageName = :packageName")
    App findByPackageName(String packageName);

    @Query("SELECT * FROM App")
    List<App> findAll();

    @Query("SELECT * FROM App WHERE App.id in (:ids)")
    List<App> findByIds(List<Integer> ids);

    //!!! TODO
    // @Query("SELECT id FROM App WHERE App.id in (:searchText)")
    // see https://microeducate.tech/how-to-dynamically-query-the-room-database-at-runtime/
    @RawQuery
    List<Integer> findIdsByExpressionSortByScore(SupportSQLiteQuery query);
}
