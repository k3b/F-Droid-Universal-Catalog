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

package de.k3b.fdroid.android.view;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import de.k3b.fdroid.android.FDroidApplication;
import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.domain.AppSearchParameter;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.service.AppWithDetailsPagerService;
import de.k3b.fdroid.service.adapter.AppRepositoryAdapterImpl;

/**
 * Android-Gui calls {@link #setFilter(AppSearchParameter)} and gets updated
 * if registered to {@link #getPagerData()}
 */
public class AppListViewModel extends ViewModel {
    private static final int PAGESIZE = 10;
    private final AppRepository appRepository = FDroidApplication.getFdroidDatabase().appRepository();

    private final AppWithDetailsPagerService pager = new AppWithDetailsPagerService(
            new AppRepositoryAdapterImpl(appRepository),
            null, null, null);

    private final MutableLiveData<AppSearchParameter> filter
            // = new MutableLiveData<>(new AppSearchParameter().text("k3b"));
            = new MutableLiveData<>(new AppSearchParameter());
    private final MutableLiveData<AppWithDetailsPagerService> pagerData = new MutableLiveData<>();

    public AppListViewModel() {
        reload();
    }

    public void reload() {
        Log.i(Global.LOG_TAG_APP, "Start reload app");
        FDroidApplication.executor.execute(() -> getPagerData().postValue(pager.init(
                appRepository.findDynamic(getFilter().getValue()), PAGESIZE)));
    }

    public MutableLiveData<AppSearchParameter> getFilter() {
        return filter;
    }

    public void setFilter(AppSearchParameter filter) {
        this.filter.setValue(filter);
    }

    public MutableLiveData<AppWithDetailsPagerService> getPagerData() {
        return pagerData;
    }
}
