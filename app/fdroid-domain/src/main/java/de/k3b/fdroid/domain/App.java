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

import de.k3b.fdroid.domain.common.AppCommon;

/**
 * Android independant: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity(foreignKeys = @androidx.room.ForeignKey(entity = Repo.class,
        parentColumns = "id", childColumns = "repoId",
        onDelete = androidx.room.ForeignKey.CASCADE),
        indices = {@androidx.room.Index("id"), @androidx.room.Index({"repoId", "packageName"})}
)
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class App extends AppCommon {

    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    private int repoId = -1;
    // public Repo repo;

    // public List<Localized> localisations;
    // public List<Version> versions;

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "repoId", this.repoId);
        super.toStringBuilder(sb);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRepoId() {
        return repoId;
    }

    public void setRepoId(int repoId) {
        this.repoId = repoId;
    }
}
