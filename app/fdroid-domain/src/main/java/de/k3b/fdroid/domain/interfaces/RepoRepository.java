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
package de.k3b.fdroid.domain.interfaces;

import java.util.List;

import de.k3b.fdroid.domain.Repo;

/**
 * Android independant interfaces to use the Database
 */
public interface RepoRepository {
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

    List<Repo> findAll();

    List<Repo> findByBusy();

    default Repo findCorrespondigRepo(Repo repoFromImport) {
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
}
