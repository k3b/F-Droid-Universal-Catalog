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

package de.k3b.fdroid.domain.entity;

/**
 * Entities (from DDD) are java data classes where the data is persistable.
 * The "fdroid.domain" contains the Android independent Database-{@link de.k3b.fdroid.domain.interfaces.Enitity}s
 * ({@link de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId})
 * that contain properties that are persisted in the Database through a {@link de.k3b.fdroid.domain.interfaces.Enitity}
 * specific {@link de.k3b.fdroid.domain.repository.Repository} .
 * A {@link de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId} may only contain primitive types, primaryKeys and foreignKeys.
 * Relations or Objects or lists are not allowed.
 */
