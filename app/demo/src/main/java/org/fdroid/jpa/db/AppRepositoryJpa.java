package org.fdroid.jpa.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import de.k3b.fdroid.room.model.App;

@Repository
public interface AppRepositoryJpa extends CrudRepository<App, Integer> {
    // @Query(value="SELECT * FROM App WHERE repoId = :repoId AND packageName = :packageName", nativeQuery = true)
    // List<App> findByRepoIdAndPackageName(@Param("repoId") int repoId, @Param("packageName") String packageName);
    List<App> findByRepoIdAndPackageName(int repoId, String packageName);

    // @Query(value="SELECT id FROM App WHERE repoId = :repoId AND packageName = :packageName", nativeQuery = true)
    // List<Integer> findIdByRepoIdAndPackageName(@Param("repoId") int repoId, @Param("packageName") String packageName);
    List<Integer> findIdByRepoIdAndPackageName(int repoId, String packageName);
}

