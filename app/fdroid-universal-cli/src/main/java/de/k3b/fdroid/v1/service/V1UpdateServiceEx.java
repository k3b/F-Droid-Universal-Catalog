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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.domain.interfaces.AppCategoryRepository;
import de.k3b.fdroid.domain.interfaces.AppHardwareRepository;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.HardwareProfileRepository;
import de.k3b.fdroid.domain.interfaces.LocaleRepository;
import de.k3b.fdroid.domain.interfaces.LocalizedRepository;
import de.k3b.fdroid.domain.interfaces.RepoRepository;
import de.k3b.fdroid.domain.interfaces.VersionRepository;
import de.k3b.fdroid.service.CategoryService;
import de.k3b.fdroid.service.ConsoleProgressUpdateObserver;
import de.k3b.fdroid.service.LanguageService;

@Service
@Transactional
public class V1UpdateServiceEx extends V1UpdateService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);

    public V1UpdateServiceEx(RepoRepository repoRepository, AppRepository appRepository,
                             CategoryService categoryService, AppCategoryRepository appCategoryRepository,
                             VersionRepository versionRepository,
                             LocalizedRepository localizedRepository,
                             LocaleRepository localeRepository,
                             HardwareProfileRepository hardwareProfileRepository,
                             AppHardwareRepository appHardwareRepository,
                             LanguageService languageService) {
        super(repoRepository, appRepository, categoryService, appCategoryRepository,
                versionRepository, localizedRepository, localeRepository, hardwareProfileRepository, appHardwareRepository, languageService);
        setProgressObserver(new ConsoleProgressUpdateObserver());
    }

    @Override
    protected String log(String s) {
        System.out.println(s);
        LOGGER.debug(s);
        return s;
    }
}
