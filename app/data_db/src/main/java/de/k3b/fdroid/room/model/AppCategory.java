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
package de.k3b.fdroid.room.model;

import org.fdroid.model.common.PojoCommon;

/**
 * Android independant: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 */
@androidx.room.Entity(foreignKeys = {
        @androidx.room.ForeignKey(entity = App.class, parentColumns = "id", childColumns = "appId"),
        @androidx.room.ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "categoryId")},
        indices = {@androidx.room.Index("appId"), @androidx.room.Index("categoryId")})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class AppCategory extends PojoCommon {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @androidx.room.PrimaryKey(autoGenerate = true)
    public int id;

    public final int appId;
    public final int categoryId;

    public AppCategory(int appId, int categoryId) {
        this.appId = appId;
        this.categoryId = categoryId;
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "appId", this.appId);
        toStringBuilder(sb, "categoryId", this.categoryId);
    }

}
