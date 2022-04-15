package de.k3b.fdroid.jpa.repository.testcase;

import java.util.List;

/**
 * custom repository method taken from tutorial
 * https://mkyong.com/spring-data/spring-data-add-custom-method-to-repository/
 */

public interface CustomerRepositoryCustomAbc {
    List<Integer> findByAVeryComplicatedQuery(String searchText);
}
