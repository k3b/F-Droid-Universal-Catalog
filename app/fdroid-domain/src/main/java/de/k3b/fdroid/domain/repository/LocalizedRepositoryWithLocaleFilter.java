/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either localized 3 of the License, or
 * (at your option) any later localized.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package de.k3b.fdroid.domain.repository;

import java.util.List;

import de.k3b.fdroid.domain.entity.Localized;

/**
 * Same as default implementation but adds a filter for localesLocalized.
 */
public class LocalizedRepositoryWithLocaleFilter implements AppDetailRepository<Localized> {
    private final LocalizedRepository localizedRepository;
    private String[] locales;

    public LocalizedRepositoryWithLocaleFilter(LocalizedRepository localizedRepository) {
        this.localizedRepository = localizedRepository;
    }

    public void setLocales(String[] locales) {
        this.locales = locales;
    }

    @Override
    public List<Localized> findByAppIds(List<Integer> appIds) {
        if (locales == null || locales.length == 0) {
            return localizedRepository.findNonHiddenByAppIds(appIds);
        } else {
            return localizedRepository.findByAppIdsAndLocaleIds(appIds, locales);
        }
    }
}
