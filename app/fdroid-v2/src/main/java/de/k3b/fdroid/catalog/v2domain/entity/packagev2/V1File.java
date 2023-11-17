package de.k3b.fdroid.catalog.v2domain.entity.packagev2;

// V1File.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.k3b.fdroid.catalog.v2domain.entity.common.IV2IndexFile;

public final class V1File implements IV2IndexFile {
    @NotNull
    private final String name;
    @NotNull
    private final String sha256;
    @Nullable
    private final Long size;
    @Nullable
    private final String ipfsCIDv1;

    public V1File(@NotNull String name, @NotNull String sha256, @Nullable Long size, @Nullable String ipfsCIDv1) {
        this.name = name;
        this.sha256 = sha256;
        this.size = size;
        this.ipfsCIDv1 = ipfsCIDv1;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getSha256() {
        return this.sha256;
    }

    @Nullable
    public Long getSize() {
        return this.size;
    }

    @Nullable
    public String getIpfsCidV1() {
        return this.ipfsCIDv1;
    }

    @NotNull
    public String toString() {
        return "V1File{name=" + this.getName() + ", sha256=" + this.getSha256() + ", size=" + this.getSize() + ", ipfsCIDv1=" + this.getIpfsCidV1() + "}";
    }
}
