package de.k3b.fdroid.v2domain.entity.packagev2;

// FileV1.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import de.k3b.fdroid.v2domain.entity.common.IndexFile;

public final class FileV1 implements IndexFile {
    @NotNull
    private final String name;
    @NotNull
    private final String sha256;
    @Nullable
    private final Long size;
    @Nullable
    private final String ipfsCIDv1;

    public FileV1(@NotNull String name, @NotNull String sha256, @Nullable Long size, @Nullable String ipfsCIDv1) {
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
        return "FileV1{name=" + this.getName() + ", sha256=" + this.getSha256() + ", size=" + this.getSize() + ", ipfsCIDv1=" + this.getIpfsCidV1() + "}";
    }
}
