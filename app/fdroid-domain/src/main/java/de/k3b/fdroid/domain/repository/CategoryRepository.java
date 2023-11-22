/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid project.
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

import de.k3b.fdroid.domain.entity.Category;
import de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId;

/**
 * Android independent interfaces to use the Database.
 * <p>
 * Persists {@link Category} (that implements {@link DatabaseEntityWithId}) in the Database.
 */
public interface CategoryRepository extends Repository {
    void insert(Category category);

    void update(Category category);

    void delete(Category category);

    void deleteById(Integer id);

    List<Category> findAll();

    Category findByName(String name);

    default void save(Category o) {
        if (mustInsert(o)) {
            insert(o);
        } else {
            update(o);
        }
    }

    default boolean mustInsert(Category o) {
        return o.getId() == 0;
    }

}
