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
import android.content.res.Resources;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.TypedValue;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class HtmlUtil extends de.k3b.fdroid.html.util.HtmlUtil {
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

    public static void setHtml(TextView textView, String htmlRaw,
                               int defaultForegroundColor,
                               int defaultBackgroundColor) {
        String html = htmlRaw
                .replace("\r", " ")
                .replace("\n", " ")
                .replace("  ", " ")
                // .replace("<br/>","\n")
                ;
        Spanned spanned = HtmlUtil.fromHtml(html);
        textView.setText(spanned);

        // interpret html: css-class='state_xxxx' will be translated to android-resource-colors
        String cssClass = getHtmlCssClassState(html);
        Context context = textView.getContext();
        textView.setBackgroundColor(getColorByName(context, cssClass, "bg_state_", defaultBackgroundColor));
        textView.setTextColor(getColorByName(context, cssClass, "fg_state_", defaultForegroundColor));
    }

    public static Integer getColorByName(Context context, String name, String namePrefix, Integer notFoundValueColor) {
        if (name != null) {
            String colorName = namePrefix + name;

            Resources resources = context.getResources();

            // assume that the android color resources are in the same module as this class.
            int identifier = resources.getIdentifier(colorName, "color", context.getPackageName());
            if (identifier != 0) {
                return resources.getColor(identifier);
            }
        }
        return notFoundValueColor;
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

    public static int getDefaultForegroundColor(Context context) {
        // https://stackoverflow.com/questions/67749943/programmatically-set-a-views-color-to-androidattr-color-attributes
        // ?android:attr/colorBackground == android.R.attr.colorSecondary
        int colorId = android.R.attr.colorForeground;
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(colorId, typedValue, true);
        return context.getResources().getColor(typedValue.resourceId);
    }
}
