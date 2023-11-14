package de.k3b.fdroid.v2domain.entity.entry;

// V2EntryFile.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.k3b.fdroid.v2domain.entity.common.IV2IndexFile;

public final class V2EntryFile implements IV2IndexFile {
    @NotNull
    private final String name;
    @NotNull
    private final String sha256;
    private final long size;
    @Nullable
    private final String ipfsCIDv1;
    private final int numPackages;

    public V2EntryFile(@NotNull String name, @NotNull String sha256, long size, @Nullable String ipfsCIDv1, int numPackages) {
        this.name = name;
        this.sha256 = sha256;
        this.size = size;
        this.ipfsCIDv1 = ipfsCIDv1;
        this.numPackages = numPackages;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String getSha256() {
        return this.sha256;
    }

    @NotNull
    public Long getSize() {
        return this.size;
    }

    @Nullable
    public String getIpfsCidV1() {
        return this.ipfsCIDv1;
    }

    public int getNumPackages() {
        return this.numPackages;
    }

    @NotNull
    public String toString() {
        return "V2EntryFile(name=" + this.getName() + ", sha256=" + this.getSha256() + ", size=" + this.getSize() + ", ipfsCIDv1=" + this.getIpfsCidV1() + ", numPackages=" + this.numPackages + ")";
    }
}
