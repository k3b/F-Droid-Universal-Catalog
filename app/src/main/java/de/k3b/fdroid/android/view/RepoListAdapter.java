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
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.android.R;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.service.FormatService;
import de.k3b.fdroid.service.adapter.ValueAndStringTranslations;

public class RepoListAdapter extends RecyclerView.Adapter<RepoListAdapter.ViewHolder> {
    private static final String TAG = Global.LOG_TAG + "RepoList";

    private final AndroidStringResourceMustacheContext res;
    private final FormatService formatService;
    private List<Repo> details;

    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final Button button;
        Repo repo;

        public ViewHolder(View v) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(v1 -> getActivity(v1).onRepoClick(repo));
            textView = v.findViewById(R.id.textView);
            button = v.findViewById(R.id.button);
            button.setOnClickListener(v1 -> getActivity(v1).onRepoButtonClick(button, repo));
        }

        private RepoListActivity getActivity(View v1) {
            RepoListActivity repoListActivity = (RepoListActivity) v1.getContext();
            return repoListActivity;
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter.
     *
     */
    public RepoListAdapter(Context context, List<Repo> details) {
        this.details = details;
        res = new AndroidStringResourceMustacheContext(context);
        formatService = new FormatService(context.getString(R.string.list_repo));
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.repo_row_item, viewGroup, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element " + position + " set.");

        Repo repo = details.get(position);
        viewHolder.repo = repo;
        ValueAndStringTranslations vt = new ValueAndStringTranslations(repo, res);
        String html = formatService.format (vt);
        Spanned spanned;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            spanned = Html.fromHtml(html, Html.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE);
        } else {
            spanned = Html.fromHtml(html);
        }
        viewHolder.getTextView().setText(spanned);

        // workaround: android Html.fromHtml(...) does not support BackgroundColor
        // TODO how to do this with themes ?
        int color = repo.isAutoDownloadEnabled() ? R.color.lime : R.color.white;
        viewHolder.getTextView().setBackgroundResource(color);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return details.size();
    }
}
