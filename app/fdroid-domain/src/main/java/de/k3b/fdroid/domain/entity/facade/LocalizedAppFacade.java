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

import de.k3b.fdroid.domain.entity.common.IAppCommon;
import de.k3b.fdroid.domain.entity.common.ILocalizedCommon;
import de.k3b.fdroid.domain.entity.common.LocalizedCommon;

@SuppressWarnings("unused")
public class LocalizedAppFacade extends AppCommonFacade implements IAppCommon, ILocalizedCommon {
    private final ILocalizedCommon localizedCommon;

    public LocalizedAppFacade(@Nullable IAppCommon appCommon, @Nullable ILocalizedCommon localizedCommon) {
        super(appCommon);
        this.localizedCommon = localizedCommon;
    }

    public ILocalizedCommon getSpecialLocalizedContent() {
        return null;
    }

    @Override
    public String getName() {
        ILocalizedCommon localizedCommon = getSpecialLocalizedContent();
        String o = null;

        if (localizedCommon != null) o = localizedCommon.getName();
        if (this.localizedCommon != null && isEmpty(o)) o = this.localizedCommon.getName();

        return o;
    }

    @Override
    public String getSummary() {
        ILocalizedCommon localizedCommon = getSpecialLocalizedContent();
        String o = null;

        if (localizedCommon != null) o = localizedCommon.getSummary();
        if (this.localizedCommon != null && isEmpty(o)) o = this.localizedCommon.getSummary();

        return o;
    }

    @Override
    public String getDescription() {
        ILocalizedCommon localizedCommon = getSpecialLocalizedContent();
        String o = null;

        if (localizedCommon != null) o = localizedCommon.getDescription();
        if (this.localizedCommon != null && isEmpty(o)) o = this.localizedCommon.getDescription();

        return o;
    }

    @Override
    public String getIcon() {
        ILocalizedCommon localizedCommon = getSpecialLocalizedContent();
        String o = null;

        if (localizedCommon != null) o = localizedCommon.getIcon();
        if (this.localizedCommon != null && isEmpty(o)) o = this.localizedCommon.getIcon();

        if (isEmpty(o)) {
            o = super.getIcon(); // use icon of app if there is no localizedCommon.icon
        }
        return o;
    }

    @Override
    public String getVideo() {
        ILocalizedCommon localizedCommon = getSpecialLocalizedContent();
        String o = null;

        if (localizedCommon != null) o = localizedCommon.getVideo();
        if (this.localizedCommon != null && isEmpty(o)) o = this.localizedCommon.getVideo();

        return o;
    }

    @Override
    public String getWhatsNew() {
        ILocalizedCommon localizedCommon = getSpecialLocalizedContent();
        String o = null;

        if (localizedCommon != null) o = localizedCommon.getWhatsNew();
        if (this.localizedCommon != null && isEmpty(o)) o = this.localizedCommon.getWhatsNew();

        return o;
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        getSpecialLocalizedContent();
        LocalizedCommon.toStringBuilder(sb, this);
    }

}
