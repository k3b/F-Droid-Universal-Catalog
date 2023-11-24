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
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2AntiFeature;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.domain.service.AntiFeatureService;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.service.TranslationService;
import de.k3b.fdroid.domain.util.Java8Util;

@SuppressWarnings("unused")
public class V2AntiFeatureUpdateService implements ProgressObservable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    private static final int PROGRESS_INTERVALL = 100;
    @NotNull
    private final AntiFeatureService antiFeatureService;
    @NotNull
    private final TranslationService antiFeatureNameService;
    @NotNull
    private final TranslationService antiFeatureDescriptionService;
    private final TranslationService antiFeatureIconService;

    private ProgressObserver progressObserver = null;
    private int progressCounter = 0;
    private int progressCountdown = 0;

    public V2AntiFeatureUpdateService(
            @Nullable TranslationRepository translationRepository,
            @NotNull AntiFeatureService antiFeatureService) {
        this.antiFeatureService = antiFeatureService;
        this.antiFeatureNameService = new TranslationService(TranslationService.TYP_AntiFeature_NAME, translationRepository);
        this.antiFeatureDescriptionService = new TranslationService(TranslationService.TYP_AntiFeature_DESCRIPTION, translationRepository);
        this.antiFeatureIconService = new TranslationService(TranslationService.TYP_AntiFeature_ICON, translationRepository);
    }

    public V2AntiFeatureUpdateService init() {
        antiFeatureService.init();
        antiFeatureNameService.init();
        antiFeatureDescriptionService.init();
        antiFeatureIconService.init();

        progressCounter = 0;
        progressCountdown = 0;
        return this;
    }

    public void update(@Nullable Map<String, V2AntiFeature> V2AntiFeatureList) {
        if (V2AntiFeatureList != null) {
            for (Map.Entry<String, V2AntiFeature> entry : V2AntiFeatureList.entrySet()) {
                update(entry.getKey(), entry.getValue());
            }
        }
    }

    public void update(@NotNull String name, @NotNull V2AntiFeature V2AntiFeature) {
        int antiFeatureId = antiFeatureService.getOrCreateAntiFeatureId(name);
        antiFeatureNameService.update(antiFeatureId, name, LanguageService.asCanonicalLocaleMap(V2AntiFeature.getName()));
        antiFeatureDescriptionService.update(antiFeatureId, "",
                LanguageService.asCanonicalLocaleMap(V2AntiFeature.getDescription()));

        this.antiFeatureIconService.updateIcon(antiFeatureId,
                Java8Util.reduce(LanguageService.asCanonicalLocaleMap(V2AntiFeature.getIcon()), V2IconUtil::getIconName));
    }

    @Override
    public void setProgressObserver(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }

    @Override
    public String toString() {
        return "V2AntiFeatureUpdateService{" +
                "antiFeatureService=" + antiFeatureService +
                ", antiFeatureNameService=" + antiFeatureNameService +
                ", antiFeatureDescriptionService=" + antiFeatureDescriptionService +
                ", antiFeatureIconService=" + antiFeatureIconService +
                '}';
    }
}
