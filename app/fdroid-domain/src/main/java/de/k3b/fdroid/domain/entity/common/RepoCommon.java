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

package de.k3b.fdroid.domain.entity.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@javax.persistence.MappedSuperclass
@SuppressWarnings("unused")
public class RepoCommon extends EntityCommon implements IRepoCommon {
    public static final String V1_JAR_NAME = "index-v1.jar";
    public static final String V1_JSON_NAME = "index-v1.json";

    @Schema(description = "Name of the [Repo].",
            example = "F-Droid")
    private String name;

    @androidx.room.ColumnInfo(defaultValue = "0")
    @Schema(description = "When the [Repo-Catalog]-download-file was last created in internal numeric format.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo-Catalog"),
            example = "1654792862000")
    private long timestamp;

    @Schema(description = "Used to calculate the url from [Address].",
            example = "fdroid-icon.png")
    private String icon;
    @Schema(description = "Root [Address] where urls are calculated from.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Address"),
            example = "https://f-droid.org/repo")
    private String address;
    @Schema(description = "Descripton of the [Repo].",
            example = "The official F-Droid Free Software repository.  Everything in this repository ...")
    private String description;

    public static void copyCommon(RepoCommon dest, IRepoCommon src) {
        dest.setName(src.getName());
        dest.setTimestamp(src.getTimestamp());
        dest.setIcon(src.getIcon());
        dest.setAddress(src.getAddress());
        String description = src.getDescription();
        dest.setDescription(description);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = maxlen(name);
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

    @Schema(description = "Name of the [Repo-Catalog]-download-file. Used to calculate the url from [Address].",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo-Catalog"),
            example = "F-Droid-index-v1.jar")
    public String getV1JarFileName() {
        return getV1JarFileName(this.getName());
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Schema(description = "Date, when the [Repo-Catalog]-download-file was last created.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo-Catalog"),
            example = "2022-06-09")
    public String getTimestampDate() {
        return timestamp == 0 ? null : asDateString(timestamp);
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = maxlen(icon);
    }

    @Override
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = maxlen(address);
    }

    @Override
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
        if (description != null && description.length() > 255) {
            description = description.substring(0, 255);
        }
        this.description = maxlen(description);

    }

    public static void toStringBuilder(
            @NotNull StringBuilder sb, @Nullable IRepoCommon content) {
        if (content != null) {
            toStringBuilder(sb, "name", content.getName());
            toDateStringBuilder(sb, "timestamp", content.getTimestamp());
            toStringBuilder(sb, "icon", content.getIcon());
            toStringBuilder(sb, "address", content.getAddress());
            toStringBuilder(sb, "description", content.getDescription(), 40);
        }
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, this);
    }
}
