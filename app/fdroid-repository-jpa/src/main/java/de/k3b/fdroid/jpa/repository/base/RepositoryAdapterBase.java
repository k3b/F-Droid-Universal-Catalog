/*
 * Copyright (c) 2022-2023 by k3b.
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

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Android-Room/JPA compatibility layer:
 * implement room aliase.
 */
public class RepositoryAdapterBase<KEY, T, R extends JpaRepository<T, KEY>> {
    protected final R jpa;

    public RepositoryAdapterBase(R jpa) {
        this.jpa = jpa;
    }

    public void insert(T roomApp) {
        jpa.saveAndFlush(roomApp);
    }

    public void update(T roomApp) {
        jpa.saveAndFlush(roomApp);
    }

    public void delete(T roomApp) {
        jpa.delete(roomApp);
    }

    public T findById(KEY repoId) {
        Optional<T> result = jpa.findById(repoId);
        if (result.isPresent()) return result.get();
        return null;
    }


    public List<T> findAll() {
        return (List<T>) jpa.findAll();
    }

    public List<T> findByIds(List<KEY> ids) {
        return (List<T>) jpa.findAllById(ids);
    }
}
