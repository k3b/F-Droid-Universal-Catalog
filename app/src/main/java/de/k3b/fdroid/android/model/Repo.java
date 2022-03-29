package de.k3b.fdroid.android.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.Query;

import java.util.List;

@Entity
public class Repo {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

}
