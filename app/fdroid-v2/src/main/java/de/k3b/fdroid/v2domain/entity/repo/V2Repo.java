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

// V2Repo.java

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.entity.common.IRepoCommon;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.v2domain.entity.V2IconUtil;

public class V2Repo implements IRepoCommon {

    /**
     * to avoid naming conflict IRepoCommon.getName()
     */
    @Nullable
    @SerializedName("name")
    private Map<String, String> nameMap;
    @Nullable
    @SerializedName("icon")
    private Map<String, V2File> iconMap;
    @Nullable
    private String address;
    @Nullable
    private String webBaseUrl;
    @Nullable
    @SerializedName("description")
    private Map<String, String> descriptionMap;
    @Nullable
    @SerializedName("mirrors")
    private List<V2Mirror> mirrorsList;
    private long timestamp;
    @Nullable
    private Map<String, V2AntiFeature> antiFeatures;
    @Nullable
    private Map<String, V2Category> categories;
    @Nullable
    private Map<String, V2ReleaseChannel> releaseChannels;

    @Nullable
    public Map<String, String> getNameMap() {
        return this.nameMap;
    }

    @Nullable
    public Map<String, V2File> getIconMap() {
        return this.iconMap;
    }

    @Nullable
    public String getAddress() {
        return this.address;
    }

    @Override
    public String getDescription() {
        return (descriptionMap == null) ? null : LanguageService.getCanonicalLocale(descriptionMap).get(LanguageService.FALLBACK_LOCALE);
    }

    @Nullable
    public String getWebBaseUrl() {
        return this.webBaseUrl;
    }

    @Nullable
    public Map<String, String> getDescriptionMap() {
        return this.descriptionMap;
    }

    @Nullable
    public List<V2Mirror> getMirrorsList() {
        return this.mirrorsList;
    }

    @Nullable
    @Override
    public String getName() {
        return (nameMap == null) ? null : LanguageService.getCanonicalLocale(nameMap).get(LanguageService.FALLBACK_LOCALE);
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    @Override
    public int getVersion() {
        return 0; // not in v2 :-(
    }

    @Override
    public int getMaxage() {
        return 0; // not in v2 :-(
    }

    @Override
    public String getIcon() {
        if (iconMap != null) {
            return V2IconUtil.getIconName(LanguageService.getCanonicalLocale(iconMap).get(LanguageService.FALLBACK_LOCALE));
        }
        return null;
    }

    @Nullable
    public Map<String, V2AntiFeature> getAntiFeatures() {
        return this.antiFeatures;
    }

    @Nullable
    public Map<String, V2Category> getCategories() {
        return this.categories;
    }

    @Nullable
    public Map<String, V2ReleaseChannel> getReleaseChannels() {
        return this.releaseChannels;
    }

    @Nullable
    public String toString() {
        return "V2Repo{name=" + this.nameMap + ", icon=" + this.iconMap + ", address=" + this.address + ", webBaseUrl=" + this.webBaseUrl + ", description=" + this.descriptionMap + ", mirrors=" + this.mirrorsList + ", timestamp=" + this.timestamp + ", antiFeatures=" + this.antiFeatures + ", categories=" + this.categories + ", releaseChannels=" + this.releaseChannels + "}";
    }
}
