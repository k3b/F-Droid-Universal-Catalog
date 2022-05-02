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

package de.k3b.fdroid.html.service;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Generate html snipptes from "Mustache Template".
 * See https://github.com/samskivert/jmustache
 * <p>
 * This is an abstraction from the JMustache implementation.
 * <p>
 * For android specific textview:
 * * https://commonsware.com/blog/Android/2010/05/26/html-tags-supported-by-textview.html
 * * https://stackoverflow.com/questions/9754076/which-html-tags-are-supported-by-android-textview
 */
public class FormatService {
    final Template tmpl;
    private final ValueAndString vt;

    public FormatService(String templateId, Class<?> itemclass, Mustache.CustomContext resourceTranslator) {
        vt = createVt(resourceTranslator);
        tmpl = Mustache.compiler().escapeHTML(true).nullValue("").compile(loadTemplate(templateId, itemclass));
    }

    public FormatService(String template, Mustache.CustomContext resourceTranslator) {
        tmpl = Mustache.compiler().escapeHTML(true).nullValue("").compile(template);
        vt = createVt(resourceTranslator);
    }

    public String format(Object values) {
        if (vt == null) return tmpl.execute(values);

        vt.v = values;
        return tmpl.execute(vt);
    }


    private ValueAndString createVt(Mustache.CustomContext resourceTranslator) {
        return resourceTranslator == null ? null : new ValueAndString(resourceTranslator);
    }

    private Reader loadTemplate(String templateId, Class<?> itemclass) {
        return new InputStreamReader(FormatService.class.getResourceAsStream("/html/" +
                itemclass.getSimpleName() + "/" +
                templateId + ".hbs"));
    }

    private class ValueAndString {
        final Mustache.CustomContext t;
        Object v;

        public ValueAndString(Mustache.CustomContext resourceTranslator) {
            t = resourceTranslator;
        }
    }

}