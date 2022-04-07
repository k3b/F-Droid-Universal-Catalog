package org.fdroid.jpa.db;

import org.fdroid.jpa.db.base.CustomRoomMethods;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepositoryJpa extends CrudRepository<TestEntity, Integer>, CustomRoomMethods<TestEntity> {
    List<TestEntity> findByFamilyName(String familyName);

    TestEntity findByName(String name);
}
