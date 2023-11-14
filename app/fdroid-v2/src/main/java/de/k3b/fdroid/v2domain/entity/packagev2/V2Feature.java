package de.k3b.fdroid.v2domain.entity.packagev2;

// V2Feature.java

import org.jetbrains.annotations.NotNull;

public final class V2Feature {
    @NotNull
    private final String name;

    public V2Feature(@NotNull String name) {
        this.name = name;
    }

    @NotNull
    public String getName() {
        return this.name;
    }

    @NotNull
    public String toString() {
        return "V2Feature{name=" + this.name + "}";
    }
}
