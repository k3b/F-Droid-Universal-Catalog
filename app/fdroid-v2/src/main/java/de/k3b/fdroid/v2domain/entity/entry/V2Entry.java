package de.k3b.fdroid.v2domain.entity.entry;

// V2Entry.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public final class V2Entry {
    private final long timestamp;
    private final long version;
    @Nullable
    private final Integer maxAge;
    @NotNull
    private final V2EntryFile index;
    @NotNull
    private final Map<String, V2EntryFile> diffs;

    public V2Entry(long timestamp, long version, @Nullable Integer maxAge, @NotNull V2EntryFile index, @NotNull Map<String, V2EntryFile> diffs) {
        this.timestamp = timestamp;
        this.version = version;
        this.maxAge = maxAge;
        this.index = index;
        this.diffs = diffs;
    }

    @Nullable
    public V2EntryFile getDiff(long timestamp) {
        return this.diffs.get(String.valueOf(timestamp));
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public long getVersion() {
        return this.version;
    }

    @Nullable
    public Integer getMaxAge() {
        return this.maxAge;
    }

    @NotNull
    public V2EntryFile getIndex() {
        return this.index;
    }

    @NotNull
    public Map<String, V2EntryFile> getDiffs() {
        return this.diffs;
    }

    @NotNull
    public String toString() {
        return "V2Entry(timestamp=" + this.timestamp + ", version=" + this.version + ", maxAge=" + this.maxAge + ", index=" + this.index + ", diffs=" + this.diffs + ")";
    }
}

