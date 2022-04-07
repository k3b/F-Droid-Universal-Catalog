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
package org.fdroid.jpa.db.base;

import org.springframework.data.repository.NoRepositoryBean;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Android-Room/JPA Compatibility layer:
 * add Android-Room compatible method alias's to JPA Repository
 */
@NoRepositoryBean
public interface CustomRoomMethods<T> {
    /**
     * Android Room alias for insert(T) and update(T) is based on JPA save(T)
     */
    default void insert(T item) {
        try {
            getMethod("save", Object.class).invoke(this, item);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Android Room alias for insert(T) and update(T) is based on JPA save(T)
     */
    default void update(T item) {
        insert(item);
    }

    /**
     * Android Room implements T[] findAll(); JPA implements Iterable<T> findAll().
     * Repository uses findAll2() as alias for JPA findAll()
     */
    default List<T> findAll2() {
        try {
            return (List<T>) getMethod("findAll").invoke(this);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    default Method getMethod(String name, Class<?>... parameterTypes) {
        try {
            return this.getClass().getMethod(name, parameterTypes);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Cannot call ").append(name);
            for (Method m : this.getClass().getMethods()) {
                sb.append(m.getName()).append("(");
                for (Class<?> c : m.getParameterTypes()) {
                    sb.append(c.getSimpleName()).append(",");
                }
                sb.append(")\n");
            }
            throw new IllegalArgumentException(sb.toString(), e);
        }

    }
}
