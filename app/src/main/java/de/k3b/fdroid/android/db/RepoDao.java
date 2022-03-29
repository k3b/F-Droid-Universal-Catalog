package de.k3b.fdroid.android.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import de.k3b.fdroid.android.model.App;
import de.k3b.fdroid.android.model.Category;
import de.k3b.fdroid.android.model.Repo;

@Dao
public abstract class RepoDao {
    @Insert
    public abstract void insertAll(Repo... repos);

    @Delete
    public abstract void delete(Repo repo);

    @Query("SELECT * FROM Repo WHERE Repo.id = :repoId")
    public abstract Repo findById(Integer repoId);

    public Repo findByApp(App app) {
        return findById(app.repoId);
    }

    @Query("SELECT * FROM Repo")
    public abstract Repo[] findAll();

}
