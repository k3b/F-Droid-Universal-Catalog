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
package de.k3b.fdroid.domain.entity;

import de.k3b.fdroid.domain.entity.common.VersionCommon;
import de.k3b.fdroid.domain.interfaces.AppDetail;
import de.k3b.fdroid.domain.util.AndroidVersionName;
import de.k3b.fdroid.domain.util.StringUtil;

/**
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(foreignKeys = {@androidx.room.ForeignKey(entity = App.class,
        parentColumns = "id", childColumns = "appId",onDelete = androidx.room.ForeignKey.CASCADE),
        @androidx.room.ForeignKey(entity = Repo.class,
                parentColumns = "id", childColumns = "repoId",onDelete = androidx.room.ForeignKey.CASCADE)},
        tableName = "AppVersion", indices = {@androidx.room.Index("id"),
        @androidx.room.Index({"appId", "repoId"})})
@javax.persistence.Entity(name = "AppVersion")
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class Version extends VersionCommon implements AppDetail {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    @androidx.room.ColumnInfo(index = true)
    private int appId;

    @androidx.room.ColumnInfo(index = true)
    private int repoId;

    private String nativecode;

    /**
     * calculated and cached from {@link #nativecode}. Not persisted in Database
     */
    @androidx.room.Ignore
    @javax.persistence.Transient
    private String[] nativecodeArray;

    // needed by android-room and jpa
    public Version() {
    }

    @androidx.room.Ignore
    public Version(int appId, int repoId) {
        setAppId(appId);
        setRepoId(repoId);
    }

    @androidx.room.Ignore
    public Version(int minSdkVersion, int targetSdkVersion, int maxSdkVersion, String nativecode) {
        setSdk(minSdkVersion, targetSdkVersion, maxSdkVersion);
        setNativecode(nativecode);
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "appId", this.appId);
        toStringBuilder(sb, "repoId", this.repoId);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "nativecode", this.nativecode);
    }

    public int getId() {
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

    public int getRepoId() {
        return repoId;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }

    public String getNativecode() {
        return nativecode;
    }

    public void setNativecode(String nativecode) {
        this.nativecode = nativecode;
        nativecodeArray = null;
    }

    public String[] getNativecodeArray() {
        if (nativecodeArray == null) {
            nativecodeArray = StringUtil.toStringArray(nativecode);
        }
        return nativecodeArray;
    }

    public String getSdkInfo() {
        return AndroidVersionName.getName(getMinSdkVersion(), getNativecode());
    }

}
