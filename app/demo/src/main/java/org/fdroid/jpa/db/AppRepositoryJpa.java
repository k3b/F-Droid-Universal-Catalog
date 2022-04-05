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
package org.fdroid.jpa.db;

/**
 * non-android-j2se-jpa implementation of XxxRepository
 * /
 * public interface AppRepositoryJpa extends AppRepository, CrudRepository<App, Integer>, CustomRoomMethods<App> {
 * // void delete(App app);
 * <p>
 * // @Query(value = "SELECT App.id FROM App WHERE App.repoId = ?1 AND App.packageName = ?2")
 * // Integer findIdByRepoIdAndPackageName(Integer repoId, String packageName);
 * <p>
 * // @Query(value = "SELECT App FROM App WHERE App.repoId = ?1 AND App.packageName = ?2")
 * App findByRepoIdAndPackageName(Integer repoId, String packageName);
 * }
 */
