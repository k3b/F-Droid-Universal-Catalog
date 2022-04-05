package org.fdroid.jpa.db;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import java.util.ArrayList;
import java.util.List;

public class CustomRoomMethodsImpl<T> implements CustomRoomMethods<T> {
    private final SimpleJpaRepository<T, Integer> parent;

    public CustomRoomMethodsImpl(SimpleJpaRepository<T, Integer> parent) {
        this.parent = parent;
    }

    @Override
    public void insert(T entity) {
        parent.save(entity);
    }

    @Override
    public void update(T entity) {
        parent.save(entity);
    }

    @Override
    public List<T> findAll2() {
        List<T> result = new ArrayList<T>();
        for (T it : parent.findAll()) {
            result.add(it);
        }
        return result;
    }
}
