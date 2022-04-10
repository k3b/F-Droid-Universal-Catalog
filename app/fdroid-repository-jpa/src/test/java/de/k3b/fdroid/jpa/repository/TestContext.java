package de.k3b.fdroid.jpa.repository;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = {"de.k3b.fdroid", "org.fdroid"})
@ComponentScan(basePackages = {"de.k3b.fdroid", "org.fdroid"})
@EntityScan({"de.k3b.fdroid", "org.fdroid"})
@SpringBootApplication
public class TestContext {
}
