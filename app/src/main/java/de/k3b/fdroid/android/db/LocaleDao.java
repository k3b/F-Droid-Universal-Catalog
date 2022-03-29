package de.k3b.fdroid.android.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import de.k3b.fdroid.android.model.Locale;

@Dao
public interface LocaleDao {
    @Insert
    void insertAll(Locale... locales);

    @Delete
    void delete(Locale locale);

    @Query("SELECT * FROM Locale")
    Locale[] findAll();

}
