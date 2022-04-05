package org.fdroid.jpa.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

@NoRepositoryBean
interface CrudeXRepositoryJpa<T> extends CrudRepository<T, Integer>
        // , CustomRoomMethods<T>
{
}

@Repository
public interface TestRepositoryJpa extends CrudeXRepositoryJpa<Test> {
    // void delete(App app);

    // @Query(value = "SELECT App.id FROM App WHERE App.repoId = ?1 AND App.packageName = ?2")
    // Integer findIdByRepoIdAndPackageName(Integer repoId, String packageName);

    // @Query(value = "SELECT App FROM App WHERE App.repoId = ?1 AND App.packageName = ?2")
    Test findByName(String name);
}
