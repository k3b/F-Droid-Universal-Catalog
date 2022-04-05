package org.fdroid.jpa.db;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface CustomRoomMethods<T> {
    void insert(T entity);

    void update(T entity);

    List<T> findAll2();
}
