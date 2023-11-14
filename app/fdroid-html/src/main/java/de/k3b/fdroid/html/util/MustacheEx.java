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
package de.k3b.fdroid.html.util;

import com.samskivert.mustache.DefaultCollector;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Mustache.VariableFetcher;

import java.util.HashMap;
import java.util.Map;

/**
 * Extended Mustache that can expose additional global fixed properties
 * that will be available in all contexts.
 * <p>
 * Example MustacheEx.addFixedProperty("today", new Date())
 * will allow {{some.object.today}} or {{today}}.
 */
public class MustacheEx {
    public static void addFixedProperty(String nameForFixed, Object fixedValue) {
        MustacheCollectorWithAdditionalFixedValues.fixedProperties.put(nameForFixed, fixedValue);
    }

    public static Mustache.Compiler createMustacheCompiler() {

        Mustache.Compiler compiler = Mustache.compiler()
                .escapeHTML(true).nullValue("")
                .withCollector(new MustacheCollectorWithAdditionalFixedValues());
        return compiler;
    }

    private static class MustacheCollectorWithAdditionalFixedValues extends DefaultCollector {
        private static final Map<String, Object> fixedProperties = new HashMap<>();
        protected static final Mustache.VariableFetcher FIXED_MAP_FETCHER = new Mustache.VariableFetcher() {
            public Object get(Object ctx, String name) throws Exception {
                return fixedProperties.get(name);
            }

            @Override
            public String toString() {
                return "FIXED_MAP_FETCHER";
            }
        };

        @Override
        public VariableFetcher createFetcher(Object ctx, String name) {
            if (fixedProperties.containsKey(name)) {
                return FIXED_MAP_FETCHER;
            }
            return super.createFetcher(ctx, name);
        }
    }
}
