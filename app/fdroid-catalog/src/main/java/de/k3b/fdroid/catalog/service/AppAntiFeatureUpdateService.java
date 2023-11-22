/*
 * Copyright (c) 2023 by k3b.
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
package de.k3b.fdroid.catalog.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.entity.AppAntiFeature;
import de.k3b.fdroid.domain.repository.AppAntiFeatureRepository;
import de.k3b.fdroid.domain.service.AntiFeatureService;
import de.k3b.fdroid.domain.util.ExceptionUtils;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class AppAntiFeatureUpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    @NotNull
    private final AntiFeatureService antiFeatureService;
    @Nullable
    private final AppAntiFeatureRepository appAntiFeatureRepository;
    private int mockId = 10000;

    public AppAntiFeatureUpdateService(@NotNull AntiFeatureService antiFeatureService, @Nullable AppAntiFeatureRepository appAntiFeatureRepository) {
        this.antiFeatureService = antiFeatureService;
        this.appAntiFeatureRepository = appAntiFeatureRepository;
    }

    public AppAntiFeatureUpdateService init() {
        antiFeatureService.init();
        return this;
    }

    public void update(int appId, List<String> v1Categories)
            throws PersistenceException {
        List<AppAntiFeature> roomAppCategories;
        int antiFeatureId = 0;
        String antiFeatureName = "";
        try {
            roomAppCategories = (appAntiFeatureRepository == null)
                    ? new ArrayList<>()
                    : appAntiFeatureRepository.findByAppId(appId);

            deleteRemoved(roomAppCategories, v1Categories);
            for (String v1AntiFeature : v1Categories) {
                antiFeatureName = v1AntiFeature;
                antiFeatureId = antiFeatureService.getOrCreateAntiFeatureId(antiFeatureName);

                AppAntiFeature roomAppAntiFeature = findByAntiFeatureId(roomAppCategories, antiFeatureId);
                if (roomAppAntiFeature == null) {
                    roomAppAntiFeature = new AppAntiFeature(appId, antiFeatureId);
                    if (appAntiFeatureRepository != null) {
                        appAntiFeatureRepository.insert(roomAppAntiFeature);
                    } else {
                        roomAppAntiFeature.setId(mockId++);
                    }
                    roomAppCategories.add(roomAppAntiFeature);
                } else {
                    // antiFeature already assigned. Nothing to do
                }
            }
        } catch (Exception ex) {
            // thrown by j2se hibernate database problem
            // hibernate DataIntegrityViolationException -> NestedRuntimeException
            // hibernate org.hibernate.exception.DataException inherits from PersistenceException
            String message = "PersistenceException in " + getClass().getSimpleName()
                    + ".update(app=" + appId
                    + ",antiFeatureId=" + antiFeatureId
                    + ",antiFeatureName='" + antiFeatureName
                    + "') "
                    + ExceptionUtils.getParentCauseMessage(ex, PersistenceException.class);
            LOGGER.error(message + "\n\tv1Categories=" + v1Categories, ex);
            throw new PersistenceException(message, ex);
        }
    }

    private AppAntiFeature findByAntiFeatureId(List<AppAntiFeature> appAntiFeatureList, int antiFeatureId) {
        for (AppAntiFeature appAntiFeature : appAntiFeatureList) {
            if (appAntiFeature.getAntiFeatureId() == antiFeatureId) return appAntiFeature;
        }
        return null;
    }

    private void deleteRemoved(List<AppAntiFeature> roomAppCategories, List<String> v1Categories) {
        for (int i = roomAppCategories.size() - 1; i >= 0; i--) {
            AppAntiFeature roomAppAntiFeature = roomAppCategories.get(i);
            if (roomAppAntiFeature != null) {
                String antiFeatureName = antiFeatureService.getAntiFeatureName(roomAppAntiFeature.getAntiFeatureId());
                if (antiFeatureName != null && !v1Categories.contains(antiFeatureName)) {
                    if (appAntiFeatureRepository != null)
                        appAntiFeatureRepository.delete(roomAppAntiFeature);
                    roomAppCategories.remove(i);
                }
            }
        }
    }
}
