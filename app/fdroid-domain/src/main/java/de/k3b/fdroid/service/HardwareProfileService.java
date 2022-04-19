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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.k3b.fdroid.domain.AppHardware;
import de.k3b.fdroid.domain.HardwareProfile;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.common.ProfileCommon;
import de.k3b.fdroid.domain.interfaces.HardwareProfileRepository;
import de.k3b.fdroid.util.StringUtil;

/**
 * Service to cache/find/insert HardwareProfile info.
 */

public class HardwareProfileService {
    private static final String REGEX_PATTERN_SPLIT_NATIVECODE = "[, \\[\\]].+";
    private final HardwareProfileRepository hardwareProfileRepository;

    Map<Integer, HardwareProfile> id2HardwareProfile = null;
    Map<String, HardwareProfile> name2HardwareProfile = null;
    private List<HardwareProfile> hardwareProfiles;


    public HardwareProfileService(HardwareProfileRepository hardwareProfileRepository) {
        this.hardwareProfileRepository = hardwareProfileRepository;
    }

    public static boolean calculateAppHardware(AppHardware result, HardwareProfile profile, List<Version> versions) {
        Version min = null;
        Version max = null;
        for (Version version : versions) {
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

    public HardwareProfileService init(List<HardwareProfile> hardwareProfiles) {
        this.hardwareProfiles = hardwareProfiles;
        id2HardwareProfile = new HashMap<>();
        name2HardwareProfile = new HashMap<>();

        for (HardwareProfile hardwareProfile : hardwareProfiles) {
            init(hardwareProfile);
        }
        return this;
    }

    private void init(HardwareProfile hardwareProfile) {
        id2HardwareProfile.put(hardwareProfile.getId(), hardwareProfile);
        name2HardwareProfile.put(hardwareProfile.getName(), hardwareProfile);
    }

    public String getHardwareProfileName(int hardwareProfileId) {
        HardwareProfile hardwareProfile = (hardwareProfileId == 0) ? null : id2HardwareProfile.get(hardwareProfileId);
        return (hardwareProfile == null) ? null : hardwareProfile.getName();
    }

    public int getOrCreateHardwareProfileId(String hardwareProfileName) {
        if (hardwareProfileName != null) {
            HardwareProfile hardwareProfile = name2HardwareProfile.get(hardwareProfileName);
            if (hardwareProfile == null) {
                // create on demand
                hardwareProfile = new HardwareProfile();
                hardwareProfile.setName(hardwareProfileName);
                hardwareProfileRepository.insert(hardwareProfile);
                init(hardwareProfile);
            }
            return hardwareProfile.getId();
        }
        return 0;
    }

    public void setHardwareProfiles(List<HardwareProfile> hardwareProfiles) {
        this.hardwareProfiles = hardwareProfiles;
    }

    protected static boolean isCompatibleSdk(int currentSdk, int profileMin, int profileMax) {
        if (profileMin == 0) profileMin = Integer.MIN_VALUE;
        if (profileMax == 0) profileMax = Integer.MAX_VALUE;
        return (currentSdk >= profileMin && currentSdk <= profileMax);
    }

    public static boolean isCompatibleNativecode(String[] currentNativecodes, String[] profileNativecodes) {
        if (StringUtil.isEmpty(currentNativecodes) || StringUtil.isEmpty(profileNativecodes))
            return true;

        for (String current : currentNativecodes) {
            if (StringUtil.contains(current, profileNativecodes)) return true;
        }

        return false;
    }

    public static boolean isCompatible(HardwareProfile profile, Version version) {
        return isCompatibleSdk(profile.getSdkVersion(), version.getMinSdkVersion(), version.getMaxSdkVersion())
                && isCompatibleNativecode(profile.getNativecodeArray(), version.getNativecodeArray());
    }
}
