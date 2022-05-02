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
package de.k3b.fdroid.android.html.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class HtmlUtil {

    public static final String STYLE_BACKGROUND_COLOR = "background-color:";

    public static Spanned fromHtml(String html) {
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(html, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
        } else {
            //noinspection deprecation
            spanned = Html.fromHtml(html);
        }
        return spanned;
    }

    public static void setHtml(TextView textView, String htmlRaw, Integer backgroundColor) {
        String html = htmlRaw
                .replace("\r", " ")
                .replace("\n", " ")
                .replace("  ", " ")
                // .replace("<br/>","\n")
                ;
        Spanned spanned = HtmlUtil.fromHtml(html);
        textView.setText(spanned);

        backgroundColor = getBackgroundColor(html, backgroundColor);
        textView.setBackgroundColor(backgroundColor);
    }

    private static Integer getBackgroundColor(String html, Integer backgroundColor) {
        // TextView does not support html backgroundcolor.
        // if html contains some style=background-color:...
        int found = html.indexOf(STYLE_BACKGROUND_COLOR);
        if (found > 1) {
            found += STYLE_BACKGROUND_COLOR.length();
            int end = html.indexOf(";", found);
            if (end > 0) {
                String colorSpec = html.substring(found, end);
                backgroundColor = Color.parseColor(colorSpec);
            }
        }
        return backgroundColor;
    }

    public static int getDefaultBackgroundColor(Context context) {
        // https://stackoverflow.com/questions/67749943/programmatically-set-a-views-color-to-androidattr-color-attributes
        // ?android:attr/colorBackground == android.R.attr.colorSecondary
        int colorId = android.R.attr.colorForegroundInverse;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            colorId = android.R.attr.colorSecondary;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(colorId, typedValue, true);
        return context.getResources().getColor(typedValue.resourceId);
    }
}
