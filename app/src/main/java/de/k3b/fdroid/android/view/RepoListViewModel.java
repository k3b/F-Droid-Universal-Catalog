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

import java.util.List;

import de.k3b.fdroid.android.FDroidApplication;
import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.domain.Repo;

public class RepoListViewModel extends DownloadViewModel {
    private final MutableLiveData<List<Repo>> repoList = new MutableLiveData<>();
    private final MutableLiveData<Repo> currentDownloaditem = new MutableLiveData<>();

    public RepoListViewModel() {
        reload();
    }

    @Override
    public void reload() {
        Log.i(Global.LOG_TAG_APP, "Start reload repo");
        setCurrentRepo(null);
        FDroidApplication.executor.execute(() -> {
            getRepoList().postValue(repoRepository.findAll());
        });
    }

    public MutableLiveData<List<Repo>> getRepoList() {
        return repoList;
    }

    public void toggleAutoDownloadEnabled(Repo repo) {
        repo.setAutoDownloadEnabled(!repo.isAutoDownloadEnabled());
        saveChanges(repo);
    }

    protected void saveChanges(Repo repo) {
        super.saveChanges(repo);
        reload();
    }

    public Repo findByDownloadTaskId(String downloadTaskId) {
        return Repo.findByDownloadTaskId(getRepoList().getValue(), downloadTaskId);
    }

}
