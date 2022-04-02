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

package org.fdroid.model.common;

/**
 * Common data for v1-Gson-json and android-room-database-Entities.
 * Only primitive types are allowed. No relations, no Objects, no Lists
 * as these are Gson/Android-Room-Database specific.
 */
public class VersionCommon extends PojoCommon {
    private long added;
    private String apkName;
    private String hash;
    private String hashType;
    private long minSdkVersion;
    private String sig;
    private String signer;
    private long size;
    private String srcname;
    private long targetSdkVersion;
    private long versionCode;
    private String versionName;

    public static void copyCommon(VersionCommon dest, VersionCommon src) {
        dest.setAdded(src.getAdded());
        dest.setApkName(src.getApkName());
        dest.setHash(src.getHash());
        dest.setHashType(src.getHashType());
        dest.setMinSdkVersion(src.getMinSdkVersion());
        dest.setSig(src.getSig());
        dest.setSigner(src.getSigner());
        dest.setSize(src.getSize());
        dest.setSrcname(src.getSrcname());
        dest.setTargetSdkVersion(src.getTargetSdkVersion());
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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getHashType() {
        return hashType;
    }

    public void setHashType(String hashType) {
        this.hashType = hashType;
    }

    public long getMinSdkVersion() {
        return minSdkVersion;
    }

    public void setMinSdkVersion(long minSdkVersion) {
        this.minSdkVersion = minSdkVersion;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getSigner() {
        return signer;
    }

    public void setSigner(String signer) {
        this.signer = signer;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getSrcname() {
        return srcname;
    }

    public void setSrcname(String srcname) {
        this.srcname = srcname;
    }

    public long getTargetSdkVersion() {
        return targetSdkVersion;
    }

    public void setTargetSdkVersion(long targetSdkVersion) {
        this.targetSdkVersion = targetSdkVersion;
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
        toStringBuilder(sb, "hash", this.hash);
        toStringBuilder(sb, "hashType", this.hashType);
        toStringBuilder(sb, "minSdkVersion", this.minSdkVersion);
        toStringBuilder(sb, "sig", this.sig);
        toStringBuilder(sb, "signer", this.signer);
        toStringBuilder(sb, "size", this.size);
        toStringBuilder(sb, "srcname", this.srcname);
        toStringBuilder(sb, "targetSdkVersion", this.targetSdkVersion);
        toStringBuilder(sb, "versionCode", this.versionCode);
        toStringBuilder(sb, "versionName", this.versionName);
    }
}
