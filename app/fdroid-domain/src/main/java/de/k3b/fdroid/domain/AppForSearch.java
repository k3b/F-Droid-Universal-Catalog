package de.k3b.fdroid.domain;

/**
 * Same as App but with more search related Properties
 */
@androidx.room.Entity(tableName = "App")
@javax.persistence.Entity
@javax.persistence.Inheritance(strategy = javax.persistence.InheritanceType.SINGLE_TABLE)
public class AppForSearch extends App {
}
