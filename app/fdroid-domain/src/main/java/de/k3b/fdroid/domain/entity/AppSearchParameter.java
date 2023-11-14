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
package de.k3b.fdroid.domain.entity;

import java.io.Serializable;

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.repository.AppRepository;

/**
 * Pseudo entity used by {@link AppRepository} as Filter/Sorter
 * to find matching apps
 */
public class AppSearchParameter extends EntityCommon implements Serializable {
    // text contained in any of the text fields (summaryXXX )
    public String searchText = null;

    // only if there is at least on version that matches this value
    public int versionSdk = 0;

    /**
     * For {@link #searchText} search: minimal required search-score required (or null)
     * null mean search everywhere
     * 10 means exclude Describtion.
     * for details see sql-sourcecode of "CREATE VIEW AppSearch AS ..."
     */
    public Integer minimumScore = null;
    public int categoryId = 0;
    public String orderBy = null;

    // supported by sqLite and hsqldb: SELECT ... FROM ... LIMIT 150 https://www.sqlitetutorial.net/sqlite-limit/
    public int maxRowCount = 150;

    /**
     * array of normalized locales or languages for the searchresult:
     * order by highest priority first.
     * <p>
     * Example [de,es,en]
     */
    public String[] locales = null;
    public String[] localePrefixes = null;

    public AppSearchParameter() {
    }

    public AppSearchParameter searchText(String searchText) {
        this.searchText = searchText;
        return this;
    }

    public AppSearchParameter versionSdk(int versionSdk) {
        this.versionSdk = versionSdk;
        return this;
    }

    public AppSearchParameter categoryId(int categoryId) {
        this.categoryId = categoryId;
        return this;
    }

    public AppSearchParameter orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public AppSearchParameter locales(String[] locales) {
        this.locales = locales;
        localePrefixes = null;
        return this;
    }

    @Override
    protected void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "searchText", this.searchText);
        toStringBuilder(sb, "versionSdk", this.versionSdk);
        toStringBuilder(sb, "categoryId", this.categoryId);
        toStringBuilder(sb, "orderBy", this.orderBy);
        toStringBuilder(sb, "locales", this.locales);
        // toStringBuilder(sb, "maxRowCount", this.maxRowCount);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb);
        int last = sb.length() - 1;
        if (last >= 0 && sb.charAt(last) == ',') {
            sb.deleteCharAt(last);
        }
        return sb.toString();
    }
}
