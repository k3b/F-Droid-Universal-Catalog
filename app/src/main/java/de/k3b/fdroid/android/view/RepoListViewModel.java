package de.k3b.fdroid.android.view;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.UUID;

import de.k3b.fdroid.android.FDroidApplication;
import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.android.service.ImportV1AndroidWorker;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.RepoRepository;

public class RepoListViewModel extends ViewModel {
    private final MutableLiveData<List<Repo>> repoList = new MutableLiveData<>();

    private final MutableLiveData<Repo> currentDownloaditem = new MutableLiveData<>();

    protected RepoRepository repoRepository = FDroidApplication.getFdroidDatabase().repoRepository();

    public RepoListViewModel() {
        reload();
    }

    public void reload() {
        Log.i(Global.LOG_TAG_APP, "Start reload repo");
        FDroidApplication.executor.execute(() -> {
            getRepoList().postValue(repoRepository.findAll());
        });
    }

    public MutableLiveData<List<Repo>> getRepoList() {
        return repoList;
    }

    public void onCmdDownload(Context context, Repo repo) {
        UUID uuid = ImportV1AndroidWorker.scheduleDownload(context, repo.getId());
        String oldDownloadTaskId = repo.getDownloadTaskId();
        if (oldDownloadTaskId == null || uuid.toString().compareTo(oldDownloadTaskId) != 0) {
            repo.setDownloadTaskId(uuid.toString());
            saveChanges(repo);
        }
    }

    public void toggleAutoDownloadEnabled(Repo repo) {
        repo.setAutoDownloadEnabled(!repo.isAutoDownloadEnabled());
        saveChanges(repo);
    }

    private void saveChanges(Repo repo) {
        repoRepository.update(repo);
        reload();
    }
}
