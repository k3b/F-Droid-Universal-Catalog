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

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;

import de.k3b.fdroid.domain.entity.common.ExtDoc;
import de.k3b.fdroid.domain.entity.common.LocalizedCommon;
import de.k3b.fdroid.domain.interfaces.AppDetail;
import de.k3b.fdroid.domain.util.StringUtil;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

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
@ExternalDocumentation(description = "Translation of an [App]-infos in a [Locale] or language", url = ExtDoc.GLOSSAR_URL + "Localized")
@SuppressWarnings("unused")
public class Localized extends LocalizedCommon implements AppDetail {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    @androidx.room.ColumnInfo(index = true)
    private int appId;

    @androidx.room.ColumnInfo(index = true)
    @NotNull
    @Schema(description = "Iso-Language-Code (Without the Country-Code).",
            example = "de")
    @Column(length = MAX_LEN_LOCALE)
    private String localeId;

    @Column(length = MAX_LEN_AGGREGATED)
    @Schema(description = "Screenshot(s) from Android-Phone.",
            example = "1-Gallery.png,2-SelectArea.png")
    private String phoneScreenshots;
    /**
     * calculated and cached from {@link #phoneScreenshots}. Not persisted in Database
     */
    @androidx.room.Ignore
    @javax.persistence.Transient
    @JsonIgnore
    private String[] phoneScreenshotArray;

    @Schema(description = "Relative url where phoneScreenshots are stored.",
            example = "de.k3b.android.androFotoFinder/en-US/phoneScreenshots/")
    private String phoneScreenshotDir;

    // needed by android-room and jpa
    public Localized() {
    }

    @androidx.room.Ignore
    public Localized(int appId, String localeId) {
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

    @Override
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

    @NotNull
    public String getLocaleId() {
        return localeId;
    }

    public void setLocaleId(@NotNull String localeId) {
        this.localeId = maxlen(localeId, MAX_LEN_LOCALE);
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
        this.phoneScreenshots = maxlen(phoneScreenshots, MAX_LEN_AGGREGATED);
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
        this.phoneScreenshotDir = maxlen(phoneScreenshotDir);
    }
}
