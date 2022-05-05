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

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.UUID;

import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.databinding.ActivityRepoListBinding;
import de.k3b.fdroid.android.service.AndroidWorkerProgressObserver;
import de.k3b.fdroid.android.service.ImportV1AndroidWorker;
import de.k3b.fdroid.domain.Repo;

// AppCompatActivity:1,4,1 requires minsdk 17
public class RepoListActivity extends BaseActivity {
    private RepoListViewModel repoListViewModel;
    private ActivityRepoListBinding binding;

    private AndroidWorkerProgressObserver repoDownloadObserver = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRepoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(R.string.label_repo_title);

        repoListViewModel = new ViewModelProvider(this).get(RepoListViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        repoListViewModel.getItems().observe(this, repos -> {
            RepoListAdapter repoListAdapter = new RepoListAdapter(this, repos);
            binding.recyclerView.setAdapter(repoListAdapter);
        });
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
                && (busy = repoListViewModel.repoRepository.getBusy(repos)) != null) {

            if (newRepoObserver == null) {
                newRepoObserver = new AndroidWorkerProgressObserver(
                        this.binding.status, () -> repoListViewModel.reload());
            }

            if (ImportV1AndroidWorker.registerProgressObserver(busy.getDownloadTaskId(), newRepoObserver)) {
                this.repoDownloadObserver = newRepoObserver;
                this.repoDownloadObserver.setRepoId(busy.getId());
                Log.i(Global.LOG_TAG_APP, "created repoDownloadObserver for " + repoDownloadObserver.getRepoId());

                newRepoObserver = null;
            } else {
                busy.setDownloadTaskId(null); // not busy any more
                Log.i(Global.LOG_TAG_APP, "Repo " + busy.getId() + " not busy any more.");
                repoListViewModel.repoRepository.save(busy);
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
        repoListViewModel.repoRepository.update(repo);
        int position = repoListViewModel.getItems().getValue().indexOf(repo);

        RecyclerView.Adapter<?> adapter = binding.recyclerView.getAdapter();
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
