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
package de.k3b.fdroid.domain.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CacheService<KEY, T> {
    protected Map<KEY, T> id2Item = null;

    public CacheService() {
    }

    public CacheService(@Nullable List<T> itemList) {
        init(itemList);
    }

    protected void init(@Nullable List<T> itemList) {
        id2Item = new HashMap<>();
        if (itemList != null) {
            for (T item : itemList) {
                init(item);
            }
        }
    }

    protected void init(T item) {
        id2Item.put(getId(item), item);
    }

    @NotNull
    abstract protected KEY getId(T item);

    abstract public T getItemById(KEY itemId);
}
