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

/**
 * Database-Entity : can be persisted to database-table through java-{@link Repository} -interface.
 * <p>
 * * There are android/jpa specific implementaions for the java-repository-interfaces.
 * * Restrictions imposed by android-room, K3b-Architecture specific:
 * * * Database-Entities can not contain relation specific informations (like master-detail,
 * one-to-many) except foreign keys
 * * * The DDD-Root-Aggregate contains master-detail-infos and therefore is not a Database-Entity.
 * Instead there is a domain specific java class "AppWithDetails" that combines
 * DDD-Root-Aggregate with pager (for lists)
 */
public interface DatabaseEntityWithId {
    int getId();

    static <T extends DatabaseEntityWithId> boolean sameId(T lhs, T rhs) {
        int lhsId = lhs == null ? -1 : lhs.getId();
        int rhsId = rhs == null ? -1 : rhs.getId();
        return lhsId == rhsId;
    }
}
