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

package org.fdroid.model.v1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.fdroid.util.Formatter;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Generated;

/**
 * Data for an android app (read from FDroid-Catalog-v1-Json format).
 *
 * Generated with https://www.jsonschema2pojo.org/ from JSON example Data in Format Gson.
 */
@Generated("jsonschema2pojo")
public class App {

    @SerializedName("categories")
    @Expose
    private List<String> categories = null;
    @SerializedName("changelog")
    @Expose
    private String changelog;
    @SerializedName("suggestedVersionName")
    @Expose
    private String suggestedVersionName;
    @SerializedName("suggestedVersionCode")
    @Expose
    private String suggestedVersionCode;
    @SerializedName("issueTracker")
    @Expose
    private String issueTracker;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("sourceCode")
    @Expose
    private String sourceCode;
    @SerializedName("webSite")
    @Expose
    private String webSite;
    @SerializedName("added")
    @Expose
    private long added;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("lastUpdated")
    @Expose
    private long lastUpdated;

    // preserve insertion order
    private Map<String, Localized> localized = new TreeMap<>();

    @SerializedName("packageName")
    @Expose
    private String packageName;

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
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

    public Map<String, Localized> getLocalized() {
        return localized;
    }

    public void setLocalized(Map<String, Localized> localized) {
        this.localized = localized;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(App.class.getSimpleName()).append('[');
        Formatter.add(sb, "packageName", this.packageName);
        Formatter.add(sb, "categories",this.categories);
        Formatter.add(sb, "changelog", this.changelog);
        Formatter.add(sb, "suggestedVersionName", this.suggestedVersionName);
        Formatter.add(sb, "suggestedVersionCode", this.suggestedVersionCode);
        Formatter.add(sb, "issueTracker", this.issueTracker);
        Formatter.add(sb, "license", this.license);
        Formatter.add(sb, "sourceCode", this.sourceCode);
        Formatter.add(sb, "webSite", this.webSite);
        Formatter.addDate(sb,"added",this.added);
        Formatter.addDate(sb,"lastUpdated",this.lastUpdated);
        Formatter.add(sb, "icon", this.icon);
        if (localized != null) {
            sb.append("localized");
            sb.append("={");
            for (Map.Entry<String, Localized> l : localized.entrySet()) {
                sb.append(l.getKey()).append(":").append(l.getValue()).append(",");
            }
            if (sb.charAt((sb.length()- 1)) == ',') {
                sb.setCharAt((sb.length()- 1), '}');
            } else {
                sb.append('}');
            }
            sb.append(",");
        }

        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
