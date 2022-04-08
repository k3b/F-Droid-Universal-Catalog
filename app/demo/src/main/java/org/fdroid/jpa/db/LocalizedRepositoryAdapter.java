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

import org.fdroid.jpa.db.base.RepositoryAdapterBase;
import org.springframework.stereotype.Service;

import java.util.List;

import de.k3b.fdroid.room.db.LocalizedRepository;
import de.k3b.fdroid.room.model.Localized;

/**
 * Spring-Boot-Jpa (Non-Android) specific Database-Repository implementation:
 * Entity-Pojo-s are transfered from/to database using Spring-Boot-Jpa.
 * XxxxRepositoryJpa implements the Database transfer.
 * XxxxRepositoryAdapter makes XxxxRepositoryJpa compatible with XxxxRepository.
 */
@Service
public class LocalizedRepositoryAdapter extends RepositoryAdapterBase<Localized, LocalizedRepositoryJpa> implements LocalizedRepository {
    public LocalizedRepositoryAdapter(LocalizedRepositoryJpa jpa) {
        super(jpa);
    }

    @Override
    public List<Localized> findByAppId(int appId) {
        return jpa.findByAppId(appId);
    }

    @Override
    public List<Localized> findByAppIdAndLocaleIds(int appId, List<Integer> localeIds) {
        return jpa.findForAppIdAndLocales(appId, localeIds);
    }
}
