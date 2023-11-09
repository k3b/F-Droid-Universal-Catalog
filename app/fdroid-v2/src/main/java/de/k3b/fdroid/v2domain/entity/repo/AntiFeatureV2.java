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

// AntiFeatureV2.java

import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class AntiFeatureV2 {
    @NotNull
    private final Map<String, FileV2> icon;
    @NotNull
    private final Map<String, String> name;
    @NotNull
    private final Map<String, String> description;

    public AntiFeatureV2(@NotNull Map<String, FileV2> icon, @NotNull Map<String, String> name, @NotNull Map<String, String> description) {
        this.icon = icon;
        this.name = name;
        this.description = description;
    }

    @NotNull
    public final Map<String, FileV2> getIcon() {
        return this.icon;
    }

    @NotNull
    public final Map<String, String> getName() {
        return this.name;
    }

    @NotNull
    public final Map<String, String> getDescription() {
        return this.description;
    }

    @NotNull
    public String toString() {
        return "AntiFeatureV2{icon=" + this.icon + ", name=" + this.name + ", description=" + this.description + "}";
    }
}
