package de.k3b.fdroid.jpa.repository.testcase;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import javax.persistence.EntityManager;

import de.k3b.fdroid.domain.interfaces.AppRepository;

/**
 * custom repository method taken from tutorial
 * https://mkyong.com/spring-data/spring-data-add-custom-method-to-repository/
 */

public class TestRepositoryJpaImpl implements CustomerRepositoryCustomAbc {
    @Autowired
    AppRepository repository;

    @Autowired
    private EntityManager entityManager;

    public TestRepositoryJpaImpl() {
    }

    @Override
    public List<Integer> findByAVeryComplicatedQuery(String searchText) {
        Object result = entityManager
                .createNativeQuery("select id from TestEntity where name like :search ")
                .setParameter("search", "%" + searchText + "%")
                .getResultList();
        return (List<Integer>) result;
    }
}
