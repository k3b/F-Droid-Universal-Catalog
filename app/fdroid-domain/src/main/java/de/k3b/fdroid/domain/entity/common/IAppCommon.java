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

package de.k3b.fdroid.domain.entity.common;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.media.Schema;

@SuppressWarnings("unused")
public interface IAppCommon {
    String getChangelog();

    String getIssueTracker();

    String getLicense();

    String getSourceCode();

    String getWebSite();

    long getAdded();

    long getLastUpdated();

    String getPackageName();

    String getSuggestedVersionName();

    String getSuggestedVersionCode();

    String getIcon();

    @Schema(description = "When the app was last updated in the [Repo].",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo"),
            example = "2020-03-14")
    default String getLastUpdatedDate() {
        return EntityCommon.asDateString(getLastUpdated());
    }

    @Schema(description = "When the app was added to the [Repo].",
            externalDocs = @ExternalDocumentation(url = ExtDoc.GLOSSAR_URL + "Repo"),
            example = "2015-07-30")
    default String getAddedDate() {
        return EntityCommon.asDateString(getAdded());
    }
}
