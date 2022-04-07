package org.fdroid.jpa.db;

import org.springframework.stereotype.Service;

import java.util.List;

import de.k3b.fdroid.room.db.AppRepository;
import de.k3b.fdroid.room.model.App;

@Service
public class AppRepositoryAdapter implements AppRepository {
    private final AppRepositoryJpa jpa;

    public AppRepositoryAdapter(AppRepositoryJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public void insert(App roomApp) {
        jpa.save(roomApp);
    }

    @Override
    public void update(App roomApp) {
        jpa.save(roomApp);
    }

    @Override
    public void delete(App roomApp) {
        jpa.delete(roomApp);
    }

    @Override
    public App findByRepoIdAndPackageName(int repoId, String packageName) {
        List<App> result = jpa.findByRepoIdAndPackageName(repoId, packageName);
        if (!result.isEmpty()) return result.get(0);
        return null;
    }

    @Override
    public int findIdByRepoIdAndPackageName(int repoId, String packageName) {
        List<Integer> result = jpa.findIdByRepoIdAndPackageName(repoId, packageName);
        if (!result.isEmpty()) return result.get(0);
        return -1;
    }

    @Override
    public List<App> findAll() {
        return (List<App>) jpa.findAll();
    }
}
