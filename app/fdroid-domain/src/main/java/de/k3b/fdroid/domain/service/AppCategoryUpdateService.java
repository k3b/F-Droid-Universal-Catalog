/*
 * Copyright (c) 2022-2023 by k3b.
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
package de.k3b.fdroid.domain.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.entity.AppCategory;
import de.k3b.fdroid.domain.repository.AppCategoryRepository;
import de.k3b.fdroid.domain.util.ExceptionUtils;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class AppCategoryUpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    @NotNull
    private final CategoryService categoryService;
    @Nullable
    private final AppCategoryRepository appCategoryRepository;
    private int mockId = 10000;

    public AppCategoryUpdateService init() {
        categoryService.init();
        return this;
    }

    public AppCategoryUpdateService(@NotNull CategoryService categoryService, @Nullable AppCategoryRepository appCategoryRepository) {
        this.categoryService = categoryService;
        this.appCategoryRepository = appCategoryRepository;
    }

    public void update(int appId, List<String> v1Categories)
            throws PersistenceException {
        List<AppCategory> roomAppCategories;
        int categoryId = 0;
        String categoryName = "";
        try {
            roomAppCategories = (appCategoryRepository == null)
                    ? new ArrayList<>()
                    : appCategoryRepository.findByAppId(appId);

            deleteRemoved(roomAppCategories, v1Categories);
            for (String v1Category : v1Categories) {
                categoryName = v1Category;
                categoryId = categoryService.getOrCreateCategoryId(categoryName);

                AppCategory roomAppCategory = findByCategoryId(roomAppCategories, categoryId);
                if (roomAppCategory == null) {
                    roomAppCategory = new AppCategory(appId, categoryId);
                    if (appCategoryRepository != null) {
                        appCategoryRepository.insert(roomAppCategory);
                    } else {
                        roomAppCategory.setId(mockId++);
                    }
                    roomAppCategories.add(roomAppCategory);
                } else {
                    // category already assigned. Nothing to do
                }
            }
        } catch (Exception ex) {
            // thrown by j2se hibernate database problem
            // hibernate DataIntegrityViolationException -> NestedRuntimeException
            // hibernate org.hibernate.exception.DataException inherits from PersistenceException
            String message = "PersistenceException in " + getClass().getSimpleName()
                    + ".update(app=" + appId
                    + ",categoryId=" + categoryId
                    + ",categoryName='" + categoryName
                    + "') "
                    + ExceptionUtils.getParentCauseMessage(ex, PersistenceException.class);
            LOGGER.error(message + "\n\tv1Categories=" + v1Categories, ex);
            throw new PersistenceException(message, ex);
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
                    if (appCategoryRepository != null)
                        appCategoryRepository.delete(roomAppCategory);
                    roomAppCategories.remove(i);
                }
            }
        }
    }
}
