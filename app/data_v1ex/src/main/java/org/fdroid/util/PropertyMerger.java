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

package org.fdroid.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PropertyMerger {
    private Gson gson = new Gson();

    /**
     * @return items.get(0) with all properties of all items
     */
    public <T extends Object> T merge(List<T> items) {
        int size = (items == null) ? 0 : items.size();
        if (size == 1) return items.get(0);

        if (size > 1) {
            List<JsonObject> result = new ArrayList<>();
            Class<?> clasz = null;
            for (T item : items) {
                clasz = item.getClass();
                result.add(gson.fromJson(gson.toJson(item), JsonObject.class));
            }
            JsonObject resultObject = mergeJsonObject(result);
            return gson.<T>fromJson(gson.toJson(resultObject), clasz);
        }
        return null;
    }

    private static JsonObject mergeJsonObject(List<JsonObject> items) {
        int size = items.size();
        JsonObject result = items.get(0);
        for (int i = 1; i < size; i++) {
            mergeJsonObject(result, items.get(i));
        }
        return result;
    }

    private static void mergeJsonObject(JsonObject result, JsonObject src) {
        mergeJsonObject(result, src, result.keySet());
        mergeJsonObject(result, src, src.keySet());
    }

    private static void mergeJsonObject(JsonObject result, JsonObject src, Set<String> keys) {
        for (String key : keys) {
            JsonElement newValue = src.get(key);

            if (mustMerge(result.get(key), newValue)) {
                result.add(key, newValue);
            }
        }
    }

    private static boolean mustMerge(JsonElement currentValue, JsonElement newValue) {
        // do not delete existing value
        if (newValue == null || newValue.isJsonNull()) return false;

        // replace if current value is null or empty
        if (currentValue == null || currentValue.isJsonNull()) return true;

        if (currentValue.isJsonPrimitive()
                && (currentValue.getAsString().length() < newValue.getAsString().length())) {
            // return the longer String value
            return true;
        }

        if (currentValue.isJsonArray()
            && (currentValue.getAsJsonArray().size() < newValue.getAsJsonArray().size())) {
            // return the array that has more array-elements
            return true;
        }

        // non null Object: the fist value wins
        return false;
    }
}
