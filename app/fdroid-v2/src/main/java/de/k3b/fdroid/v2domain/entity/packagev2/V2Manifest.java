package de.k3b.fdroid.v2domain.entity.packagev2;

// V2Manifest.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public final class V2Manifest implements IV2PackageManifest {
    @Nullable
    private final Integer minSdkVersion;
    @NotNull
    private final List<String> featureNames;
    @NotNull
    private final String versionName;
    private final long versionCode;
    @Nullable
    private final V2UsesSdk usesSdk;
    @Nullable
    private final Integer maxSdkVersion;
    @Nullable
    private final V2Signer signer;
    @NotNull
    private final List<V2Permission> usesPermission;
    @NotNull
    private final List<V2Permission> usesPermissionSdk23;
    @NotNull
    private final List<String> nativecode;
    @NotNull
    private final List<V2Feature> features;

    public V2Manifest(@NotNull String versionName, long versionCode, @Nullable V2UsesSdk usesSdk, @Nullable Integer maxSdkVersion,
                      @Nullable V2Signer signer, @NotNull List<V2Permission> usesPermission, @NotNull List<V2Permission> usesPermissionSdk23,
                      @NotNull List<String> nativecode,
                      @NotNull List<V2Feature> features) {
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
    public String getVersionName() {
        return this.versionName;
    }

    public long getVersionCode() {
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

    @NotNull
    public List<V2Permission> getUsesPermission() {
        return this.usesPermission;
    }

    @NotNull
    public List<V2Permission> getUsesPermissionSdk23() {
        return this.usesPermissionSdk23;
    }

    @NotNull
    public List<String> getNativecode() {
        return this.nativecode;
    }

    @NotNull
    public List<V2Feature> getFeatures() {
        return this.features;
    }

    @NotNull
    public String toString() {
        return "V2Manifest{versionName=" + this.versionName + ", versionCode=" + this.versionCode + ", usesSdk=" + this.usesSdk + ", maxSdkVersion=" + this.getMaxSdkVersion() + ", signer=" + this.signer + ", usesPermission=" + this.usesPermission + ", usesPermissionSdk23=" + this.usesPermissionSdk23 + ", nativecode=" + this.getNativecode() + ", features=" + this.features + "}";
    }
}
