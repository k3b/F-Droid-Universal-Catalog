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

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.AppHardware;
import de.k3b.fdroid.domain.HardwareProfile;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.common.ProfileCommon;
import de.k3b.fdroid.domain.interfaces.AppHardwareRepository;
import de.k3b.fdroid.domain.interfaces.AppRepository;
import de.k3b.fdroid.domain.interfaces.HardwareProfileRepository;
import de.k3b.fdroid.domain.interfaces.ProgressObservable;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.util.StringUtil;

/**
 * Service to cache/find/insert HardwareProfile info.
 */

public class HardwareProfileService implements ProgressObservable {
    private final AppRepository appRepository;
    private final HardwareProfileRepository hardwareProfileRepository;
    private final AppHardwareRepository appHardwareRepository;
    private ProgressObserver progressObserver;

    private List<HardwareProfile> hardwareProfiles;
    private boolean hpForDelete = false;

    /**
     * @param appRepository if not null: may delete app if it is not compatible with any profile
     */
    public HardwareProfileService(@Nullable AppRepository appRepository,
                                  HardwareProfileRepository hardwareProfileRepository,
                                  AppHardwareRepository appHardwareRepository) {
        this.appRepository = appRepository;
        this.hardwareProfileRepository = hardwareProfileRepository;
        this.appHardwareRepository = appHardwareRepository;
    }

    /**
     * @return true if app belonging to result is compatible with versionList
     */
    public static boolean calculateAppHardware(AppHardware result, HardwareProfile profile, List<Version> versionList) {
        Version min = null;
        Version max = null;
        for (Version version : versionList) {
            if (isCompatible(profile, version)) {
                if (min == null || min.getVersionCode() > version.getVersionCode()) {
                    min = version;
                }
                if (max == null || max.getVersionCode() < version.getVersionCode()) {
                    max = version;
                }
            }
        }

        if (min != null) {
            ProfileCommon.copyCommon(result.getMin(), min);
            ProfileCommon.copyCommon(result.getMax(), max);
            return true;
        }
        return false;
    }

    public HardwareProfileService init() {
        return init(hardwareProfileRepository.findAll());
    }

    /**
     * Note: a armeabi processer can execute armabi code but not vice versa .
     * See https://stackoverflow.com/questions/8060174/what-are-the-purposes-of-the-arm-abi-and-eabi
     */
    public static boolean isCompatibleNativecode(String[] appVersionNativecodes, String[] profileNativecodes) {
        if (StringUtil.isEmpty(appVersionNativecodes) || StringUtil.isEmpty(profileNativecodes))
            return true;

        for (String current : appVersionNativecodes) {
            if (StringUtil.contains(current, profileNativecodes)) return true;
        }

        return false;
    }

    protected static boolean isCompatibleSdk(int currentSdk, int profileMin, int profileMax) {
        if (profileMin == 0) profileMin = Integer.MIN_VALUE;
        if (profileMax == 0) profileMax = Integer.MAX_VALUE;
        return (currentSdk >= profileMin && currentSdk <= profileMax);
    }

    public HardwareProfileService init(List<HardwareProfile> hardwareProfiles) {
        this.hardwareProfiles = hardwareProfiles;
        if (appRepository != null) {
            HardwareProfile hpForDelete = null;
            for (HardwareProfile hp : this.hardwareProfiles) {
                if (hp.isDeleteIfNotCompatible()) {
                    hpForDelete = hp;
                }
            }
            this.hpForDelete = (hpForDelete != null);
        }
        return this;
    }

    public static boolean isCompatible(HardwareProfile profile, Version version) {
        return isCompatibleSdk(profile.getSdkVersion(), version.getMinSdkVersion(), version.getMaxSdkVersion())
                && isCompatibleNativecode(profile.getNativecodeArray(), version.getNativecodeArray());
    }

    public void updateAppProfiles(App app, List<Version> versionList) {
        if ((this.hardwareProfiles != null) && (!hardwareProfiles.isEmpty()) && !versionList.isEmpty()) {
            List<AppHardware> appHardwareListCompatible = this.appHardwareRepository.findByAppId(app.getId());
            List<AppHardware> appHardwareListNotCompatible = new ArrayList<>();

            updateAppProfiles(app.getId(), versionList, appHardwareListCompatible, appHardwareListNotCompatible);

            save(app, appHardwareListCompatible, appHardwareListNotCompatible, versionList.get(0).getNativecode());
        }
    }

    private void updateAppProfiles(int appId, List<Version> versionList,
                                   List<AppHardware> appHardwareListCompatible,
                                   List<AppHardware> appHardwareListNotCompatible) {
        Map<Integer, AppHardware> id2AppHardware = new HashMap<>();

        for (AppHardware appHardware : appHardwareListCompatible) {
            id2AppHardware.put(appHardware.getHardwareProfileId(), appHardware);
        }

        for (HardwareProfile hp : this.hardwareProfiles) {
            AppHardware appHardware = id2AppHardware.get(hp.getId());
            if (appHardware == null) {
                appHardware = new AppHardware(appId, hp.getId());
            } else {
                appHardwareListCompatible.remove(appHardware);
            }

            if (calculateAppHardware(appHardware, hp, versionList)) {
                appHardwareListCompatible.add(appHardware);
            } else {
                appHardwareListNotCompatible.add(appHardware);
            }
        }
    }

    private void save(App app, List<AppHardware> appHardwareListCompatible, List<AppHardware> appHardwareListNotCompatible, String nativecode) {
        for (AppHardware appHardware : appHardwareListCompatible) {
            if (appHardware.getId() == 0) {
                appHardwareRepository.insert(appHardware);
            } else {
                appHardwareRepository.update(appHardware);
            }
        }
        for (AppHardware appHardware : appHardwareListNotCompatible) {
            if (appHardware.getId() != 0) {
                appHardwareRepository.delete(appHardware);
            }
        }
        deleteAppIfNotCompatible(app, appHardwareListCompatible, nativecode);
    }

    private void deleteAppIfNotCompatible(App app, List<AppHardware> appHardwareListCompatible, String nativecode) {
        if (hpForDelete && appHardwareListCompatible.isEmpty() && app != null) {
            StringBuilder message = new StringBuilder()
                    .append("Deleting app '").append(app.getPackageName()).append("':").append(app.getSearchName())
                    .append(" (sdk:").append(app.getSearchSdk());
            if (nativecode != null) {
                message.append(", nativecode: '").append(nativecode);
            }
            message.append("'), because it is not compatible with any HardwareProfile");
            log(message.toString());
            appRepository.delete(app);
        }
    }

    private void log(String message) {
        if (progressObserver != null) progressObserver.log(message);
    }

    public void setProgressListener(ProgressObserver progressObserver) {
        this.progressObserver = progressObserver;
    }
}
