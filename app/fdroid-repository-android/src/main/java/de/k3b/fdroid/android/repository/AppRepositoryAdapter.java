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

import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import java.util.ArrayList;
import java.util.List;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.interfaces.AppRepository;

public class AppRepositoryAdapter implements AppRepository {
    private final AppDao appDao;

    public AppRepositoryAdapter(AppDao appDao) {
        this.appDao = appDao;
    }

    @Override
    public void insert(App apps) {
        appDao.insert(apps);
    }

    @Override
    public void update(App roomApp) {
        appDao.update(roomApp);
    }

    @Override
    public void delete(App app) {
        appDao.delete(app);
    }

    @Override
    public App findByRepoIdAndPackageName(int repoId, String packageName) {
        return appDao.findByRepoIdAndPackageName(repoId, packageName);
    }

    @Override
    public int findIdByRepoIdAndPackageName(int repoId, String packageName) {
        return appDao.findIdByRepoIdAndPackageName(repoId, packageName);
    }

    @Override
    public List<App> findAll() {
        return appDao.findAll();
    }

    @Override
    public List<App> findByIds(List<Integer> ids) {
        return appDao.findByIds(ids);
    }

    @Override
    public List<Integer> findIdsByExpressionSortByScore(String searchText) {
        // https://microeducate.tech/how-to-dynamically-query-the-room-database-at-runtime/
        String queryString = "...";
        List<Object> args = new ArrayList<>();
        SupportSQLiteQuery query = new SimpleSQLiteQuery(queryString, args.toArray());
        return appDao.findIdsByExpressionSortByScore(query);
    }
}
