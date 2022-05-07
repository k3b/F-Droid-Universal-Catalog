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
package de.k3b.fdroid.domain;

import de.k3b.fdroid.domain.common.EntityCommon;
import de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId;

/**
 * Android independent: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(indices = {@androidx.room.Index("id")})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class Locale extends EntityCommon implements DatabaseEntityWithId {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    private String code; // ie de

    private String symbol; // ie 🇩🇪

    private String nameNative; // ie Deutsch
    private String nameEnglish; // ie German
    private int languagePriority; // translation order highest first

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "code", this.code);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "symbol", this.symbol);
        toStringBuilder(sb, "nameNative", this.nameNative);
        toStringBuilder(sb, "nameEnglish", this.nameEnglish);
        toStringBuilder(sb, "languagePriority", this.getLanguagePriority());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * locale-language-code. Usually two-letter-lowercase. i.e. it for italian
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
