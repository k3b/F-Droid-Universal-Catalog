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
package de.k3b.fdroid.v1.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.AppCategory;
import de.k3b.fdroid.domain.Category;
import de.k3b.fdroid.domain.interfaces.AppCategoryRepository;
import de.k3b.fdroid.domain.interfaces.CategoryRepository;
/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class AppCategoryUpdateService {
    private final CategoryRepository categoryRepository;
    private final AppCategoryRepository appCategoryRepository;

    Map<Integer, Category> id2Category = null;
    Map<String, Category> name2Category = null;

    public AppCategoryUpdateService(CategoryRepository categoryRepository, AppCategoryRepository appCategoryRepository) {
        this.categoryRepository = categoryRepository;
        this.appCategoryRepository = appCategoryRepository;
    }

    public AppCategoryUpdateService init() {
        id2Category = new HashMap<>();
        name2Category = new HashMap<>();
        List<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            init(category);
        }
        return this;
    }

    private void init(Category category) {
        id2Category.put(category.id, category);
        name2Category.put(category.name, category);
    }

    private String getCategoryName(int categoryId) {
        Category category = (categoryId == 0) ? null : id2Category.get(categoryId);
        return (category == null) ? null : category.name;
    }

    private int getOrCreateCategoryId(String categoryName) {
        if (categoryName != null) {
            Category category = name2Category.get(categoryName);
            if (category == null) {
                // create on demand
                category = new Category();
                category.name = categoryName;
                categoryRepository.insert(category);
                init(category);
            }
            return category.id;
        }
        return 0;
    }

    public void update(int appId, List<String> v1Categories) {
        List<AppCategory> roomAppCategories = appCategoryRepository.findByAppId(appId);

        deleteRemoved(roomAppCategories, v1Categories);
        for (String v1Category : v1Categories) {
            int categoryId = getOrCreateCategoryId(v1Category);

            AppCategory roomAppCategory = findByCategoryId(roomAppCategories, categoryId);
            if (roomAppCategory == null) {
                roomAppCategory = new AppCategory(appId, categoryId);
                appCategoryRepository.insert(roomAppCategory);
                roomAppCategories.add(roomAppCategory);
            } else {
                // category already assigned. Nothing to do
            }
        }
    }

    private AppCategory findByCategoryId(List<AppCategory> appCategoryList, int categoryId) {
        for (AppCategory appCategory : appCategoryList) {
            if (appCategory.categoryId == categoryId) return appCategory;
        }
        return null;
    }

    private void deleteRemoved(List<AppCategory> roomAppCategories, List<String> v1Categories) {
        for (int i = roomAppCategories.size() - 1; i >= 0; i--) {
            AppCategory roomAppCategory = roomAppCategories.get(i);
            if (roomAppCategory != null) {
                String categoryName = getCategoryName(roomAppCategory.categoryId);
                if (categoryName != null && !v1Categories.contains(categoryName)) {
                    appCategoryRepository.delete(roomAppCategory);
                    roomAppCategories.remove(i);
                }
            }
        }
    }
}
