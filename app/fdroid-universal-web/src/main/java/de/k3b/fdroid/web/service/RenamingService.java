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
package de.k3b.fdroid.web.service;

import com.samskivert.mustache.Mustache;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import de.k3b.fdroid.html.domain.ValueAndTranslate;
import de.k3b.fdroid.html.service.DummyMustacheContext;

/**
 * wraps items into a hashmap with a key to be used in mustache
 */
@Service
public class RenamingService<T> {
    private final Mustache.CustomContext resourceTranslator;

    public RenamingService() {
        resourceTranslator = new DummyMustacheContext(); // ResourceBundleMustacheContext(Locale.getDefault());
    }

    public RenamingService(Mustache.CustomContext resourceTranslator) {
        this.resourceTranslator = resourceTranslator;
    }

    public List<ValueAndTranslate<T>> templateValue(List<T> items) {
        List<ValueAndTranslate<T>> result = new ArrayList<>();
        for (T item : items) {
            result.add(templateValue(item));
        }
        return result;
    }

    public ValueAndTranslate<T> templateValue(T item) {
        ValueAndTranslate<T> result = new ValueAndTranslate<>(resourceTranslator);
        result.v = item;
        return result;
    }
}
