package de.k3b.fdroid.v2domain.entity.packagev2;

// MetadataV2.java

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.k3b.fdroid.v2domain.entity.repo.FileV2;

public final class MetadataV2 {
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

    @Nullable
    public final Map<String, String> getName() {
        return this.name;
    }

    @Nullable
    public final Map<String, String> getSummary() {
        return this.summary;
    }

    @Nullable
    public final Map<String, String> getDescription() {
        return this.description;
    }

    public final long getAdded() {
        return this.added;
    }

    public final long getLastUpdated() {
        return this.lastUpdated;
    }

    @Nullable
    public final String getWebSite() {
        return this.webSite;
    }

    @Nullable
    public final String getChangelog() {
        return this.changelog;
    }

    @Nullable
    public final String getLicense() {
        return this.license;
    }

    @Nullable
    public final String getSourceCode() {
        return this.sourceCode;
    }

    @Nullable
    public final String getIssueTracker() {
        return this.issueTracker;
    }

    @Nullable
    public final String getTranslation() {
        return this.translation;
    }

    @Nullable
    public final String getPreferredSigner() {
        return this.preferredSigner;
    }

    @NotNull
    public final List<String> getCategories() {
        return this.categories;
    }

    @Nullable
    public final String getAuthorName() {
        return this.authorName;
    }

    @Nullable
    public final String getAuthorEmail() {
        return this.authorEmail;
    }

    @Nullable
    public final String getAuthorWebSite() {
        return this.authorWebSite;
    }

    @Nullable
    public final String getAuthorPhone() {
        return this.authorPhone;
    }

    @NotNull
    public final List<String> getDonate() {
        return this.donate;
    }

    @Nullable
    public final String getLiberapayID() {
        return this.liberapayID;
    }

    @Nullable
    public final String getLiberapay() {
        return this.liberapay;
    }

    @Nullable
    public final String getOpenCollective() {
        return this.openCollective;
    }

    @Nullable
    public final String getBitcoin() {
        return this.bitcoin;
    }

    @Nullable
    public final String getLitecoin() {
        return this.litecoin;
    }

    @Nullable
    public final String getFlattrID() {
        return this.flattrID;
    }

    @Nullable
    public final Map<String, FileV2> getIcon() {
        return this.icon;
    }

    @Nullable
    public final Map<String, FileV2> getFeatureGraphic() {
        return this.featureGraphic;
    }

    @Nullable
    public final Map<String, FileV2> getPromoGraphic() {
        return this.promoGraphic;
    }

    @Nullable
    public final Map<String, FileV2> getTvBanner() {
        return this.tvBanner;
    }

    @Nullable
    public final Map<String, String> getVideo() {
        return this.video;
    }

    @Nullable
    public final Screenshots getScreenshots() {
        return this.screenshots;
    }

    @NotNull
    public String toString() {
        return "MetadataV2{name=" + this.name + ", summary=" + this.summary + ", description=" + this.description + ", added=" + this.added + ", lastUpdated=" + this.lastUpdated + ", webSite=" + this.webSite + ", changelog=" + this.changelog + ", license=" + this.license + ", sourceCode=" + this.sourceCode + ", issueTracker=" + this.issueTracker + ", translation=" + this.translation + ", preferredSigner=" + this.preferredSigner + ", categories=" + this.categories + ", authorName=" + this.authorName + ", authorEmail=" + this.authorEmail + ", authorWebSite=" + this.authorWebSite + ", authorPhone=" + this.authorPhone + ", donate=" + this.donate + ", liberapayID=" + this.liberapayID + ", liberapay=" + this.liberapay + ", openCollective=" + this.openCollective + ", bitcoin=" + this.bitcoin + ", litecoin=" + this.litecoin + ", flattrID=" + this.flattrID + ", icon=" + this.icon + ", featureGraphic=" + this.featureGraphic + ", promoGraphic=" + this.promoGraphic + ", tvBanner=" + this.tvBanner + ", video=" + this.video + ", screenshots=" + this.screenshots + "}";
    }
}
