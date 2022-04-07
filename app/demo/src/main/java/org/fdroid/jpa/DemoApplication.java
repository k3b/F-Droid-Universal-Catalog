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

import org.fdroid.jpa.db.Test;
import org.fdroid.jpa.db.TestRepositoryJpa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * j2se-jpa-db implementation that reads from fdroid-v1-jar and updates a jpa database
 */

// @EntityScan("de.k3b.fdroid.room.model,org.fdroid.model.common, org.fdroid.jpa.db, org.fdroid.jpa.db")
// @EntityScan("org.fdroid.jpa.db")
// @EnableJpaRepositories("org.fdroid.jpa.db.*")

@EnableJpaRepositories // ("org.fdroid.jpa.db.*")
// @ComponentScan(basePackages = { "org.fdroid.jpa.db.*" })
@EntityScan // ("org.fdroid.jpa.db.*")
@SpringBootApplication
public class DemoApplication {
	private static final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(TestRepositoryJpa repository) {
		return (args) -> {
			// save a few customers
			Test test = new Test();
			test.name = "my.demo.test";
			repository.insert(test);

			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Test customer : repository.findAll2()) {
				log.info(customer.name);
			}
			log.info("");

		};
	}

}
