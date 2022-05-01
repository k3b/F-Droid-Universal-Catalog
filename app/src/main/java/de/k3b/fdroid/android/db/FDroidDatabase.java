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

import de.k3b.fdroid.android.repository.AppDao;
import de.k3b.fdroid.android.repository.AppRepositoryAdapter;
import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.AppCategory;
import de.k3b.fdroid.domain.AppHardware;
import de.k3b.fdroid.domain.Category;
import de.k3b.fdroid.domain.HardwareProfile;
import de.k3b.fdroid.domain.Locale;
import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.interfaces.AppRepository;

@Database(version = 1, entities = {App.class, AppCategory.class, Category.class, Locale.class,
        Localized.class, Repo.class, Version.class, AppHardware.class, HardwareProfile.class})
public abstract class FDroidDatabase extends RoomDatabase implements FDroidDatabaseFactory {
    private static FDroidDatabaseFactory INSTANCE = null;

    private AppRepository appRepository = null;

    public static FDroidDatabaseFactory getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    FDroidDatabase.class, "FDroidData.db")
                    .allowMainThreadQueries()
                    .build();

        }
        return INSTANCE;
    }

    @Override
    public AppRepository appRepository() {
        if (appRepository == null) appRepository = new AppRepositoryAdapter(appDao());
        return appRepository;
    }

    public abstract AppDao appDao();

}

