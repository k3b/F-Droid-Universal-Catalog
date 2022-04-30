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
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.k3b.fdroid.android.AndroidServiceFactory;
import de.k3b.fdroid.android.FDroidApplicaton;
import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.android.service.ImportV1AndroidWorker;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.v1.service.V1DownloadAndImportService;


public class RepoListActivity extends Activity {
    protected RepoListAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected RepoRepository repoRepository;
    private List<Repo> items = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repo_list);
        setTitle(R.string.label_repo_title);

        mRecyclerView = findViewById(R.id.recyclerView);

        repoRepository = FDroidDatabase.getINSTANCE(this).repoRepository();

        FDroidApplicaton.executor.execute(() -> {
            List<Repo> repos = repoRepository.findAll();

            runOnUiThread(() -> {
                mAdapter = new RepoListAdapter(RepoListActivity.this, repos);
                // Set CustomAdapter as the adapter for RecyclerView.
                mRecyclerView.setAdapter(mAdapter);
                items = repos;

            });
        });
    }

    public void onRepoClick(Repo repo) {
        repo.setAutoDownloadEnabled(!repo.isAutoDownloadEnabled());
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
        ImportV1AndroidWorker.scheduleDownload(this, repo.getId());
        return true;
    }
}
