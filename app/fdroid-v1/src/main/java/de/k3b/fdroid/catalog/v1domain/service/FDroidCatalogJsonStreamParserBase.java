/*
 * Copyright (c) 2022-2023 by k3b.
 *
 * This file is part of org.fdroid.v1domain the fdroid json catalog-format-v1 parser.
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

package de.k3b.fdroid.catalog.v1domain.service;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import de.k3b.fdroid.Global;
import de.k3b.fdroid.catalog.v1domain.entity.V1App;
import de.k3b.fdroid.catalog.v1domain.entity.V1Repo;
import de.k3b.fdroid.catalog.v1domain.entity.V1Version;
import de.k3b.fdroid.domain.entity.common.RepoCommon;
import de.k3b.fdroid.domain.util.NoCloseInputStream;

/**
 * Abstract Json-Stream-Parser for FDroid-Catalog-v1 format.
 * * can import big json files without too much memory requirements.
 * * Only the data of one app is kept in memory at a time.
 * <p>
 * To implement overwrite the abstract methods
 * {@link #onRepo(V1Repo)}, {@link #onApp(V1App)}, {@link #onVersion(String, V1Version)}
 */
public abstract class FDroidCatalogJsonStreamParserBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(Global.LOG_TAG_IMPORT);
    public static String charsetName = "UTF-8";

    /**
     * parse the FDroid-Catalog-v1-Json into a stream of calls to consuming
     * {@link #onRepo(V1Repo)}, {@link #onApp(V1App)}, {@link #onVersion(String, V1Version)}
     *
     * @param jsonInputStream uncompressed Json inputstream
     * @throws IOException if there are errors in the InputStream or the JSON v1 format.
     */
    public void readJsonStream(InputStream jsonInputStream) throws IOException {
        if (jsonInputStream == null) throw new NullPointerException();

        // StandardCharsets.UTF_8=Charset.forName("UTF-8") not supported by api-14
        Charset utf8 = Charset.forName(charsetName); // StandardCharsets.UTF_8;
        try (JsonReader reader = new JsonReader(new InputStreamReader(jsonInputStream, utf8))) {

            Gson gson = new Gson();
            while (reader.hasNext()) {
                JsonToken token = reader.peek();
                if (JsonToken.NAME.equals(token)) {
                    readNameValue(gson, reader);
                } else {
                    debug(reader);
                }
            }
        }
    }

    /**
     * parse the compressed/signed jar file into a stream of calls to consuming
     * {@link #onRepo(V1Repo)}, {@link #onApp(V1App)}, {@link #onVersion(String, V1Version)}
     *
     * @param jarInputStream jar-/zip-compressed inputstream containing json data
     * @throws IOException if there are errors in the InputStream, the jar-compression or the JSON v1 format.
     */
    public void readFromJar(InputStream jarInputStream) throws IOException {
        if (jarInputStream == null) throw new NullPointerException();

        try (JarInputStream zipInputStream = new JarInputStream(jarInputStream)) {
            ZipEntry entry;
            while (null != (entry = zipInputStream.getNextEntry())) {
                if (RepoCommon.V1_JSON_NAME.equalsIgnoreCase(entry.getName())) {
                    readJsonStream(new NoCloseInputStream(zipInputStream));
                    afterJsonJarRead((JarEntry) entry);
                    return;
                }
            }
        }
    }

    /**
     * called after json reading of jar was completed. can be used to verify signature.
     */
    protected void afterJsonJarRead(JarEntry zipInputStream) {
    }

    private void readNameValue(Gson gson, JsonReader reader) throws IOException {
        String name = reader.nextName();
        LOGGER.debug("readObject(" + name + ")");

        switch (name) {
            case "repo":
                onRepo(repoFromJson(gson, reader));
                break;
            case "apps":
                readAppsArray(gson, reader);
                break;
            case "packages":
                readPackagesArray(gson, reader);
                break;
            default:
                skipJsonValue(reader);
                break;
        }
    }

    protected void skipJsonValue(JsonReader reader) throws IOException {
        reader.skipValue();
    }

    protected V1Repo repoFromJson(Gson gson, JsonReader reader) throws IOException {
        return gson.fromJson(reader, V1Repo.class);
    }

    protected V1App appFromJson(Gson gson, JsonReader reader) throws IOException {
        return gson.fromJson(reader, V1App.class);
    }

    protected V1Version versionFromJson(Gson gson, JsonReader reader) throws IOException {
        return gson.fromJson(reader, V1Version.class);
    }

    private void readPackagesArray(Gson gson, JsonReader reader) throws IOException {
        // {"com.chancehorizon.just24hoursplus": [{...},{...}]} , ...

        reader.beginObject();
        JsonToken token = reader.peek();
        while (JsonToken.NAME.equals(token)) {
            String packageName = reader.nextName();
            reader.beginArray();
            token = reader.peek();
            while (!JsonToken.END_ARRAY.equals(token)) {
                if (JsonToken.BEGIN_OBJECT.equals(token)) {
                    onVersion(packageName, versionFromJson(gson, reader));
                    token = reader.peek();
                } else {
                    debug(reader);
                }
            }
            reader.endArray();
            token = reader.peek();
        }
        onVersion(null, null);
    }

    private void readAppsArray(Gson gson, JsonReader reader) throws IOException {
        reader.beginArray();
        JsonToken token = reader.peek();
        while (!JsonToken.END_ARRAY.equals(token)) {
            if (JsonToken.BEGIN_OBJECT.equals(token)) {
                onApp(appFromJson(gson, reader));
                token = reader.peek();
            } else {
                debug(reader);
            }
        }
        reader.endArray();
        onApp(null);
    }

    private void debug(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();
        switch (token) {
            case BEGIN_ARRAY:
                reader.beginArray();
                LOGGER.debug("" + token);
                break;
            case END_ARRAY:
                reader.endArray();
                LOGGER.debug("" + token);
                break;
            case BEGIN_OBJECT:
                reader.beginObject();
                LOGGER.debug("" + token);
                break;
            case END_OBJECT:
                reader.endObject();
                LOGGER.debug("" + token);
                break;
            case NAME:
                String name = reader.nextName();
                LOGGER.debug("NAME(" + name + ")");
                break;
            case STRING:
                String s = reader.nextString();
                LOGGER.debug("STRING(" + s + ")");
                break;
            case NUMBER:
                String n = reader.nextString();
                LOGGER.debug("NUMBER(" + n + ")");
                break;
            case BOOLEAN:
                boolean b = reader.nextBoolean();
                LOGGER.debug("BOOLEAN(" + b + ")");
                break;
            case NULL:
                reader.nextNull();
                LOGGER.debug("" + token);
                break;
            case END_DOCUMENT:
                reader.endObject();
                LOGGER.debug("" + token);
                break;
        }
    }

    /**
     * Stream event, when something has to be logged
     */
    protected abstract String log(String s);

    /**
     * Stream event, when a {@link V1Repo} was read
     */
    protected abstract void onRepo(V1Repo v1Repo);

    /**
     * Stream event, when a {@link V1App} was read
     */
    protected abstract void onApp(V1App v1App);

    /**
     * Stream event, when a {@link V1Version} was read
     */
    protected abstract void onVersion(String name, V1Version v1Version);
}
