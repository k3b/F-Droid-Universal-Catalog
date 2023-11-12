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

package de.k3b.fdroid.v2domain.entity.packagev2;

// MetadataV2.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.entity.common.IAppCommon;
import de.k3b.fdroid.v2domain.entity.repo.FileV2;

public final class MetadataV2 implements IAppCommon {
    @Nullable
    private final Map<String, String> name;
    @Nullable
    private final Map<String, String> summary;
    @Nullable
    private final Map<String, String> description;
    private final long added;
    private final long lastUpdated;
    @Nullable
    private final String webSite;
    @Nullable
    private final String changelog;
    @Nullable
    private final String license;
    @Nullable
    private final String sourceCode;
    @Nullable
    private final String issueTracker;
    @Nullable
    private final String translation;
    @Nullable
    private final String preferredSigner;
    @NotNull
    private final List<String> categories;
    @Nullable
    private final String authorName;
    @Nullable
    private final String authorEmail;
    @Nullable
    private final String authorWebSite;
    @Nullable
    private final String authorPhone;
    @NotNull
    private final List<String> donate;
    @Nullable
    private final String liberapayID;
    @Nullable
    private final String liberapay;
    @Nullable
    private final String openCollective;
    @Nullable
    private final String bitcoin;
    @Nullable
    private final String litecoin;
    @Nullable
    private final String flattrID;
    @Nullable
    private final Map<String, FileV2> icon;
    @Nullable
    private final Map<String, FileV2> featureGraphic;
    @Nullable
    private final Map<String, FileV2> promoGraphic;
    @Nullable
    private final Map<String, FileV2> tvBanner;
    @Nullable
    private final Map<String, String> video;
    @Nullable
    private final Screenshots screenshots;

    public MetadataV2(@Nullable Map<String, String> name, @Nullable Map<String, String> summary, @Nullable Map<String, String> description,
                      long added, long lastUpdated, @Nullable String webSite, @Nullable String changelog, @Nullable String license,
                      @Nullable String sourceCode, @Nullable String issueTracker, @Nullable String translation,
                      @Nullable String preferredSigner, @NotNull List<String> categories, @Nullable String authorName,
                      @Nullable String authorEmail, @Nullable String authorWebSite, @Nullable String authorPhone,
                      @NotNull List<String> donate, @Nullable String liberapayID, @Nullable String liberapay,
                      @Nullable String openCollective, @Nullable String bitcoin, @Nullable String litecoin,
                      @Nullable String flattrID, @Nullable Map<String, FileV2> icon, @Nullable
                      Map<String, FileV2> featureGraphic, @Nullable Map<String, FileV2> promoGraphic, @Nullable Map<String, FileV2> tvBanner,
                      @Nullable Map<String, String> video, @Nullable Screenshots screenshots) {
        this.name = name;
        this.summary = summary;
        this.description = description;
        this.added = added;
        this.lastUpdated = lastUpdated;
        this.webSite = webSite;
        this.changelog = changelog;
        this.license = license;
        this.sourceCode = sourceCode;
        this.issueTracker = issueTracker;
        this.translation = translation;
        this.preferredSigner = preferredSigner;
        this.categories = categories;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.authorWebSite = authorWebSite;
        this.authorPhone = authorPhone;
        this.donate = donate;
        this.liberapayID = liberapayID;
        this.liberapay = liberapay;
        this.openCollective = openCollective;
        this.bitcoin = bitcoin;
        this.litecoin = litecoin;
        this.flattrID = flattrID;
        this.icon = icon;
        this.featureGraphic = featureGraphic;
        this.promoGraphic = promoGraphic;
        this.tvBanner = tvBanner;
        this.video = video;
        this.screenshots = screenshots;
    }

    public static String getIconName(FileV2 icon) {
        String iconName = (icon == null) ? null : icon.getName();
        if (iconName != null) {
            //  && iconName.startsWith("/"))
            int lastSeperator = iconName.lastIndexOf("/");
            iconName = iconName.substring(lastSeperator + 1);
        }
        return iconName;
    }

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

    @NotNull
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

    @NotNull
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
    public Map<String, FileV2> getIcon() {
        return this.icon;
    }

    @Nullable
    public Map<String, FileV2> getFeatureGraphic() {
        return this.featureGraphic;
    }

    @Nullable
    public Map<String, FileV2> getPromoGraphic() {
        return this.promoGraphic;
    }

    @Nullable
    public Map<String, FileV2> getTvBanner() {
        return this.tvBanner;
    }

    @Nullable
    public Map<String, String> getVideo() {
        return this.video;
    }

    @Nullable
    public Screenshots getScreenshots() {
        return this.screenshots;
    }

    @NotNull
    public String toString() {
        return "MetadataV2{name=" + this.name + ", summary=" + this.summary + ", description=" + this.description + ", added=" + this.added + ", lastUpdated=" + this.lastUpdated + ", webSite=" + this.webSite + ", changelog=" + this.changelog + ", license=" + this.license + ", sourceCode=" + this.sourceCode + ", issueTracker=" + this.issueTracker + ", translation=" + this.translation + ", preferredSigner=" + this.preferredSigner + ", categories=" + this.categories + ", authorName=" + this.authorName + ", authorEmail=" + this.authorEmail + ", authorWebSite=" + this.authorWebSite + ", authorPhone=" + this.authorPhone + ", donate=" + this.donate + ", liberapayID=" + this.liberapayID + ", liberapay=" + this.liberapay + ", openCollective=" + this.openCollective + ", bitcoin=" + this.bitcoin + ", litecoin=" + this.litecoin + ", flattrID=" + this.flattrID + ", icon=" + this.icon + ", featureGraphic=" + this.featureGraphic + ", promoGraphic=" + this.promoGraphic + ", tvBanner=" + this.tvBanner + ", video=" + this.video + ", screenshots=" + this.screenshots + "}";
    }
}
