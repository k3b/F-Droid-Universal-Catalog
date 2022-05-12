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
package de.k3b.fdroid.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.util.StringUtil;

/**
 * sql used by android-room and by springboot-jpa-hsqldb
 */
public class AppIdSql {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_SQL);

    public static String getSql(
            AppRepository.FindDynamicParameter findDynamicParameter,
            Map<String, Object> params,
            boolean forAndroid) {
        StringBuilder sql = new StringBuilder();
        if (!StringUtil.isEmpty(findDynamicParameter.search)) {
            // android-sqLite does not support "LIMIT" in "GROUP BY" queries
            return bySearchScore(sql, params, findDynamicParameter.search,
                    findDynamicParameter.orderBy,
                    (forAndroid) ? 0 : findDynamicParameter.maxRowCount).toString();
        }

        sql.append("SELECT id from App ");
        addOrderBy(sql, findDynamicParameter.orderBy);
        addLimit(sql, params, findDynamicParameter.maxRowCount);
        LOGGER.debug("SQL: {}\n\tParams: {}", sql, params);
        return sql.toString();
    }

    private static StringBuilder bySearchScore(StringBuilder sql, Map<String, Object> params, String search, String orderBy, int maxRowCount) {
        if (orderBy == null) {
            // sql.append("SELECT id from ("); // valid for hsqldb but not for sqLite
            bySearchScoreImpl(sql, params, search, maxRowCount);
            // sql.append(")");
        } else {
            throw new UnsupportedOperationException("SQL search with order by");
        }
        LOGGER.debug("SQL: {}\n\tParams: {}", sql, params);
        return sql;
    }

    private static StringBuilder bySearchScoreImpl(StringBuilder sql, Map<String, Object> params, String search, int maxRowCount) {
        if (StringUtil.isEmpty(search)) throw new NullPointerException();

        sql.append("select\n" +
                "    id,\n" +
                "    packageName,\n" +
                "    sum(score) AS score_sum\n" +
                "from AppSearch\n" +
                "where (");
        String[] expressions = search.split(" ");
        int index = 1;
        for (String expresson : expressions) {
            if (index > 1) sql.append(" OR ");
            sql.append("search like :search").append(index);
            params.put("search" + index, "%" + expresson + "%");

            index++;
        }
        sql.append(")\n group by id, packageName\n" +
                " order by score_sum desc, packageName ");
        addLimit(sql, params, maxRowCount);
        return sql;
    }

    private static void addOrderBy(StringBuilder sql, String orderBy) {
        if (orderBy == null) orderBy = "lastUpdated desc";
        sql.append(" ORDER BY ").append(orderBy);
    }

    private static void addLimit(StringBuilder sql, Map<String, Object> params, int maxRowCount) {
        if (maxRowCount != 0) {
            addParam(sql, params, " LIMIT :maxRowCount ", "maxRowCount", maxRowCount);
        }
    }

    private static void addParam(StringBuilder sql, Map<String, Object> params, String expresson, String paramName, Object paramValue) {
        sql.append(expresson);
        params.put(paramName, paramValue);
    }
}
