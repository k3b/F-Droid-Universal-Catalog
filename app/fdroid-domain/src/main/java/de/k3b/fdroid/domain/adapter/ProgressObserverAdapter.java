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
package de.k3b.fdroid.domain.adapter;

import de.k3b.fdroid.domain.interfaces.IProgressObserver;

public class ProgressObserverAdapter implements IProgressObserver {
    private final IProgressObserver child;

    public ProgressObserverAdapter(IProgressObserver child) {

        this.child = child;
    }

    @Override
    public void onProgress(int counter, String progressChar, String progressContext) {
        child.onProgress(counter, progressChar, progressContext);
    }

    @Override
    public IProgressObserver setProgressContext(String progressPrefix, String progressSuffix) {
        return child.setProgressContext(progressPrefix, progressSuffix);
    }

    @Override
    public void log(String message) {
        child.log(message);
    }
}
