package de.k3b.fdroid.android.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import de.k3b.fdroid.android.model.App;
import de.k3b.fdroid.android.model.Category;

@Dao
public interface CategoryDao {
    @Insert
    void insertAll(Category... categorys);

    @Delete
    void delete(Category category);

    @Query("SELECT * FROM Category")
    Category[] findAll();

}
