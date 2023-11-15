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

import de.k3b.fdroid.domain.entity.HardwareProfile;
import de.k3b.fdroid.domain.entity.Version;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Common data for App_{@link Version} and {@link HardwareProfile}.
 * <p>
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
@javax.persistence.MappedSuperclass
@SuppressWarnings("unused")
public class ProfileCommon extends EntityCommon implements IProfileCommon {
    private static final double MEGA_BYTE = 1024 * 1024;

    @Schema(description = "Filename of the apk version that will become part of the download url.",
            example = "de.k3b.android.androFotoFinder_45.apk")
    private String apkName;
    @Schema(description = "Code of the apk version.",
            example = "45")
    private int versionCode;
    @Schema(description = "Name of the apk version.",
            example = "0.8.1.200212")
    private String versionName;
    @Schema(description = "Date when the version of the [App] added the [Repo] in internal numeric format.",
            example = "1581465600000")
    private long added;
    @Schema(description = "Apk size in Bytes.",
            example = "1492672")
    private int size;

    public static void copyCommon(ProfileCommon dest, IProfileCommon src) {
        dest.setApkName(src.getApkName());
        dest.setSize(src.getSize());
        dest.setVersion(src.getVersionCode(), src.getVersionName(), src.getAdded());
    }

    public void setVersion(int versionCode, String versionName, long added) {
        setVersionCode(versionCode);
        setVersionName(versionName);
        setAdded(added);
    }

    @Override
    public long getAdded() {
        return added;
    }

    public void setAdded(long added) {
        this.added = added;
    }

    @Schema(description = "Date when the version of the [App] added the [Repo].",
            example = "2020-02-12")
    public String getAddedDate() {
        return asDateString(added);
    }

    @Override
    public String getApkName() {
        return apkName;
    }

    public void setApkName(String apkName) {
        this.apkName = maxlen(apkName);
    }

    @Schema(description = "Apk size in Megabytes.",
            example = "1.4 MB")
    public String getSizeMB() {
        double megabyte = size / MEGA_BYTE;
        int m = (int) (megabyte * 10);

        return "" + (m / 10.0) + " MB";
    }

    @Override
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = maxlen(versionName);
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
