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
package de.k3b.fdroid.jpa.repository.base;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitBasicColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitEntityNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyJpaImpl;

/**
 * let JPA and android-room create the same table and field names
 * * Example entity AppVersion.versionCode
 * * jpa default: APP_VERSION-VERSION_CODE
 * * android rooom: AppVersion.versionCode
 * * jpa room APPVERSION.VERSIONCODE can be used with AppVersion.versionCode
 * <p>
 * see application.properties
 * * spring.jpa.hibernate.naming.physical-strategy=de.k3b.fdroid.jpa.repository.base.PhysicalNamingStrategyRoomImpl
 * * spring.jpa.hibernate.naming.implicit-strategy=de.k3b.fdroid.jpa.repository.base.ImplicitNamingStrategyRoomJpaImpl
 */
public class ImplicitNamingStrategyRoomJpaImpl extends ImplicitNamingStrategyLegacyJpaImpl {
    // lower-case-only eleminates the "_"
    public static Identifier getRoomIdentifier(Identifier identifier) {
        if (identifier.isQuoted()) return identifier;
        return new Identifier(identifier.getText().toLowerCase(), identifier.isQuoted());
    }

    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        Identifier identifier = super.determineBasicColumnName(source);
        return getRoomIdentifier(identifier);
    }

    @Override
    public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
        Identifier identifier = super.determinePrimaryTableName(source);
        return getRoomIdentifier(identifier);
    }

}
