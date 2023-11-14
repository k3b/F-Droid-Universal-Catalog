/*
 * Copyright (c) 2023 by k3b.
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

package de.k3b.fdroid.domain.entity;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class LocalizedLocalesSorter<T extends Localized> implements Comparator<T> {
    private final Map<String, Integer> locale2Order = new HashMap<>();

    public LocalizedLocalesSorter(String[] locales) {
        if (locales != null) {
            int order = 200;
            for (String l : locales) {
                locale2Order.put(l, order--);
            }
        }
    }

    public boolean isEmpty() {
        return locale2Order.size() < 2;
    }

    private int getOrder(T item) {
        if (item == null) return 0;
        Integer order = locale2Order.get(item.getLocaleId());
        if (order == null) return 0;
        return order.intValue();
    }

    @Override
    public int compare(T lhs, T rhs) {
        return getOrder(rhs) - getOrder(lhs);
    }
}
