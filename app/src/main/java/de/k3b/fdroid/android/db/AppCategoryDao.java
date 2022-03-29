package de.k3b.fdroid.android.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

import de.k3b.fdroid.android.model.App;
import de.k3b.fdroid.android.model.AppCategory;

@Dao
public interface AppCategoryDao {
    @Insert
    void insertAll(AppCategory... appCategorys);

    @Delete
    void delete(AppCategory appCategory);

}
