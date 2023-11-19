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
package de.k3b.fdroid.catalog.v2domain.entity.packagev2;

// V2Signer.java

import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public class V2Signer {
    @NotNull
    private final List<String> sha256;
    private boolean hasMultipleSigners;

    public V2Signer(@NotNull List<String> sha256) {
        this.sha256 = sha256;
    }

    @NotNull
    public List<String> getSha256() {
        return this.sha256;
    }

    public boolean getHasMultipleSigners() {
        return this.hasMultipleSigners;
    }

    @NotNull
    public String toString() {
        return "V2Signer{sha256=" + this.sha256 + ", hasMultipleSigners=" + this.hasMultipleSigners + "}";
    }
}
