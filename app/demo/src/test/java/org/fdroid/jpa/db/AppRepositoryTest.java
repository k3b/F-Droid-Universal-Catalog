package org.fdroid.jpa.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.Assert;

import de.k3b.fdroid.room.db.AppRepository;
import de.k3b.fdroid.room.model.App;

@DataJpaTest
public class AppRepositoryTest {
    private static final String MY_PACKAGE_NAME = "my.package.name";
    private static final String MY_ICON = "myIcon.ico";
    private static final int MY_REPO_ID = 47110815;

    @Autowired
    private AppRepositoryJpa jpa;
    @Autowired
    private AppRepository repo;

    private int id = 0;

    @BeforeEach
    public void init() {
        App app = new App();
        app.repoId = MY_REPO_ID;
        app.setPackageName(MY_PACKAGE_NAME);
        app.setIcon(MY_ICON);
        repo.insert(app);
        id = app.id;
    }

    @AfterEach
    public void finish() {
        jpa.deleteById(id);
        id = 0;
    }

    @Test
    public void injectedComponentsAreNotNull() {
        Assert.notNull(jpa, "jpa");
        Assert.notNull(repo, "repo");
    }

    @Test
    public void findByRepoIdAndPackageName() {
        App app = repo.findByRepoIdAndPackageName(MY_REPO_ID, MY_PACKAGE_NAME);
        Assert.notNull(app, "found");
    }
}