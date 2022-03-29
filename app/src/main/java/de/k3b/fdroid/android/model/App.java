package de.k3b.fdroid.android.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(foreignKeys = @ForeignKey(entity = Repo.class,parentColumns = "id",
                                            childColumns = "repoId", onDelete = CASCADE),
        indices = {@Index("repoId")}
)
public class App {
    @PrimaryKey(autoGenerate = true)
    public Integer id;

    public Integer repoId;
    // public Repo repo;

    // public List<Localized> localisations;
    // public List<Version> versions;
}
