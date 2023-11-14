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

package de.k3b.fdroid.v1domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Generated;

import de.k3b.fdroid.domain.entity.common.AppCommon;

/**
 * Data for an android app (read from FDroid-Catalog-v1-Json format).
 * <p>
 * The {@link V1JsonEntity} {@link V1App} correspond to the
 * {@link de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId} {@link de.k3b.fdroid.domain.App}.
 */
@Generated("jsonschema2pojo")
public class V1App extends AppCommon implements V1JsonEntity {

    // redundant fallback if there is no localized (converted to locale)
    private String summary;
    private String description;

    private List<String> categories = new ArrayList<>();
    // preserve insertion order
    private Map<String, Localized> localized = new TreeMap<>();

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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
    protected void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "summary", this.summary);
        toStringBuilder(sb, "description", this.description);

        if (this.categories != null && !this.categories.isEmpty())
            toStringBuilder(sb, "categories", this.categories);
        if (localized != null && !localized.isEmpty()) {
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
