package de.k3b.fdroid.v2domain.entity.packagev2;

// PackageVersionV2.java

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.k3b.fdroid.v2domain.entity.repo.FileV2;

public final class PackageVersionV2 implements PackageVersion {
    private final long versionCode;
    @Nullable
    private final SignerV2 signer;
    @NotNull
    private final PackageManifest packageManifest;
    private final long added;
    @NotNull
    private final FileV1 file;
    @Nullable
    private final FileV2 src;
    @NotNull
    private final ManifestV2 manifest;
    @NotNull
    private final List<String> releaseChannels;
    @NotNull
    private final Map<String, Map<String, String>> antiFeatures;
    @NotNull
    private final Map<String, String> whatsNew;

    public PackageVersionV2(long added, @NotNull FileV1 file, @Nullable FileV2 src, @NotNull ManifestV2 manifest, @NotNull List<String> releaseChannels, @NotNull Map<String, Map<String, String>> antiFeatures, @NotNull Map<String, String> whatsNew) {
        this.added = added;
        this.file = file;
        this.src = src;
        this.manifest = manifest;
        this.releaseChannels = releaseChannels;
        this.antiFeatures = antiFeatures;
        this.whatsNew = whatsNew;
        this.versionCode = this.manifest.getVersionCode();
        this.signer = this.manifest.getSigner();
        this.packageManifest = (PackageManifest) this.manifest;
    }

    public long getVersionCode() {
        return this.versionCode;
    }

    @Nullable
    public SignerV2 getSigner() {
        return this.signer;
    }

    @NotNull
    public PackageManifest getPackageManifest() {
        return this.packageManifest;
    }

    public boolean getHasKnownVulnerability() {
        return this.antiFeatures.containsKey("KnownVuln");
    }

    public final long getAdded() {
        return this.added;
    }

    @NotNull
    public final FileV1 getFile() {
        return this.file;
    }

    @Nullable
    public final FileV2 getSrc() {
        return this.src;
    }

    @NotNull
    public final ManifestV2 getManifest() {
        return this.manifest;
    }

    @NotNull
    public List<String> getReleaseChannels() {
        return this.releaseChannels;
    }

    @NotNull
    public final Map<String, Map<String, String>> getAntiFeatures() {
        return this.antiFeatures;
    }

    @NotNull
    public final Map<String, String> getWhatsNew() {
        return this.whatsNew;
    }

    @NotNull
    public String toString() {
        return "PackageVersionV2{added=" + this.added + ", file=" + this.file + ", src=" + this.src + ", manifest=" + this.manifest + ", releaseChannels=" + this.getReleaseChannels() + ", antiFeatures=" + this.antiFeatures + ", whatsNew=" + this.whatsNew + "}";
    }
}
