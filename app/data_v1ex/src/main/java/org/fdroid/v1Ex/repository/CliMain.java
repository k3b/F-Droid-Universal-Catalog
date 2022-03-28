package org.fdroid.v1Ex.repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CliMain {
    public static void main(String[] args) throws IOException {
        FDroidCatalogJsonStreamParserBase repo = new FDroidCatalogJsonStreamParserDemo();

        // InputStream is = new FileInputStream("/home/EVE/StudioProjects/FDroid/app/data_v1ex/src/main/java/org/fdroid/v1Ex/repository/index-v1.small.json");
        InputStream is = new FileInputStream("/home/EVE/StudioProjects/FDroid/app/data_v1ex/src/main/java/org/fdroid/v1Ex/repository/index-v1.full.json");
        //index-v1ex.small.json");

        repo.readJsonStream(is);

        is.close();
    }
}
