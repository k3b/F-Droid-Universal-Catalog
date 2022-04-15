package de.k3b.fdroid.jpa.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import de.k3b.fdroid.domain.App;
import de.k3b.fdroid.domain.Locale;
import de.k3b.fdroid.domain.Repo;

@Service
public class JpaTestHelper {
    @Autowired
    EntityManager entityManager;

    List<Object> created = new ArrayList<>();

    private Repo repo;

    public void createItems() {
        Repo repo = createRepo();
        createApp(repo);

    }

    public App createApp() {
        if (repo == null) repo = createRepo();
        return createApp(repo);
    }

    public App createApp(Repo repo) {
        App app = new App(repo.getId());
        entityManager.persist(app);
        app.setPackageName("test.app." + app.getId());
        return save(app);
    }

    public <T> T save(T entity) {
        created.add(entity);
        entityManager.persist(entity);
        return entity;
    }

    public Repo createRepo() {
        Repo repo = new Repo();
        entityManager.persist(repo);
        repo.setName("test-repo-" + repo.getId());
        repo.setAddress("testrepo.org." + repo.getId());
        return save(repo);
    }

    public Locale createLocale(String code) {
        Locale locale = new Locale();
        locale.setCode(code);
        return save(locale);
    }

    public void rollback() {
        int len = created.size();
        for (int i = len - 1; i >= 0; i--) {
            entityManager.remove(created.get(i));
            created.remove(i);
        }
    }
}
