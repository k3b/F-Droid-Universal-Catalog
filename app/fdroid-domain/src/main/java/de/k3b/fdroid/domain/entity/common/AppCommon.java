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

package de.k3b.fdroid.domain.entity.common;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@javax.persistence.MappedSuperclass
/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@SuppressWarnings("unused")
public class AppCommon extends EntityCommon {
    @Schema(description = "Unique package name of the app.",
            example = "de.k3b.android.androFotoFinder")
    private String packageName;
    @Schema(description = "Url where you can see the change history of the app.",
            example = "https://github.com/k3b/APhotoManager/wiki/History")
    private String changelog;
    @Schema(description = "Most recent (stable) Version-Name of the app.",
            externalDocs = @ExternalDocumentation(url = WebReferences.GLOSSAR_URL + "Version"),
            example = "0.8.3.200315")
    private String suggestedVersionName;
    @Schema(description = "Most recent (stable) Version-Code of the app.",
            externalDocs = @ExternalDocumentation(url = WebReferences.GLOSSAR_URL + "Version"),
            example = "47")
    private String suggestedVersionCode;
    @Schema(description = "Url where you can see the open issues of the app.",
            example = "https://github.com/k3b/APhotoManager/issues")
    private String issueTracker;
    @Schema(description = "License of the app.",
            example = "GPL-3.0-only")
    private String license;
    @Schema(description = "Url where you can download the sourcecode of the app.",
            example = "https://github.com/k3b/APhotoManager")
    private String sourceCode;
    @Schema(description = "Url of the website of the app.",
            example = "https://github.com/k3b/APhotoManager/wiki")
    private String webSite;
    @Schema(description = "When the app was added to the Repo in internal numeric format.",
            externalDocs = @ExternalDocumentation(url = WebReferences.GLOSSAR_URL + "Repo"),
            example = "1438214400000")
    private long added;
    @Schema(description = "Relative url of the icon of the app.",
            example = "de.k3b.android.androFotoFinder.44.png")
    private String icon;
    @Schema(description = "When the app was last updated in the Repo in internal numeric format.",
            externalDocs = @ExternalDocumentation(url = WebReferences.GLOSSAR_URL + "Repo"),
            example = "1584144000000")
    private long lastUpdated;

    public static void copyCommon(AppCommon dest, AppCommon src) {
        dest.setIcon(ifNotNull(src.getIcon(), dest.getIcon()));
        dest.setPackageName(ifNotNull(src.getPackageName(), dest.getPackageName()));
        dest.setChangelog(ifNotNull(src.getChangelog(), dest.getChangelog()));
        dest.setSuggestedVersionName(ifNotNull(src.getSuggestedVersionName(), dest.getSuggestedVersionName()));
        dest.setSuggestedVersionCode(ifNotNull(src.getSuggestedVersionCode(), dest.getSuggestedVersionCode()));
        dest.setIssueTracker(ifNotNull(src.getIssueTracker(), dest.getIssueTracker()));
        dest.setLicense(ifNotNull(src.getLicense(), dest.getLicense()));
        dest.setSourceCode(ifNotNull(src.getSourceCode(),dest.getSourceCode()));
        dest.setWebSite(ifNotNull(src.getWebSite(),dest.getWebSite()));
        dest.setAdded(ifNotNull(src.getAdded(),dest.getAdded()));
        dest.setLastUpdated(ifNotNull(src.getLastUpdated(), dest.getLastUpdated()));
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public String getSuggestedVersionName() {
        return suggestedVersionName;
    }

    public void setSuggestedVersionName(String suggestedVersionName) {
        this.suggestedVersionName = suggestedVersionName;
    }

    public String getSuggestedVersionCode() {
        return suggestedVersionCode;
    }

    public void setSuggestedVersionCode(String suggestedVersionCode) {
        this.suggestedVersionCode = suggestedVersionCode;
    }

    public String getIssueTracker() {
        return issueTracker;
    }

    public void setIssueTracker(String issueTracker) {
        this.issueTracker = issueTracker;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    @Schema(description = "When the app was added to the Repo.",
            externalDocs = @ExternalDocumentation(url = WebReferences.GLOSSAR_URL + "Repo"),
            example = "2015-07-30")
    public String getAddedDate() {
        return EntityCommon.asDateString(added);
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Schema(description = "When the app was last updated in the Repo.",
            externalDocs = @ExternalDocumentation(url = WebReferences.GLOSSAR_URL + "Repo"),
            example = "2020-03-14")
    public String getLastUpdatedDate() {
        return asDateString(lastUpdated);
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    protected void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "packageName", this.packageName);
        toStringBuilder(sb, "changelog", this.changelog);
        toStringBuilder(sb, "suggestedVersionName", this.suggestedVersionName);
        toStringBuilder(sb, "suggestedVersionCode", this.suggestedVersionCode);
        toStringBuilder(sb, "issueTracker", this.issueTracker);
        toStringBuilder(sb, "license", this.license);
        toStringBuilder(sb, "sourceCode", this.sourceCode);
        toStringBuilder(sb, "webSite", this.webSite);
        toDateStringBuilder(sb, "added", this.added);
        toDateStringBuilder(sb, "lastUpdated", this.lastUpdated);
        toStringBuilder(sb, "icon", this.icon);
    }

}
