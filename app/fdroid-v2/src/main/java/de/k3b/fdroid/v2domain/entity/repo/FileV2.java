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

// FileV2.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import de.k3b.fdroid.v2domain.entity.common.IndexFile;

public final class FileV2 implements IndexFile {
    @NotNull
    private final String name;
    @Nullable
    private final String sha256;
    @Nullable
    private final Long size;
    @Nullable
    private final String ipfsCIDv1;

    public FileV2(@NotNull String name, @Nullable String sha256, @Nullable Long size, @Nullable String ipfsCIDv1) {
        this.name = name;
        this.sha256 = sha256;
        this.size = size;
        this.ipfsCIDv1 = ipfsCIDv1;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @Nullable
    public String getSha256() {
        return this.sha256;
    }

    @Nullable
    public Long getSize() {
        return this.size;
    }

    @Nullable
    public String getIpfsCidV1() {
        return this.ipfsCIDv1;
    }

    @NotNull
    public String toString() {
        return "FileV2{name=" + this.getName() + ", sha256=" + this.getSha256() + ", size=" + this.getSize() + ", ipfsCIDv1=" + this.getIpfsCidV1() + "}";
    }
}
