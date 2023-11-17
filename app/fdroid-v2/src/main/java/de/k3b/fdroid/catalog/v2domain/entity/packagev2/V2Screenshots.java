/*
 * Copyright (c) 2023 by k3b.
 *
 * This file is part of de.k3b.fdroid.v2domain the fdroid json catalog-format-v2 parser.
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
package de.k3b.fdroid.catalog.v2domain.entity.packagev2;
// V2Screenshots.java

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import de.k3b.fdroid.catalog.v2domain.entity.repo.V2File;

public class V2Screenshots {

    /**
     * phone Screenshot Dir per language (not part of v2-catalog but calculated during import)
     */
    @Nullable
    private Map<String, String> phoneDir;

    @Nullable
    private Map<String, List<V2File>> phone;
    @Nullable
    private Map<String, List<V2File>> sevenInch;
    @Nullable
    private Map<String, List<V2File>> tenInch;
    @Nullable
    private Map<String, List<V2File>> wear;
    @Nullable
    private Map<String, List<V2File>> tv;

    public boolean isNull() {
        return this.phone == null && this.sevenInch == null && this.tenInch == null && this.wear == null && this.tv == null;
    }

    @Nullable
    public Map<String, List<V2File>> getPhone() {
        return this.phone;
    }

    public void setPhone(Map<String, List<V2File>> phone) {
        this.phone = phone;
    }

    @Nullable
    public Map<String, String> getPhoneDir() {
        return this.phoneDir;
    }

    public void setPhoneDir(Map<String, String> phoneDir) {
        this.phoneDir = phoneDir;
    }

    @Nullable
    public Map<String, List<V2File>> getSevenInch() {
        return this.sevenInch;
    }

    @Nullable
    public Map<String, List<V2File>> getTenInch() {
        return this.tenInch;
    }

    @Nullable
    public Map<String, List<V2File>> getWear() {
        return this.wear;
    }

    @Nullable
    public Map<String, List<V2File>> getTv() {
        return this.tv;
    }

    @NotNull
    public String toString() {
        return "V2Screenshots{phone=" + this.phone + ", sevenInch=" + this.sevenInch + ", tenInch=" + this.tenInch + ", wear=" + this.wear + ", tv=" + this.tv + "}";
    }
}
