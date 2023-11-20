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

import javax.persistence.Column;

import io.swagger.v3.oas.annotations.media.Schema;

@javax.persistence.MappedSuperclass
/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@SuppressWarnings("unused")
public class LocalizedCommon extends EntityCommon implements ILocalizedCommon {
    @Schema(description = "Localized [App] name.",
            example = "A Photo Manager (Manejador de fotos)")
    private String name;
    @Schema(description = "Localized description summary of the [App].",
            example = "Verwalte lokale Photos: Suchen/Kopieren/Exif bearbeiten/Gallerie/Landkarte.")
    private String summary;
    @Column(length = MAX_LEN_AGGREGATED)
    @Schema(description = "Localized description of the [App].",
            example = "Merkmale: Schnelle Bildsuche per Tags(Suchbegriffe), ...")
    private String description;
    private String icon;
    @Schema(description = "Url: To an app video.",
            example = "https://www.youtube.com/watch?v=......")
    private String video;
    @Column(length = MAX_LEN_AGGREGATED)
    @Schema(description = "Localized 'What is new' info of the [App].",
            example = "#168: Bugfix crash in  ...")
    private String whatsNew;

    public static void copyCommon(LocalizedCommon dest, LocalizedCommon src) {
        dest.setName(ifNotNull(src.getName(), dest.getName()));
        dest.setIcon(ifNotNull(src.getIcon(), dest.getIcon()));
        dest.setDescription(ifNotNull(src.getDescription(), dest.getDescription()));
        dest.setSummary(ifNotNull(src.getSummary(),dest.getSummary()));
        dest.setVideo(ifNotNull(src.getVideo(),dest.getVideo()));
        dest.setWhatsNew(ifNotNull(src.getWhatsNew(),dest.getWhatsNew()));
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = maxlen(name);
    }

    @Override
    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = maxlen(summary);
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = maxlen(description, MAX_LEN_AGGREGATED);
    }

    @Override
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = maxlen(icon);
    }

    @Override
    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = maxlen(video);
    }

    @Override
    public String getWhatsNew() {
        return whatsNew;
    }

    public void setWhatsNew(String whatsNew) {
        this.whatsNew = maxlen(whatsNew, MAX_LEN_AGGREGATED);
    }

    public static void toStringBuilder(
            @NotNull StringBuilder sb, @Nullable ILocalizedCommon content) {
        if (content != null) {
            toStringBuilder(sb, "name", content.getName());
            toStringBuilder(sb, "summary", content.getSummary());
            toStringBuilder(sb, "description", content.getDescription());
            toStringBuilder(sb, "icon", content.getIcon());
            toStringBuilder(sb, "video", content.getVideo());
            toStringBuilder(sb, "whatsNew", content.getWhatsNew());
        }
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, this);
    }

}
