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

import java.util.List;

import javax.persistence.EntityManager;

import de.k3b.fdroid.domain.interfaces.AppRepositoryFindIdsByExpression;

public class AppRepositoryJpaImpl implements AppRepositoryFindIdsByExpression {
    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Integer> findIdsByExpression(String searchText) {
        String sql = "select id from (select\n" +
                "    id,\n" +
                "    PACKAGE_NAME,\n" +
                "    sum(score) score\n" +
                "from APP_SEARCH\n" +
                "where search like :search\n" +
                "group by id, PACKAGE_NAME\n" +
                "order by sum(score) desc, PACKAGE_NAME\n" +
                ")";


        Object result = entityManager
                .createNativeQuery(sql)
                .setParameter("search", "%" + searchText + "%")
                .getResultList();
        return (List<Integer>) result;
    }
}
