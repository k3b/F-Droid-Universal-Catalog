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

import javax.persistence.Column;

import io.swagger.v3.oas.annotations.media.Schema;

@javax.persistence.MappedSuperclass
/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@SuppressWarnings("unused")
public class LocalizedCommon extends EntityCommon {
    @Schema(description = "Localized app name.",
            example = "A Photo Manager (Manejador de fotos)")
    private String name;
    @Schema(description = "Localized description summary of the app.",
            example = "Verwalte lokale Photos: Suchen/Kopieren/Exif bearbeiten/Gallerie/Landkarte.")
    private String summary;
    @Column(length = MAX_LEN_AGGREGATED)
    @Schema(description = "Localized description of the app.",
            example = "Merkmale: Schnelle Bildsuche per Tags(Suchbegriffe), ...")
    private String description;
    private String icon;
    private String video;
    @Column(length = MAX_LEN_AGGREGATED)
    @Schema(description = "Localized 'What is new' info of the app.",
            example = "#168: Bugfix crash in  ...")
    private String whatsNew;

    public static void copyCommon(LocalizedCommon dest, LocalizedCommon src) {
        dest.setName(ifNotNull(src.getName(), dest.getName()));
        dest.setIcon(ifNotNull(src.getIcon(),dest.getIcon()));
        dest.setDescription(ifNotNull(src.getDescription(),dest.getDescription()));
        dest.setSummary(ifNotNull(src.getSummary(),dest.getSummary()));
        dest.setVideo(ifNotNull(src.getVideo(),dest.getVideo()));
        dest.setWhatsNew(ifNotNull(src.getWhatsNew(),dest.getWhatsNew()));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = maxlen(name);
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = maxlen(summary);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = maxlen(description, MAX_LEN_AGGREGATED);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = maxlen(icon);
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = maxlen(video);
    }

    public String getWhatsNew() {
        return whatsNew;
    }

    public void setWhatsNew(String whatsNew) {
        this.whatsNew = maxlen(whatsNew, MAX_LEN_AGGREGATED);
    }

    protected void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "name", this.name);
        toStringBuilder(sb, "summary", this.summary);
        toStringBuilder(sb, "description", this.description);
        toStringBuilder(sb, "icon", this.icon);
        toStringBuilder(sb, "video", this.video);
        toStringBuilder(sb, "whatsNew", this.whatsNew);
    }

}
