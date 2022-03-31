package org.fdroid.model;

import org.fdroid.util.Formatter;

public class RepoCommon extends PojoCommon {
    private String name;
    private long timestamp;
    private long version;
    private long maxage;
    private String icon;
    private String address;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public long getMaxage() {
        return maxage;
    }

    public void setMaxage(long maxage) {
        this.maxage = maxage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    protected void toString(StringBuilder sb) {
        Formatter.add(sb, "name", this.name);
        Formatter.addDate(sb, "timestamp", this.timestamp);
        Formatter.add(sb, "version", this.version);
        Formatter.add(sb, "maxage", this.maxage);
        Formatter.add(sb, "icon", this.icon);
        Formatter.add(sb, "address", this.address);
        Formatter.add(sb, "description", this.description);
    }
}
