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
package de.k3b.fdroid.android.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;

import java.io.File;
import java.util.concurrent.Executor;

import de.k3b.fdroid.domain.interfaces.CachedDownloadImageService;

/**
 * {@link Html.ImageGetter} used to resolve images in a {@link android.widget.TextView}
 * via  myTextView.setText({@link Html#fromHtml(String, Html.ImageGetter, Html.TagHandler)});
 * <p>
 * The image is either rendered immediatley from local cached {@link File}
 * or it is downloaded and display is postponed until download is complete.
 */
public class CachedDownloadImageGetter implements Html.ImageGetter {
    private final Context context;
    private final View imageViewOwner;
    private final int imageSize;
    private final CachedDownloadImageService cachedDownloadImageService;
    private final Executor threadExecutor;

    public CachedDownloadImageGetter(
            Context context, View imageViewOwner,
            float imageSize, CachedDownloadImageService cachedDownloadImageService,
            Executor threadExecutor) {
        this.context = context;
        this.imageViewOwner = imageViewOwner;
        this.imageSize = (int) imageSize;
        this.cachedDownloadImageService = cachedDownloadImageService;
        this.threadExecutor = threadExecutor;
    }

    @Override
    public Drawable getDrawable(String source) {

        CachedDownloadDrawable result = new CachedDownloadDrawable();
        final String packageName = new File(source).getName();
        File iconFile = cachedDownloadImageService.getExistingLocalIconFileOrNull(packageName);

        if (iconFile != null) {
            updateDrawable(result, iconFile);
        } else {
            threadExecutor.execute(() -> {
                File downloadLocalIconFile = cachedDownloadImageService.getOrDownloadLocalIconFile(packageName);
                if (downloadLocalIconFile != null) {
                    updateDrawable(result, downloadLocalIconFile);
                }
            });
        }
        return result;
    }

    private void updateDrawable(CachedDownloadDrawable result, File iconFile) {
        if (iconFile != null) {
            result.set(iconFile, imageSize);
            imageViewOwner.post(imageViewOwner::invalidate);
        }
    }
}
