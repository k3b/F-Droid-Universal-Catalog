package de.k3b.fdroid.android.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import de.k3b.fdroid.android.model.App;
import de.k3b.fdroid.android.model.Localized;

@Dao
public abstract class LocalizedDao {
    @Insert
    public abstract void insertAll(Localized... localized);

    @Delete
    public abstract void delete(Localized localized);

    @Query("SELECT * FROM Localized WHERE Localized.appId = :appId ")
    public abstract List<Localized> findLocalizedForApp(Integer appId);

    @Query("SELECT * FROM Localized WHERE Localized.appId = :appId and Localized.localeId in (:localeIds) ")
    public abstract List<Localized> findLocalizedForApp(Integer appId, List<Integer> localeIds);

    public List<Localized> findLocalizedForApp(App app, List<Integer> localeIds) {
        if (localeIds == null || localeIds.isEmpty()) {
            return findLocalizedForApp(app.id);
        }
        return findLocalizedForApp(app.id, localeIds);
    }
}
