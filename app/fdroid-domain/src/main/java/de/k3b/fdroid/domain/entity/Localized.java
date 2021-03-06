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

import javax.persistence.Column;

import de.k3b.fdroid.domain.entity.common.LocalizedCommon;
import de.k3b.fdroid.domain.interfaces.AppDetail;
import de.k3b.fdroid.domain.util.StringUtil;

/**
 * Translated infos about an {@link App}.
 * <p>
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(foreignKeys = {@androidx.room.ForeignKey(entity = App.class,
        parentColumns = "id", childColumns = "appId",onDelete = androidx.room.ForeignKey.CASCADE),
        @androidx.room.ForeignKey(entity = Locale.class,
                parentColumns = "id", childColumns = "localeId",onDelete = androidx.room.ForeignKey.CASCADE)},
        indices = {@androidx.room.Index("id"),
        @androidx.room.Index({"appId", "localeId"})})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class Localized extends LocalizedCommon implements AppDetail {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    @androidx.room.ColumnInfo(index = true)
    private int appId;

    @androidx.room.ColumnInfo(index = true)
    private int localeId;

    @Column(length = MAX_LEN_AGGREGATED)
    private String phoneScreenshots;
    /**
     * calculated and cached from {@link #phoneScreenshots}. Not persisted in Database
     */
    @androidx.room.Ignore
    @javax.persistence.Transient
    private String[] phoneScreenshotArray;

    private String phoneScreenshotDir;

    // needed by android-room and jpa
    public Localized() {
    }

    @androidx.room.Ignore
    public Localized(int appId, int localeId) {
        setAppId(appId);
        setLocaleId(localeId);
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "appId", this.appId);
        toStringBuilder(sb, "localeId", this.localeId);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "phoneScreenshotDir", phoneScreenshotDir);
        toStringBuilder(sb, "phoneScreenshots", phoneScreenshots, 20);
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

    public int getLocaleId() {
        return localeId;
    }

    public void setLocaleId(int localeId) {
        this.localeId = localeId;
    }

    /**
     * comma seperated list to be downloaded from
     * {@link Repo} identified by {@link App#getResourceRepoId()}
     * <p>
     * Example phoneScreenshot "1-game.jpg" can be downloaded from
     * https://f-droid.org/repo/dev.lonami.klooni/en-US/phoneScreenshots/1-game.jpg
     */
    public String getPhoneScreenshots() {
        return phoneScreenshots;
    }

    public void setPhoneScreenshots(String phoneScreenshots) {
        this.phoneScreenshots = phoneScreenshots;
        phoneScreenshotArray = null;
    }

    public String[] getPhoneScreenshotArray() {
        if (phoneScreenshotArray == null) {
            phoneScreenshotArray = StringUtil.toStringArray(phoneScreenshots);
        }
        return phoneScreenshotArray;
    }

    /**
     * ie dev.lonami.klooni/en-US/phoneScreenshots/
     */
    public String getPhoneScreenshotDir() {
        return phoneScreenshotDir;
    }

    public void setPhoneScreenshotDir(String phoneScreenshotDir) {
        this.phoneScreenshotDir = phoneScreenshotDir;
    }
}
