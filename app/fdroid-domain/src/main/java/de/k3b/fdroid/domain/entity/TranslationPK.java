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

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * {@link Translation}: An {@link App} can belong to zero or more {@link Category}s.
 * <p>
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 */
@javax.persistence.MappedSuperclass
public class TranslationPK extends EntityCommon implements Serializable {
    @NotNull
    @Schema(description = "Type of translation. Ie RI=RepositoryIcon.",
            example = "RI")
    @Column(length = MAX_LEN_LOCALE)
    @Id
    private String typ;

    @Schema(description = "Id of the translateded Icon. Example if typ='RI'(RepositoryIcon) the id is repo.id.",
            example = "1")
    @Id
    private int id;

    @NotNull
    @Schema(description = "Iso-Language-Code (Without the Country-Code).",
            example = "de")
    @Column(length = MAX_LEN_LOCALE)
    @Id
    private String localeId;

    public TranslationPK() {
    }

    @androidx.room.Ignore
    public TranslationPK(String typ, int id, String localeId) {
        this.typ = typ;
        this.id = id;
        this.localeId = localeId;
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "typ", this.typ);
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "localeId", this.localeId);
        super.toStringBuilder(sb);
    }

    public String getTyp() {
        return typ;
    }

    public void setTyp(String typ) {
        this.typ = typ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocaleId() {
        return localeId;
    }

    public void setLocaleId(String localeId) {
        this.localeId = localeId;
    }
}
