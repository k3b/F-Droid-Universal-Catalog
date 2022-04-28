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
package de.k3b.fdroid.domain;

import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.domain.interfaces.ItemWithId;
import de.k3b.fdroid.util.StringUtil;

/**
 * Android independant: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa.
 * <p>
 * Some repo-s
 * <p>
 * https://guardianproject.info/fdroid/repo/index-v1.jar
 * https://apt.izzysoft.de/fdroid/repo/index-v1.jar
 * https://f-droid.org/repo/index-v1.jar
 * https://f-droid.org/archive/index-v1.jar
 */
@androidx.room.Entity(indices = {@androidx.room.Index("id")})
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class Repo extends RepoCommon implements ItemWithId {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    private String mirrors = null;
    /**
     * calculated and cached from {@link #mirrors}. Not persisted in Database
     */
    @androidx.room.Ignore
    @javax.persistence.Transient
    private String[] mirrorsArray;

    private String jarSigningCertificate;

    private String jarSigningCertificateFingerprint;

    private String lastUsedDownloadMirror;

    @androidx.room.ColumnInfo(defaultValue = "0")
    private long lastUsedDownloadDateTimeUtc;

    @androidx.room.ColumnInfo(defaultValue = "0")
    private int lastAppCount;

    @androidx.room.ColumnInfo(defaultValue = "0")
    private int lastVersionCount;

    @androidx.room.ColumnInfo(defaultValue = "0")
    private boolean autoDownloadEnabled;

    public Repo() {
    }

    @androidx.room.Ignore
    public Repo(String name, String address) {
        setName(name);
        setAddress(address);
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "autoDownloadEnabled", this.autoDownloadEnabled);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "mirrors", this.mirrors);
        toDateStringBuilder(sb, "lastUsedDownloadDateTimeUtc", this.lastUsedDownloadDateTimeUtc);
        toStringBuilder(sb, "lastAppCount", this.lastAppCount);
        toStringBuilder(sb, "lastVersionCount", this.lastVersionCount);
        toStringBuilder(sb, "lastUsedDownloadMirror", this.lastUsedDownloadMirror);
        toStringBuilder(sb, "jarSigningCertificate", this.jarSigningCertificate, 14);
        toStringBuilder(sb, "jarSigningCertificateFingerprint", this.jarSigningCertificateFingerprint, 14);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMirrors() {
        return mirrors;
    }

    public void setMirrors(String mirrors) {
        this.mirrors = mirrors;
        mirrorsArray = null;
    }

    public String[] getMirrorsArray() {
        if (mirrorsArray == null) {
            mirrorsArray = StringUtil.toStringArray(getAddress() + "," + mirrors);
        }
        return mirrorsArray;
    }

    /**
     * values of current "index-v1.jar"
     */
    public String getJarSigningCertificate() {
        return jarSigningCertificate;
    }

    public void setJarSigningCertificate(String jarSigningCertificate) {
        this.jarSigningCertificate = jarSigningCertificate;
    }

    /**
     * values of current "index-v1.jar"
     */
    public String getJarSigningCertificateFingerprint() {
        return jarSigningCertificateFingerprint;
    }

    public void setJarSigningCertificateFingerprint(String jarSigningCertificateFingerprint) {
        this.jarSigningCertificateFingerprint = jarSigningCertificateFingerprint;
    }

    public String getLastUsedDownloadMirror() {
        if (lastUsedDownloadMirror != null) {
            return lastUsedDownloadMirror;
        }
        return getAddress();
    }

    public void setLastUsedDownloadMirror(String lastUsedDownloadMirror) {
        this.lastUsedDownloadMirror = lastUsedDownloadMirror;
    }

    public String getV1Url() {
        String server = getLastUsedDownloadMirror();
        if (server == null) return null;
        StringBuilder url = new StringBuilder().append(server);
        if (!server.endsWith(".jar")) {
            if (!server.endsWith("/")) url.append("/");
            url.append(V1_JAR_NAME);
        }
        return url.toString();
    }

    public long getLastUsedDownloadDateTimeUtc() {
        if (lastUsedDownloadDateTimeUtc != 0) {
            return lastUsedDownloadDateTimeUtc;
        }
        return getTimestamp();
    }
    public String getLastUsedDownloadDateTimeUtcDate() {
        long l = getLastUsedDownloadDateTimeUtc();
        return l == 0 ? null : asDateString(l);
    }

    public void setLastUsedDownloadDateTimeUtc(long lastUsedDownloadDateTimeUtc) {
        this.lastUsedDownloadDateTimeUtc = lastUsedDownloadDateTimeUtc;
    }

    public int getLastAppCount() {
        return lastAppCount;
    }

    public void setLastAppCount(int lastAppCount) {
        this.lastAppCount = lastAppCount;
    }

    public int getLastVersionCount() {
        return lastVersionCount;
    }

    public void setLastVersionCount(int lastVersionCount) {
        this.lastVersionCount = lastVersionCount;
    }

    public boolean isAutoDownloadEnabled() {
        return autoDownloadEnabled;
    }

    public void setAutoDownloadEnabled(boolean autoDownloadEnabled) {
        this.autoDownloadEnabled = autoDownloadEnabled;
    }
}
