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
package de.k3b.fdroid.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.AppCategory;
import de.k3b.fdroid.domain.AppWithDetails;
import de.k3b.fdroid.domain.Category;
import de.k3b.fdroid.domain.LinkedDatabaseEntity;
import de.k3b.fdroid.domain.Localized;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.interfaces.AppDetail;
import de.k3b.fdroid.domain.interfaces.AppDetailRepository;

/**
 * Load cached items, loadOnDemand +-pageSize if necessary
 */
public class AppWithDetailsPagerService {
    private final AppDetailRepository<App> appRepository;
    private final AppDetailRepository<Localized> localizedRepository;
    private final AppDetailRepository<Version> versionRepository;
    private final AppDetailRepository<LinkedDatabaseEntity<AppCategory, Category>> categoryRepository;

    private int currentIndex;

    private Integer[] appIds;
    private AppWithDetails[] appWithDetailsList;
    private int pageSize;

    public AppWithDetailsPagerService(
            AppDetailRepository<App> appRepository,
            AppDetailRepository<Localized> localizedRepository,
            AppDetailRepository<Version> versionRepository,
            AppDetailRepository<LinkedDatabaseEntity<AppCategory, Category>> categoryRepository) {
        this.appRepository = appRepository;
        this.localizedRepository = localizedRepository;
        this.versionRepository = versionRepository;
        this.categoryRepository = categoryRepository;
    }

    public AppWithDetailsPagerService init(List<Integer> appIds, int pageSize) {
        this.appIds = appIds.toArray(new Integer[0]);
        appWithDetailsList = new AppWithDetails[appIds.size()];
        this.pageSize = pageSize;
        return this;
    }

    public String getName(int currentIndex) {
        AppWithDetails appWithDetails = getAppLocalized(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getName() != null) return l.getName();
        }
        return appWithDetails.getApp().getPackageName();
    }

    public String getSummary(int currentIndex) {
        AppWithDetails appWithDetails = getAppLocalized(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getSummary() != null) return l.getSummary();
        }
        return "";
    }

    public String getDescription(int currentIndex) {
        AppWithDetails appWithDetails = getAppLocalized(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getDescription() != null) return l.getDescription();
        }
        return "";
    }

    public String getIcon(int currentIndex) {
        AppWithDetails appWithDetails = getAppLocalized(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getIcon() != null) return l.getIcon();
        }
        return "";
    }

    public String getVideo(int currentIndex) {
        AppWithDetails appWithDetails = getAppLocalized(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getVideo() != null) return l.getVideo();
        }
        return "";
    }

    public String getWhatsNew(int currentIndex) {
        AppWithDetails appWithDetails = getAppLocalized(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getWhatsNew() != null) return l.getWhatsNew();
        }
        return "";
    }

    public App getAppByOffset(int currentIndex) {
        return getAppLocalized(currentIndex).getApp();
    }

    public List<Localized> getLocalizedListByOffset(int currentIndex) {
        return getAppLocalized(currentIndex).getLocalizedList();
    }

    // get from cache. if currentIndex not loaded yet load currentIndex +-
    private AppWithDetails getAppLocalized(int currentIndex) {
        if (currentIndex < 0 || currentIndex >= getSize()) throw new IndexOutOfBoundsException();
        AppWithDetails result = appWithDetailsList[currentIndex];
        if (result == null) {
            int from = getFrom(currentIndex - 1);
            int to = getTo(currentIndex + 1);
            ArrayList<Integer> appIdList = new ArrayList<>(to - from);
            for (int i = from; i <= to; i++) {
                appIdList.add(appIds[i]);
            }
            update(appIdList, from);
            result = appWithDetailsList[currentIndex];
        }

        return result;
    }

    public int getSize() {
        return appWithDetailsList.length;
    }

    private void update(ArrayList<Integer> appIdList, int from) {
        Map<Integer, AppWithDetails> id2AppLocalizedList = new HashMap<>();
        List<App> apps = appRepository.findByAppIds(appIdList);
        for (App app : apps) {
            AppWithDetails appWithDetails = new AppWithDetails(app);
            appWithDetailsList[from] = appWithDetails;
            id2AppLocalizedList.put(app.getId(), appWithDetails);
            from++;
        }

        load(Localized.class, appIdList, localizedRepository, id2AppLocalizedList);
        load(Version.class, appIdList, versionRepository, id2AppLocalizedList);
        load(Category.class, appIdList, categoryRepository, id2AppLocalizedList);
    }

    private <T extends AppDetail> void load(
            Class<?> clasz, ArrayList<Integer> appIdList,
            AppDetailRepository<T> repository, Map<Integer, AppWithDetails> id2AppLocalizedList) {
        if (repository != null) {
            List<T> foundList = repository.findByAppIds(appIdList);
            for (T found : foundList) {
                id2AppLocalizedList.get(found.getAppId()).getList(clasz).add(found);
            }
        }
    }

    private int getTo(int to) {
        int i = 0;
        while (to < appWithDetailsList.length && appWithDetailsList[to] == null && i < pageSize) {
            to++;
            i++;
        }
        return to - 1;
    }

    private int getFrom(int from) {
        int i = 0;
        while (from >= 0 && appWithDetailsList[from] == null && i < pageSize) {
            from--;
            i++;
        }
        return from + 1;
    }

    public int size() {
        return appIds.length;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }
}
