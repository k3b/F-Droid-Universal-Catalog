/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid.v1domain the fdroid json catalog-format-v1 parser.
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
package de.k3b.fdroid.catalog;

import de.k3b.fdroid.domain.entity.Repo;

public class CatalogJarException extends RuntimeException {
    private static final long serialVersionUID = -108203133870545897L;

    public CatalogJarException(String message) {
        super(message);
    }

    public CatalogJarException(String message, Throwable cause) {
        super(message, cause);
    }

    public CatalogJarException(Repo repo, String message) {
        super(createMessage(repo, message));
    }

    public CatalogJarException(Repo repo, String message, Throwable cause) {
        super(createMessage(repo, message), cause);
    }

    private static String createMessage(Repo repo, String message) {
        if (repo == null) return message;
        StringBuilder msg = new StringBuilder().append("Repo ").append(repo.getName() != null ? repo.getName() : "").append(": ").append(message);
        if (repo.getLastUsedDownloadMirror() != null)
            msg.append(" url=").append(repo.getLastUsedDownloadMirror());
        return msg.toString();
    }
}
