/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid.v1 the fdroid json catalog-format-v1 parser.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */
package de.k3b.fdroid.android.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import de.k3b.fdroid.room.model.App;
import de.k3b.fdroid.room.model.AppCategory;
import de.k3b.fdroid.room.model.Category;
import de.k3b.fdroid.room.model.Locale;
import de.k3b.fdroid.room.model.Localized;
import de.k3b.fdroid.room.model.Repo;
import de.k3b.fdroid.room.model.Version;

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

