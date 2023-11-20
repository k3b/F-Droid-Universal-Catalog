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
import de.k3b.fdroid.domain.entity.common.ILocalizedCommon;
import de.k3b.fdroid.domain.entity.common.LocalizedCommon;

@SuppressWarnings("unused")
public class LocalizedCommonFacade extends EntityCommon implements ILocalizedCommon {
    @Nullable
    private final ILocalizedCommon content;

    public LocalizedCommonFacade(@Nullable ILocalizedCommon content) {
        this.content = content;
    }

    public ILocalizedCommon getSpecialContent() {
        return null;
    }

    @Override
    public String getName() {
        ILocalizedCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getName();
        if (this.content != null && isEmpty(o)) o = this.content.getName();

        return o;
    }

    @Override
    public String getSummary() {
        ILocalizedCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getSummary();
        if (this.content != null && isEmpty(o)) o = this.content.getSummary();

        return o;
    }

    @Override
    public String getDescription() {
        ILocalizedCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getDescription();
        if (this.content != null && isEmpty(o)) o = this.content.getDescription();

        return o;
    }

    @Override
    public String getIcon() {
        ILocalizedCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getIcon();
        if (this.content != null && isEmpty(o)) o = this.content.getIcon();

        return o;
    }

    @Override
    public String getVideo() {
        ILocalizedCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getVideo();
        if (this.content != null && isEmpty(o)) o = this.content.getVideo();

        return o;
    }

    @Override
    public String getWhatsNew() {
        ILocalizedCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getWhatsNew();
        if (this.content != null && isEmpty(o)) o = this.content.getWhatsNew();

        return o;
    }


    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        LocalizedCommon.toStringBuilder(sb, this);
    }
}
