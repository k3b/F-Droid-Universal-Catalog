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
package de.k3b.fdroid.v2domain.entity.repo;

// RepoV2.java

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class RepoV2 {
    @NotNull
    private final Map<String, String> name;
    @NotNull
    private final Map<String, FileV2> icon;
    @NotNull
    private final String address;
    @Nullable
    private final String webBaseUrl;
    @NotNull
    private final Map<String, String> description;
    @NotNull
    private final List<MirrorV2> mirrors;
    private final long timestamp;
    @NotNull
    private final Map<String, AntiFeatureV2> antiFeatures;
    @NotNull
    private final Map<String, CategoryV2> categories;
    @NotNull
    private final Map<String, ReleaseChannelV2> releaseChannels;

    public RepoV2(@NotNull Map<String, String> name, @NotNull Map<String, FileV2> icon, @NotNull String address, @Nullable String webBaseUrl,
                  @NotNull Map<String, String> description, @NotNull List<MirrorV2> mirrors, long timestamp,
                  @NotNull Map<String, AntiFeatureV2> antiFeatures, @NotNull Map<String, CategoryV2> categories, @NotNull Map<String, ReleaseChannelV2> releaseChannels) {
        this.name = name;
        this.icon = icon;
        this.address = address;
        this.webBaseUrl = webBaseUrl;
        this.description = description;
        this.mirrors = mirrors;
        this.timestamp = timestamp;
        this.antiFeatures = antiFeatures;
        this.categories = categories;
        this.releaseChannels = releaseChannels;
    }

    @NotNull
    public final Map<String, String> getName() {
        return this.name;
    }

    @NotNull
    public final Map<String, FileV2> getIcon() {
        return this.icon;
    }

    @NotNull
    public final String getAddress() {
        return this.address;
    }

    @Nullable
    public final String getWebBaseUrl() {
        return this.webBaseUrl;
    }

    @NotNull
    public final Map<String, String> getDescription() {
        return this.description;
    }

    @NotNull
    public final List<MirrorV2> getMirrors() {
        return this.mirrors;
    }

    public final long getTimestamp() {
        return this.timestamp;
    }

    @NotNull
    public final Map<String, AntiFeatureV2> getAntiFeatures() {
        return this.antiFeatures;
    }

    @NotNull
    public final Map<String, CategoryV2> getCategories() {
        return this.categories;
    }

    @NotNull
    public final Map<String, ReleaseChannelV2> getReleaseChannels() {
        return this.releaseChannels;
    }

    @NotNull
    public String toString() {
        return "RepoV2{name=" + this.name + ", icon=" + this.icon + ", address=" + this.address + ", webBaseUrl=" + this.webBaseUrl + ", description=" + this.description + ", mirrors=" + this.mirrors + ", timestamp=" + this.timestamp + ", antiFeatures=" + this.antiFeatures + ", categories=" + this.categories + ", releaseChannels=" + this.releaseChannels + "}";
    }
}
