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
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import de.k3b.android.util.BarcodeScanActivityResultContract;
import de.k3b.fdroid.android.FDroidApplication;
import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.databinding.ActivityRepoListBinding;
import de.k3b.fdroid.domain.entity.Repo;

// AppCompatActivity:1,4,1 requires minsdk 17
public class RepoListActivity extends BaseActivity {
    private RepoListViewModel viewModel;
    private ActivityRepoListBinding binding;

    private final BarcodeScanActivityResultContract barcodeScanContract = new BarcodeScanActivityResultContract();
    private final ActivityResultLauncher<Void> barcodeScanLauncher
            = registerForActivityResult(barcodeScanContract, this::onCmdBarcodeResult);

    public static void showActivity(Activity context) {
        Intent intent = new Intent(context, RepoListActivity.class);

        context.startActivity(intent);
    }

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
            RepoListAdapter repoListAdapter = new RepoListAdapter(this, repoList,
                    FDroidApplication.getAndroidServiceFactory().getRepoIconService(),
                    FDroidApplication.executor);
            binding.recyclerView.setAdapter(repoListAdapter);
        });
        viewModel.getStatus().observe(this, s -> binding.status.setText(s));
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
            menuItem.setVisible(barcodeScanContract.isAvailable(this, null));
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
        } else if (id == R.id.cmd_apps) {
            AppListActivity.showActivity(this);
            this.finish();
            return true;
        } else if (id == R.id.cmd_scan_qr) {
            return onCmdBarcode();
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }

    private boolean onCmdBarcode() {
        try {
            if (barcodeScanContract.isAvailable(this, null)) {
                barcodeScanLauncher.launch(null);
            } else {
                Log.e(Global.LOG_TAG_APP, "No scanner installed");
            }
        } catch (Exception ex) {
            Log.e(Global.LOG_TAG_APP, "error", ex);
        }

        return true;
    }

    private void onCmdBarcodeResult(String s) {
        Log.e(Global.LOG_TAG_APP, "Scanresult " + s);
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
