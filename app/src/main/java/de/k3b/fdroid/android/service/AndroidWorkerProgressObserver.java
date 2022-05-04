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

package de.k3b.fdroid.android.service;

import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.work.Data;

import java.lang.ref.WeakReference;

/**
 * Observer that can be attached to a {@link androidx.work.Worker}
 * so that an Activity can receive async progressmessages from
 * {@link androidx.work.Worker#setProgressAsync(Data)}.
 * <p>
 * See {@link ImportV1AndroidWorker#registerProgressObserver(String, AndroidWorkerProgressObserver)}
 * for an example.
 */
public class AndroidWorkerProgressObserver implements LifecycleOwner {
    /* Required by {@link LifecycleOwner} */
    private final LifecycleRegistry lifecycle;

    @Nullable
    private final WeakReference<TextView> progressMessageTextView;
    @Nullable
    private final WeakReference<Runnable> onNullProgressMessageHandler;

    /**
     * Use with {@link ImportV1AndroidWorker#registerProgressObserver(String, AndroidWorkerProgressObserver)}
     *
     * @param progressMessageTextView      progress messages will be displayed here.
     * @param onNullProgressMessageHandler is called when a null-progress-message is received.
     *                                     Used to reload list from database and refresh display.
     */
    public AndroidWorkerProgressObserver(
            @Nullable TextView progressMessageTextView,
            @Nullable Runnable onNullProgressMessageHandler) {
        lifecycle = new LifecycleRegistry(this);
        this.progressMessageTextView = (progressMessageTextView == null)
                ? null
                : new WeakReference<>(progressMessageTextView);
        this.onNullProgressMessageHandler = (onNullProgressMessageHandler == null)
                ? null :
                new WeakReference<>(onNullProgressMessageHandler);
        lifecycle.setCurrentState(Lifecycle.State.STARTED);
    }

    /* Required by {@link LifecycleOwner} */
    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return lifecycle;
    }

    public Runnable getOnNullProgressMessageHandler() {
        if (onNullProgressMessageHandler == null) return null;
        return onNullProgressMessageHandler.get();
    }

    public TextView getProgressMessageTextView() {
        if (progressMessageTextView == null) return null;
        return progressMessageTextView.get();
    }

    @MainThread
    public void onProgressMessage(String progressMessage) {
        TextView textView = getProgressMessageTextView();
        Runnable onNullProgressMessageHandler = getOnNullProgressMessageHandler();
        if (textView != null) {
            if (progressMessage != null) {
                textView.setText(progressMessage);
            } else if (onNullProgressMessageHandler != null) {
                onNullProgressMessageHandler.run();
            }
        }
    }

    /**
     * Must be called in owner-onDestroy method to stop receiving
     * progress messages (i.e. in MyActivity#onDestroy())
     */
    public void onDestroy() {
        lifecycle.setCurrentState(Lifecycle.State.DESTROYED);
    }
}
