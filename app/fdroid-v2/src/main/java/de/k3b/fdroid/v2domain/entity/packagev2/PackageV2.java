package de.k3b.fdroid.v2domain.entity.packagev2;

// PackageV2.java

import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PackageV2 {
    @NotNull
    private final MetadataV2 metadata;
    @NotNull
    private final Map<String, PackageVersionV2> versions;


    public PackageV2(@NotNull MetadataV2 metadata, @NotNull Map<String, PackageVersionV2> versions) {
        this.metadata = metadata;
        this.versions = versions;
    }

    @NotNull
    public final MetadataV2 getMetadata() {
        return this.metadata;
    }

    @NotNull
    public final Map<String, PackageVersionV2> getVersions() {
        return this.versions;
    }

    @NotNull
    public String toString() {
        return "PackageV2(metadata=" + this.metadata + ", versions=" + this.versions + ")";
    }
}
