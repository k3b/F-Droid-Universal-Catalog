package de.k3b.fdroid.v2domain.entity.entry;

// Entry.java

import java.util.Map;
import java.util.Objects;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class Entry {
    private final long timestamp;
    private final long version;
    @Nullable
    private final Integer maxAge;
    @NotNull
    private final EntryFileV2 index;
    @NotNull
    private final Map<String, EntryFileV2> diffs;

    public Entry(long timestamp, long version, @Nullable Integer maxAge, @NotNull EntryFileV2 index, @NotNull Map<String, EntryFileV2> diffs) {
        this.timestamp = timestamp;
        this.version = version;
        this.maxAge = maxAge;
        this.index = index;
        this.diffs = diffs;
    }

    @Nullable
    public final EntryFileV2 getDiff(long timestamp) {
        return (EntryFileV2) this.diffs.get(String.valueOf(timestamp));
    }

    public final long getTimestamp() {
        return this.timestamp;
    }

    public final long getVersion() {
        return this.version;
    }

    @Nullable
    public final Integer getMaxAge() {
        return this.maxAge;
    }

    @NotNull
    public final EntryFileV2 getIndex() {
        return this.index;
    }

    @NotNull
    public final Map<String, EntryFileV2> getDiffs() {
        return this.diffs;
    }

    @NotNull
    public String toString() {
        return "Entry(timestamp=" + this.timestamp + ", version=" + this.version + ", maxAge=" + this.maxAge + ", index=" + this.index + ", diffs=" + this.diffs + ")";
    }
}

