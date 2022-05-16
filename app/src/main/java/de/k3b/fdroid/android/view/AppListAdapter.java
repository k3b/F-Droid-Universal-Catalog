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
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.android.FDroidApplication;
import de.k3b.fdroid.android.R;
import de.k3b.fdroid.android.gui.CachedDownloadImageGetter;
import de.k3b.fdroid.android.html.AndroidStringResourceMustacheContext;
import de.k3b.fdroid.android.html.util.HtmlUtil;
import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.html.service.FormatService;
import de.k3b.fdroid.service.AppWithDetailsPagerService;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private static final String TAG = Global.LOG_TAG + "AppList";
    public static final int ICON_SIZE_DP = 40;

    private final AppWithDetailsPagerService details;

    private final FormatService formatService;
    private final int defaultBackgroundColor;
    private final int defaultForegroundColor;
    private final Html.ImageGetter imageGetter;

    /**
     * Initialize the dataset of the Adapter.
     */
    public AppListAdapter(Context context, View imageViewOwner, AppWithDetailsPagerService details) {
        this.details = details;

        formatService = new FormatService("list_app_summary", App.class,
                new AndroidStringResourceMustacheContext(context));

        this.defaultBackgroundColor = HtmlUtil.getDefaultBackgroundColor(context);
        this.defaultForegroundColor = HtmlUtil.getDefaultForegroundColor(context);
        float iconSize = context.getResources().getDisplayMetrics().density * ICON_SIZE_DP;

        this.imageGetter = new CachedDownloadImageGetter(
                context, imageViewOwner, iconSize,
                FDroidApplication.getAndroidServiceFactory().getAppIconService(),
                FDroidApplication.executor
        );
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        viewHolder.item = details.itemAtOffset(position);
        TextView textView = viewHolder.getTextView();

        String html = formatService.format(viewHolder.item);
        HtmlUtil.setHtml(textView, html, defaultForegroundColor, defaultBackgroundColor, imageGetter);
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

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return details.size();
    }

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private AppWithDetailsPagerService.ItemAtOffset item;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(v1 -> Log.d(TAG, "Element " + getAbsoluteAdapterPosition() + "/"
                    + getBindingAdapterPosition() + " clicked."));
            textView = v.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
