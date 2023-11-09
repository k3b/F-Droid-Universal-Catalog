package de.k3b.fdroid.v2domain.entity.packagev2;

// PackageVersion.java

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PackageVersion {
    long getVersionCode();

    @Nullable
    SignerV2 getSigner();

    @Nullable
    List<String> getReleaseChannels();

    @NotNull
    PackageManifest getPackageManifest();

    boolean getHasKnownVulnerability();
}
