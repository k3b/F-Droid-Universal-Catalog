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
package de.k3b.fdroid.domain.repository;

import java.util.List;

import de.k3b.fdroid.domain.entity.Translation;

/**
 * Android independent interfaces to use the Database.
 * <p>
 * Persists {@link Translation} in the Database.
 */
public interface TranslationRepository extends Repository {
    void insert(Translation translation);

    void update(Translation translation);

    void delete(Translation translation);

    List<Translation> findByTyp(String typ);

    List<Translation> findByTypAndId(String typ, int id);
}
