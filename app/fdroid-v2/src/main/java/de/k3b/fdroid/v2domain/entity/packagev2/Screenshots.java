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
// Screenshots.java

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.k3b.fdroid.v2domain.entity.repo.FileV2;

public final class Screenshots {
    @Nullable
    private final Map<String, List<FileV2>> phone;
    @Nullable
    private final Map<String, List<FileV2>> sevenInch;
    @Nullable
    private final Map<String, List<FileV2>> tenInch;
    @Nullable
    private final Map<String, List<FileV2>> wear;
    @Nullable
    private final Map<String, List<FileV2>> tv;

    public Screenshots(@Nullable Map<String, List<FileV2>> phone, @Nullable Map<String, List<FileV2>> sevenInch,
                       @Nullable Map<String, List<FileV2>> tenInch, @Nullable Map<String, List<FileV2>> wear,
                       @Nullable Map<String, List<FileV2>> tv) {
        this.phone = phone;
        this.sevenInch = sevenInch;
        this.tenInch = tenInch;
        this.wear = wear;
        this.tv = tv;
    }

    public final boolean isNull() {
        return this.phone == null && this.sevenInch == null && this.tenInch == null && this.wear == null && this.tv == null;
    }

    @Nullable
    public final Map<String, List<FileV2>> getPhone() {
        return this.phone;
    }

    @Nullable
    public final Map<String, List<FileV2>> getSevenInch() {
        return this.sevenInch;
    }

    @Nullable
    public final Map<String, List<FileV2>> getTenInch() {
        return this.tenInch;
    }

    @Nullable
    public final Map<String, List<FileV2>> getWear() {
        return this.wear;
    }

    @Nullable
    public final Map<String, List<FileV2>> getTv() {
        return this.tv;
    }

    @NotNull
    public String toString() {
        return "Screenshots{phone=" + this.phone + ", sevenInch=" + this.sevenInch + ", tenInch=" + this.tenInch + ", wear=" + this.wear + ", tv=" + this.tv + "}";
    }
}
