/*
 * Copyright (c) 2022 by k3b.
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

    /**
     * Creates sql for {@link de.k3b.fdroid.domain.repository.AppRepository#findDynamic(AppSearchParameter)}
     * sql prefixes:
     * * s=search (AppSearch or App)
     * * av=AppVersion (if versionSdk > 0)
     * * c=AppCategory (if categoryId > 0)
     *
     * @return sql plus updeated parameters
     */
    public static String getSql(
            AppSearchParameter appSearchParameter,
            Map<String, Object> params,
            boolean forAndroid) {
        boolean withSearchText = !StringUtil.isEmpty(appSearchParameter.searchText);

        StringBuilder sql = new StringBuilder();
        if (withSearchText) {
            sql.append("SELECT\n" +
                    "    s.id,\n" +
                    "    s.packageName,\n" +
                    "    sum(s.score) AS score_sum\n" +
                    "FROM AppSearch AS s\n");
        } else {
            sql.append("SELECT s.id FROM App AS s\n");
        }

        int versionSdk = appSearchParameter.versionSdk;
        if (versionSdk > 0) {
            sql.append("INNER JOIN AppVersion AS av ON s.id=av.appId\n");
        }

        int categoryId = appSearchParameter.categoryId;
        if (categoryId > 0) {
            sql.append("INNER JOIN AppCategory AS c ON s.id=c.appId\n");
        }

        sql.append("WHERE ");
        boolean noCondition = true;
        if (withSearchText) {
            noCondition = false;
            addWhereSearchText(sql, appSearchParameter, params);
        }

        if (versionSdk > 0) {
            if (!noCondition) {
                sql.append(" AND ");
            }
            if (withSearchText && forAndroid) {
                // parameter replacement does not work for complex sql statements on my android-10 :-(
                sql.append(" ((av.minSdkVersion <= " + versionSdk + " AND\n" +
                        "    ((av.maxSdkVersion = 0) OR (av.maxSdkVersion >= " + versionSdk + ")))) ");
            } else {
                addParam(sql, params,
                        " ((av.minSdkVersion <= :sdkversion AND\n" +
                                "    ((av.maxSdkVersion = 0) OR (av.maxSdkVersion >= :sdkversion)))) ",
                        "sdkversion", versionSdk);
            }
            noCondition = false;
        }

        if (categoryId > 0) {
            if (!noCondition) {
                sql.append(" AND ");
            }
            if (forAndroid) {
                // parameter replacement does not work for complex sql statements on my android-10 :-(
                sql.append(" (c.categoryId = " + categoryId + ") ");
            } else {
                addParam(sql, params, " (c.categoryId = :categoryId) ", "categoryId", categoryId);
            }
            noCondition = false;
        }

        if (noCondition) {
            sql.append("1=1");
        }

        String orderBy = appSearchParameter.orderBy;
        if (withSearchText) {
            sql.append("\n GROUP BY s.id, s.packageName ");
            if (StringUtil.isEmpty(orderBy)) {
                orderBy = "score_sum desc, s.packageName ";
            }
        }
        addOrderBy(sql, orderBy);
        addLimit(sql, params, appSearchParameter.maxRowCount, forAndroid);

        LOGGER.info("SQL: {}\n\tParams: {}", sql, params);
        return sql.toString();
    }

    private static void addWhereSearchText(StringBuilder sql, AppSearchParameter appSearchParameter, Map<String, Object> params) {
        sql.append(" (");
        String[] expressions = appSearchParameter.searchText.split(" ");
        int index = 1;
        for (String expresson : expressions) {
            if (index > 1) sql.append(" AND ");
            sql.append("s.search LIKE :search").append(index);
            params.put("search" + index, "%" + expresson + "%");

            index++;
        }
        sql.append(")");

        Integer minimumScore = appSearchParameter.minimumScore;
        if (minimumScore != null) {
            sql.append(" AND s.score >= :minimumScore ");
            params.put("minimumScore", minimumScore);
        }
    }

    private static void addOrderBy(StringBuilder sql, String orderBy) {
        if (StringUtil.isEmpty(orderBy)) orderBy = "lastUpdated desc";
        sql.append(" ORDER BY ").append(orderBy);
    }

    private static void addLimit(StringBuilder sql, Map<String, Object> params, int maxRowCount, boolean forAndroid) {
        if (maxRowCount != 0) {
            if (forAndroid) {
                // parameter replacement does not work for complex sql statements on my android-10 :-(
                sql.append(" LIMIT " + maxRowCount + " ");
            } else {
                addParam(sql, params, " LIMIT :maxRowCount ", "maxRowCount", maxRowCount);
            }
        }
    }

    private static void addParam(StringBuilder sql, Map<String, Object> params, String expresson, String paramName, Object paramValue) {
        sql.append(expresson);
        params.put(paramName, paramValue);
    }
}
