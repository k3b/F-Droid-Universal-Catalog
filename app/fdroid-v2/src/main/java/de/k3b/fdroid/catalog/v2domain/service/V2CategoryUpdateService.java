/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of de.k3b.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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

package de.k3b.fdroid.catalog.v2domain.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.catalog.v2domain.entity.V2IconUtil;
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2Category;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.domain.service.CategoryService;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.service.TranslationService;
import de.k3b.fdroid.domain.util.Java8Util;

@SuppressWarnings("unused")
public class V2CategoryUpdateService implements ProgressObservable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    private static final int PROGRESS_INTERVALL = 100;
    @NotNull
    private final CategoryService categoryService;
    @NotNull
    private final TranslationService categoryNameService;
    @NotNull
    private final TranslationService categoryDescriptionService;
    private final TranslationService categoryIconService;

    private ProgressObserver progressObserver = null;
    private int progressCounter = 0;
    private int progressCountdown = 0;

    public V2CategoryUpdateService(
            @Nullable TranslationRepository translationRepository,
            @NotNull CategoryService categoryService) {
        this.categoryService = categoryService;
        this.categoryNameService = new TranslationService(TranslationService.TYP_CATEGORY_NAME, translationRepository);
        this.categoryDescriptionService = new TranslationService(TranslationService.TYP_CATEGORY_DESCRIPTION, translationRepository);
        this.categoryIconService = new TranslationService(TranslationService.TYP_CATEGORY_ICON, translationRepository);
    }

    public V2CategoryUpdateService init() {
        categoryService.init();
        categoryNameService.init();
        categoryDescriptionService.init();
        categoryIconService.init();

        progressCounter = 0;
        progressCountdown = 0;
        return this;
    }

    public void update(@Nullable Map<String, V2Category> v2CategoryList) {
        if (v2CategoryList != null) {
            for (Map.Entry<String, V2Category> entry : v2CategoryList.entrySet()) {
                update(entry.getKey(), entry.getValue());
            }
        }
    }

    public void update(@NotNull String name, @NotNull V2Category v2Category) {
        int categoryId = categoryService.getOrCreateCategoryId(name);
        categoryNameService.update(categoryId, name, LanguageService.asCanonicalLocaleMap(v2Category.getName()));
        categoryDescriptionService.update(categoryId, "",
                LanguageService.asCanonicalLocaleMap(v2Category.getDescription()));

        this.categoryIconService.updateIcon(categoryId,
                Java8Util.reduce(LanguageService.asCanonicalLocaleMap(v2Category.getIcon()), V2IconUtil::getIconName));
    }

    @Override
    public void setProgressObserver(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }

    @Override
    public String toString() {
        return "V2CategoryUpdateService{" +
                "categoryService=" + categoryService +
                ", categoryNameService=" + categoryNameService +
                ", categoryDescriptionService=" + categoryDescriptionService +
                ", categoryIconService=" + categoryIconService +
                '}';
    }
}
