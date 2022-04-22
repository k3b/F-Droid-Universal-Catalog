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

package de.k3b.fdroid.v1.service;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

import de.k3b.fdroid.domain.common.RepoCommon;
import de.k3b.fdroid.util.NoCloseInputStream;
import de.k3b.fdroid.v1.domain.App;
import de.k3b.fdroid.v1.domain.Repo;
import de.k3b.fdroid.v1.domain.Version;

/**
 * Abstract Json Stream parser for FDroid-Catalog-v1 format.
 * <p>
 * To implement overwrite the abstract methods
 * {@link #onRepo(Repo)}, {@link #onApp(App)}, {@link #onVersion(String, Version)}
 */
public abstract class FDroidCatalogJsonStreamParserBase {

    /**
     * parse the FDroid-Catalog-v1-Json into a stream of calls to consuming
     * {@link #onRepo(Repo)}, {@link #onApp(App)}, {@link #onVersion(String, Version)}
     * @param jsonInputStream uncompressed Json inputstream
     * @throws IOException if there are errors jsonInputStream the JSON inputstream
     */
    public void readJsonStream(InputStream jsonInputStream) throws IOException {
        // StandardCharsets.UTF_8=Charset.forName("UTF-8") not supported by api-14
        try (JsonReader reader = new JsonReader(new InputStreamReader(jsonInputStream, StandardCharsets.UTF_8))) {

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

    public void readFromJar(InputStream jarInputStream) throws IOException {
        try (JarInputStream zipInputStream = new JarInputStream(jarInputStream)) {
            ZipEntry entry;
            while (null != (entry = zipInputStream.getNextEntry())) {
                if (RepoCommon.V1_JSON_NAME.equalsIgnoreCase(entry.getName())) {
                    readJsonStream(new NoCloseInputStream(zipInputStream));
                    afterJsonJarRead((JarEntry) entry);
                }
            }
        }
    }

    /**
     * called after json reading of jar was completed. can be used to verify signature.
     *
     * @param zipInputStream
     */
    protected void afterJsonJarRead(JarEntry zipInputStream) {
    }

    private void readNameValue(Gson gson, JsonReader reader) throws IOException {
        String name = reader.nextName();
        log("readObject(" + name + ")");
        switch (name) {
            case "repo":
                Repo repo = gson.fromJson(reader, Repo.class);
                onRepo(repo);
                break;
            case "apps":
                readAppsArray(gson, reader);
                break;
            case "packages":
                readPackagesArray(gson, reader);
                break;
            default:
                reader.skipValue();
                break;
        }
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
                    Version version = gson.fromJson(reader, Version.class);
                    onVersion(packageName, version);
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
                App app = gson.fromJson(reader, App.class);
                onApp(app);
                token = reader.peek();
            } else {
                debug(reader);
            }
        }
        reader.endArray();
        onApp(null);
    }

    private String debug(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();
        switch (token) {
            case BEGIN_ARRAY:
                reader.beginArray();
                return log("" + token);
            case END_ARRAY:
                reader.endArray();
                return log("" + token);
            case BEGIN_OBJECT:
                reader.beginObject();
                return log("" + token);
            case END_OBJECT:
                reader.endObject();
                return log("" + token);
            case NAME:
                String name = reader.nextName();
                return log("NAME(" + name +")" );
            case STRING:
                String s = reader.nextString();
                return log("STRING(" + s +")" );
            case NUMBER:
                String n = reader.nextString();
                return log("NUMBER(" + n +")" );
            case BOOLEAN:
                boolean b = reader.nextBoolean();
                return log("BOOLEAN(" + b +")" );
            case NULL:
                reader.nextNull();
                return log("" + token);
            case END_DOCUMENT:
                reader.endObject();
                return log("" + token);
                // return;
        }
        return "";
    }

    /** Stream event, when something has to be logged */
    protected abstract String log(String s);

    /**
     * Stream event, when a {@link de.k3b.fdroid.v1.domain.Repo} was read
     */
    protected abstract void onRepo(Repo repo);

    /** Stream event, when a {@link de.k3b.fdroid.v1.domain.App} was read */
    protected abstract void onApp(App app);

    /** Stream event, when a {@link de.k3b.fdroid.v1.domain.Version} was read */
    protected abstract void onVersion(String name, Version version);
}
