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
package de.k3b.fdroid.domain.entity;

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.entity.common.ExtDoc;
import de.k3b.fdroid.domain.interfaces.AppDetail;
import io.swagger.v3.oas.annotations.ExternalDocumentation;

/**
 * {@link AppCategory}: An {@link App} can belong to zero or more {@link Category}s.
 * <p>
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 */
@androidx.room.Entity(foreignKeys = {@androidx.room.ForeignKey(entity = App.class,
        parentColumns = "id", childColumns = "appId", onDelete = androidx.room.ForeignKey.CASCADE),
        @androidx.room.ForeignKey(entity = Category.class, parentColumns = "id",
                childColumns = "categoryId", onDelete = androidx.room.ForeignKey.CASCADE)},
        indices = {@androidx.room.Index({"appId", "categoryId"})})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@SuppressWarnings("unused")
@ExternalDocumentation(description = "Category of an App", url = ExtDoc.GLOSSAR_URL + "Category")
public class AppCategory extends EntityCommon implements AppDetail {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    @androidx.room.ColumnInfo(index = true)
    private int appId;
    @androidx.room.ColumnInfo(index = true)
    private int categoryId;

    // needed by android-room and jpa
    public AppCategory() {
    }

    @androidx.room.Ignore
    public AppCategory(int appId, int categoryId) {
        this.appId = appId;
        this.categoryId = categoryId;
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "appId", this.appId);
        toStringBuilder(sb, "categoryId", this.categoryId);
        super.toStringBuilder(sb);
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
