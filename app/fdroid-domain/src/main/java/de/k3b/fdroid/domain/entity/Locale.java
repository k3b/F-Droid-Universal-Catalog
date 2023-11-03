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

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId;

/**
 * {@link Locale}: Language of a {@link Localized}.
 * <p>
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(indices = {@androidx.room.Index("id")})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
@SuppressWarnings("unused")
public class Locale extends EntityCommon implements DatabaseEntityWithId<String> {
    @javax.persistence.Id
    @androidx.room.PrimaryKey
    @NotNull
    private String id; // ie de

    private String symbol; // ie 🇩🇪

    private String nameNative; // ie Deutsch
    private String nameEnglish; // ie German

    // translation order highest first; -1 == hidden (Translations are NOT contained in Database)
    private int languagePriority;

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "symbol", this.symbol);
        toStringBuilder(sb, "nameNative", this.nameNative);
        toStringBuilder(sb, "nameEnglish", this.nameEnglish);
        toStringBuilder(sb, "languagePriority", this.getLanguagePriority());
    }

    /**
     * locale-language-code. Usually two-letter-lowercase. i.e. it for italian
     */
    @NotNull
    @Override
    public String getId() {
        return id;
    }

    public void setId(@NotNull String id) {
        this.id = id;
    }

    /**
     * a flag to symbolize the language. i.e. 🇮🇹
     */
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getNameNative() {
        return nameNative;
    }

    public void setNameNative(String nameNative) {
        this.nameNative = nameNative;
    }

    public String getNameEnglish() {
        return nameEnglish;
    }

    public void setNameEnglish(String nameEnglish) {
        this.nameEnglish = nameEnglish;
    }

    public int getLanguagePriority() {
        return languagePriority;
    }

    public void setLanguagePriority(int languagePriority) {
        this.languagePriority = languagePriority;
    }
}
