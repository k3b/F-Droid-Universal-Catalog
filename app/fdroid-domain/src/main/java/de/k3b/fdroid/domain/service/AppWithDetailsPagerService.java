/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid project.
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
package de.k3b.fdroid.domain.service;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.entity.AntiFeature;
import de.k3b.fdroid.domain.entity.App;
import de.k3b.fdroid.domain.entity.AppAntiFeature;
import de.k3b.fdroid.domain.entity.AppCategory;
import de.k3b.fdroid.domain.entity.AppSearchParameter;
import de.k3b.fdroid.domain.entity.AppWithDetails;
import de.k3b.fdroid.domain.entity.Category;
import de.k3b.fdroid.domain.entity.LinkedDatabaseEntity;
import de.k3b.fdroid.domain.entity.Localized;
import de.k3b.fdroid.domain.entity.Version;
import de.k3b.fdroid.domain.interfaces.IAppDetail;
import de.k3b.fdroid.domain.repository.AppDetailRepository;

/**
 * Load cached items, loadOnDemand +-pageSize if necessary
 */
@SuppressWarnings("unused")
public class AppWithDetailsPagerService {
    private final AppDetailRepository<App> appRepository;
    private final AppDetailRepository<Localized> localizedRepository;
    private final AppDetailRepository<Version> versionRepository;
    private final AppDetailRepository<LinkedDatabaseEntity<AppCategory, Category>> categoryRepository;
    private final AppDetailRepository<LinkedDatabaseEntity<AppAntiFeature, AntiFeature>> antiFeatureRepository;

    /**
     * all matching appIds
     */
    private Integer[] appIds;

    /**
     * all items. null means not loaded yet
     */
    private AppWithDetails[] appWithDetailsList;
    private int pageSize;
    private AppSearchParameter appSearchParameter;

    public AppWithDetailsPagerService(
            AppDetailRepository<App> appRepository,
            AppDetailRepository<Localized> localizedRepository,
            AppDetailRepository<Version> versionRepository,
            AppDetailRepository<LinkedDatabaseEntity<AppCategory, Category>> categoryRepository,
            AppDetailRepository<LinkedDatabaseEntity<AppAntiFeature, AntiFeature>> antiFeatureRepository) {
        this.appRepository = appRepository;
        this.localizedRepository = localizedRepository;
        this.versionRepository = versionRepository;
        this.categoryRepository = categoryRepository;
        this.antiFeatureRepository = antiFeatureRepository;
    }

    public AppWithDetailsPagerService init(List<Integer> appIds, int pageSize, AppSearchParameter appSearchParameter) {
        this.appIds = appIds.toArray(new Integer[0]);
        appWithDetailsList = new AppWithDetails[appIds.size()];
        this.pageSize = pageSize;
        this.appSearchParameter = appSearchParameter;
        return this;
    }

    public String getName(int currentIndex) {
        AppWithDetails appWithDetails = getAppWithDetailsByOffset(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getName() != null) return l.getName();
        }
        return appWithDetails.getApp().getLocalizedName();
    }

    public String getSummary(int currentIndex) {
        AppWithDetails appWithDetails = getAppWithDetailsByOffset(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getSummary() != null) return l.getSummary();
        }
        return appWithDetails.getApp().getLocalizedSummary();
    }

    public String getDescription(int currentIndex) {
        AppWithDetails appWithDetails = getAppWithDetailsByOffset(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getDescription() != null) return l.getDescription();
        }
        return appWithDetails.getApp().getLocalizedDescription();
    }

    public String getWhatsNew(int currentIndex) {
        AppWithDetails appWithDetails = getAppWithDetailsByOffset(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getWhatsNew() != null) return l.getWhatsNew();
        }
        return appWithDetails.getApp().getLocalizedWhatsNew();
    }

    public String getIcon(int currentIndex) {
        AppWithDetails appWithDetails = getAppWithDetailsByOffset(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getIcon() != null) return l.getIcon();
        }
        return "";
    }

    public String getVideo(int currentIndex) {
        AppWithDetails appWithDetails = getAppWithDetailsByOffset(currentIndex);
        for (Localized l : appWithDetails.getLocalizedList()) {
            if (l.getVideo() != null) return l.getVideo();
        }
        return "";
    }

    public App getAppByOffset(int currentIndex) {
        return getAppWithDetailsByOffset(currentIndex).getApp();
    }

    public List<Localized> getLocalizedListByOffset(int currentIndex) {
        return getAppWithDetailsByOffset(currentIndex).getLocalizedList();
    }

    // get from cache. if currentIndex not loaded yet load currentIndex +-
    @NotNull
    private AppWithDetails getAppWithDetailsByOffset(int currentIndex) {
        if (currentIndex < 0 || currentIndex >= size())
            throw new IndexOutOfBoundsException("getAppWithDetailsByOffset[" +
                    currentIndex + "]");

        AppWithDetails result = appWithDetailsList[currentIndex];
        if (result == null) {
            // implement load on demand
            int from = getFrom(currentIndex - 1);
            int to = getTo(currentIndex + 1);
            ArrayList<Integer> appIdList = new ArrayList<>(to - from);
            appIdList.addAll(Arrays.asList(appIds).subList(from, to + 1));
            update(appIdList, from);
            result = appWithDetailsList[currentIndex];
        }

        if (result == null) throw new ArithmeticException("getAppWithDetailsByOffset[" +
                currentIndex + "] == null");
        return result;
    }

    private void update(ArrayList<Integer> appIdList, int from) {
        Map<Integer, AppWithDetails> id2AppLocalizedList = new HashMap<>();
        List<App> apps = appRepository.findByAppIds(appIdList);
        if (appSearchParameter != null) {
            for (App app : apps) {
                app.setAppSearchParameter(appSearchParameter);
            }
        }

        for (Integer appId : appIdList) {
            // apps order might not match appIdList order
            AppWithDetails appWithDetails = new AppWithDetails(findByAppId(apps, appId));
            appWithDetailsList[from] = appWithDetails;
            id2AppLocalizedList.put(appId, appWithDetails);
            from++;
        }

        load(Localized.class, appIdList, localizedRepository, id2AppLocalizedList);
        load(Version.class, appIdList, versionRepository, id2AppLocalizedList);
        load(Category.class, appIdList, categoryRepository, id2AppLocalizedList);
        load(AntiFeature.class, appIdList, antiFeatureRepository, id2AppLocalizedList);
    }

    private App findByAppId(@NotNull List<App> apps, int appId) {
        for (App app : apps) {
            if (appId == app.getId()) return app;
        }
        return null;
    }

    private <T extends IAppDetail> void load(
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

    public AppItemAtOffset itemAtOffset(int index) {
        return new AppItemAtOffset(index);
    }

    public AppItemAtOffset[] itemAtOffset(int from, int to) {
        int size = size();

        if (from < 0) from = 0;
        if (to > size) to = size;
        int resultCount = to - from;
        if (resultCount < 0) resultCount = 0;

        AppItemAtOffset[] result = new AppItemAtOffset[resultCount];
        for (int i = 0; i < resultCount; i++) {
            result[i] = itemAtOffset(i + from);
        }

        return result;
    }

    /**
     * Aggregate root display representing an {@link App} with all it-s details.
     * Available Properties:
     * * name
     * * summary
     * * description
     * * icon
     * * video
     * * whatsNew
     * * app
     * * localizedList
     * * versionList
     */
    public class AppItemAtOffset {
        private final int currentIndex;

        private AppItemAtOffset(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        public String getName() {
            return AppWithDetailsPagerService.this.getName(currentIndex);
        }

        public String getSummary() {
            return AppWithDetailsPagerService.this.getSummary(currentIndex);
        }

        public String getDescription() {
            return AppWithDetailsPagerService.this.getDescription(currentIndex);
        }

        public String getIcon() {
            return AppWithDetailsPagerService.this.getIcon(currentIndex);
        }

        public String getVideo() {
            return AppWithDetailsPagerService.this.getVideo(currentIndex);
        }

        public String getWhatsNew() {
            return AppWithDetailsPagerService.this.getWhatsNew(currentIndex);
        }

        @NotNull
        public App getApp() {
            return getAppWithDetails().getApp();
        }

        public List<Localized> getLocalizedList() {
            return getAppWithDetails().getLocalizedList();
        }

        public List<Version> getVersionList() {
            return getAppWithDetails().getVersionList();
        }

        public AppWithDetails getAppWithDetails() {
            return AppWithDetailsPagerService.this.getAppWithDetailsByOffset(currentIndex);
        }
    }

    public AppWithDetails[] getAppWithDetailsArray(int pagenumber) {
        int from = Math.max(0, pagenumber * pageSize);
        int to = Math.min(size(), from + pageSize);
        if (to - from < 1) return new AppWithDetails[0];

        AppItemAtOffset[] items = itemAtOffset(from, to);
        AppWithDetails[] result = new AppWithDetails[items.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = items[i].getAppWithDetails();
        }
        return result;
    }
}
