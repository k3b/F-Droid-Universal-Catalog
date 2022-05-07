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
package de.k3b.fdroid.android.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * Same as {@link ActivityResultContract} with additional method to check in advance
 * if there is at least one matching activity available.
 *
 * @param <I> input type
 * @param <O> output typ
 */
public abstract class ActivityResultContractEx<I, O> extends ActivityResultContract<I, O> {
    public boolean isAvailable(@NonNull Context context, I input) {
        PackageManager pm = context.getPackageManager();
        Intent intent = createIntent(context, input);
        List<ResolveInfo> resolveInfo = pm.queryIntentActivities(
                intent, 0);
        return !resolveInfo.isEmpty();
    }
}
