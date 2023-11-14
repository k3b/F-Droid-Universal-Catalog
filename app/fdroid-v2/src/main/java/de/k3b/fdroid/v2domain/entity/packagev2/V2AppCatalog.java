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

// V2AppCatalog.java

import org.jetbrains.annotations.NotNull;

import java.util.Map;

import de.k3b.fdroid.v2domain.entity.repo.V2Repo;

public final class V2AppCatalog {
    @NotNull
    private final V2Repo repo;
    @NotNull
    private final Map<String, V2App> packages;

    public V2AppCatalog(@NotNull V2Repo repo, @NotNull Map<String, V2App> packages) {
        this.repo = repo;
        this.packages = packages;
    }

    @NotNull
    public V2Repo getRepo() {
        return this.repo;
    }

    @NotNull
    public Map<String, V2App> getPackages() {
        return this.packages;
    }

    public String toString() {
        return "V2AppCatalog{repo=" + this.repo + ", packages=" + this.packages + "}";
    }
}
