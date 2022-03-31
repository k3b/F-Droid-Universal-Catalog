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

import javax.annotation.Generated;

/**
 * Data for a FDroid-Repository (read from FDroid-Catalog-v1-Json format).
 *
 * Generated with https://www.jsonschema2pojo.org/ from JSON example Data in Format Gson.
 */
@Generated("jsonschema2pojo")
public class Repo {

    @SerializedName("timestamp")
    @Expose
    private long timestamp;
    @SerializedName("version")
    @Expose
    private long version;
    @SerializedName("maxage")
    @Expose
    private long maxage;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("icon")
    @Expose
    private String icon;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("mirrors")
    @Expose
    private List<String> mirrors = null;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getMirrors() {
        return mirrors;
    }

    public void setMirrors(List<String> mirrors) {
        this.mirrors = mirrors;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Repo.class.getSimpleName()).append('[');
        Formatter.add(sb, "name", this.name);
        Formatter.addDate(sb, "timestamp", this.timestamp);
        Formatter.add(sb, "version", this.version);
        Formatter.add(sb, "maxage", this.maxage);
        Formatter.add(sb, "icon", this.icon);
        Formatter.add(sb, "address", this.address);
        Formatter.add(sb, "description", this.description);
        Formatter.add(sb, "mirrors", this.mirrors);
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
