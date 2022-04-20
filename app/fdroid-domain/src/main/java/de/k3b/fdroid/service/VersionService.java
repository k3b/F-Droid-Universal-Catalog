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

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Version;
import de.k3b.fdroid.domain.common.VersionCommon;
import de.k3b.fdroid.util.StringUtil;

public class VersionService {
    private static final DecimalFormat numFormatter = new DecimalFormat("00");

    /**
     * if a Version has a maxSdkVersion then all previous versions get the same maxSdkVersion
     */
    public void fixMaxSdk(List<Version> versionList) {
        // requires api 24. not compatible with api 14
        // ArrayList<Version> sortedList = new ArrayList<>(versionList);
        // sortedList.sort(java.util.Comparator.comparing(Version::versionCode));

        Version[] sorted = versionList.toArray(new Version[0]);
        Arrays.sort(sorted, VersionCommon.compareByVersionCodeDescending());

        Version lastFound;
        do {
            lastFound = null;
            for (int i = 0; i < sorted.length; i++) {
                Version v = sorted[i];
                if (v != null) {
                    if (v.getMaxSdkVersion() != 0 && lastFound == null) {
                        lastFound = v;

                        sorted[i] = null;
                    } else if (v.getMaxSdkVersion() != 0 && lastFound != null && isCompatibleNativeCode(lastFound, v)) {
                        lastFound = v;

                        sorted[i] = null;
                    } else if (isCompatibleNativeCode(lastFound, v)) {
                        fixMaxSdk(v, lastFound.getMaxSdkVersion());

                        sorted[i] = null;
                    }
                }
            }
        } while (lastFound != null);
        // return versionList;
    }

    private void fixMaxSdk(Version v, int maxSdkVersion) {
        v.setMaxSdkVersion(maxSdkVersion);
    }

    private boolean isCompatibleNativeCode(Version lastFound, Version v) {
        return lastFound != null &&
                v.getMinSdkVersion() <= lastFound.getMaxSdkVersion() &&
                HardwareProfileService.isCompatibleNativecode(lastFound.getNativecodeArray(), v.getNativecodeArray());
    }

    public void updateAppAggregates(App app, List<Version> roomVersionList) {
        Version minVersion = null;
        Version maxVersion = null;
        StringBuilder signer = new StringBuilder();

        for (Version roomVersion : roomVersionList) {
            String s = roomVersion.getSigner();
            if (!StringUtil.isEmpty(s) && !signer.toString().contains(s)) {
                signer.append(s).append(" ");
            }
            if (minVersion == null || minVersion.getVersionCode() > roomVersion.getVersionCode()) {
                minVersion = roomVersion;
            }
            if (maxVersion == null || maxVersion.getVersionCode() < roomVersion.getVersionCode()) {
                maxVersion = roomVersion;
            }
        }

        StringBuilder sdk = new StringBuilder();
        add(sdk, minVersion.getMinSdkVersion(), minVersion.getTargetSdkVersion(), minVersion.getMaxSdkVersion());

        StringBuilder code = new StringBuilder();
        add(code, minVersion.getVersionName(), minVersion.getVersionCode());

        if (minVersion.getVersionCode() != maxVersion.getVersionCode()) {
            add(sdk.append(" - "), maxVersion.getMinSdkVersion(), maxVersion.getTargetSdkVersion(), maxVersion.getMaxSdkVersion());
            add(code.append(" - "), maxVersion.getVersionName(), maxVersion.getVersionCode());
        }

        app.setSearchVersion(code.toString());
        app.setSearchSdk(sdk.toString());
        app.setSearchSigner(signer.toString());

    }

    private void add(StringBuilder code, String versionName, int versionCode) {
        if (!StringUtil.isEmpty(versionName)) {
            code.append(versionName);
        }
        if (!StringUtil.isEmpty(versionCode)) {
            code.append("(").append(versionCode).append(")");
        }
    }

    private void add(StringBuilder version, int minSdkVersion, int targetSdkVersion, int maxSdkVersion) {
        version.append("[");
        if (!StringUtil.isEmpty(minSdkVersion)) version.append(numFormatter.format(minSdkVersion));
        version.append(",");
        if (!StringUtil.isEmpty(targetSdkVersion))
            version.append(numFormatter.format(targetSdkVersion));
        version.append(",");
        if (!StringUtil.isEmpty(maxSdkVersion)) version.append(numFormatter.format(maxSdkVersion));
        version.append("]");
    }

}