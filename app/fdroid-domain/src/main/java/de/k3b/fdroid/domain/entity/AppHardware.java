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

import org.jetbrains.annotations.NotNull;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.entity.common.ProfileCommon;
import de.k3b.fdroid.domain.interfaces.AppDetail;

/**
 * One Database Entity per {@link App} that is compatible with a {@link HardwareProfile}.
 * <p>
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, embedded entities, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */

@androidx.room.Entity(foreignKeys = {@androidx.room.ForeignKey(entity = App.class,
        parentColumns = "id", childColumns = "appId", onDelete = androidx.room.ForeignKey.CASCADE),
        @androidx.room.ForeignKey(entity = HardwareProfile.class,
                parentColumns = "id", childColumns = "hardwareProfileId", onDelete = androidx.room.ForeignKey.CASCADE)},
        indices = {@androidx.room.Index({"appId", "hardwareProfileId"})})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class AppHardware extends EntityCommon implements AppDetail {
    @androidx.room.Embedded
    @javax.persistence.Embedded
    ProfileCommon max = new ProfileCommon();
    @androidx.room.Embedded(prefix = "min_")
    @javax.persistence.Embedded
    @javax.persistence.AttributeOverrides({
            // Verbose: every persisted property in ProfileCommon needs an entry
            // see https://stackoverflow.com/questions/12912063/jpa-multiple-embedded-fields-with-prefix
            @AttributeOverride(name = "added", column = @Column(name = "min_added")),
            @AttributeOverride(name = "apkName", column = @Column(name = "min_apkName")),
            @AttributeOverride(name = "versionCode", column = @Column(name = "min_versionCode")),
            @AttributeOverride(name = "versionName", column = @Column(name = "min_versionName")),
            @AttributeOverride(name = "size", column = @Column(name = "min_size"))
    })
    ProfileCommon min = new ProfileCommon();
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;
    @androidx.room.ColumnInfo(index = true)
    private int appId;
    @androidx.room.ColumnInfo(index = true)
    private int hardwareProfileId;

    // needed by android-room and jpa
    public AppHardware() {
    }

    @androidx.room.Ignore
    public AppHardware(int appId, int hardwareProfileId) {
        this.appId = appId;
        this.hardwareProfileId = hardwareProfileId;
    }

    @NotNull
    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public ProfileCommon getMin() {
        return min;
    }

    public void setMin(ProfileCommon min) {
        this.min = min;
    }

    public ProfileCommon getMax() {
        return max;
    }

    public void setMax(ProfileCommon max) {
        this.max = max;
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "appId", this.appId);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "hardwareProfileId", this.hardwareProfileId);
        toStringBuilder(sb, "max", this.max);
        toStringBuilder(sb, "min", this.min);
    }

    public int getHardwareProfileId() {
        return hardwareProfileId;
    }

    public void setHardwareProfileId(int hardwareProfileId) {
        this.hardwareProfileId = hardwareProfileId;
    }
}
