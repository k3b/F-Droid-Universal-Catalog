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

package de.k3b.fdroid.room.db.v1;

import org.fdroid.model.common.RepoCommon;

import de.k3b.fdroid.room.db.RepoRepository;
import de.k3b.fdroid.room.model.Repo;

/**
 * update android-room-database from fdroid-v1-rest-gson data
 */
public class RepoUpdateService {
    private final RepoRepository repoRepository;

    public RepoUpdateService(RepoRepository repoRepository) {
        this.repoRepository = repoRepository;
    }

    public Repo update(org.fdroid.model.v1.Repo v1Repo) {
        Repo roomRepo = repoRepository.findByAddress(v1Repo.getAddress());
        if (roomRepo == null) {
            roomRepo = new Repo();
            RepoCommon.copyCommon(roomRepo, v1Repo);
            repoRepository.insert(roomRepo);
        } else {
            RepoCommon.copyCommon(roomRepo, v1Repo);
            repoRepository.update(roomRepo);
        }
        return roomRepo;
    }
}
