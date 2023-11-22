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
package de.k3b.fdroid.android.repository;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import de.k3b.fdroid.domain.entity.Translation;
import de.k3b.fdroid.domain.repository.TranslationRepository;

@Dao
public interface TranslationDao extends TranslationRepository {
    @Insert
    void insert(Translation translation);

    @Update
    void update(Translation translation);

    @Delete
    void delete(Translation translation);

    @Query("SELECT * FROM Translation")
    List<Translation> findAll();

    @Query("SELECT * FROM Translation where Translation.typ = :typ")
    List<Translation> findByTyp(String typ);

    @Query("SELECT * FROM Translation where Translation.typ = :typ and Translation.id = :id")
    List<Translation> findByTypAndId(String typ, int id);

    @Query("SELECT * FROM Translation where Translation.typ = :typ and Translation.id = :id and Translation.localeId in (:localeIds)")
    List<Translation> findByTypAndIdAndLocales(String typ, int id, String... localeIds);

    @Query("SELECT * FROM Translation where Translation.typ in (:typs) and Translation.localeId in (:localeIds)")
    List<Translation> findByTypsAndLocales(String[] typs, String... localeIds);

}
