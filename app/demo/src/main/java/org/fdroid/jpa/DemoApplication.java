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
			// repository.insert(test);
			repository.save(test);

			log.info("Customers found with findAll():");
			log.info("-------------------------------");
			for (Test customer : repository.findAll()) {
				log.info(customer.name);
			}
			log.info("");

		};
	}

}
