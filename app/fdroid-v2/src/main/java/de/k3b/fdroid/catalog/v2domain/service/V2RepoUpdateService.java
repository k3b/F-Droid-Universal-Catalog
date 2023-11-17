/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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

package de.k3b.fdroid.catalog.v2domain.service;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2Mirror;
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2Repo;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.common.RepoCommon;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.util.ExceptionUtils;
import de.k3b.fdroid.domain.util.Java8Util;
import de.k3b.fdroid.domain.util.StringUtil;

/**
 * Service that updates {@link Repo}
 * from {@link V2Repo} using a {@link RepoRepository}
 */
public class V2RepoUpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    @Nullable
    private final RepoRepository repoRepository;

    private int nextMockAppId = 200142;

    public V2RepoUpdateService(@Nullable RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public Repo update(Repo roomRepoOrNull, V2Repo v2Repo)
            throws PersistenceException {
        Repo roomRepo = roomRepoOrNull;
        try {
            if (roomRepo == null && repoRepository != null) {
                roomRepo = repoRepository.findByAddress(v2Repo.getAddress());
            }
            if (roomRepo == null) {
                roomRepo = new Repo();
                copy(roomRepo, v2Repo);
                if (repoRepository != null) {
                    repoRepository.insert(roomRepo);
                } else {
                    roomRepo.setId(nextMockAppId++);
                }
            } else {
                copy(roomRepo, v2Repo);
                if (repoRepository != null) repoRepository.update(roomRepo);
            }
            return roomRepo;
        } catch (PersistenceException ex) {
            // thrown by j2se hibernate database problem
            StringBuilder message = new StringBuilder()
                    .append("PersistenceException in ")
                    .append(getClass().getSimpleName())
                    .append(".update(repo(");
            if (roomRepo != null) {
                message.append(roomRepo.getId()).append(")=").append(roomRepo.getAddress());
            }
            message.append(") ")
                    .append(ExceptionUtils.getParentCauseMessage(ex, PersistenceException.class));
            LOGGER.error(message + "\n\tv2Repo=" + v2Repo, ex);
            throw new PersistenceException(message.toString(), ex);
        }
    }

    private void copy(Repo dest, V2Repo src) {
        RepoCommon.copyCommon(dest, src);

        dest.setMirrors(StringUtil.toCsvStringOrNull(Java8Util.reduce(src.getMirrorsList(), V2Mirror::getUrl)));
        if (dest.getLastUsedDownloadDateTimeUtc() < src.getTimestamp()) {
            dest.setLastUsedDownloadDateTimeUtc(src.getTimestamp());
        }
    }

    public V2RepoUpdateService init() {
        return this;
    }
}
