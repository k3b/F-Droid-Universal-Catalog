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

package de.k3b.fdroid.v1domain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.common.RepoCommon;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.util.ExceptionUtils;
import de.k3b.fdroid.domain.util.StringUtil;
import de.k3b.fdroid.v1domain.entity.UpdateService;

/**
 * {@link UpdateService} that updates {@link Repo}
 * from {@link de.k3b.fdroid.v1domain.entity.Repo} using a {@link RepoRepository}
 */
public class RepoUpdateService implements UpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    private final RepoRepository repoRepository;

    public RepoUpdateService(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public Repo update(de.k3b.fdroid.v1domain.entity.Repo v1Repo, Repo roomRepoOrNull)
            throws PersistenceException {
        Repo roomRepo = roomRepoOrNull;
        try {
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
        } catch (PersistenceException ex) {
            // thrown by j2se hibernate database problem
            String message = "PersistenceException in " + getClass().getSimpleName() + ".update(repo("
                    + roomRepo.getId() + ")="
                    + roomRepo.getAddress() + ") "
                    + ExceptionUtils.getParentCauseMessage(ex, PersistenceException.class);
            LOGGER.error(message + "\n\tv1Repo=" + v1Repo, ex);
            throw new PersistenceException(message, ex);
        }
    }

    private void copy(Repo dest, de.k3b.fdroid.v1domain.entity.Repo src) {
        RepoCommon.copyCommon(dest, src);

        dest.setMirrors(StringUtil.toCsvStringOrNull(src.getMirrors()));
        if (dest.getLastUsedDownloadDateTimeUtc() < src.getTimestamp()) {
            dest.setLastUsedDownloadDateTimeUtc(src.getTimestamp());
        }
    }
}
