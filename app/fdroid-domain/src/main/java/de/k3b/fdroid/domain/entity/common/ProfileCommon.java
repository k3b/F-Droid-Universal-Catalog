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

import de.k3b.fdroid.domain.entity.HardwareProfile;
import de.k3b.fdroid.domain.entity.Version;

/**
 * Common data for App_{@link Version} and {@link HardwareProfile}.
 * <p>
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@javax.persistence.MappedSuperclass
@SuppressWarnings("unused")
public class ProfileCommon extends EntityCommon {
    private static final double MEGA_BYTE = 1024 * 1024;

    private String apkName;
    private int versionCode;
    private String versionName;
    private long added;
    private int size;

    public static void copyCommon(ProfileCommon dest, ProfileCommon src) {
        dest.setApkName(src.getApkName());
        dest.setSize(src.getSize());
        dest.setVersion(src.getVersionCode(), src.getVersionName(), src.getAdded());
    }

    public void setVersion(int versionCode, String versionName, long added) {
        setVersionCode(versionCode);
        setVersionName(versionName);
        setAdded(added);
    }

    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    public String getAddedDate() {
        return asDateString(added);
    }

    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = apkName;
    }

    public String getSizeMB() {
        double megabyte = size / MEGA_BYTE;
        int m = (int) (megabyte * 10);

        return "" + (m / 10.0) + " MB";
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
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
