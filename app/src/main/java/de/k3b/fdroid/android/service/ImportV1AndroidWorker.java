/*
 * Copyright (c) 2022 by k3b.
 *
 * This file is part of org.fdroid.v1 the fdroid json catalog-format-v1 parser.
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>
 */

package de.k3b.fdroid.android.service;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.UUID;

import de.k3b.fdroid.android.FDroidApplication;
import de.k3b.fdroid.android.Global;
import de.k3b.fdroid.domain.Repo;
import de.k3b.fdroid.domain.interfaces.ProgressObserver;
import de.k3b.fdroid.util.StringUtil;
import de.k3b.fdroid.v1.service.V1DownloadAndImportService;
import de.k3b.fdroid.v1.service.V1JarException;

// see https://developer.android.com/topic/libraries/architecture/workmanager/basics
public class ImportV1AndroidWorker extends Worker {

    private static final String KEY_REPO_ID = "repoId";
    private static final String KEY_DOWNLOAD_URL = "downloadUrl";
    private static final String KEY_JAR_SIGNING_CERTIFICATE_FINGERPRINT = "jarSigningCertificateFingerprintOrNull";
    private static final String KEY_RESULT = "resultMessage";
    private static final String TAG_IMPORTV1 = "importV1";
    public static final String KEY_PROGRESS = "progress";
    private final ProgressObserverAdapter progressObserver;

    V1DownloadAndImportService v1DownloadAndImportService;

    public ImportV1AndroidWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);

        v1DownloadAndImportService = FDroidApplication
                .getAndroidServiceFactory().getV1DownloadAndImportService();

        progressObserver = new ProgressObserverAdapter();
        v1DownloadAndImportService.setProgressObserver(progressObserver);
    }

    public static UUID scheduleDownload(Context context,
                                        String downloadUrl, String jarSigningCertificateFingerprintOrNull) {
        if (downloadUrl == null)
            throw new NullPointerException("ImportV1AndroidWorker.scheduleDownload(downloadUrl)");

        Data data = new Data.Builder()
                .putString(KEY_DOWNLOAD_URL, downloadUrl)
                .putString(KEY_JAR_SIGNING_CERTIFICATE_FINGERPRINT, jarSigningCertificateFingerprintOrNull)
                .build();
        return scheduleDownload(context, data);
    }

    public static UUID scheduleDownload(Context context, int repoId) {
        Data data = new Data.Builder()
                .putInt(KEY_REPO_ID, repoId)
                .build();
        return scheduleDownload(context, data);
    }

    /**
     * Allows an activity to receive Progress-Messages
     *
     * @param progressObserver where the progress-info goes to
     * @return false if no worker for taskId was found
     */
    public static boolean registerProgressObserver(String taskId, AndroidWorkerProgressObserver progressObserver) {
        LiveData<WorkInfo> data = WorkManager
                .getInstance(progressObserver.getProgressMessageTextView().getContext())
                .getWorkInfoByIdLiveData(UUID.fromString(taskId));

        if (data.getValue() == null) {
            Log.d(Global.LOG_TAG_IMPORT, "registerProgressObserver(" + taskId + ") on observe repo: ");
            return false;
        }
        data.observe(progressObserver,
                info -> {
                    Log.d(Global.LOG_TAG_IMPORT, "on observe repo: " + info);
                    // if (info != null && info.getState()==??? ) // TODO fintue filter
                    progressObserver.onProgressMessage(info.getProgress().getString(KEY_PROGRESS));
                });
        return true;
    }

    private static UUID scheduleDownload(Context context, Data data) {
        // WorkRequest importWorkRequest =
        OneTimeWorkRequest importWorkRequest =
                new OneTimeWorkRequest.Builder(ImportV1AndroidWorker.class)
                        .setInputData(data)
                        .addTag(TAG_IMPORTV1)
                        .setConstraints(new Constraints.Builder()
                                .setRequiredNetworkType(Global.DOWNLOAD_NETWORK_TYPE)
                                .build())
                        .build();
        WorkManager
                .getInstance(context)
                .enqueueUniqueWork(TAG_IMPORTV1, ExistingWorkPolicy.REPLACE, importWorkRequest);
        return importWorkRequest.getId();
    }

    @NonNull
    @Override
    public Result doWork() {

        Repo result;
        Data data = getInputData();
        int repoId = data.getInt(KEY_REPO_ID, 0);
        try {
            if (repoId != 0) {
                result = v1DownloadAndImportService.download(repoId, getId().toString());
            } else {
                String downloadUrl = data.getString(KEY_DOWNLOAD_URL);
                String jarSigningCertificateFingerprintOrNull = data.getString(KEY_JAR_SIGNING_CERTIFICATE_FINGERPRINT);
                result = v1DownloadAndImportService.download(downloadUrl, jarSigningCertificateFingerprintOrNull, getId().toString());
            }
        } catch (V1JarException ex) {
            return fail(ex.getMessage());
        }
        if (!StringUtil.isEmpty(result.getLastErrorMessage())) {
            return fail(result.getLastErrorMessage());
        }
        progressObserver.log("done");
        progressObserver.log(null);

        return Result.success();
    }

    private Result fail(String message) {
        Data output = new Data.Builder()
                .putString(KEY_RESULT, message)
                .build();
        progressObserver.log(message);
        progressObserver.log(null);
        return Result.failure(output);
    }

    /**
     * Translates from k3b-s internal {@link ProgressObserver} to {@link Worker#setProgressAsync(Data)}
     */
    private class ProgressObserverAdapter implements ProgressObserver {
        private String progressPrefix = "";
        private String progressSuffix = "";

        @Override
        public ProgressObserver setProgressContext(String progressPrefix, String progressSuffix) {
            if (progressPrefix != null) this.progressPrefix = progressPrefix;
            if (progressSuffix != null) this.progressSuffix = progressSuffix;
            return this;
        }

        @Override
        public void onProgress(int count, String progressChar, String packageName) {
            log(progressPrefix + count + progressSuffix);
        }

        @Override
        public void log(final String message) {
            Log.d(Global.LOG_TAG_IMPORT, message);
            ImportV1AndroidWorker.this.setProgressAsync(new Data.Builder().putString(KEY_PROGRESS, message).build());
        }
    }
}
