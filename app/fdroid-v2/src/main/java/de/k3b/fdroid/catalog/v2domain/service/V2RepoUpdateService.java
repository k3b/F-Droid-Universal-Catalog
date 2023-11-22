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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.catalog.v2domain.entity.V2IconUtil;
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2Mirror;
import de.k3b.fdroid.catalog.v2domain.entity.repo.V2Repo;
import de.k3b.fdroid.domain.entity.Repo;
import de.k3b.fdroid.domain.entity.common.RepoCommon;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.domain.repository.TranslationRepository;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.service.TranslationService;
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
    @Nullable
    private final V2CategoryUpdateService categoryUpdateService;
    @NotNull
    private final TranslationService categoryIconService;
    @NotNull
    private final TranslationService categoryNameService;
    @NotNull
    private final TranslationService categoryDescriptionService;


    @Nullable
    private final V2AntiFeatureUpdateService antiFeatureUpdateService;

    private int nextMockAppId = 200142;

    public V2RepoUpdateService(@Nullable RepoRepository repoRepository,
                               @Nullable TranslationRepository translationRepository,
                               @Nullable V2CategoryUpdateService categoryUpdateService,
                               @Nullable V2AntiFeatureUpdateService v2AntiFeatureUpdateService) {
        this.repoRepository = repoRepository;
        this.categoryUpdateService = categoryUpdateService;
        this.categoryNameService = new TranslationService(TranslationService.TYP_REPOSITORY_NAME, translationRepository);
        this.categoryDescriptionService = new TranslationService(TranslationService.TYP_REPOSITORY_DESCRIPTION, translationRepository);
        this.categoryIconService = new TranslationService(TranslationService.TYP_REPOSITORY_ICON, translationRepository);

        this.antiFeatureUpdateService = v2AntiFeatureUpdateService;
    }


    public V2RepoUpdateService init() {
        if (categoryUpdateService != null) categoryUpdateService.init();
        categoryNameService.init();
        categoryDescriptionService.init();
        categoryIconService.init();

        if (antiFeatureUpdateService != null) antiFeatureUpdateService.init();
        return this;
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
            updateDetails(roomRepo, v2Repo);
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

    private void updateDetails(Repo repo, @NotNull V2Repo v2Repo) {
        if (categoryUpdateService != null) categoryUpdateService.update(v2Repo.getCategories());
        this.categoryNameService.update(repo.getId(), repo.getName(), v2Repo.getNameMap());
        this.categoryDescriptionService.update(repo.getId(), repo.getDescription(), v2Repo.getDescriptionMap());
        this.categoryIconService.update(repo.getId(), repo.getIcon(),
                Java8Util.reduce(LanguageService.getCanonicalLocale(v2Repo.getIconMap())
                        , V2IconUtil::getIconName));

        if (antiFeatureUpdateService != null)
            antiFeatureUpdateService.update(v2Repo.getAntiFeatures());
    }

    private void copy(Repo dest, V2Repo src) {
        RepoCommon.copyCommon(dest, src);

        dest.setMirrors(StringUtil.toCsvStringOrNull(Java8Util.reduce(src.getMirrorsList(), V2Mirror::getUrl)));
        if (dest.getLastUsedDownloadDateTimeUtc() < src.getTimestamp()) {
            dest.setLastUsedDownloadDateTimeUtc(src.getTimestamp());
        }
    }

    @Override
    public String toString() {
        return "V2RepoUpdateService{" +
                "categoryUpdateService=" + categoryUpdateService +
                ", categoryIconService=" + categoryIconService +
                ", categoryNameService=" + categoryNameService +
                ", categoryDescriptionService=" + categoryDescriptionService +

                ", antiFeatureUpdateService=" + antiFeatureUpdateService +
                ", nextMockAppId=" + nextMockAppId +
                '}';
    }
}
