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
package de.k3b.fdroid.jpa.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import de.k3b.fdroid.domain.entity.Localized;

/**
 * Spring-Boot-Jpa (Non-Android) specific Database-Repository implementation:
 * Entity-Pojo-s are transfered from/to database using Spring-Boot-Jpa.
 * XxxxRepositoryJpa implements the Database transfer.
 * XxxxRepositoryAdapter makes XxxxRepositoryJpa compatible with XxxxRepository.
 */
@Repository
public interface LocalizedRepositoryJpa extends CrudRepository<Localized, Integer> {
    // @Query(value = "select l from Localized l where l.appId = ?1")
    List<Localized> findByAppId(int appId);

    @Query(value = "select l from Localized l where l.appId = ?1 and l.localeId in (?2)")
    List<Localized> findForAppIdAndLocales(int appId, List<Integer> localeIds);

    @Query(value = "select al from Localized al " +
            "inner join Locale l on al.localeId = l.id " +
            "where al.appId in (?1) and l.languagePriority <> -1 " +
            "order by al.appId, l.languagePriority desc")
    List<Localized> findNonHiddenByAppIds(List<Integer> appIds);
}
