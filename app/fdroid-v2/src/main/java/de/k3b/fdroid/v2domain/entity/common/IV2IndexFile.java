package de.k3b.fdroid.v2domain.entity.common;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IV2IndexFile {
    @NotNull
    String getName();

    @Nullable
    String getSha256();

    @Nullable
    String getIpfsCidV1();

    @Nullable
    Long getSize();
}
