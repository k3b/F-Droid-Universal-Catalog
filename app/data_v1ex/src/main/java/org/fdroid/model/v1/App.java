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

package org.fdroid.model.v1;

import org.fdroid.model.AppCommon;
import org.fdroid.util.Formatter;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * Data for an android app (read from FDroid-Catalog-v1-Json format).
 * <p>
 * Generated with https://www.jsonschema2pojo.org/ from JSON example Data in Format Gson.
 */
@Generated("jsonschema2pojo")
public class App extends AppCommon {
    private List<String> categories = null;

    // preserve insertion order
    private Map<String, Localized> localized = new TreeMap<>();

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Map<String, Localized> getLocalized() {
        return localized;
    }

    public void setLocalized(Map<String, Localized> localized) {
        this.localized = localized;
    }

    @Override
    protected void toString(StringBuilder sb) {
        super.toString(sb);
        Formatter.add(sb, "categories", this.categories);
        if (localized != null) {
            sb.append("localized");
            sb.append("={");
            for (Map.Entry<String, Localized> l : localized.entrySet()) {
                sb.append(l.getKey()).append(":").append(l.getValue()).append(",");
            }
            if (sb.charAt((sb.length() - 1)) == ',') {
                sb.setCharAt((sb.length() - 1), '}');
            } else {
                sb.append('}');
            }
            sb.append(",");
        }
    }
}
