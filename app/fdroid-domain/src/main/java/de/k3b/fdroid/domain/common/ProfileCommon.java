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

/**
 * Common data for App_{@link de.k3b.fdroid.domain.Version} and {@link de.k3b.fdroid.domain.HardwareProfile}.
 * <p>
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@javax.persistence.MappedSuperclass
public class ProfileCommon extends PojoCommon {
    private String apkName;
    private long versionCode;
    private String versionName;
    private long added;
    private long size;

    public static void copyCommon(ProfileCommon dest, ProfileCommon src) {
        dest.setApkName(src.getApkName());
        dest.setAdded(src.getAdded());
        dest.setSize(src.getSize());
        dest.setVersionCode(src.getVersionCode());
        dest.setVersionName(src.getVersionName());
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(long versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    protected void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "apkName", this.apkName);
        toDateStringBuilder(sb, "added", this.added);
        toStringBuilder(sb, "versionCode", this.versionCode);
        toStringBuilder(sb, "versionName", this.versionName);
        toStringBuilder(sb, "size", this.size);
    }
}
