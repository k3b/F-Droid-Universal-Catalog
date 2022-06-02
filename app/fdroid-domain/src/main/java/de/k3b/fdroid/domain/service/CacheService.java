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
package de.k3b.fdroid.domain.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId;

public class CacheService<T extends DatabaseEntityWithId> {
    private Map<Integer, T> id2Item = null;

    public CacheService() {
    }

    public CacheService(List<T> itemList) {
        init(itemList);
    }

    protected void init(List<T> itemList) {
        id2Item = new HashMap<>();
        for (T item : itemList) {
            init(item);
        }
    }

    protected void init(T item) {
        id2Item.put(item.getId(), item);
    }

    public T getItemById(int itemId) {
        T item = (itemId == 0) ? null : id2Item.get(itemId);
        return item;
    }
}
