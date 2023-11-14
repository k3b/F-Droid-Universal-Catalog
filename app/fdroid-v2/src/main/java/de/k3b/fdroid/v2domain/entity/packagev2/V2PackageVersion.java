package de.k3b.fdroid.v2domain.entity.packagev2;

// V2PackageVersion.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import de.k3b.fdroid.v2domain.entity.repo.V2File;

public final class V2PackageVersion implements IV2PackageVersion {
    private final long versionCode;
    @Nullable
    private final V2Signer signer;
    @NotNull
    private final IV2PackageManifest packageManifest;
    private final long added;
    @NotNull
    private final V1File file;
    @Nullable
    private final V2File src;
    @NotNull
    private final V2Manifest manifest;
    @NotNull
    private final List<String> releaseChannels;
    @NotNull
    private final Map<String, Map<String, String>> antiFeatures;
    @NotNull
    private final Map<String, String> whatsNew;

    public V2PackageVersion(long added, @NotNull V1File file, @Nullable V2File src, @NotNull V2Manifest manifest, @NotNull List<String> releaseChannels, @NotNull Map<String, Map<String, String>> antiFeatures, @NotNull Map<String, String> whatsNew) {
        this.added = added;
        this.file = file;
        this.src = src;
        this.manifest = manifest;
        this.releaseChannels = releaseChannels;
        this.antiFeatures = antiFeatures;
        this.whatsNew = whatsNew;
        this.versionCode = this.manifest.getVersionCode();
        this.signer = this.manifest.getSigner();
        this.packageManifest = this.manifest;
    }

    public long getVersionCode() {
        return this.versionCode;
    }

    @Nullable
    public V2Signer getSigner() {
        return this.signer;
    }

    @NotNull
    public IV2PackageManifest getPackageManifest() {
        return this.packageManifest;
    }

    public boolean getHasKnownVulnerability() {
        return this.antiFeatures.containsKey("KnownVuln");
    }

    public long getAdded() {
        return this.added;
    }

    @NotNull
    public V1File getFile() {
        return this.file;
    }

    @Nullable
    public V2File getSrc() {
        return this.src;
    }

    @NotNull
    public V2Manifest getManifest() {
        return this.manifest;
    }

    @NotNull
    public List<String> getReleaseChannels() {
        return this.releaseChannels;
    }

    @NotNull
    public Map<String, Map<String, String>> getAntiFeatures() {
        return this.antiFeatures;
    }

    @NotNull
    public Map<String, String> getWhatsNew() {
        return this.whatsNew;
    }

    @NotNull
    public String toString() {
        return "V2PackageVersion{added=" + this.added + ", file=" + this.file + ", src=" + this.src + ", manifest=" + this.manifest + ", releaseChannels=" + this.getReleaseChannels() + ", antiFeatures=" + this.antiFeatures + ", whatsNew=" + this.whatsNew + "}";
    }
}
