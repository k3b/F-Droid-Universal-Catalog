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
package de.k3b.fdroid.v2domain.entity.repo;
// V2Category.java

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class V2Category {
    @NotNull
    private final Map<String, V2File> icon;
    @NotNull
    private final Map<String, String> name;
    @NotNull
    private final Map<String, String> description;

    public V2Category(@NotNull Map<String, V2File> icon, @NotNull Map<String, String> name, @NotNull Map<String, String> description) {
        this.icon = icon;
        this.name = name;
        this.description = description;
    }

    @NotNull
    public Map<String, V2File> getIcon() {
        return this.icon;
    }

    @NotNull
    public Map<String, String> getName() {
        return this.name;
    }

    @NotNull
    public Map<String, String> getDescription() {
        return this.description;
    }

    @NotNull
    public String toString() {
        return "V2Category{icon=" + this.icon + ", name=" + this.name + ", description=" + this.description + "}";
    }
}
