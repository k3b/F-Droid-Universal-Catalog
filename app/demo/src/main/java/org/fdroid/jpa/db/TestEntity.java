package org.fdroid.jpa.db;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class TestEntity extends TestEntityCommon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int id;

    public String name;
}
