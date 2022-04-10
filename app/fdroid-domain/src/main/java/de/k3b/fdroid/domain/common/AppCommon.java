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

package de.k3b.fdroid.domain.common;

@javax.persistence.MappedSuperclass
/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
public class AppCommon extends PojoCommon {
    private String packageName;
    private String changelog;
    private String suggestedVersionName;
    private String suggestedVersionCode;
    private String issueTracker;
    private String license;
    private String sourceCode;
    private String webSite;
    private long added;
    private String icon;
    private long lastUpdated;

    public static void copyCommon(AppCommon dest, AppCommon src) {
        dest.setIcon(src.getIcon());
        dest.setPackageName(src.getPackageName());
        dest.setChangelog(src.getChangelog());
        dest.setSuggestedVersionName(src.getSuggestedVersionName());
        dest.setSuggestedVersionCode(src.getSuggestedVersionCode());
        dest.setIssueTracker(src.getIssueTracker());
        dest.setLicense(src.getLicense());
        dest.setSourceCode(src.getSourceCode());
        dest.setWebSite(src.getWebSite());
        dest.setAdded(src.getAdded());
        dest.setLastUpdated(src.getLastUpdated());
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
