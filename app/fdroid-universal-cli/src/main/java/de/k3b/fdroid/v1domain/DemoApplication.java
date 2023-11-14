/*
 * Copyright (c) 2022-2023 by k3b.
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
package de.k3b.fdroid.v1domain;

import com.samskivert.mustache.Mustache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import de.k3b.fdroid.domain.repository.AppRepository;
import de.k3b.fdroid.domain.repository.LocalizedRepository;
import de.k3b.fdroid.domain.repository.RepoRepository;
import de.k3b.fdroid.html.service.ResourceBundleMustacheContext;
import de.k3b.fdroid.html.util.MustacheEx;
import de.k3b.fdroid.v1domain.service.HttpV1JarDownloadService;
import de.k3b.fdroid.v1domain.service.V1CommandService;
import de.k3b.fdroid.v1domain.service.V1UpdateServiceEx;

/**
 * j2se-jpa-db implementation that reads from fdroid-v1-jar and updates a jpa database
 */

@EnableJpaRepositories(basePackages = {"de.k3b.fdroid"})
@ComponentScan(basePackages = {"de.k3b.fdroid"})
@EntityScan({"de.k3b.fdroid"})
@SpringBootApplication
public class DemoApplication {
	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	@Value("${de.k3b.fdroid.db.dir}")
	private String dbDir;

	@Value("${de.k3b.fdroid.downloads:~/.fdroid/downloads}")
	private String downloadPath;

	@Value("${spring.datasource.url}")
	private String jdbc;

	@Autowired private RepoRepository repoRepository;
	@Autowired private AppRepository appRepository;
	@Autowired private LocalizedRepository localizedRepository;
	@Autowired private V1UpdateServiceEx importService;
	@Autowired private HttpV1JarDownloadService downloadService;

	// example commandline parameters
	// F-Droid_Archive-index-v1.jar reload archive
	// -f "k3b" find apps by k3b
	public static void main(String[] args) {
		if (args == null || args.length == 0) {
			System.out.println(V1CommandService.getHelp());
			System.exit(0);
		}

		changeJdbcIfServerRunning();

		SpringApplication application = new SpringApplication(DemoApplication.class);
		// ... customize application settings here
		application.run(args);
	}

	@Bean
	public Mustache.Compiler mustacheCompiler(Mustache.TemplateLoader mustacheTemplateLoader) {
		return MustacheEx
				.createMustacheCompiler()
				.withLoader(mustacheTemplateLoader)
				;
	}

	private static void changeJdbcIfServerRunning() {
		String serverConnect = "";
		Connection c;
		try {
			String dbName = getProperty("de.k3b.fdroid.db.name");
			serverConnect = "jdbc:hsqldb:hsql://localhost/" + dbName;
			c = DriverManager.getConnection(serverConnect);
			System.out.println(c.toString());
			c.close();
			System.out.println("Connecting to running server " + serverConnect);
			System.setProperty("spring.datasource.url",serverConnect);
		} catch (SQLException throwables) {
			System.out.println("No running server " + serverConnect + " found. Using local file instead");
		}
	}

	private static String getProperty(String key)  {
		String dbName;
		dbName = System.getProperty(key);

		if (dbName == null) dbName = System.getenv(key);
		if (dbName == null) {
			Properties p = new Properties();
			try {
				p.load(DemoApplication.class.getResourceAsStream("/application.properties"));
				dbName = p.getProperty(key);
			} catch (IOException ioException) {
				// ignore if not found
			}
		}
		return dbName;
	}

	@Bean
	public CommandLineRunner demo() {
		new File(dbDir).mkdirs();
		new File(downloadPath).mkdirs();

		// jdbc
		System.out.println("Using jdbc " + jdbc);
		return (args) -> {
			V1CommandService commandService = new V1CommandService(
					repoRepository, appRepository, localizedRepository, downloadService, importService, downloadPath);
			commandService.exec(args);
		};
	}

	private void demoImport(V1UpdateServiceEx importer) throws IOException {
		MustacheEx.addFixedProperty("t", new ResourceBundleMustacheContext(Locale.US));

		String inputPath;
		inputPath = "/home/EVE/StudioProjects/FDroid/app/fdroid-v1/src/test/java/de/k3b/fdroid/v1/exampledata/index-v1.jar";
//			inputPath = "/home/EVE/StudioProjects/FDroid/app/fdroid-v1/src/test/java/de/k3b/fdroid/v1/exampledata/index-v1.small.json";
		InputStream is = new FileInputStream(inputPath);
		if (inputPath.toLowerCase().endsWith(".jar")) {
			importer.readFromJar(is, null);
		} else {
			importer.readJsonStream(is);
		}
		is.close();
	}

	private void demoAppEntity(AppRepository appRepo) {
        List<de.k3b.fdroid.domain.entity.App> all = appRepo.findAll();
        for (de.k3b.fdroid.domain.entity.App app : all) appRepo.delete(app);

        de.k3b.fdroid.domain.entity.App app = new de.k3b.fdroid.domain.entity.App();
        app.setPackageName("my.package.name");
        app.setIcon("myIcon.ico");
        appRepo.insert(app);
        log.info("Customers found with findAll():");
        log.info("-------------------------------");
        for (de.k3b.fdroid.domain.entity.App app2 : appRepo.findAll()) {
            log.info(app2.toString());
        }
        log.info("");


        de.k3b.fdroid.domain.entity.App app3 = appRepo.findByPackageName("my.package.name");
        log.info("search result " + app3.toString());

        all = appRepo.findAll();
        for (de.k3b.fdroid.domain.entity.App a : all) appRepo.delete(a);

    }
}
