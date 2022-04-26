package de.k3b.fdroidjsonparser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.k3b.fdroid.android.db.FDroidDatabase;
import de.k3b.fdroid.android.db.V1UpdateServiceAndroid;
import de.k3b.fdroid.domain.Locale;
import de.k3b.fdroid.domain.interfaces.LocaleRepository;
import de.k3b.fdroid.v1.service.V1UpdateService;

@RunWith(AndroidJUnit4.class)
public class LocaleEntityReadWriteTest {
    private LocaleRepository localeDao;
    private FDroidDatabase db;
    private V1UpdateService importer;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, FDroidDatabase.class).build();

        importer = V1UpdateServiceAndroid.create(db);
        localeDao = db.localeDao();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeLocaleAndReadInList() throws Exception {
        Locale locale = new Locale();
        locale.setCode("ZQ");
        localeDao.insert(locale);
        List<Locale> byName = localeDao.findAll();
        assertThat(byName.get(0), equalTo(locale));
    }


    @Test
    public void importV1Jar() throws Exception {
        InputStream in = LocaleEntityReadWriteTest.class.getResourceAsStream("/c_geo_nightlies-index-v1.jar");
        importer.readFromJar(in);
    }
}
