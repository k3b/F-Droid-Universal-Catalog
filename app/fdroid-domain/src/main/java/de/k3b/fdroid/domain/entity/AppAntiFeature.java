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

import org.jetbrains.annotations.NotNull;

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.entity.common.ExtDoc;
import de.k3b.fdroid.domain.interfaces.AppDetail;
import io.swagger.v3.oas.annotations.ExternalDocumentation;

/**
 * {@link AppAntiFeature}: An {@link App} can belong to zero or more {@link AntiFeature}s.
 * <p>
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 */
@androidx.room.Entity(foreignKeys = {@androidx.room.ForeignKey(entity = App.class,
        parentColumns = "id", childColumns = "appId", onDelete = androidx.room.ForeignKey.CASCADE),
        @androidx.room.ForeignKey(entity = AntiFeature.class, parentColumns = "id",
                childColumns = "antiFeatureId", onDelete = androidx.room.ForeignKey.CASCADE)},
        indices = {@androidx.room.Index({"appId", "antiFeatureId"})})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@SuppressWarnings("unused")
@ExternalDocumentation(description = "AntiFeature of an App", url = ExtDoc.GLOSSAR_URL + "AntiFeature")
public class AppAntiFeature extends EntityCommon implements AppDetail {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    @androidx.room.ColumnInfo(index = true)
    private int appId;
    @androidx.room.ColumnInfo(index = true)
    private int antiFeatureId;

    // needed by android-room and jpa
    public AppAntiFeature() {
    }

    @androidx.room.Ignore
    public AppAntiFeature(int appId, int antiFeatureId) {
        this.appId = appId;
        this.antiFeatureId = antiFeatureId;
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "appId", this.appId);
        toStringBuilder(sb, "antiFeatureId", this.antiFeatureId);
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

    public int getAntiFeatureId() {
        return antiFeatureId;
    }

    public void setAntiFeatureId(int antiFeatureId) {
        this.antiFeatureId = antiFeatureId;
    }
}
