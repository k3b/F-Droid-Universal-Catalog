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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.PersistenceException;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.entity.common.EntityCommon;
import de.k3b.fdroid.domain.entity.common.LocalizedCommon;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.service.LanguageService;
import de.k3b.fdroid.domain.service.LocalizedService;
import de.k3b.fdroid.domain.util.ExceptionUtils;
import de.k3b.fdroid.domain.util.Java8Util;
import de.k3b.fdroid.domain.util.StringUtil;
import de.k3b.fdroid.v1domain.entity.UpdateService;

/**
 * {@link UpdateService} that updates {@link Localized}
 * from {@link de.k3b.fdroid.v1domain.entity.Localized} using a {@link LocalizedRepository}
 */
public class LocalizedUpdateService implements UpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);
    @Nullable
    private final LocalizedRepository localizedRepository;
    @NotNull
    private final LanguageService languageService;
    @NotNull
    private final LocalizedService localizedService;

    public LocalizedUpdateService(@Nullable LocalizedRepository localizedRepository,
                                  @NotNull LanguageService languageService) {
        this.localizedRepository = localizedRepository;
        this.languageService = languageService;
        this.localizedService = new LocalizedService(languageService);
    }

    public LocalizedUpdateService init() {
        languageService.init();
        return this;
    }

    public List<Localized> update(
            int repoId, int appId, App roomApp,
            Map<String, de.k3b.fdroid.v1domain.entity.Localized> v1LocalizedMap)
            throws PersistenceException {
        String packageName = null;
        if (roomApp != null) packageName = roomApp.getPackageName();
        Java8Util.OutParam<Localized> exceptionContext = new Java8Util.OutParam<>(null);
        try {
            List<Localized> roomLocalizedList = (localizedRepository == null)
                    ? new ArrayList<>()
                    : localizedRepository.findByAppId(appId);

            int phoneScreenshotCount = update(appId, roomLocalizedList, v1LocalizedMap, exceptionContext);

            List<Localized> deleted = localizedService.deleteHidden(roomLocalizedList);
            if (roomApp != null) {
                if (repoId != 0 && (roomApp.getResourceRepoId() == null || phoneScreenshotCount > 0)) {
                    roomApp.setResourceRepoId(repoId);
                }
                localizedService.recalculateSearchFields(repoId, roomApp, roomLocalizedList);
            }
            if (localizedRepository != null) localizedRepository.deleteAll(deleted);
            return roomLocalizedList;
        } catch (Exception ex) {
            Localized roomLocalized = exceptionContext.getValue();

            // thrown by j2se hibernate database problem
            // hibernate DataIntegrityViolationException -> NestedRuntimeException
            // hibernate org.hibernate.exception.DataException inherits from PersistenceException
            StringBuilder message = new StringBuilder();
            message.append("Exception in ").append(getClass().getSimpleName())
                    .append(".update(repo=").append(repoId)
                    .append(", app(").append(appId).append(")=").append(packageName);
            if (roomLocalized != null) {
                message.append(", localized(")
                        .append(roomLocalized.getId()).append(",")
                        .append(roomLocalized.getLocaleId()).append(")");
            }
            message.append(") ").append(ExceptionUtils.getParentCauseMessage(ex, PersistenceException.class));

            LOGGER.error(message + "\n\tv1Localized=" + v1LocalizedMap, ex);
            throw new PersistenceException(message.toString(), ex);
        }

    }

    // Persistence free entrypoint for unittest
    protected int update(int appId,
                         List<Localized> roomLocalizedList,
                         Map<String, de.k3b.fdroid.v1domain.entity.Localized> v1LocalizedMap,
                         Java8Util.OutParam<Localized> exceptionContext) {
        Localized currentRoomLocalized = null;
        int phoneScreenshotCount = 0;
        for (Map.Entry<String, de.k3b.fdroid.v1domain.entity.Localized> v1Entry : v1LocalizedMap.entrySet()) {
            String localeId = v1Entry.getKey();
            languageService.getOrCreateLocaleByCode(localeId);

            if (!languageService.isHidden(localeId)) {
                de.k3b.fdroid.v1domain.entity.Localized v1Localized = v1Entry.getValue();
                currentRoomLocalized = LanguageService.findByLocaleId(roomLocalizedList, localeId);
                if (currentRoomLocalized == null) {
                    currentRoomLocalized = new Localized(appId, localeId);
                    roomLocalizedList.add(currentRoomLocalized);
                }
                exceptionContext.setValue(currentRoomLocalized);

                phoneScreenshotCount += v1Localized.getPhoneScreenshotCount();

                copy(currentRoomLocalized, v1Localized);

                if (localizedRepository != null) localizedRepository.save(currentRoomLocalized);
            } // if not hidden
        } // for each v1 language
        return phoneScreenshotCount;
    }

    private void copy(Localized roomDest, de.k3b.fdroid.v1domain.entity.Localized v1Src) {
        LocalizedCommon.copyCommon(roomDest, v1Src);
        String phoneScreenshots = StringUtil.toCsvStringOrNull(v1Src.getPhoneScreenshots());
        if (!StringUtil.isEmpty(phoneScreenshots)) {
            roomDest.setPhoneScreenshots(StringUtil.maxLen(phoneScreenshots, EntityCommon.MAX_LEN_AGGREGATED, "phoneScreenshots"));
            roomDest.setPhoneScreenshotDir(v1Src.getPhoneScreenshotDir());
        }
    }

    private String getLocaleCodeByLocalized(Localized roomLocalized) {
        return roomLocalized == null ? null : roomLocalized.getLocaleId();
    }
}
