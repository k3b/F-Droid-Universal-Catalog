/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid project.
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
package de.k3b.android.util;

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

        /* Lint warning
            "Consider adding a `<queries>` declaration to your manifest when calling this
            method; see @see <a href="https://g.co/dev/packagevisibility">packagevisibility</a>
            for details"

            @see <a href="https://g.co/dev/packagevisibility">packagevisibility</a> =
            @see <a href="https://developer.android.com/training/package-visibility/declaring">package-visibility/declaring</a>

            From the docs:
            If your app targets Android 11 (api 30) or higher Manifest may need a <queries> element.

            Promlem: I do not know in advance which Package(=android-app-id) will privide the barcode scanner-

            With current targetSdk=31 (Android 12) the code is working on my android-10 device.
         */
        List<ResolveInfo> resolveInfo = pm.queryIntentActivities(
                intent, 0);
        return !resolveInfo.isEmpty();
    }
}
