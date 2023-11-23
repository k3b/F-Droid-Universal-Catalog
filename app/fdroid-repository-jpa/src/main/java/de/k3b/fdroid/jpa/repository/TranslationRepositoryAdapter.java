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

import org.springframework.stereotype.Service;

import java.util.List;

import de.k3b.fdroid.domain.entity.Translation;
import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.jpa.repository.base.RepositoryAdapterBase;

/**
 * Spring-Boot-Jpa (Non-Android) specific Database-Repository implementation:
 * Entity-Pojo-s are transfered from/to database using Spring-Boot-Jpa.
 * XxxxRepositoryJpa implements the Database transfer.
 * XxxxRepositoryAdapter makes XxxxRepositoryJpa compatible with XxxxRepository.
 */
@Service
public class TranslationRepositoryAdapter extends RepositoryAdapterBase<Integer, Translation, TranslationRepositoryJpa> implements TranslationRepository {
    public TranslationRepositoryAdapter(TranslationRepositoryJpa jpa) {
        super(jpa);
    }

    @Override
    public void deleteByTyp(String typ) {
        jpa.deleteByTyp(typ);
    }

    @Override
    public void deleteByTypAndId(String typ, int id) {
        jpa.deleteByTypAndId(typ, id);
    }

    @Override
    public void deleteByTypAndIdAndLocale(String typ, int id, String localeId) {
        jpa.deleteByTypAndIdAndLocale(typ, id, localeId);
    }

    @Override
    public void deleteByTypAndLocale(String typ, String localeId) {
        jpa.deleteTypsAndLocale(typ, localeId);
    }

    @Override
    public List<Translation> findByTyp(String typ) {
        return jpa.findByTyp(typ);
    }

    @Override
    public List<Translation> findByTypAndId(String typ, int id) {
        return jpa.findByTypAndId(typ, id);
    }

    @Override
    public List<Translation> findByTypAndIdAndLocales(String typ, int id, String... localeIds) {
        return jpa.findByTypAndIdAndLocales(typ, id, localeIds);
    }

    @Override
    public List<Translation> findByTypsAndLocales(String[] typs, String... localeIds) {
        return jpa.findByTypsAndLocales(typs, localeIds);
    }
}
