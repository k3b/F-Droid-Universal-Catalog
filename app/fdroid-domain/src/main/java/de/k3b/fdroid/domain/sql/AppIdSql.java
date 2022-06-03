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
package de.k3b.fdroid.domain.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.util.StringUtil;

/**
 * sql used by android-room and by springboot-jpa-hsqldb
 */
public class AppIdSql {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_SQL);

    public static String getSql(
            AppSearchParameter appSearchParameter,
            Map<String, Object> params,
            boolean forAndroid) {
        StringBuilder sql = new StringBuilder();
        if (!StringUtil.isEmpty(appSearchParameter.searchText)) {
            // android-sqLite does not support "LIMIT" in "GROUP BY" queries
            return bySearchScore(sql, params, appSearchParameter.searchText,
                    appSearchParameter.minimumScore, appSearchParameter.versionSdk,
                    appSearchParameter.orderBy,
                    (forAndroid) ? 0 : appSearchParameter.maxRowCount).toString();
        }

        sql.append("SELECT id from App ");

        addWhere(sql, params, appSearchParameter);

        addOrderBy(sql, appSearchParameter.orderBy);
        addLimit(sql, params, appSearchParameter.maxRowCount);
        LOGGER.info("SQL: {}\n\tParams: {}", sql, params);
        return sql.toString();
    }

    private static void addWhere(StringBuilder sql, Map<String, Object> params, AppSearchParameter appSearchParameter) {
        boolean needsWhere = true;
        if (appSearchParameter.versionSdk > 0) {
            if (needsWhere) {
                sql.append(" WHERE ");
                needsWhere = false;
            } else {
                sql.append(" AND ");
            }
            sql.append(" id in ("
                    + ("SELECT distinct av.id " +
                    "FROM AppVersion AS av " +
                    "WHERE ((av.minSdkVersion <= :sdkversion AND " +
                    " ((av.maxSdkVersion IS NULL) OR (av.maxSdkVersion = 0) OR (av.maxSdkVersion >= :sdkversion)))) ")
                    + ")");
            params.put("sdkversion", appSearchParameter.versionSdk);
        }
    }

    private static StringBuilder bySearchScore(
            StringBuilder sql, Map<String, Object> params,
            String search, Integer minimumScore, int versionSdk, String orderBy, int maxRowCount) {
        if (StringUtil.isEmpty(orderBy)) {
            // sql.append("SELECT id from ("); // valid for hsqldb but not for sqLite
            bySearchScoreImpl(sql, params, search, minimumScore, versionSdk, maxRowCount);
            // sql.append(")");
        } else {
            throw new UnsupportedOperationException("SQL search with order by");
        }
        LOGGER.debug("SQL: {}\n\tParams: {}", sql, params);
        return sql;
    }

    private static StringBuilder bySearchScoreImpl(
            StringBuilder sql, Map<String, Object> params,
            String search, Integer minimumScore, int versionSdk, int maxRowCount) {
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
            if (index > 1) sql.append(" AND ");
            sql.append("search like :search").append(index);
            params.put("search" + index, "%" + expresson + "%");

            index++;
        }
        sql.append(")");

        if (minimumScore != null) {
            sql.append(" AND score >= :minimumScore ");
            params.put("minimumScore", minimumScore);
        }

        if (versionSdk > 0) {
            sql.append(" AND id in (\n" +
                    "    SELECT DISTINCT av.id\n" +
                    "    FROM AppVersion AS av\n" +
                    "    WHERE ((av.minSdkVersion <= :sdkversion AND\n" +
                    "            ((av.maxSdkVersion IS NULL) OR (av.maxSdkVersion = 0) OR (av.maxSdkVersion >= :sdkversion))))\n" +
                    ")");
            params.put("sdkversion", versionSdk);
        }
        sql.append("\n group by id, packageName\n" +
                " order by score_sum desc, packageName ");
        addLimit(sql, params, maxRowCount);
        return sql;
    }

    private static void addOrderBy(StringBuilder sql, String orderBy) {
        if (StringUtil.isEmpty(orderBy)) orderBy = "lastUpdated desc";
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
