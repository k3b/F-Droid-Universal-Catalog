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
package de.k3b.fdroid.domain.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.entity.AntiFeature;
import de.k3b.fdroid.domain.repository.AntiFeatureRepository;

/**
 * Service to cache/find/insert AntiFeature info.
 */

public class AntiFeatureService extends CacheServiceInteger<AntiFeature> {
    @Nullable
    private final AntiFeatureRepository antiFeatureRepository;

    Map<String, AntiFeature> name2AntiFeature;

    // used to generate fake antiFeature-id-s if antiFeatureRepository is null
    private int mockId = 12100;

    public AntiFeatureService(@Nullable AntiFeatureRepository antiFeatureRepository) {
        this.antiFeatureRepository = antiFeatureRepository;
    }

    public AntiFeatureService init() {
        List<AntiFeature> all = (antiFeatureRepository == null) ? null : antiFeatureRepository.findAll();
        init(all);
        return this;
    }

    protected void init(@Nullable List<AntiFeature> itemList) {
        name2AntiFeature = new HashMap<>();
        super.init(itemList);
    }

    protected void init(AntiFeature antiFeature) {
        super.init(antiFeature);
        name2AntiFeature.put(antiFeature.getName(), antiFeature);
    }

    public String getAntiFeatureName(int antiFeatureId) {
        AntiFeature antiFeature = (antiFeatureId == 0) ? null : getItemById(antiFeatureId);
        return (antiFeature == null) ? null : antiFeature.getName();
    }

    @NotNull
    public AntiFeature getOrCreateAntiFeature(@NotNull String antiFeatureName) {
        AntiFeature antiFeature = name2AntiFeature.get(antiFeatureName);
        if (antiFeature == null) {
            // create on demand
            antiFeature = new AntiFeature();
            antiFeature.setName(antiFeatureName);
            if (antiFeatureRepository != null) {
                antiFeatureRepository.insert(antiFeature);
            } else {
                antiFeature.setId(mockId++);
            }
            init(antiFeature);
        }
        return antiFeature;
    }

    public int getOrCreateAntiFeatureId(@NotNull String antiFeatureName) {
        return getOrCreateAntiFeature(antiFeatureName).getId();
    }

    @Override
    public String toString() {
        return "AntiFeatureService{" +
                "name2AntiFeature=" + name2AntiFeature +
                ", mockId=" + mockId +
                '}';
    }
}
