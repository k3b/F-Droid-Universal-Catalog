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

package de.k3b.fdroid.domain.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * what all Pojo-s have in Common:
 * Nearly a pojo except there is toString() with toStringBuilder support.
 * pojo Properties are not allowed
 */
public class PojoCommon {
    /**
     * non standard string len for aggregated fields and for app description
     */
    public static final int MAX_LEN_AGGREGATED = 8000;
    /**
     * non standard string len for aggregated_description
     */
    public static final int MAX_LEN_AGGREGATED_DESCRIPTION = 256000;

    public static String asDateString(long longDate) {
        if (longDate == 0) return "";

        Date date = new Date(longDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(date);
    }

    protected void toStringBuilder(StringBuilder sb, String name, Object value) {
        if (value != null)
            sb.append(name).append('=').append(value).append(',');
    }

    protected void toStringBuilder(StringBuilder sb, String name, PojoCommon value) {
        if (value != null) {
            sb.append(name).append("=").append(value).append(",");
        }
    }

    protected void toStringBuilder(StringBuilder sb, String name, long value) {
        if (value != 0 && value != -1)
            sb.append(name).append('=').append(value).append(',');
    }

    protected void toStringBuilder(StringBuilder sb, String name, boolean value) {
        if (value)
            sb.append(name).append('=').append(value).append(',');
    }

    public void toDateStringBuilder(StringBuilder sb, String name, long value) {
        if (value != 0)
            sb.append(name).append('=').append(asDateString(value)).append(',');
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append('[');
        toStringBuilder(sb);
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    protected void toStringBuilder(StringBuilder sb) {
    }
}
