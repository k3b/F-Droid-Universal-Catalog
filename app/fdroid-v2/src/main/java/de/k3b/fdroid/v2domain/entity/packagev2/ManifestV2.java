package de.k3b.fdroid.v2domain.entity.packagev2;

// ManifestV2.java

import java.util.List;
import java.util.stream.Collectors;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ManifestV2 implements PackageManifest {
    @Nullable
    private final Integer minSdkVersion;
    @NotNull
    private final List<String> featureNames;
    @NotNull
    private final String versionName;
    private final long versionCode;
    @Nullable
    private final UsesSdkV2 usesSdk;
    @Nullable
    private final Integer maxSdkVersion;
    @Nullable
    private final SignerV2 signer;
    @NotNull
    private final List<PermissionV2> usesPermission;
    @NotNull
    private final List<PermissionV2> usesPermissionSdk23;
    @NotNull
    private final List<String> nativecode;
    @NotNull
    private final List<FeatureV2> features;

    public ManifestV2(@NotNull String versionName, long versionCode, @Nullable UsesSdkV2 usesSdk, @Nullable Integer maxSdkVersion,
                      @Nullable SignerV2 signer, @NotNull List<PermissionV2> usesPermission, @NotNull List<PermissionV2> usesPermissionSdk23,
                      @NotNull List<String> nativecode,
                      @NotNull List<FeatureV2> features) {
        this.versionName = versionName;
        this.versionCode = versionCode;
        this.usesSdk = usesSdk;
        this.maxSdkVersion = maxSdkVersion;
        this.signer = signer;
        this.usesPermission = usesPermission;
        this.usesPermissionSdk23 = usesPermissionSdk23;
        this.nativecode = nativecode;
        this.features = features;

        this.minSdkVersion = usesSdk != null ? usesSdk.getMinSdkVersion() : null;

        this.featureNames = features.stream().map(f -> f.getName()).collect(Collectors.toList());
    }

    @Nullable
    public Integer getMinSdkVersion() {
        return this.minSdkVersion;
    }

    @NotNull
    public List<String> getFeatureNames() {
        return this.featureNames;
    }

    @NotNull
    public final String getVersionName() {
        return this.versionName;
    }

    public final long getVersionCode() {
        return this.versionCode;
    }

    @Nullable
    public final UsesSdkV2 getUsesSdk() {
        return this.usesSdk;
    }

    @Nullable
    public Integer getMaxSdkVersion() {
        return this.maxSdkVersion;
    }

    @Nullable
    public final SignerV2 getSigner() {
        return this.signer;
    }

    @NotNull
    public final List<PermissionV2> getUsesPermission() {
        return this.usesPermission;
    }

    @NotNull
    public final List<PermissionV2> getUsesPermissionSdk23() {
        return this.usesPermissionSdk23;
    }

    @NotNull
    public List<String> getNativecode() {
        return this.nativecode;
    }

    @NotNull
    public final List<FeatureV2> getFeatures() {
        return this.features;
    }

    @NotNull
    public String toString() {
        return "ManifestV2{versionName=" + this.versionName + ", versionCode=" + this.versionCode + ", usesSdk=" + this.usesSdk + ", maxSdkVersion=" + this.getMaxSdkVersion() + ", signer=" + this.signer + ", usesPermission=" + this.usesPermission + ", usesPermissionSdk23=" + this.usesPermissionSdk23 + ", nativecode=" + this.getNativecode() + ", features=" + this.features + "}";
    }
}
