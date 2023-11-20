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

package de.k3b.fdroid.html.service;

import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.InputStreamReader;
import java.io.Reader;

import de.k3b.fdroid.html.util.MustacheEx;

/**
 * Generate html snipptes from "Mustache Templates".
 * See @see <a href="http://mustache.github.io/mustache.5.html">mustache.5.html</a> and
 *
 * @see <a href="https://github.com/samskivert/jmustache">jmustache</a>
 * <p>
 * This is an abstraction from the JMustache implementation.
 * <p>
 * use de.k3b.fdroid.android.html.util.HtmlUtil#setHtml(TextView, html, ....) to to add
 * limited css-color support to TextView
 * <p>
 * For android specific textview:
 * * @see <a href="https://commonsware.com/blog/Android/2010/05/26/html-tags-supported-by-textview.html">html-tags-supported-by-textview.html</a>
 * * @see <a href="https://stackoverflow.com/questions/9754076/which-html-tags-are-supported-by-android-textview">which-html-tags-are-supported-by-android-textview</a>
 */
public class FormatService {
    final Template tmpl;
    public FormatService(String templateId, Class<?> itemclass, Mustache.CustomContext resourceTranslator) {
        MustacheEx.addFixedProperty("t", resourceTranslator);
        tmpl = MustacheEx
                .createMustacheCompiler()
                .compile(loadTemplate(templateId, itemclass));
    }

    public FormatService(String template, Mustache.CustomContext resourceTranslator) {
        MustacheEx.addFixedProperty("t", resourceTranslator);
        tmpl = MustacheEx
                .createMustacheCompiler()
                .compile(template);
    }

    public String format(Object values) {
        return tmpl.execute(values);
    }

    private Reader loadTemplate(String templateId, Class<?> itemclass) {
        return new InputStreamReader(FormatService.class.getResourceAsStream("/templates/" +
                itemclass.getSimpleName() + "/" +
                templateId + ".hbs"));
    }
}
