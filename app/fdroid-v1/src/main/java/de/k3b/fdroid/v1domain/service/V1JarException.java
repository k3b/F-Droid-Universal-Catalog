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
package de.k3b.fdroid.v1domain.service;

import de.k3b.fdroid.domain.entity.Repo;

public class V1JarException extends RuntimeException {
    private static final long serialVersionUID = -108203133870545897L;

    public V1JarException(String message) {
        super(message);
    }

    public V1JarException(String message, Throwable cause) {
        super(message, cause);
    }

    public V1JarException(Repo repo, String message) {
        super(createMessage(repo, message));
    }

    public V1JarException(Repo repo, String message, Throwable cause) {
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
