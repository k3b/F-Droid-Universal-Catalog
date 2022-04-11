package de.k3b.fdroid.jpa.repository;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"de.k3b.fdroid"})
@ComponentScan(basePackages = {"de.k3b.fdroid"})
@EntityScan({"de.k3b.fdroid"})
@SpringBootConfiguration
public class TestConfig {
}
