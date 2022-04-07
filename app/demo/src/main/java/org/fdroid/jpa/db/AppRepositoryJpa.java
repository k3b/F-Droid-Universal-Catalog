package org.fdroid.jpa.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import de.k3b.fdroid.room.model.App;

@Repository
public interface AppRepositoryJpa extends CrudRepository<App, Integer> {
    App findByRepoIdAndPackageName(int repoId, String packageName);
}

