
package org.fdroid.v1Ex.model.deprecated;

import java.util.List;
import javax.annotation.Generated;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("localized")
    @Expose
    private List<Localized> localized = null;

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
