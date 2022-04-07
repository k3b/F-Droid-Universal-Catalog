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
package org.fdroid.jpa;

import org.fdroid.jpa.db.TestEntity;
import org.fdroid.jpa.db.TestRepositoryJpa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

import de.k3b.fdroid.room.db.AppRepository;
import de.k3b.fdroid.room.model.App;

/**
 * j2se-jpa-db implementation that reads from fdroid-v1-jar and updates a jpa database
 */

@EnableJpaRepositories(basePackages = {"de.k3b.fdroid.room.db", "de.k3b.fdroid.room.model", "org.fdroid.jpa.db", "org.fdroid.model.common"})
@ComponentScan(basePackages = {"de.k3b.fdroid.room.db", "de.k3b.fdroid.room.model", "org.fdroid.jpa.db", "org.fdroid.model.common"})
@EntityScan({"de.k3b.fdroid.room.db", "de.k3b.fdroid.room.model", "org.fdroid.jpa.db", "org.fdroid.model.common"})
@SpringBootApplication
public class DemoApplication {
	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TestRepositoryJpa repository, AppRepository appRepo) {
		return (args) -> {
			demoTestEntity(repository);

			demoAppEntity(appRepo);
		};
	}

	private void demoAppEntity(AppRepository appRepo) {
		App app = new App();
		app.repoId = 1;
		app.setPackageName("my.package.name");
		app.setIcon("myIcon.ico");
		appRepo.insert(app);
		log.info("Customers found with findAll():");
		log.info("-------------------------------");
		for (App app2 : appRepo.findAll()) {
			log.info(app2.toString());
		}
		log.info("");


		App app3 = appRepo.findByRepoIdAndPackageName(1, "my.package.name");
		log.info("search result " + app3.toString());

		// not working
		int id = appRepo.findIdByRepoIdAndPackageName(1, "my.package.name");
		log.info("search result " + id);
	}

	private void demoTestEntity(TestRepositoryJpa repository) {
		// save a few customers
		TestEntity testEntity = new TestEntity();
		testEntity.name = "my.demo.testEntity";
		testEntity.familyName = "smith";
		repository.insert(testEntity);

		log.info("Customers found with findAll():");
		log.info("-------------------------------");
		for (TestEntity customer : repository.findAll2()) {
			log.info("id: " + customer.id +
					", name:" + customer.name +
					", familyName" + customer.familyName);
		}
		log.info("");
		List<TestEntity> testEntity2 = repository.findByFamilyName("smith");
		log.info("search result " + testEntity2.get(0).toString());
		TestEntity testEntity3 = repository.findByName("my.demo.testEntity");
		log.info("search result " + testEntity3.toString());
	}

}
