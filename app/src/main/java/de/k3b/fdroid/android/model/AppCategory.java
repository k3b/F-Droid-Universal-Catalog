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
package de.k3b.fdroid.android.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(primaryKeys = {"appId", "categoryId"}, foreignKeys = {
        @ForeignKey(entity = App.class,parentColumns = "id",childColumns = "appId"),
        @ForeignKey(entity = Category.class,parentColumns = "id",childColumns = "categoryId")},
        indices = {@Index("appId"), @Index("categoryId")})
public class AppCategory {
    @NonNull public final Integer appId;
    @NonNull public final Integer categoryId;

    public AppCategory(@NonNull Integer appId, @NonNull Integer categoryId) {
        this.appId = appId;
        this.categoryId = categoryId;
    }
}
