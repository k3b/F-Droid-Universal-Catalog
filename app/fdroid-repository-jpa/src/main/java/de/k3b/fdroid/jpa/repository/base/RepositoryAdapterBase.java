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
package de.k3b.fdroid.jpa.repository.base;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

import de.k3b.fdroid.domain.Repo;

/**
 * Android-Room/JPA compatibility layer:
 * implement room aliase.
 */
public class RepositoryAdapterBase<T, R extends CrudRepository<T, Integer>> {
    protected final R jpa;

    public RepositoryAdapterBase(R jpa) {
        this.jpa = jpa;
    }

    public void insert(T roomApp) {
        jpa.save(roomApp);
    }

    public void update(T roomApp) {
        jpa.save(roomApp);
    }

    public void delete(T roomApp) {
        jpa.delete(roomApp);
    }

    public Repo findById(Integer repoId) {
        Optional<T> result = jpa.findById(repoId);
        if (result.isPresent()) result.get();
        return null;
    }


    public List<T> findAll() {
        return (List<T>) jpa.findAll();
    }
}
