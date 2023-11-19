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

// @ExternalDocumentation(description = "Translation of content", url = ExtDoc.GLOSSAR_URL + "Category")
@androidx.room.Entity(primaryKeys = {"typ", "id", "localeId"},
        indices = {@androidx.room.Index({"typ", "id", "localeId"})})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@javax.persistence.IdClass(TranslationPK.class)
@SuppressWarnings("unused")
public class Translation extends TranslationPK {
    private String localizedText;

    // needed by android-room and jpa
    public Translation() {
    }

    @androidx.room.Ignore
    public Translation(String typ, int id, String localeId) {
        super(typ, id, localeId);
    }

    protected void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "localizedText", this.localizedText);
    }

    public String getLocalizedText() {
        return localizedText;
    }

    public void setLocalizedText(String localizedText) {
        this.localizedText = localizedText;
    }
}
