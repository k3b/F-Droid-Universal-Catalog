package de.k3b.fdroid.v2domain.entity.packagev2;
// IV2PackageManifest.java

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IV2PackageManifest {
    @Nullable
    Integer getMinSdkVersion();

    @Nullable
    Integer getMaxSdkVersion();

    @Nullable
    List<String> getFeatureNames();

    @Nullable
    List<String> getNativecode();
}
