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

package de.k3b.fdroid.v1.service;

import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.util.StringUtil;
import de.k3b.fdroid.v1.domain.UpdateService;

/**
 * {@link UpdateService} that updates {@link de.k3b.fdroid.domain.Repo}
 * from {@link de.k3b.fdroid.v1.domain.Repo} using a {@link RepoRepository}
 */
public class RepoUpdateService implements UpdateService {
    private final RepoRepository repoRepository;

    public RepoUpdateService(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public Repo update(de.k3b.fdroid.v1.domain.Repo v1Repo, Repo roomRepoOrNull) {
        Repo roomRepo = roomRepoOrNull;
        if (roomRepo == null) {
            roomRepo = repoRepository.findByAddress(v1Repo.getAddress());
        }
        if (roomRepo == null) {
            roomRepo = new Repo();
            copy(roomRepo, v1Repo);
            repoRepository.insert(roomRepo);
        } else {
            copy(roomRepo, v1Repo);
            repoRepository.update(roomRepo);
        }
        return roomRepo;
    }

    private void copy(Repo dest, de.k3b.fdroid.v1.domain.Repo src) {
        RepoCommon.copyCommon(dest, src);

        dest.setMirrors(StringUtil.toCsvStringOrNull(src.getMirrors()));
        if (dest.getLastUsedDownloadDateTimeUtc() < src.getTimestamp()) {
            dest.setLastUsedDownloadDateTimeUtc(src.getTimestamp());
        }
    }
}
