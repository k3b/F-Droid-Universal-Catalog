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

package org.fdroid.v1.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(App.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("categories");
        sb.append('=');
        sb.append(((this.categories == null)?"<null>":this.categories));
        sb.append(',');
        sb.append("changelog");
        sb.append('=');
        sb.append(((this.changelog == null)?"<null>":this.changelog));
        sb.append(',');
        sb.append("suggestedVersionName");
        sb.append('=');
        sb.append(((this.suggestedVersionName == null)?"<null>":this.suggestedVersionName));
        sb.append(',');
        sb.append("suggestedVersionCode");
        sb.append('=');
        sb.append(((this.suggestedVersionCode == null)?"<null>":this.suggestedVersionCode));
        sb.append(',');
        sb.append("issueTracker");
        sb.append('=');
        sb.append(((this.issueTracker == null)?"<null>":this.issueTracker));
        sb.append(',');
        sb.append("license");
        sb.append('=');
        sb.append(((this.license == null)?"<null>":this.license));
        sb.append(',');
        sb.append("sourceCode");
        sb.append('=');
        sb.append(((this.sourceCode == null)?"<null>":this.sourceCode));
        sb.append(',');
        sb.append("webSite");
        sb.append('=');
        sb.append(((this.webSite == null)?"<null>":this.webSite));
        sb.append(',');
        sb.append("added");
        sb.append('=');
        sb.append(this.added);
        sb.append(',');
        sb.append("icon");
        sb.append('=');
        sb.append(((this.icon == null)?"<null>":this.icon));
        sb.append(',');
        sb.append("lastUpdated");
        sb.append('=');
        sb.append(this.lastUpdated);
        sb.append(',');
        sb.append("packageName");
        sb.append('=');
        sb.append(((this.packageName == null)?"<null>":this.packageName));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
