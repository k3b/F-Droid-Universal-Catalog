package de.k3b.fdroid.v2domain.entity.packagev2;
// PackageManifest.java

import java.util.List;

import org.jetbrains.annotations.Nullable;

public interface PackageManifest {
    @Nullable
    Integer getMinSdkVersion();

    @Nullable
    Integer getMaxSdkVersion();

    @Nullable
    List<String> getFeatureNames();

    @Nullable
    List<String> getNativecode();
}
