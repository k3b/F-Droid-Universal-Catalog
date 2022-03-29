package de.k3b.fdroid.android.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import de.k3b.fdroid.android.model.*;

@Database(version = 1, entities = {App.class, AppCategory.class, Category.class, Locale.class,
        Localized.class, Repo.class, Version.class})
public abstract class FDroidDatabase  extends RoomDatabase {
    public abstract AppDao appDao();
    public abstract AppCategoryDao appCategoryDao();
    public abstract CategoryDao categoryDao();
    public abstract LocaleDao localeDao();
    public abstract LocalizedDao localizedDao();
    public abstract RepoDao repoDao();
    public abstract VersionDao versionDao();

    private static FDroidDatabase INSTANCE = null;
    public static FDroidDatabase getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    FDroidDatabase.class, "FDroidData.db").build();

        }
        return INSTANCE;
    }
}

