package de.k3b.fdroid.v2domain.entity.packagev2;

// FeatureV2.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class FeatureV2 {
    @NotNull
    private final String name;

    public FeatureV2(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public String toString() {
        return "FeatureV2{name=" + this.name + "}";
    }
}
