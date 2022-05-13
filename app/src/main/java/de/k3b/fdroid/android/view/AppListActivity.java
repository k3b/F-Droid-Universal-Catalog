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
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.databinding.ActivityAppListBinding;

public class AppListActivity extends BaseActivity {
    protected AppListAdapter mAdapter;
    protected RecyclerView mRecyclerView;

    private AppListViewModel viewModel;
    private ActivityAppListBinding binding;

    public static void showActivity(Activity context) {
        Intent intent = new Intent(context, AppListActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAppListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AppListViewModel.class);

        /*
        mRecyclerView =  findViewById(R.id.recyclerView);

        AppRepository appRepository = FDroidApplication.getFdroidDatabase().appRepository();
        List<Integer> appIdList = appRepository.findDynamic(new AppRepositoryFindDynamic.AppSearchParameter().text("k3b"));

        AppWithDetailsPagerService details = new AppWithDetailsPagerService(
                new AppRepositoryAdapterImpl(appRepository),
                null, // new LocalizedRepositoryAdapterImpl(localizedRepository),
                null, null);

        details.init(appIdList, 10);

        mAdapter = new AppListAdapter(details);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
         */
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.getPagerData().observe(this, repoList -> {
            AppListAdapter repoListAdapter = new AppListAdapter(this, repoList);
            binding.recyclerView.setAdapter(repoListAdapter);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_app_list, menu);
        return result;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        // Handle action bar menuItem clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menuItem.getItemId();

        if (id == R.id.cmd_repos) {
            RepoListActivity.showActivity(this);
            this.finish();
            return true;
        } else {
            return super.onOptionsItemSelected(menuItem);
        }
    }


}
