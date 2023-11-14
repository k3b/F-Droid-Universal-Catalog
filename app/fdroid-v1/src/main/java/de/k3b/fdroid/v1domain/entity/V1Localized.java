/*
 * Copyright (c) 2022-2023 by k3b.
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

package de.k3b.fdroid.v1domain.entity;

import java.util.List;

import javax.annotation.Generated;

import de.k3b.fdroid.domain.entity.common.LocalizedCommon;

/**
 * Data for a Localisation (Translation) of an android app (read from FDroid-Catalog-v1-Json format).
 * <p>
 * The {@link V1JsonEntity} {@link V1Localized} correspond to the
 * {@link de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId} {@link de.k3b.fdroid.domain.entity.Localized}.
 */
@Generated("jsonschema2pojo")
public class V1Localized extends LocalizedCommon implements V1JsonEntity {
    private String phoneScreenshotDir;
    private List<String> phoneScreenshots = null;

    public int getPhoneScreenshotCount() {
        if (phoneScreenshots != null) return phoneScreenshots.size();
        return 0;
    }

    public List<String> getPhoneScreenshots() {
        return phoneScreenshots;
    }

    public void setPhoneScreenshots(List<String> phoneScreenshots) {
        this.phoneScreenshots = phoneScreenshots;
    }

    protected void toStringBuilder(StringBuilder sb) {
        super.toStringBuilder(sb);
        toStringBuilder(sb, "phoneScreenshotDir", phoneScreenshotDir);
        toStringBuilder(sb, "phoneScreenshots", this.phoneScreenshots);
    }

    /**
     * ie dev.lonami.klooni/en-US/phoneScreenshots/
     */
    public String getPhoneScreenshotDir() {
        return phoneScreenshotDir;
    }

    public void setPhoneScreenshotDir(String phoneScreenshotDir) {
        this.phoneScreenshotDir = phoneScreenshotDir;
    }
}
