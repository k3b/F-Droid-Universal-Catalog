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

package de.k3b.fdroid.v1domain.entity;

import java.util.List;

import javax.annotation.Generated;

import de.k3b.fdroid.domain.entity.common.RepoCommon;

/**
 * Data for a FDroid-Repository (read from FDroid-Catalog-v1-Json format).
 * <p>
 * The {@link V1JsonEntity} {@link Repo} correspond to the
 * {@link de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId} {@link de.k3b.fdroid.domain.entity.Repo}.
 */
@Generated("jsonschema2pojo")
public class Repo extends RepoCommon implements V1JsonEntity {

    private List<String> mirrors = null;

    public List<String> getMirrors() {
        return mirrors;
    }

    public void setMirrors(List<String> mirrors) {
        this.mirrors = mirrors;
    }

    protected void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "mirrors", this.mirrors);
    }
}
