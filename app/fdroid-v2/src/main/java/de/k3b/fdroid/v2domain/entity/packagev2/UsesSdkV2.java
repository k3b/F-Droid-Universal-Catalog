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

// UsesSdkV2.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class UsesSdkV2 {
    private final int minSdkVersion;
    private final int targetSdkVersion;

    public UsesSdkV2(int minSdkVersion, int targetSdkVersion) {
        this.minSdkVersion = minSdkVersion;
        this.targetSdkVersion = targetSdkVersion;
    }

    public final int getMinSdkVersion() {
        return this.minSdkVersion;
    }

    public final int getTargetSdkVersion() {
        return this.targetSdkVersion;
    }

    @NotNull
    public String toString() {
        return "UsesSdkV2{minSdkVersion=" + this.minSdkVersion + ", targetSdkVersion=" + this.targetSdkVersion + "}";
    }
}
