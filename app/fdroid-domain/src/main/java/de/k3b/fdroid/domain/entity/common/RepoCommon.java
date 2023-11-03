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

package de.k3b.fdroid.domain.entity.common;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@javax.persistence.MappedSuperclass
@SuppressWarnings("unused")
public class RepoCommon extends EntityCommon {
    public static final String V1_JAR_NAME = "index-v1.jar";
    public static final String V1_JSON_NAME = "index-v1.json";

    @Schema(description = "Name of the Repo.",
            example = "F-Droid")
    private String name;

    @androidx.room.ColumnInfo(defaultValue = "0")
    @Schema(description = "When the Repo-Catalog-download-file was last created in internal numeric format.",
            externalDocs = @ExternalDocumentation(url = WebReferences.GLOSSAR_URL + "Repo-Catalog"),
            example = "1654792862000")
    private long timestamp;

    @androidx.room.ColumnInfo(defaultValue = "0")
    private int version;

    @androidx.room.ColumnInfo(defaultValue = "0")
    @Schema(description = "After [xxx=14] Days the repository-content should be reloaded.",
            example = "14")
    private int maxage;

    @Schema(description = "Used to calculate the url from [address].",
            example = "fdroid-icon.png")
    private String icon;
    @Schema(description = "Root adress where urls are calculated from.",
            example = "https://f-droid.org/repo")
    private String address;
    @Schema(description = "Descripton of the Repo.",
            example = "The official F-Droid Free Software repository.  Everything in this repository ...")
    private String description;

    public static void copyCommon(RepoCommon dest, RepoCommon src) {
        dest.setName(src.getName());
        dest.setTimestamp(src.getTimestamp());
        dest.setVersion(src.getVersion());
        dest.setMaxage(src.getMaxage());
        dest.setIcon(src.getIcon());
        dest.setAddress(src.getAddress());
        String description = src.getDescription();
        if (description != null && description.length() > 255) {
            dest.setDescription(description.substring(0,255));
        } else {
            dest.setDescription(description);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getV1JarFileName(String name) {
        String fileName = name
                .replace(' ', '_')
                .replace('/', '_')
                .replace('\\', '_')
                .replace(':', '_')
                .replace("__", "_");
        return fileName + "-" + V1_JAR_NAME;
    }

    @Schema(description = "Name of the Repo-Catalog-download-file. Used to calculate the url from [address].",
            externalDocs = @ExternalDocumentation(url = WebReferences.GLOSSAR_URL + "Repo-Catalog"),
            example = "F-Droid-index-v1.jar")
    public String getV1JarFileName() {
        return getV1JarFileName(this.getName());
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Schema(description = "Date, when the Repo-Catalog-download-file was last created.",
            externalDocs = @ExternalDocumentation(url = WebReferences.GLOSSAR_URL + "Repo-Catalog"),
            example = "2022-06-09")
    public String getTimestampDate() {
        return timestamp == 0 ? null : asDateString(timestamp);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getMaxage() {
        return maxage;
    }

    public void setMaxage(int maxage) {
        this.maxage = maxage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Only the first sentence of the Description. The whole Description is
     * to much for an android listview
     */
    @Schema(description = "The first sentence of the [description]. The whole [description] is to much for an android listview",
            example = "The official F-Droid Free Software repository.")
    public String getShortDescription() {
        int len = (description != null) ? description.length() : 0;
        if (len > 100) {
            len = description.indexOf('.');
            if (len > 0) return description.substring(0, len + 1);
        }

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "name", this.name);
        toDateStringBuilder(sb, "timestamp", this.timestamp);
        toStringBuilder(sb, "version", this.version);
        toStringBuilder(sb, "maxage", this.maxage);
        toStringBuilder(sb, "icon", this.icon);
        toStringBuilder(sb, "address", this.address);
        toStringBuilder(sb, "description", this.description, 40);
    }
}
