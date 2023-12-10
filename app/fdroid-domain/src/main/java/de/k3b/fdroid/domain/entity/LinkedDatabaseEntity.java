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
package de.k3b.fdroid.domain.entity;

import de.k3b.fdroid.domain.interfaces.IAppDetail;
import de.k3b.fdroid.domain.interfaces.IDatabaseEntityWithId;

public class LinkedDatabaseEntity<LINK extends IAppDetail, ITEM extends IDatabaseEntityWithId> implements IAppDetail {
    private final LINK link;
    private final ITEM item;

    public LinkedDatabaseEntity(LINK link, ITEM item) {
        this.link = link;
        this.item = item;
    }

    public LINK getLink() {
        return link;
    }

    public ITEM getItem() {
        return item;
    }

    @Override
    public int getAppId() {
        return link.getAppId();
    }

    @Override
    public int getId() {
        return link.getId();
    }
}
