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

import java.util.List;

import de.k3b.fdroid.domain.AppCategory;
import de.k3b.fdroid.domain.interfaces.AppCategoryRepository;
import de.k3b.fdroid.service.CategoryService;
import de.k3b.fdroid.v1.domain.UpdateService;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class AppCategoryUpdateService implements UpdateService {
    private final CategoryService categoryService;
    private final AppCategoryRepository appCategoryRepository;

    public AppCategoryUpdateService(CategoryService categoryService, AppCategoryRepository appCategoryRepository) {
        this.categoryService = categoryService;
        this.appCategoryRepository = appCategoryRepository;
    }

    public AppCategoryUpdateService init() {
        categoryService.init();
        return this;
    }

    public void update(int appId, List<String> v1Categories) {
        List<AppCategory> roomAppCategories = appCategoryRepository.findByAppId(appId);

        deleteRemoved(roomAppCategories, v1Categories);
        for (String v1Category : v1Categories) {
            int categoryId = categoryService.getOrCreateCategoryId(v1Category);

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
            if (appCategory.getCategoryId() == categoryId) return appCategory;
        }
        return null;
    }

    private void deleteRemoved(List<AppCategory> roomAppCategories, List<String> v1Categories) {
        for (int i = roomAppCategories.size() - 1; i >= 0; i--) {
            AppCategory roomAppCategory = roomAppCategories.get(i);
            if (roomAppCategory != null) {
                String categoryName = categoryService.getCategoryName(roomAppCategory.getCategoryId());
                if (categoryName != null && !v1Categories.contains(categoryName)) {
                    appCategoryRepository.delete(roomAppCategory);
                    roomAppCategories.remove(i);
                }
            }
        }
    }
}
