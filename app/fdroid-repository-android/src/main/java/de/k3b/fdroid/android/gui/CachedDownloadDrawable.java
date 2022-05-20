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

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.File;

/**
 * A BitmapDrawable based on a local file that may not exist yet,
 * because download is not complete yet
 */
public class CachedDownloadDrawable extends BitmapDrawable {
    protected int iconSize;
    protected File iconFile;

    public CachedDownloadDrawable(Resources resources) {
        super(resources);
    }

    public void set(File iconFile, int iconSize) {
        this.iconFile = iconFile;
        this.iconSize = iconSize;
    }

    @Override
    public void draw(Canvas canvas) {
        if (iconFile != null) {
            Drawable iconDrawable = BitmapDrawable.createFromPath(iconFile.getAbsolutePath());
            if (iconSize != 0) {
                iconDrawable.setBounds(0, 0, iconSize, iconSize);
            } else {
                iconDrawable.setBounds(0, 0, getIntrinsicWidth(), getIntrinsicHeight());
            }
            iconDrawable.draw(canvas);
        }
    }
}