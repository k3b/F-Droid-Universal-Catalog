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
package de.k3b.fdroid.domain.entity;

import de.k3b.fdroid.domain.interfaces.AppDetail;
import de.k3b.fdroid.domain.interfaces.DatabaseEntityWithId;

public class LinkedDatabaseEntity<LINK extends AppDetail, ITEM extends DatabaseEntityWithId> implements AppDetail {
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
