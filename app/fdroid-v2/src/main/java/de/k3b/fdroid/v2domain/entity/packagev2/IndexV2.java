package de.k3b.fdroid.v2domain.entity.packagev2;

// IndexV2.java

import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.k3b.fdroid.v2domain.entity.repo.RepoV2;

public final class IndexV2 {
    @NotNull
    private final RepoV2 repo;
    @NotNull
    private final Map<String, PackageV2> packages;

    public IndexV2(@NotNull RepoV2 repo, @NotNull Map<String, PackageV2> packages) {
        this.repo = repo;
        this.packages = packages;
    }

    @NotNull
    public final RepoV2 getRepo() {
        return this.repo;
    }

    @NotNull
    public final Map<String, PackageV2> getPackages() {
        return this.packages;
    }

    public String toString() {
        return "IndexV2{repo=" + this.repo + ", packages=" + this.packages + "}";
    }
}
