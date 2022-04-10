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

package org.fdroid.model.v1;

import java.util.List;

import javax.annotation.Generated;

import de.k3b.fdroid.domain.common.VersionCommon;

/**
 * Data for the program version of an android app (read from FDroid-Catalog-v1-Json format).
 */
@Generated("jsonschema2pojo")
public class Version extends VersionCommon {
    private List<String> nativecode = null;

    public List<String> getNativecode() {
        return nativecode;
    }

    public void setNativecode(List<String> nativecode) {
        this.nativecode = nativecode;
    }

    protected void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "nativecode", this.nativecode);
    }
}
