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

package org.fdroid.v1.repository;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.fdroid.v1.model.Repo;
import org.fdroid.v1.model.Version;
import org.fdroid.v1.model.App;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;

/**
 * Abstract Json Stream parser for FDroid-Catalog-v1 format.
 *
 * To implement overwrite the abstract methods
 * {@link #onRepo(Repo)}, {@link #onApp(App)}, {@link #onVersion(String, Version)}
 */
public abstract class FDroidCatalogJsonStreamParserBase {

    public static final String SIGNED_FILE_NAME = "index-v1.jar";
    public static final String DATA_FILE_NAME = "index-v1.json";

    /**
     * parse the FDroid-Catalog-v1-Json into a stream of calls to consuming
     * {@link #onRepo(Repo)}, {@link #onApp(App)}, {@link #onVersion(String, Version)}
     * @param jsonInputStream uncompressed Json inputstream
     * @throws IOException if there are errors jsonInputStream the JSON inputstream
     */
    public void readJsonStream(InputStream jsonInputStream) throws IOException {
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
                if (DATA_FILE_NAME.equalsIgnoreCase(entry.getName())) {
                    readJsonStream(zipInputStream);

                    // stream was closed by caller
                    return ;
                }
            }
        }
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

    public String asDateString(long longDate) {
        if (longDate == 0) return "";

        Date date = new Date(longDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        return format.format(date);
    }

    /** Stream event, when something has to be logged */
    protected abstract String log(String s);

    /** Stream event, when a {@link org.fdroid.v1.model.Repo} was read */
    protected abstract void onRepo(Repo repo);

    /** Stream event, when a {@link org.fdroid.v1.model.App} was read */
    protected abstract void onApp(App app);

    /** Stream event, when a {@link org.fdroid.v1.model.Version} was read */
    protected abstract void onVersion(String name, Version version);
}
