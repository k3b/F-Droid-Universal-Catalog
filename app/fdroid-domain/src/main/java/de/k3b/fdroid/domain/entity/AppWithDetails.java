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
package de.k3b.fdroid.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.entity.common.ExtDoc;
import de.k3b.fdroid.domain.interfaces.AggregateRoot;
import de.k3b.fdroid.domain.interfaces.AppDetail;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DDD {@link AggregateRoot} for {@link App}
 */
@JsonInclude(Include.NON_NULL)
@SuppressWarnings({"unchecked", "unsafe", "unused"})
@ExternalDocumentation(description = "Information about an Android [App] that is available in a [Repo] or [Mirror]",
        url = ExtDoc.GLOSSAR_URL + "App")
public class AppWithDetails extends EntityCommon implements AppDetail, AggregateRoot {
    @NotNull
    private final App app;
    private final List<Localized> localizedList = new ArrayList<>();
    private final List<Version> versionList = new ArrayList<>();
    private final List<LinkedDatabaseEntity<AppCategory, Category>> categoryList = new ArrayList<>();

    // does not work with key=string
    //private final List<LinkedDatabaseEntity<Localized, Locale>> localeList = new ArrayList<>();

    public AppWithDetails(@NotNull App app) {
        this.app = app;
    }

    @Override
    public int getId() {
        return getApp().getId();
    }

    @Override
    public int getAppId() {
        return getApp().getAppId();
    }

    @NotNull
    public App getApp() {
        return app;
    }

    public List<Localized> getLocalizedList() {
        return localizedList;
    }

    public List<Version> getVersionList() {
        return versionList;
    }

    @androidx.room.Ignore
    @javax.persistence.Transient
    @Schema(description = "Screenshot(s) from Android-Phone.",
            example = "de.k3b.android.androFotoFinder/en-US/phoneScreenshots/1-Gallery.png")
    public String[] getPhoneScreenshotUrls() {
        Localized[] localizedArray = getLocalizedSorted();
        if (localizedArray != null) {
            for (Localized l : localizedArray) {
                String[] urls = l.getPhoneScreenshotArray();
                String dir = l.getPhoneScreenshotDir();
                if (urls != null && urls.length > 0 && dir != null) {
                    for (int i = 0; i < urls.length; i++) {
                        urls[i] = dir + urls[i];
                    }
                    return urls;
                }
            }
        }
        return null;
    }

    @JsonIgnore
    @androidx.room.Ignore
    @javax.persistence.Transient
    public Localized[] getLocalizedSorted() {
        if (getLocalizedList() == null) return null;

        Localized[] localizedArray = getLocalizedList().toArray(new Localized[0]);

        AppSearchParameter appSearchParameter = getApp().getAppSearchParameter();
        String[] locales = (appSearchParameter == null) ? null : appSearchParameter.locales;

        if (locales != null && locales.length > 1) {
            LocalizedLocalesSorter<Localized> sorter = new LocalizedLocalesSorter<>(locales);
            Arrays.sort(localizedArray, sorter);
        }
        return localizedArray;
    }

    public <T extends AppDetail> List<T> getList(Class<?> classz) {
        if (classz.equals(Localized.class)) return (List<T>) localizedList;
        if (classz.equals(Version.class)) return (List<T>) versionList;
        if (classz.equals(Category.class)) return (List<T>) categoryList;

        // does not work with key=string
        // if (classz.equals(Locale.class)) return (List<T>) localeList;
        throw new IllegalArgumentException("" + classz.getName());
    }

    @Override
    protected void toStringBuilder(@NotNull StringBuilder sb) {
        toStringBuilder(sb, "app",
                app.getPackageName() + "(A:" + app.getId() + ")");
        super.toStringBuilder(sb);
        toStringBuilder(sb, "localizedList", this.localizedList.size());
        toStringBuilder(sb, "versionList", this.versionList.size());
        toStringBuilder(sb, "categoryList", this.categoryList.size());
    }

}
