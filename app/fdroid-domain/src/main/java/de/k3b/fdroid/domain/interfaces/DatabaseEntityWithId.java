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

import de.k3b.fdroid.domain.entity.AppWithDetails;
import de.k3b.fdroid.domain.repository.Repository;

/**
 * Database-Entity : An {@link Enitity} that can be persisted to database-table through
 * java-{@link Repository}-interface.
 * <p>
 * * There are android/jpa specific implementaions for the java-{@link Repository}-interfaces.
 * * Restrictions imposed by android-room, K3b-Architecture specific:
 * * * Database-Entities can not contain relation specific informations (like master-detail,
 * one-to-many) except foreign keys.
 * * * The DDD- {@link AggregateRoot} contains master-detail-infos and therefore is not a Database-Entity.
 * Instead there is a domain specific java class {@link AppWithDetails} that
 * holds the master-Detail-Relations for one {@link de.k3b.fdroid.domain.entity.App} as {@link AggregateRoot}.
 * There is also a {@link de.k3b.fdroid.domain.service.AppWithDetailsPagerService} that implements paging with
 * load-on-demand.
 */
public interface DatabaseEntityWithId extends Enitity {
    int getId();

    static <T extends DatabaseEntityWithId> boolean sameId(T lhs, T rhs) {
        int lhsId = lhs == null ? -1 : lhs.getId();
        int rhsId = rhs == null ? -1 : rhs.getId();
        return lhsId == rhsId;
    }
}
