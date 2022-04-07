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

package org.fdroid.model.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * what all Pojo-s have in Common: Currently only toString() support. Properties are not allowed
 */
public class PojoCommon {
    public static String asDateString(long longDate) {
        if (longDate == 0) return "";

        Date date = new Date(longDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(date);
    }

    public void toStringBuilder(StringBuilder sb, String name, Object value) {
        if (value != null)
            sb.append(name).append('=').append(value).append(',');
    }

    public void toStringBuilder(StringBuilder sb, String name, long value) {
        if (value != 0 && value != -1)
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
