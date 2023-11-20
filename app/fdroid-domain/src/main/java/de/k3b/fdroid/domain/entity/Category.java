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


import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.entity.common.ExtDoc;
import de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * {@link AppCategory}: An {@link App} can belong to zero or more {@link Category}s.
 * <p>
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(indices = {@androidx.room.Index("id"), @androidx.room.Index(value = "name", unique = true)})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@SuppressWarnings("unused")
@ExternalDocumentation(description = "Category of an [App]", url = ExtDoc.GLOSSAR_URL + "Category")
public class Category extends EntityCommon implements DatabaseEntityWithId {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    @Schema(description = "Category name in English.",
            example = "Graphics")
    private String name;

    // needed by android-room and jpa
    public Category() {
    }

    @androidx.room.Ignore
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "name", this.name);
        super.toStringBuilder(sb);
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = maxlen(name);
    }

    public static final Comparator<Category> COMPARE_BY_NAME =  (o1, o2) -> o1.getName().compareTo(o2.getName());
}
