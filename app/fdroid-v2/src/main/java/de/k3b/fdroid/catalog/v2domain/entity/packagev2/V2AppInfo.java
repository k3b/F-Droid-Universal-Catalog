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

package de.k3b.fdroid.catalog.v2domain.entity.packagev2;

// V2AppInfo.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import de.k3b.fdroid.catalog.v2domain.entity.repo.V2File;
import de.k3b.fdroid.domain.entity.common.IAppCommon;

public class V2AppInfo implements IAppCommon {
    @Nullable
    private Map<String, String> name;
    @Nullable
    private Map<String, String> summary;
    @Nullable
    private Map<String, String> description;
    private long added;
    private long lastUpdated;
    @Nullable
    private String webSite;
    @Nullable
    private String changelog;
    @Nullable
    private String license;
    @Nullable
    private String sourceCode;
    @Nullable
    private String issueTracker;
    @Nullable
    private String translation;
    @Nullable
    private String preferredSigner;
    @Nullable
    private List<String> categories;
    @Nullable
    private String authorName;
    @Nullable
    private String authorEmail;
    @Nullable
    private String authorWebSite;
    @Nullable
    private String authorPhone;
    @Nullable
    private List<String> donate;
    @Nullable
    private String liberapayID;
    @Nullable
    private String liberapay;
    @Nullable
    private String openCollective;
    @Nullable
    private String bitcoin;
    @Nullable
    private String litecoin;
    @Nullable
    private String flattrID;
    @Nullable
    private Map<String, V2File> icon;
    @Nullable
    private Map<String, V2File> featureGraphic;
    @Nullable
    private Map<String, V2File> promoGraphic;
    @Nullable
    private Map<String, V2File> tvBanner;
    @Nullable
    private Map<String, String> video;
    @Nullable
    private V2Screenshots screenshots;

    @Nullable
    public Map<String, String> getName() {
        return this.name;
    }

    @Nullable
    public Map<String, String> getSummary() {
        return this.summary;
    }

    @Nullable
    public Map<String, String> getDescription() {
        return this.description;
    }

    public long getAdded() {
        return this.added;
    }

    public long getLastUpdated() {
        return this.lastUpdated;
    }

    @Nullable
    public String getWebSite() {
        return this.webSite;
    }

    @Nullable
    public String getChangelog() {
        return this.changelog;
    }

    @Nullable
    public String getLicense() {
        return this.license;
    }

    @Nullable
    public String getSourceCode() {
        return this.sourceCode;
    }

    @Nullable
    public String getIssueTracker() {
        return this.issueTracker;
    }

    @Nullable
    public String getTranslation() {
        return this.translation;
    }

    @Nullable
    public String getPreferredSigner() {
        return this.preferredSigner;
    }

    @Nullable
    public List<String> getCategories() {
        return this.categories;
    }

    @Nullable
    public String getAuthorName() {
        return this.authorName;
    }

    @Nullable
    public String getAuthorEmail() {
        return this.authorEmail;
    }

    @Nullable
    public String getAuthorWebSite() {
        return this.authorWebSite;
    }

    @Nullable
    public String getAuthorPhone() {
        return this.authorPhone;
    }

    @Nullable
    public List<String> getDonate() {
        return this.donate;
    }

    @Nullable
    public String getLiberapayID() {
        return this.liberapayID;
    }

    @Nullable
    public String getLiberapay() {
        return this.liberapay;
    }

    @Nullable
    public String getOpenCollective() {
        return this.openCollective;
    }

    @Nullable
    public String getBitcoin() {
        return this.bitcoin;
    }

    @Nullable
    public String getLitecoin() {
        return this.litecoin;
    }

    @Nullable
    public String getFlattrID() {
        return this.flattrID;
    }

    @Nullable
    public Map<String, V2File> getIcon() {
        return this.icon;
    }

    @Nullable
    public Map<String, V2File> getFeatureGraphic() {
        return this.featureGraphic;
    }

    @Nullable
    public Map<String, V2File> getPromoGraphic() {
        return this.promoGraphic;
    }

    @Nullable
    public Map<String, V2File> getTvBanner() {
        return this.tvBanner;
    }

    @Nullable
    public Map<String, String> getVideo() {
        return this.video;
    }

    @Nullable
    public V2Screenshots getScreenshots() {
        return this.screenshots;
    }

    @NotNull
    public String toString() {
        return "V2AppInfo{name=" + this.name + ", summary=" + this.summary + ", description=" + this.description + ", added=" + this.added + ", lastUpdated=" + this.lastUpdated + ", webSite=" + this.webSite + ", changelog=" + this.changelog + ", license=" + this.license + ", sourceCode=" + this.sourceCode + ", issueTracker=" + this.issueTracker + ", translation=" + this.translation + ", preferredSigner=" + this.preferredSigner + ", categories=" + this.categories + ", authorName=" + this.authorName + ", authorEmail=" + this.authorEmail + ", authorWebSite=" + this.authorWebSite + ", authorPhone=" + this.authorPhone + ", donate=" + this.donate + ", liberapayID=" + this.liberapayID + ", liberapay=" + this.liberapay + ", openCollective=" + this.openCollective + ", bitcoin=" + this.bitcoin + ", litecoin=" + this.litecoin + ", flattrID=" + this.flattrID + ", icon=" + this.icon + ", featureGraphic=" + this.featureGraphic + ", promoGraphic=" + this.promoGraphic + ", tvBanner=" + this.tvBanner + ", video=" + this.video + ", screenshots=" + this.screenshots + "}";
    }
}
