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
package de.k3b.fdroid.domain.interfaces;

import java.util.List;

import de.k3b.fdroid.domain.Version;

/**
 * Android independant interfaces to use the Database
 */
public interface VersionRepository {
    void insert(Version version);

    void update(Version version);

    void delete(Version version);

    List<Version> findByAppId(int appId);

    Version findByRepoPackageAndVersionCode(int repoId, String packageName, long versionCode);
}
