package de.k3b.fdroid.android.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.k3b.fdroid.android.model.App;
import de.k3b.fdroid.android.model.Version;

@Dao
public abstract class VersionDao {
    @Insert
    public abstract void insertAll(Version... versions);

    @Delete
    public abstract void delete(Version version);

    @Query("SELECT * FROM Version WHERE Version.appId = :appId")
    public abstract List<Version> findVersionsForApp(Integer appId);

    public List<Version> findVersionsForApp(App app) {
        return findVersionsForApp(app.id);
    }
}
