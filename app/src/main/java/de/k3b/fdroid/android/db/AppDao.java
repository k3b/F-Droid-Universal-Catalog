package de.k3b.fdroid.android.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import de.k3b.fdroid.android.model.App;

@Dao
public interface AppDao {
    @Insert
    void insertAll(App... apps);

    @Delete
    void delete(App app);

}
