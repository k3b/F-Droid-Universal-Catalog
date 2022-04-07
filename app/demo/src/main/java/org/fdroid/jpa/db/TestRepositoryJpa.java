package org.fdroid.jpa.db;

import org.fdroid.jpa.db.base.CustomRoomMethods;
import org.springframework.data.repository.CrudRepository;

public interface TestRepositoryJpa extends CrudRepository<Test, Integer>, CustomRoomMethods<Test> {
}
