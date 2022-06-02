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
package de.k3b.fdroid.domain.util;

import java.util.HashMap;
import java.util.Map;

public class AndroidVersionName {
    private static final Map<Integer, String> ID_TO_NAME = new HashMap<>();

    static {
        ID_TO_NAME.put(0, "");
        // from android.os.Build.VERSION_CODES
        // see also https://apilevels.com/
        register(1, "Android 1.0", null, "September 2008");
        register(2, "Android 1.1", null, "February 2009");
        register(3, "Android 1.5", "CUPCAKE", "April 2009");
        register(4, "Android 1.6", "DONUT", "September 2009");
        register(5, "Android 2.0", "ECLAIR", "October 2009");
        register(6, "Android 2.0.1", "ECLAIR_0_1", "December 2009");
        register(7, "Android 2.1", "ECLAIR_MR1", "January 2010");
        register(8, "Android 2.2", "FROYO", "May 2010");
        register(9, "Android 2.3", "GINGERBREAD", "December 2010");
        register(10, "Android 2.3.3", "GINGERBREAD_MR1", "February 2011");
        register(11, "Android 3.0", "HONEYCOMB", "February 2011");
        register(12, "Android 3.1", "HONEYCOMB_MR1", "May 2011");
        register(13, "Android 3.2", "HONEYCOMB_MR2", "July 2011");
        register(14, "Android 4.0", "ICE_CREAM_SANDWICH", "October 2011");
        register(15, "Android 4.03", "ICE_CREAM_SANDWICH_MR1", "December 2011");
        register(16, "Android 4.1", "JELLY_BEAN", "July 2012");
        register(17, "Android 4.2", "JELLY_BEAN_MR1", "November 2012");
        register(18, "Android 4.3", "JELLY_BEAN_MR2", "July 2013");
        register(19, "Android 4.4", "KITKAT", "October 2013");
        register(20, "Android 4.4W", "KITKAT_WATCH", "June 2014");
        register(21, "Android 5.0", "LOLLIPOP", "November 2014");
        register(22, "Android 5.1", "LOLLIPOP_MR1", "March 2015");
        register(23, "Android 6.0", "MARSHMALLOW", "October 2015");
        register(24, "Android 7.0", "NOUGAT", "August 2016");
        register(25, "Android 7.1", "NOUGAT_MR1", "October 2016");
        register(26, "Android 8.0", "OREO", "August 2017");
        register(27, "Android 8.1", "OREO_MR1", "December 2017");
        register(28, "Android 9", "PIE", "August 2018");
        register(29, "Android 10", "Q", "September 2019");
        register(30, "Android 11", "R", "September 2020");
        register(31, "Android 12", "S", "October 2021");
        register(32, "Android 12.1", "S_V2", "March 2022");
        register(33, "Android 13", "T", "September 2022");
    }

    private static void register(int version, String versionName, String codeName, String releaseDate) {
        StringBuilder name = new StringBuilder().append(versionName);
        if (codeName != null) name.append(" - ").append(codeName);
        name.append(" - SDK").append(version);
        if (releaseDate != null) name.append(" - ").append(releaseDate);

        ID_TO_NAME.put(version, name.toString());
    }

    public static String getName(int verionNumber, String nativeCode) {
        String result = ID_TO_NAME.get(Math.max(0, verionNumber));
        if (result == null) result = "Android ??? Api " + verionNumber;
        if (nativeCode == null) {
            return result;
        }
        return nativeCode + " " + result;
    }
}
