/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid project.
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
package de.k3b.fdroid.android;

import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.android.html.AndroidStringResourceMustacheContext;
import de.k3b.fdroid.android.repository.FDroidDatabaseFactory;
import de.k3b.fdroid.android.v1.service.AndroidServiceFactory;
import de.k3b.fdroid.html.util.MustacheEx;

public class FDroidApplication extends Application {
    public static final Executor executor = Executors.newFixedThreadPool(4);
    private static AndroidServiceFactory androidServiceFactory = null;

    private static FDroidDatabaseFactory fdroidDatabaseFactory = null;

    public static AndroidServiceFactory getAndroidServiceFactory() {
        return androidServiceFactory;
    }

    public static FDroidDatabaseFactory getFdroidDatabase() {
        return fdroidDatabaseFactory;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fdroidDatabaseFactory = FDroidDatabase.getINSTANCE(this, false);
        androidServiceFactory = new AndroidServiceFactory(
                this, fdroidDatabaseFactory);
        MustacheEx.addFixedProperty("t", new AndroidStringResourceMustacheContext(this));
    }
}
