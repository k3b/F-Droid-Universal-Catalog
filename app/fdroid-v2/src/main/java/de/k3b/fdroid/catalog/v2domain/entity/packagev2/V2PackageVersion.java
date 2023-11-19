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
package de.k3b.fdroid.catalog.v2domain.entity.packagev2;

// V2PackageVersion.java

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import de.k3b.fdroid.catalog.v2domain.entity.repo.V2File;
import de.k3b.fdroid.domain.entity.common.IVersionCommon;

@SuppressWarnings("unused")
public class V2PackageVersion implements IVersionCommon {
    // private int versionCode;

    @Nullable
    private V2Manifest packageManifest;
    private long added;
    @Nullable
    private V1File file;
    @Nullable
    private V2File src;
    @Nullable
    private V2Manifest manifest;
    @Nullable
    private List<String> releaseChannels;
    @Nullable
    private Map<String, Map<String, String>> antiFeatures;
    @Nullable
    private Map<String, String> whatsNew;

    public int getVersionCode() {
        return (manifest == null) ? 0 : manifest.getVersionCode();
        // return this.versionCode;
    }

    @Override
    public String getVersionName() {
        return (manifest == null) ? null : manifest.getVersionName();
    }

    public V2UsesSdk getUsesSdk() {
        return (manifest == null) ? null : manifest.getUsesSdk();
    }

    @Override
    public int getTargetSdkVersion() {
        return (getUsesSdk() == null) ? 0 : getUsesSdk().getTargetSdkVersion();
    }


    @Override
    public int getMinSdkVersion() {
        return (getUsesSdk() == null) ? 0 : getUsesSdk().getMinSdkVersion();
    }

    @Override
    public int getMaxSdkVersion() {
        Integer integer = (manifest == null) ? null : manifest.getMaxSdkVersion();
        return integer == null ? 0 : integer;
    }

    @Override
    public String getSigner() {
        V2Signer signer = getSignerObject();
        return (signer == null) ? null : signer.getSha256().get(0);
    }

    @Nullable
    public V2Signer getSignerObject() {
        return (manifest == null) ? null : manifest.getSigner();
    }

    @Nullable
    public V2Manifest getPackageManifest() {
        return this.packageManifest;
    }

    public boolean getHasKnownVulnerability() {
        return antiFeatures != null && this.antiFeatures.containsKey("KnownVuln");
    }

    public long getAdded() {
        return this.added;
    }

    @Nullable
    public V2Manifest getManifest() {
        return this.manifest;
    }

    @Nullable
    public List<String> getReleaseChannels() {
        return this.releaseChannels;
    }

    @Nullable
    public Map<String, Map<String, String>> getAntiFeatures() {
        return this.antiFeatures;
    }

    @Nullable
    public Map<String, String> getWhatsNew() {
        return this.whatsNew;
    }

    @Nullable
    public String toString() {
        return "V2PackageVersion{added=" + this.added + ", file=" + this.file + ", src=" + this.src + ", manifest=" + this.manifest + ", releaseChannels=" + this.getReleaseChannels() + ", antiFeatures=" + this.antiFeatures + ", whatsNew=" + this.whatsNew + "}";
    }

    public List<String> getNativecode() {
        return (manifest == null) ? null : manifest.getNativecode();
    }

    @Nullable
    public V2File getSrc() {
        return this.src;
    }

    @Override
    public String getSrcname() {
        return (src == null) ? null : src.getName();
    }

    @Nullable
    public V1File getFile() {
        return this.file;
    }

    @Override
    public String getApkName() {
        return (file == null) ? null : file.getName();
    }

    @Override
    public int getSize() {
        Long i = (file == null) ? null : file.getSize();
        return (i == null) ? 0 : i.intValue();
    }


    @Override
    public String getHash() {
        return (file == null) ? null : file.getSha256();
    }

    @Override
    public String getHashType() {
        return (file == null) ? null : "sha256";
    }

}
