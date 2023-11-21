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

package de.k3b.fdroid.domain.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Helper to compensate missing java8 stream api
 */
public class Java8Util {

    public static <T, R> Map<String, R> getKeyValueMap(Map<String, T> map, Getter<T, R> getter) {
        if (map == null) return null;
        Map<String, R> result = new TreeMap<>();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            result.put(entry.getKey(), getter.get(entry.getValue()));
        }
        return result;
    }

    public static <T, R> List<R> reduce(List<T> list, Getter<T, R> getter) {
        if (list == null) return null;
        List<R> result = new ArrayList<>();
        for (T entry : list) {
            result.add(getter.get(entry));
        }
        return result;
    }

    public static <K, T, R> Map<K, R> reduce(Map<K, T> list, Getter<T, R> getter) {
        if (list == null) return null;
        TreeMap<K, R> result = new TreeMap<>();
        for (Map.Entry<K, T> entry : list.entrySet()) {
            result.put(entry.getKey(), getter.get(entry.getValue()));
        }
        return result;
    }

    // java 8 supports lamda expressions but not yet generic parameters java.util.functions//
    public interface Getter<T, R> {
        R get(T l);
    }

    public interface Setter<T, R> {
        void set(T l, R value);
    }

    public static class OutParam<T> {
        private T value;

        public OutParam(T value) {
            setValue(value);
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }
}
