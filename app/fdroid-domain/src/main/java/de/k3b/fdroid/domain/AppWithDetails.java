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
package de.k3b.fdroid.domain;

import java.util.ArrayList;
import java.util.List;

import de.k3b.fdroid.domain.common.PojoCommon;
import de.k3b.fdroid.domain.interfaces.AppDetail;

/**
 * DDD Aggregate-Root for {@link App}
 */
@SuppressWarnings({"unchecked", "unsafe"})
public class AppWithDetails extends PojoCommon implements AppDetail {
    private final App app;
    private final List<Localized> localizedList = new ArrayList<>();
    private final List<Version> versionList = new ArrayList<>();
    private final List<LinkedItem<AppCategory, Category>> categoryList = new ArrayList<>();
    private final List<LinkedItem<Localized, Locale>> localeList = new ArrayList<>();

    public AppWithDetails(App app) {
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

    public App getApp() {
        return app;
    }

    public List<Localized> getLocalizedList() {
        return localizedList;
    }

    public List<Version> getVersionList() {
        return versionList;
    }

    public <T extends AppDetail> List<T> getList(Class<?> classz) {
        if (classz.equals(Localized.class)) return (List<T>) localizedList;
        if (classz.equals(Version.class)) return (List<T>) versionList;
        if (classz.equals(Category.class)) return (List<T>) categoryList;
        if (classz.equals(Locale.class)) return (List<T>) localeList;
        throw new IllegalArgumentException("" + classz.getName());
    }

    @Override
    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "app",
                app.getPackageName() + "(A:" + app.getId() + ")");
        super.toStringBuilder(sb);
        toStringBuilder(sb, "localizedList", this.localizedList.size());
        toStringBuilder(sb, "versionList", this.versionList.size());
        toStringBuilder(sb, "categoryList", this.categoryList.size());
    }

}
