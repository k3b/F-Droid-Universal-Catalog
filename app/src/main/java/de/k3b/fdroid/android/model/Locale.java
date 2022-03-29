package de.k3b.fdroid.android.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Locale {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

}
