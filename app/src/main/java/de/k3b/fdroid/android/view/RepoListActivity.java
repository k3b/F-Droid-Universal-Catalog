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

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import de.k3b.fdroid.android.FDroidApplication;
import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.service.AndroidWorkerProgressObserver;
import de.k3b.fdroid.android.service.ImportV1AndroidWorker;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.RepoRepository;

public class RepoListActivity extends Activity {
    protected RepoListAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected RepoRepository repoRepository;
    private List<Repo> items = null;
    private TextView mStatusView;
    private AndroidWorkerProgressObserver repoDownloadObserver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repo_list);
        setTitle(R.string.label_repo_title);

        mRecyclerView = findViewById(R.id.recyclerView);
        mStatusView = findViewById(R.id.status);

        repoRepository = FDroidApplication.getFdroidDatabase().repoRepository();

        reload();
    }

    private void reload() {
        Log.i(Global.LOG_TAG_APP, "Start reload repo");
        FDroidApplication.executor.execute(() -> {
            List<Repo> repos = repoRepository.findAll();
            runOnUiThread(() -> onReloadDone(repos));
        });
    }

    // @MainThread
    private void onReloadDone(List<Repo> repos) {
        Log.i(Global.LOG_TAG_APP, "done reload repo");
        fixRepoDownloadObserver(repos);

        this.items = repos;
        this.mAdapter = new RepoListAdapter(this, repos);
        // Set CustomAdapter as the adapter for RecyclerView.
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    private void fixRepoDownloadObserver(List<Repo> repos) {
        // finish current observer if done.
        if (this.repoDownloadObserver != null && !this.repoDownloadObserver.isAlive()) {
            Log.i(Global.LOG_TAG_APP, "destroy repoDownloadObserver for " + repoDownloadObserver.getRepoId());

            this.repoDownloadObserver.onDestroy();
            this.repoDownloadObserver = null;
        }

        Repo busy;
        AndroidWorkerProgressObserver newRepoObserver = null;
        while (this.repoDownloadObserver == null
                && (busy = repoRepository.getBusy(repos)) != null) {

            if (newRepoObserver == null) {
                newRepoObserver = new AndroidWorkerProgressObserver(
                        this.mStatusView, () -> reload());
            }

            if (ImportV1AndroidWorker.registerProgressObserver(busy.getDownloadTaskId(), newRepoObserver)) {
                this.repoDownloadObserver = newRepoObserver;
                this.repoDownloadObserver.setRepoId(busy.getId());
                Log.i(Global.LOG_TAG_APP, "created repoDownloadObserver for " + repoDownloadObserver.getRepoId());

                newRepoObserver = null;
            } else {
                busy.setDownloadTaskId(null); // not busy any more
                Log.i(Global.LOG_TAG_APP, "Repo " + busy.getId() + " not busy any more.");
                repoRepository.save(busy);
            }
        }
        if (newRepoObserver != null) {
            Log.i(Global.LOG_TAG_APP, "repoDownloadObserver : no busy repo found");
            newRepoObserver.onDestroy();
            newRepoObserver = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (repoDownloadObserver != null) {
            repoDownloadObserver.onDestroy();
        }
        repoDownloadObserver = null;
        super.onDestroy();
    }

    public void onRepoClick(Repo repo) {
        repo.setAutoDownloadEnabled(!repo.isAutoDownloadEnabled());
        saveChanges(repo);
    }

    private void saveChanges(Repo repo) {
        repoRepository.update(repo);
        int position = items.indexOf(repo);

        RecyclerView.Adapter<?> adapter = mRecyclerView.getAdapter();
        if (adapter != null) adapter.notifyItemChanged(position);
    }

    public void onRepoButtonClick(View view, final Repo repo) {
        PopupMenu menu = new PopupMenu(this, view);
        getMenuInflater().inflate(R.menu.menu_repo_list, menu.getMenu());
        menu.setOnMenuItemClickListener(menuItem -> onRepoMenuClick(menuItem, repo));
        menu.show();
    }

    private boolean onRepoMenuClick(MenuItem menuItem, Repo repo) {
        if (menuItem.getItemId() == R.id.cmd_download) {
            return onCmdDownload(menuItem, repo);
        }
        return false;
    }

    private boolean onCmdDownload(MenuItem menuItem, Repo repo) {
        UUID uuid = ImportV1AndroidWorker.scheduleDownload(this, repo.getId());
        repo.setDownloadTaskId(uuid.toString());
        saveChanges(repo);
        return true;
    }
}
