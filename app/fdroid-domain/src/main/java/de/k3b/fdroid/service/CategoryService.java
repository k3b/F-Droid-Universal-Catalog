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
package de.k3b.fdroid.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.Category;
import de.k3b.fdroid.domain.interfaces.CategoryRepository;

/**
 * Service to cache/find/insert Category info.
 */

public class CategoryService {
    private final CategoryRepository categoryRepository;

    Map<Integer, Category> id2Category = null;
    Map<String, Category> name2Category = null;


    public CategoryService(CategoryRepository categoryRepository) {

        this.categoryRepository = categoryRepository;
    }

    public CategoryService init() {
        id2Category = new HashMap<>();
        name2Category = new HashMap<>();
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            init(category);
        }
        return this;
    }

    private void init(Category category) {
        id2Category.put(category.getId(), category);
        name2Category.put(category.getName(), category);
    }

    public String getCategoryName(int categoryId) {
        Category category = (categoryId == 0) ? null : id2Category.get(categoryId);
        return (category == null) ? null : category.getName();
    }

    public int getOrCreateCategoryId(String categoryName) {
        if (categoryName != null) {
            Category category = name2Category.get(categoryName);
            if (category == null) {
                // create on demand
                category = new Category();
                category.setName(categoryName);
                categoryRepository.insert(category);
                init(category);
            }
            return category.getId();
        }
        return 0;
    }

}
