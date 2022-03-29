package de.k3b.fdroid.android.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Version {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public Integer appId;
}
