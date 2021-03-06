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
package de.k3b.fdroid.domain.repository;

import java.util.List;

import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId;

/**
 * Android independent interfaces to use the Database.
 * <p>
 * Persists {@link Repo} (that implements {@link DatabaseEntityWithId}) in the Database.
 */
public interface RepoRepository extends Repository {
    void insert(Repo repo);

    void update(Repo repo);

    default void save(Repo repo) {
        if (repo.getId() == 0) {
            insert(repo);
        } else {
            update(repo);
        }
    }

    void delete(Repo repo);

    Repo findById(Integer repoId);

    Repo findByName(String name);

    Repo findByAddress(String address);

    List<Repo> findListByAppId(int appId);

    List<Repo> findAll();

    List<Repo> findByBusy();

    default Repo findCorrespondigRepo(RepoCommon repoFromImport) {
        Repo repoFromDatabase = null;
        if (repoFromDatabase == null && repoFromImport.getName() != null) {
            repoFromDatabase = findByName(repoFromImport.getName());
        }
        if (repoFromDatabase == null && repoFromImport.getAddress() != null) {
            repoFromDatabase = findByAddress(repoFromImport.getAddress());
        }
        return repoFromDatabase;
    }

    default Repo getBusy(List<Repo> repos) {
        if (repos != null) {
            for (Repo repo : repos) {
                if (repo.isBusy()) return repo;
            }
        }
        return null;
    }

    default Repo findFirstByAppId(int appId) {
        List<Repo> repoList = findListByAppId(appId);
        if (repoList == null || repoList.size() == 0) return null;
        return repoList.get(0);
    }

}
