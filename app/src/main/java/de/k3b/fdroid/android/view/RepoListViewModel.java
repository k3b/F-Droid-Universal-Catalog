package de.k3b.fdroid.android.view;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import de.k3b.fdroid.android.FDroidApplication;
import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.RepoRepository;

public class RepoListViewModel extends ViewModel {
    private final MutableLiveData<List<Repo>> items = new MutableLiveData<>();

    private final MutableLiveData<Repo> currentDownloaditem = new MutableLiveData<>();

    protected RepoRepository repoRepository = FDroidApplication.getFdroidDatabase().repoRepository();

    public RepoListViewModel() {
        reload();
    }

    public void reload() {
        Log.i(Global.LOG_TAG_APP, "Start reload repo");
        FDroidApplication.executor.execute(() -> {
            getItems().postValue(repoRepository.findAll());
        });
    }

    public MutableLiveData<List<Repo>> getItems() {
        return items;
    }
}
