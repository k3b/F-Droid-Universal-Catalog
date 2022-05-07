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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.databinding.ActivityRepoListBinding;
import de.k3b.fdroid.android.util.BarcodeScanActivityResultContract;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_repo_list, menu);
        return result;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.cmd_scan_qr);
        if (menuItem != null) {
            menuItem.setVisible(new BarcodeScanActivityResultContract()
                    .isAvailable(this, null));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle action bar menuItem clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menuItem.getItemId();

        if (id == R.id.cmd_reload_list) {
            viewModel.reload();
            return true;
        } else if (id == R.id.cmd_scan_qr) {
            return onCmdBarcode();
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }

    private boolean onCmdBarcode() {
        BarcodeScanActivityResultContract contract = new BarcodeScanActivityResultContract();
        if (contract.isAvailable(this, null)) {
            registerForActivityResult(contract,
                    s -> Log.e(Global.LOG_TAG_APP, "Scanresult " + s)).launch(null);
        } else {
            Log.e(Global.LOG_TAG_APP, "No scanner installed");
        }

        return true;
    }

    public void onRepoClick(Repo repo) {
        viewModel.toggleAutoDownloadEnabled(repo);
    }

    public void onRepoButtonClick(View view, final Repo repo) {
        PopupMenu menu = new PopupMenu(this, view);
        getMenuInflater().inflate(R.menu.menu_repo_list_item, menu.getMenu());
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
