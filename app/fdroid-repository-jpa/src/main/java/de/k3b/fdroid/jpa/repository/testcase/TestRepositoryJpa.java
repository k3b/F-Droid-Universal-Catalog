package de.k3b.fdroid.jpa.repository.testcase;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepositoryJpa extends CrudRepository<TestEntity, Integer> {

    List<TestEntity> findByFamilyName(String familyName);

    TestEntity findByName(String name);
}
