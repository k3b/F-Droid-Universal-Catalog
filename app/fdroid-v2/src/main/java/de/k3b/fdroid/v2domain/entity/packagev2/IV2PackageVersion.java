package de.k3b.fdroid.v2domain.entity.packagev2;

// IV2PackageVersion.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IV2PackageVersion {
    long getVersionCode();

    @Nullable
    V2Signer getSigner();

    @Nullable
    List<String> getReleaseChannels();

    @NotNull
    IV2PackageManifest getPackageManifest();

    boolean getHasKnownVulnerability();
}
