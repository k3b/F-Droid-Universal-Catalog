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
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.service.AppWithDetailsPagerService;
import de.k3b.fdroid.service.adapter.AppRepositoryAdapterImpl;


public class RepoListActivity extends Activity {
    protected RepoListAdapter mAdapter;
    protected RecyclerView mRecyclerView;
    protected RepoRepository repoRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repo_list);
        setTitle(R.string.label_repo_title);

        mRecyclerView =  findViewById(R.id.recyclerView);

        repoRepository = FDroidDatabase.getINSTANCE(this).repoRepository();

        new RepoAsyncTask().execute();
    }

    private class RepoAsyncTask extends AsyncTask<Integer, Integer, List<Repo>> {
        @Override
        protected List<Repo> doInBackground(Integer... params) {
            return repoRepository.findAll();
        }

        @Override
        protected void onPostExecute(List<Repo> repos) {
            super.onPostExecute(repos);
            mAdapter = new RepoListAdapter(RepoListActivity.this, repos);
            // Set CustomAdapter as the adapter for RecyclerView.
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}
