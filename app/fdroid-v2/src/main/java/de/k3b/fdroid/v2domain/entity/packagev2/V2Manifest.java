/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of de.k3b.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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
package de.k3b.fdroid.v2domain.entity.packagev2;

// V2Manifest.java

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class V2Manifest {
    @Nullable
    private Integer minSdkVersion;
    @Nullable
    private List<String> featureNames;
    @Nullable
    private String versionName;
    private int versionCode;
    @Nullable
    private V2UsesSdk usesSdk;
    @Nullable
    private Integer maxSdkVersion;
    @Nullable
    private V2Signer signer;
    @Nullable
    private List<V2Permission> usesPermission;
    @Nullable
    private List<V2Permission> usesPermissionSdk23;
    @Nullable
    private List<String> nativecode;
    @Nullable
    private List<V2Feature> features;

    @Nullable
    public Integer getMinSdkVersion() {
        return this.minSdkVersion;
    }

    @Nullable
    public List<String> getFeatureNames() {
        return this.featureNames;
    }

    @Nullable
    public String getVersionName() {
        return this.versionName;
    }

    public int getVersionCode() {
        return this.versionCode;
    }

    @Nullable
    public V2UsesSdk getUsesSdk() {
        return this.usesSdk;
    }

    @Nullable
    public Integer getMaxSdkVersion() {
        return this.maxSdkVersion;
    }

    @Nullable
    public V2Signer getSigner() {
        return this.signer;
    }

    @Nullable
    public List<V2Permission> getUsesPermission() {
        return this.usesPermission;
    }

    @Nullable
    public List<V2Permission> getUsesPermissionSdk23() {
        return this.usesPermissionSdk23;
    }

    @Nullable
    public List<String> getNativecode() {
        return this.nativecode;
    }

    @Nullable
    public List<V2Feature> getFeatures() {
        return this.features;
    }

    @Nullable
    public String toString() {
        return "V2Manifest{versionName=" + this.versionName + ", versionCode=" + this.versionCode + ", usesSdk=" + this.usesSdk + ", maxSdkVersion=" + this.getMaxSdkVersion() + ", signer=" + this.signer + ", usesPermission=" + this.usesPermission + ", usesPermissionSdk23=" + this.usesPermissionSdk23 + ", nativecode=" + this.getNativecode() + ", features=" + this.features + "}";
    }
}
