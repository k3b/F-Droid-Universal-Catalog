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

import de.k3b.fdroid.domain.common.PojoCommon;
import de.k3b.fdroid.domain.interfaces.ItemWithId;
import de.k3b.fdroid.util.StringUtil;

/**
 * android device compatibility caracteristics used for filtering against App-{@link Version}
 * To answer the questioon: is there an app-version that is compatible witch my device.
 * <p>
 * Example: I have several android devices or {@link HardwareProfile}s:
 * * name="my android-10", SdkVersion=29(=Android-10), nativecode=[armeabi-v7a, armeabi].
 * * name="my android-7.0", SdkVersion=24(=Android-7.0), nativecode=[arme64-v8a, armeabi-v7a, armeabi].
 * * name="my android-4.2 Tablet", SdkVersion=17 (=Android-4.2), nativecode=[armeabi-v7a].
 * <p>
 * (Hardware data can be taken from App-Manager(397=2.6.5.1) https://f-droid.org/packages/io.github.muntashirakon.AppManager/)
 * <p>
 * Android independant: Pojo-s with all properties that are persisted in the Database.
 * Only primitives, primaryKeys and foreignKeys. No Relations or Objects or lists.
 * Database Entity compatible with Android-Room and non-android-j2se-jpa
 */
@androidx.room.Entity
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class HardwareProfile extends PojoCommon implements ItemWithId {
    @javax.persistence.Id
    @javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.AUTO)
    @androidx.room.PrimaryKey(autoGenerate = true)
    private int id;

    private String name;
    private int SdkVersion;
    private String nativecode;
    /**
     * calculated and cached from {@link #nativecode}. Not persisted in Database
     */
    @androidx.room.Ignore
    @javax.persistence.Transient
    private String[] nativecodeArray;

    private boolean deleteIfNotCompatible = false;

    public HardwareProfile() {
    }

    public HardwareProfile(String name) {
        this.setName(name);
    }

    public HardwareProfile(String name, int sdkVersion, String nativecode) {
        this(name);
        this.setSdkVersion(sdkVersion);
        this.setNativecode(nativecode);

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSdkVersion() {
        return SdkVersion;
    }

    public void setSdkVersion(int sdkVersion) {
        SdkVersion = sdkVersion;
    }

    public String getNativecode() {
        return nativecode;
    }

    public void setNativecode(String nativecode) {
        this.nativecode = nativecode;
        nativecodeArray = null;
    }

    public String[] getNativecodeArray() {
        if (nativecodeArray == null) {
            nativecodeArray = StringUtil.toStringArray(nativecode);
        }
        return nativecodeArray;
    }

    public boolean isDeleteIfNotCompatible() {
        return deleteIfNotCompatible;
    }

    public void setDeleteIfNotCompatible(boolean deleteIfNotCompatible) {
        this.deleteIfNotCompatible = deleteIfNotCompatible;
    }

    protected void toStringBuilder(StringBuilder sb) {
        toStringBuilder(sb, "id", this.id);
        toStringBuilder(sb, "name", this.name);
        super.toStringBuilder(sb);
        toStringBuilder(sb, "deleteIfNotCompatible", this.deleteIfNotCompatible);
        toStringBuilder(sb, "SdkVersion", this.SdkVersion);
        toStringBuilder(sb, "nativecode", this.nativecode);
    }
}
