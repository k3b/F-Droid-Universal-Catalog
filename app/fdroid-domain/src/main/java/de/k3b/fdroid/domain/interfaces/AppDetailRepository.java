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

/**
 * load the Details for one or more {@link de.k3b.fdroid.domain.App} identified by appId.
 * <p>
 * Persists a {@link DatabaseEntityWithId}) that relates to a app {@link de.k3b.fdroid.domain.App} in the Database.
 */
public interface AppDetailRepository<T extends AppDetail> extends Repository {
    List<T> findByAppIds(List<Integer> appIds);
}
