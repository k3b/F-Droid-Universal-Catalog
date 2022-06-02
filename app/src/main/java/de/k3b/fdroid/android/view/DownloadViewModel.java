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

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import java.util.UUID;

import de.k3b.fdroid.android.FDroidApplication;
import de.k3b.fdroid.android.service.ImportV1AndroidWorker;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId;
import de.k3b.fdroid.domain.repository.RepoRepository;

public class DownloadViewModel extends StatusViewModel {
    protected final RepoRepository repoRepository = FDroidApplication.getFdroidDatabase().repoRepository();
    private final MutableLiveData<Repo> currentRepo = new MutableLiveData<>();

    public void onCmdDownload(Context context, Repo repo) {
        UUID uuid = ImportV1AndroidWorker.scheduleDownload(context, repo.getId());
        String oldDownloadTaskId = repo.getDownloadTaskId();
        if (oldDownloadTaskId == null || uuid.toString().compareTo(oldDownloadTaskId) != 0) {
            repo.setDownloadTaskId(uuid.toString());
            saveChanges(repo);
        }


        WorkManager.getInstance(context).getWorkInfoByIdLiveData(uuid).observe(
                (LifecycleOwner) context, this::onStatusChange);
    }

    protected void onStatusChange(WorkInfo workInfo) {
        super.onStatusChange(workInfo);
        if (workInfo != null && workInfo.getState().isFinished()) {
            setCurrentRepoInstance(null); // memorized value is probably outdated
            reload();
        }
    }

    protected void saveChanges(Repo repo) {
        repoRepository.update(repo);
        setCurrentRepo(repo);
    }

    public void reload() {
    }

    /**
     * sets only if id is defferent
     */
    public void setCurrentRepoInstance(Repo repo) {
        Repo old = getCurrentRepo().getValue();
        if (!DatabaseEntityWithId.sameId(old, repo)) {
            setCurrentRepo(repo);
        }
    }

    public MutableLiveData<Repo> getCurrentRepo() {
        return currentRepo;
    }

    public void setCurrentRepo(Repo repo) {
        getCurrentRepo().setValue(repo);
    }
}
