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
import de.k3b.fdroid.domain.entity.common.IVersionCommon;
import de.k3b.fdroid.domain.entity.common.ProfileCommon;
import de.k3b.fdroid.domain.entity.common.VersionCommon;

@SuppressWarnings("unused")
public class VersionCommonFacade extends EntityCommon implements IVersionCommon {
    @Nullable
    private final IVersionCommon content;

    public VersionCommonFacade(@Nullable IVersionCommon content) {
        this.content = content;
    }

    public IVersionCommon getSpecialContent() {
        return null;
    }

    @Override
    public long getAdded() {
        IVersionCommon content = getSpecialContent();
        long o = 0;

        if (content != null) o = content.getAdded();
        if (this.content != null && isEmpty(o)) o = this.content.getAdded();

        return o;
    }

    @Override
    public String getApkName() {
        IVersionCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getApkName();
        if (this.content != null && isEmpty(o)) o = this.content.getApkName();

        return o;
    }

    @Override
    public int getSize() {
        IVersionCommon content = getSpecialContent();
        int o = 0;

        if (content != null) o = content.getSize();
        if (this.content != null && isEmpty(o)) o = this.content.getSize();

        return o;
    }

    @Override
    public int getVersionCode() {
        IVersionCommon content = getSpecialContent();
        int o = 0;

        if (content != null) o = content.getVersionCode();
        if (this.content != null && isEmpty(o)) o = this.content.getVersionCode();

        return o;
    }

    @Override
    public String getVersionName() {
        IVersionCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getVersionName();
        if (this.content != null && isEmpty(o)) o = this.content.getVersionName();

        return o;
    }

    @Override
    public String getHash() {
        IVersionCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getHash();
        if (this.content != null && isEmpty(o)) o = this.content.getHash();

        return o;
    }

    @Override
    public int getMinSdkVersion() {
        IVersionCommon content = getSpecialContent();
        int o = 0;

        if (content != null) o = content.getMinSdkVersion();
        if (this.content != null && isEmpty(o)) o = this.content.getMinSdkVersion();

        return o;
    }

    @Override
    public int getMaxSdkVersion() {
        IVersionCommon content = getSpecialContent();
        int o = 0;

        if (content != null) o = content.getMaxSdkVersion();
        if (this.content != null && isEmpty(o)) o = this.content.getMaxSdkVersion();

        return o;
    }

    @Override
    public String getSigner() {
        IVersionCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getSigner();
        if (this.content != null && isEmpty(o)) o = this.content.getSigner();

        return o;
    }

    @Override
    public String getSrcname() {
        IVersionCommon content = getSpecialContent();
        String o = null;

        if (content != null) o = content.getSrcname();
        if (this.content != null && isEmpty(o)) o = this.content.getSrcname();

        return o;
    }

    @Override
    public int getTargetSdkVersion() {
        IVersionCommon content = getSpecialContent();
        int o = 0;

        if (content != null) o = content.getTargetSdkVersion();
        if (this.content != null && isEmpty(o)) o = this.content.getTargetSdkVersion();

        return o;
    }


    protected void toStringBuilder(@NotNull StringBuilder sb) {
        super.toStringBuilder(sb);
        ProfileCommon.toStringBuilder(sb, this);
        VersionCommon.toStringBuilder(sb, this);
    }
}
