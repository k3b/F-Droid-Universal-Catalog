/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of org.fdroid project.
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

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import de.k3b.fdroid.domain.entity.Translation;

/**
 * Spring-Boot-Jpa (Non-Android) specific Database-Repository implementation:
 * Entity-Pojo-s are transfered from/to database using Spring-Boot-Jpa.
 * XxxxRepositoryJpa implements the Database transfer.
 * XxxxRepositoryAdapter makes XxxxRepositoryJpa compatible with XxxxRepository.
 */
@Repository
public interface TranslationRepositoryJpa extends CrudRepository<Translation, Integer> {
    List<Translation> findByTyp(String typ);

    List<Translation> findByTypAndId(String typ, int id);

    @Query("select t from Translation t WHERE t.typ= ?1 and t.id = ?2 and t.localeId in (?3)")
        // List<Person> getAllByCars...(@Param("car") String car)
    List<Translation> findByTypAndIdAndLocales(String typ, int id, String... localeIds);

    @Query("select t from Translation t WHERE t.typ in (?1) and t.localeId in (?2)")
    List<Translation> findByTypsAndLocales(String[] typs, String... localeIds);

    @Modifying
    @Query(nativeQuery = true, value = "delete from Translation t where t.typ = ?1")
    void deleteByTyp(String typ);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM Translation t where t.typ = ?1 and t.id = ?2")
    void deleteByTypAndId(String typ, int id);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM Translation t where t.typ = ?1 and t.id = ?2 and t.localeId = ?3")
    void deleteByTypAndIdAndLocale(String typ, int id, String localeId);

    @Modifying
    @Query(nativeQuery = true, value = "DELETE FROM Translation t where t.typ = ?1 and t.localeId = ?2")
    void deleteTypsAndLocale(String typ, String localeId);
}

