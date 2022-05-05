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
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.databinding.ActivityRepoListBinding;
import de.k3b.fdroid.domain.Repo;

// AppCompatActivity:1,4,1 requires minsdk 17
public class RepoListActivity extends BaseActivity {
    private RepoListViewModel viewModel;
    private ActivityRepoListBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRepoListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(R.string.label_repo_title);

        viewModel = new ViewModelProvider(this).get(RepoListViewModel.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getRepoList().observe(this, repoList -> {
            RepoListAdapter repoListAdapter = new RepoListAdapter(this, repoList);
            binding.recyclerView.setAdapter(repoListAdapter);
        });
        viewModel.getDownloadStatus().observe(this, s -> binding.status.setText(s));
    }

    public void onRepoClick(Repo repo) {
        viewModel.toggleAutoDownloadEnabled(repo);
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
        viewModel.onCmdDownload(this, repo);
        return true;
    }
}
