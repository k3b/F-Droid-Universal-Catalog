/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of de.k3b.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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

package de.k3b.fdroid.v2domain.entity.packagev2;

// V2App.java

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class V2App {
    @NotNull
    private final V2Metadata metadata;
    @NotNull
    private final Map<String, V2PackageVersion> versions;


    public V2App(@NotNull V2Metadata metadata, @NotNull Map<String, V2PackageVersion> versions) {
        this.metadata = metadata;
        this.versions = versions;
    }

    @NotNull
    public V2Metadata getMetadata() {
        return this.metadata;
    }

    @NotNull
    public Map<String, V2PackageVersion> getVersions() {
        return this.versions;
    }

    @NotNull
    public String toString() {
        return "V2App(metadata=" + this.metadata + ", versions=" + this.versions + ")";
    }
}
