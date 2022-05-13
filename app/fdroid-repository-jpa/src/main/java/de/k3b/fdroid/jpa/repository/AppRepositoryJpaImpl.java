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

import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import de.k3b.fdroid.domain.AppSearchParameter;
import de.k3b.fdroid.domain.interfaces.AppRepositoryFindDynamic;
import de.k3b.fdroid.sql.AppIdSql;

public class AppRepositoryJpaImpl implements AppRepositoryFindDynamic {
    @Autowired
    private EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> findDynamic(AppSearchParameter appSearchParameter) {
        Map<String, Object> params = new HashMap<>();
        String sql = AppIdSql.getSql(appSearchParameter, params, false);
        Query nativeQuery = entityManager
                .createNativeQuery(sql);

        for (Map.Entry<String, Object> param : params.entrySet()) {
            nativeQuery.setParameter(param.getKey(), param.getValue());
        }

        //noinspection unchecked
        return (List<Integer>) nativeQuery.getResultList();
    }

}
