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

import de.k3b.fdroid.domain.entity.common.AppCommon;
import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.entity.common.IAppCommon;

/**
 * Parameterized Integration test for XxxCommon(Facade):
 * Make shure that XxxCommon[Full|Empty].toString() generate the same as XxxCommonFacade[Full|Empty].toString()
 * For details @see <a href="https://pragmatists.github.io/JUnitParams/">JUnitParams</a>
 */
@SuppressWarnings("unused")
public class AppCommonFacade extends EntityCommon implements IAppCommon {
    @Nullable
    private final IAppCommon content;

    public AppCommonFacade(@Nullable IAppCommon content) {
        this.content = content;
    }

    public IAppCommon getSpecialContent() {
        return null;
    }

    @Override
    public String getChangelog() {
        IAppCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getChangelog();
        if (this.content != null && isEmpty(o)) o = this.content.getChangelog();

        return o;
    }

    @Override
    public String getIssueTracker() {
        IAppCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getIssueTracker();
        if (this.content != null && isEmpty(o)) o = this.content.getIssueTracker();

        return o;
    }

    @Override
    public String getLicense() {
        IAppCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getLicense();
        if (this.content != null && isEmpty(o)) o = this.content.getLicense();

        return o;
    }

    @Override
    public String getSourceCode() {
        IAppCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getSourceCode();
        if (this.content != null && isEmpty(o)) o = this.content.getSourceCode();

        return o;
    }

    @Override
    public String getWebSite() {
        IAppCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getWebSite();
        if (this.content != null && isEmpty(o)) o = this.content.getWebSite();

        return o;
    }

    @Override
    public long getAdded() {
        IAppCommon content = getSpecialContent();
        long o = 0;

        if (content != null) o = content.getAdded();
        if (this.content != null && isEmpty(o)) o = this.content.getAdded();

        return o;
    }

    @Override
    public long getLastUpdated() {
        IAppCommon content = getSpecialContent();
        long o = 0;

        if (content != null) o = content.getLastUpdated();
        if (this.content != null && isEmpty(o)) o = this.content.getLastUpdated();

        return o;
    }

    @Override
    public String getPackageName() {
        IAppCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getPackageName();
        if (this.content != null && isEmpty(o)) o = this.content.getPackageName();

        return o;
    }

    @Override
    public String getSuggestedVersionName() {
        IAppCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getSuggestedVersionName();
        if (this.content != null && isEmpty(o)) o = this.content.getSuggestedVersionName();

        return o;
    }

    @Override
    public String getSuggestedVersionCode() {
        IAppCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getSuggestedVersionCode();
        if (this.content != null && isEmpty(o)) o = this.content.getSuggestedVersionCode();

        return o;
    }

    @Override
    public String getIcon() {
        IAppCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getIcon();
        if (this.content != null && isEmpty(o)) o = this.content.getIcon();

        return o;
    }

    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        AppCommon.toStringBuilder(sb, this);
    }
}
