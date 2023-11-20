/*
 * Copyright (c) 2023 by k3b.
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
package de.k3b.fdroid.domain.entity.facade;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.entity.common.IRepoCommon;
import de.k3b.fdroid.domain.entity.common.RepoCommon;

@SuppressWarnings("unused")
public class RepoCommonFacade extends EntityCommon implements IRepoCommon {
    @Nullable
    private final IRepoCommon content;

    public RepoCommonFacade(@Nullable IRepoCommon content) {
        this.content = content;
    }

    public IRepoCommon getSpecialContent() {
        return null;
    }

    @Override
    public String getName() {
        IRepoCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getName();
        if (this.content != null && isEmpty(o)) o = this.content.getName();

        return o;
    }

    @Override
    public long getTimestamp() {
        IRepoCommon content = getSpecialContent();
        long o = 0;

        if (content != null) o = content.getTimestamp();
        if (this.content != null && isEmpty(o)) o = this.content.getTimestamp();

        return o;
    }

    @Override
    public String getIcon() {
        IRepoCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getIcon();
        if (this.content != null && isEmpty(o)) o = this.content.getIcon();

        return o;
    }

    @Override
    public String getAddress() {
        IRepoCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getAddress();
        if (this.content != null && isEmpty(o)) o = this.content.getAddress();

        return o;
    }

    @Override
    public String getDescription() {
        IRepoCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getDescription();
        if (this.content != null && isEmpty(o)) o = this.content.getDescription();

        return o;
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        RepoCommon.toStringBuilder(sb, this);
    }
}
