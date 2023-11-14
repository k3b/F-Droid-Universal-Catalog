/*
 * Copyright (c) 2022 by k3b.
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
package de.k3b.fdroid.jpa.repository.base;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

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
public class PhysicalNamingStrategyRoomImpl extends PhysicalNamingStrategyStandardImpl {
    @Override
    public Identifier toPhysicalColumnName(Identifier name, JdbcEnvironment context) {
        Identifier identifier = super.toPhysicalColumnName(name, context);
        return ImplicitNamingStrategyRoomJpaImpl.getRoomIdentifier(identifier);
    }

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        Identifier identifier = super.toPhysicalTableName(name, context);
        return ImplicitNamingStrategyRoomJpaImpl.getRoomIdentifier(identifier);
    }
}
