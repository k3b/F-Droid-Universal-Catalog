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

package de.k3b.fdroid.v1domain.entity;

import java.util.List;
import java.util.Map;

public class V1AppCatalog implements V1JsonEntity {
    private V1Repo repo;
    private List<V1App> apps;

    private Map<String, List<Version>> packages;

    public V1Repo getRepo() {
        return repo;
    }

    public void setRepo(V1Repo v1Repo) {
        this.repo = v1Repo;
    }


    public List<V1App> getApps() {
        return apps;
    }

    public void setApps(List<V1App> apps) {
        this.apps = apps;
    }

    public Map<String, List<Version>> getPackages() {
        return packages;
    }

    public void setPackages(Map<String, List<Version>> packages) {
        this.packages = packages;
    }

}
