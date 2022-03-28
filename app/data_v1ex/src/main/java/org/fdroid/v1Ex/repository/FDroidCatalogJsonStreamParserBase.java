package org.fdroid.v1Ex.repository;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import org.fdroid.v1Ex.model.Repo;
import org.fdroid.v1Ex.model.Version;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class FDroidCatalogJsonStreamParserBase {
    public void readJsonStream(InputStream in) throws IOException {
        try (JsonReader reader = new JsonReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {

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
                JsonObject app = gson.fromJson(reader, JsonObject.class);
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

    protected abstract String log(String s);

    protected abstract void onRepo(Repo repo);

    // cannot use generated java class App because of localisation map
    // example usage: app.getAsJsonObject("localized").getAsJsonObject("en-US").get("summary");
    protected abstract void onApp(JsonObject app);

    public String asDateString(JsonElement lastUpdated) {
        if (lastUpdated == null) return "";

        long longDate = lastUpdated.getAsLong();
        return asDateString(longDate);
    }

    public String asDateString(long longDate) {
        if (longDate == 0) return "";

        Date date = new Date(longDate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = format.format(date);
        return dateString;
    }

    protected abstract void onVersion(String name, Version version);
}
