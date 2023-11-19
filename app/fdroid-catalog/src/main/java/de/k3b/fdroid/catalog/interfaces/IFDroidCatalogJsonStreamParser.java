/*
 * Copyright (c) 2023 by k3b.
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
package de.k3b.fdroid.catalog.interfaces;

import java.io.IOException;
import java.io.InputStream;

public interface IFDroidCatalogJsonStreamParser {
    /**
     * parse the FDroid-Catalog-Json from a stream.
     *
     * @param jsonInputStream uncompressed Json inputstream
     * @throws IOException if there are errors in the InputStream or the JSON v1 format.
     */
    void readJsonStream(InputStream jsonInputStream) throws IOException;

    /**
     * parse the compressed/signed jar file from a stream
     *
     * @param jarInputStream jar-/zip-compressed inputstream containing json data
     * @throws IOException if there are errors in the InputStream, the jar-compression or the JSON v1 format.
     */
    void readFromJar(InputStream jarInputStream) throws IOException;
}
