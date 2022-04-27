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

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.service.AppWithDetailsPagerService;
import de.k3b.fdroid.service.adapter.AppRepositoryAdapterImpl;


public class AppListActivity extends Activity {
    private static final int DATASET_COUNT = 60;
    protected String[] mDataset;
    protected AppListAdapter mAdapter;
    protected RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_list);

        mRecyclerView =  findViewById(R.id.recyclerView);

        AppRepository appRepository = FDroidDatabase.getINSTANCE(this).appRepository();
        String search = "k3b";
        List<Integer> appIdList = appRepository.findIdsByExpressionSortByScore(search);

        AppWithDetailsPagerService details = new AppWithDetailsPagerService(
                new AppRepositoryAdapterImpl(appRepository),
                null, // new LocalizedRepositoryAdapterImpl(localizedRepository),
                null, null);

        details.init(appIdList, 10);

        mAdapter = new AppListAdapter(details);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
    }
}
