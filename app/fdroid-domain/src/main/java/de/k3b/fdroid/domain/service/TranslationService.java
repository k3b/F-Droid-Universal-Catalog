/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of org.fdroid project.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either translation 3 of the License, or
 * (at your option) any later translation.
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

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import de.k3b.fdroid.domain.entity.Translation;
import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.domain.util.StringUtil;

public class TranslationService {
    public static final String TYP_REPOSITORY_NAME = "RN";
    public static final String TYP_REPOSITORY_DESCRIPTION = "RD";
    public static final String TYP_REPOSITORY_ICON = "RI";
    public static final String TYP_CATEGORY_NAME = "CN";
    public static final String TYP_CATEGORY_DESCRIPTION = "CD";
    public static final String TYP_CATEGORY_ICON = "CI";
    public static final String TYP_AntiFeature_NAME = "AN";
    public static final String TYP_AntiFeature_DESCRIPTION = "AD";
    public static final String TYP_AntiFeature_ICON = "AI";
    private final String typ;
    private final TranslationRepository translationRepository;

    private final Map<Integer, Map<String, Translation>> all = new TreeMap<>();

    public TranslationService(@NotNull String typ, @Nullable TranslationRepository translationRepository) {
        this.typ = typ;
        this.translationRepository = translationRepository;
    }

    public void init() {
        loadAll();
    }

    private static void put(Map<String, Translation> result, Translation t) {
        result.put(t.getLocaleId(), t);
    }

    public TranslationService loadAll() {
        List<Translation> list = (translationRepository == null)
                ? new ArrayList<>()
                : translationRepository.findByTyp(typ);
        load(list);
        return this;
    }

    public TranslationService loadById(int id) {
        List<Translation> list = (translationRepository == null)
                ? new ArrayList<>()
                : translationRepository.findByTypAndId(typ, id);
        load(list);
        return this;
    }

    public Translation getTranslation(int id, String localeId) {
        Map<String, Translation> localeMap = getLocaleMap(id, false);
        if (localeMap != null) {
            return localeMap.get(localeId);
        }
        return null;
    }

    public Translation save(int id, String localeId, String localizedText) {
        Translation result = getTranslation(id, localeId);
        boolean mustDelete = StringUtil.isEmpty(localizedText);
        boolean mustInsert = false;

        if (!mustDelete && result == null) {
            mustInsert = true;
            result = new Translation(typ, id, localeId);
            put(result);
        }

        if (result != null) {
            result.setLocalizedText(localizedText);
            if (mustDelete) {
                getLocaleMap(result, true).remove(result.getLocaleId());
            }
            if (translationRepository != null) {
                if (mustDelete) {
                    translationRepository.delete(result);
                } else if (mustInsert) {
                    translationRepository.insert(result);
                } else {
                    translationRepository.update(result);
                }
            }
        }
        return result;
    }

    public String getLocalizedText(int id, String localeId) {
        Translation result = getTranslation(id, localeId);
        return (result == null) ? null : result.getLocalizedText();
    }

    private void load(@Nullable List<Translation> list) {
        if (list != null) {
            for (Translation t : list) {
                put(t);
            }
        }
    }

    protected void put(Translation t) {
        put(getLocaleMap(t, true), t);
    }

    @NonNull
    private Map<String, Translation> getLocaleMap(@NotNull Translation t, boolean createIfNotFound) {
        return getLocaleMap(t.getId(), createIfNotFound);
    }

    @Nullable
    private Map<String, Translation> getLocaleMap(Integer id, boolean createIfNotFound) {
        Map<String, Translation> idMap = all.get(id);
        if (idMap == null && createIfNotFound) {
            idMap = new TreeMap<>();
            all.put(id, idMap);
        }
        return idMap;
    }

    public void update(int id, @Nullable String name, Map<String, String> localeMap) {
        if (localeMap != null) {
            for (Map.Entry<String, String> entry : localeMap.entrySet()) {
                String translation = entry.getValue();
                if (translation != null && (name == null || name.compareTo(translation) != 0)) {
                    save(id, entry.getKey(), translation);
                }
            }
        }
    }

    public void updateIcon(int categoryId, Map<String, String> iconMap) {
        String fallbackIcon = iconMap.get(LanguageService.FALLBACK_LOCALE);

        // fallbackIcon is added for FALLBACK_LOCALE but not for the other locales
        save(categoryId, LanguageService.FALLBACK_LOCALE, fallbackIcon);
        update(categoryId, fallbackIcon, iconMap);
    }

    @Override
    public String toString() {
        return "TranslationService{" +
                "'" + typ +
                "' : " + all +
                '}';
    }
}
