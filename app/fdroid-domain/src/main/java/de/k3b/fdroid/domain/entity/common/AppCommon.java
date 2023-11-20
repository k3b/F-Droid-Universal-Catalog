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

package de.k3b.fdroid.domain.entity.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@javax.persistence.MappedSuperclass
/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@SuppressWarnings("unused")
public class AppCommon extends EntityCommon implements IAppCommon {
    @Schema(description = "Unique package name of the [App].",
            example = "de.k3b.android.androFotoFinder")
    private String packageName;
    @Schema(description = "Url where you can see the change history of the [App].",
            example = "https://github.com/k3b/APhotoManager/wiki/History")
    private String changelog;
    @Schema(description = "Most recent (stable) [Version]-Name of the [App].",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Version"),
            example = "0.8.3.200315")
    private String suggestedVersionName;
    @Schema(description = "Most recent (stable) Version-Code of the [App].",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Version"),
            example = "47")
    private String suggestedVersionCode;
    @Schema(description = "Url where you can see the open issues of the [App].",
            example = "https://github.com/k3b/APhotoManager/issues")
    private String issueTracker;
    @Schema(description = "License of the [App].",
            example = "GPL-3.0-only")
    private String license;
    @Schema(description = "Url where you can download the sourcecode of the [App].",
            example = "https://github.com/k3b/APhotoManager")
    private String sourceCode;
    @Schema(description = "Url of the website of the [App].",
            example = "https://github.com/k3b/APhotoManager/wiki")
    private String webSite;
    @Schema(description = "When the app was added to the [Repo] in internal numeric format.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo"),
            example = "1438214400000")
    private long added;
    @Schema(description = "Relative url of the [App]-icon.",
            example = "de.k3b.android.androFotoFinder.44.png")
    private String icon;
    @Schema(description = "When the [App] was last updated in the [Repo] in internal numeric format.",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo"),
            example = "1584144000000")
    private long lastUpdated;

    public AppCommon() {
        super();
    }

    @androidx.room.Ignore
    public AppCommon(@Nullable IAppCommon src) {
        super();
        copyCommon(this, src, null);
    }

    public static void copyCommon(@NotNull AppCommon dest, @Nullable IAppCommon src, @Nullable AppCommon src2) {
        if (src != null) {
            dest.setChangelog(ifNotNull(src.getChangelog(), dest.getChangelog()));
            dest.setIssueTracker(ifNotNull(src.getIssueTracker(), dest.getIssueTracker()));
            dest.setLicense(ifNotNull(src.getLicense(), dest.getLicense()));
            dest.setSourceCode(ifNotNull(src.getSourceCode(), dest.getSourceCode()));
            dest.setWebSite(ifNotNull(src.getWebSite(), dest.getWebSite()));
            dest.setAdded(ifNotNull(src.getAdded(), dest.getAdded()));
            dest.setLastUpdated(ifNotNull(src.getLastUpdated(), dest.getLastUpdated()));

            dest.setIcon(ifNotNull(src.getIcon(), dest.getIcon()));
            dest.setPackageName(ifNotNull(src.getPackageName(), dest.getPackageName()));
            dest.setSuggestedVersionName(ifNotNull(src.getSuggestedVersionName(), dest.getSuggestedVersionName()));
            dest.setSuggestedVersionCode(ifNotNull(src.getSuggestedVersionCode(), dest.getSuggestedVersionCode()));
        }

        if (src2 != null) {
            dest.setIcon(ifNotNull(src2.getIcon(), dest.getIcon()));
            dest.setPackageName(ifNotNull(src2.getPackageName(), dest.getPackageName()));
            dest.setSuggestedVersionName(ifNotNull(src2.getSuggestedVersionName(), dest.getSuggestedVersionName()));
            dest.setSuggestedVersionCode(ifNotNull(src2.getSuggestedVersionCode(), dest.getSuggestedVersionCode()));
        }
    }

    @Override
    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = maxlen(changelog);
    }

    public static void toStringBuilder(
            @NotNull StringBuilder sb, @Nullable IAppCommon content) {
        if (content != null) {
            toStringBuilder(sb, "packageName", content.getPackageName());
            toStringBuilder(sb, "changelog", content.getChangelog());
            toStringBuilder(sb, "suggestedVersionName", content.getSuggestedVersionName());
            toStringBuilder(sb, "suggestedVersionCode", content.getSuggestedVersionCode());
            toStringBuilder(sb, "issueTracker", content.getIssueTracker());
            toStringBuilder(sb, "license", content.getLicense());
            toStringBuilder(sb, "sourceCode", content.getSourceCode());
            toStringBuilder(sb, "webSite", content.getWebSite());
            toDateStringBuilder(sb, "added", content.getAdded());
            toDateStringBuilder(sb, "lastUpdated", content.getLastUpdated());
            toStringBuilder(sb, "icon", content.getIcon());
        }
    }

    public void setSuggestedVersionName(String suggestedVersionName) {
        this.suggestedVersionName = maxlen(suggestedVersionName);
    }

    @Override
    public String getSuggestedVersionName() {
        return suggestedVersionName;
    }

    public void setSuggestedVersionCode(String suggestedVersionCode) {
        this.suggestedVersionCode = maxlen(suggestedVersionCode);
    }

    @Override
    public String getIssueTracker() {
        return issueTracker;
    }

    public void setIssueTracker(String issueTracker) {
        this.issueTracker = maxlen(issueTracker);
    }

    @Override
    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = maxlen(license);
    }

    @Override
    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = maxlen(sourceCode);
    }

    @Override
    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = maxlen(webSite);
    }

    @Override
    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    @Override
    public String getSuggestedVersionCode() {
        return suggestedVersionCode;
    }

    public void setIcon(String icon) {
        this.icon = maxlen(icon);
    }

    @Override
    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String getIcon() {
        return icon;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = maxlen(packageName);
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, this);
    }

}
