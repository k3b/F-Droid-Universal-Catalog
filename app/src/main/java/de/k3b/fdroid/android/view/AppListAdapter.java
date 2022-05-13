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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.html.AndroidStringResourceMustacheContext;
import de.k3b.fdroid.android.html.util.HtmlUtil;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.html.service.FormatService;
import de.k3b.fdroid.service.AppWithDetailsPagerService;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private static final String TAG = Global.LOG_TAG + "AppList";

    private final AppWithDetailsPagerService details;

    private final FormatService formatService;
    private final int defaultBackgroundColor;
    private final int defaultForegroundColor;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAbsoluteAdapterPosition() + "/"
                                    + getBindingAdapterPosition() + " clicked.");
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     */
    public AppListAdapter(Context context, AppWithDetailsPagerService details) {
        this.details = details;
        formatService = new FormatService("list_repo", Repo.class,
                new AndroidStringResourceMustacheContext(context));

        this.defaultBackgroundColor = HtmlUtil.getDefaultBackgroundColor(context);
        this.defaultForegroundColor = HtmlUtil.getDefaultForegroundColor(context);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.app_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        // Get element from your dataset at this position and replace the contents of the view
        // with that element
        viewHolder.getTextView().setText(details.getName(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return details.size();
    }
}
