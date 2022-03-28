package org.fdroid.v1Ex.repository;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.fdroid.v1Ex.model.Repo;
import org.fdroid.v1Ex.model.Version;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

/** json stream praser demo für FDroid.org index v1 format. */
public class FDroidCatalogJsonStreamParserDemo extends FDroidCatalogJsonStreamParserBase {
    @Override
    protected String log(String s) {
        System.out.println(s);
        return s;
    }

    @Override
    protected void onRepo(Repo repo) {
        log("onRepo(" + repo.getName() + ")");
    }

    // cannot use generated java class App because of localisation map
    @Override
    protected void onApp(JsonObject app) {
        StringBuilder nameWithLocales = new StringBuilder();
        JsonElement lastUpdated = app.get("lastUpdated");
        if (lastUpdated != null) {
            String dateString = asDateString(lastUpdated);
            nameWithLocales.append(dateString).append(" ");
        }

        nameWithLocales.append(app.get("packageName"));

        JsonObject locales = app.getAsJsonObject("localized");
        if (locales != null) {
            Set<String> localeNames = locales.keySet();
            for (String locale : localeNames) {
                nameWithLocales.append(" ").append(locale);
                addStatistics(locale, locales.getAsJsonObject(locale));
            }
        }
        log("onApp(" + nameWithLocales + ")");
        // app.getAsJsonObject("localized").getAsJsonObject("en-US").get("summary");
    }

    private HashMap<String,Integer> summary = new HashMap<>();
    private HashMap<String,Integer> description = new HashMap<>();

    private void addStatistics(String locale, JsonObject asJsonObject) {
        addStatistics(summary, locale, asJsonObject.get("summary"));
        addStatistics(description, locale, asJsonObject.get("description"));
    }

    private void addStatistics(HashMap<String, Integer> map, String locale, JsonElement value) {
        if (value != null && !value.getAsString().trim().isEmpty()) {
            Integer oldValue = map.get(locale);
            if (oldValue == null) oldValue = 0;
            map.put(locale, oldValue + 1);
        }
    }

    @Override
    protected void onVersion(String name, Version version) {
        log("onVersion(" + name +
                "," + version.getVersionName() + "," + asDateString(version.getAdded()) +
                ")");
    }
}
