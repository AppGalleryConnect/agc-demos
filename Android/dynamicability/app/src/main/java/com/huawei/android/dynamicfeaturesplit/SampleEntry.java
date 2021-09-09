/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.android.dynamicfeaturesplit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hms.feature.install.FeatureInstallManager;
import com.huawei.hms.feature.install.FeatureInstallManagerFactory;
import com.huawei.hms.feature.listener.InstallStateListener;
import com.huawei.hms.feature.model.FeatureInstallException;
import com.huawei.hms.feature.model.FeatureInstallRequest;
import com.huawei.hms.feature.model.FeatureInstallSessionStatus;
import com.huawei.hms.feature.model.InstallState;
import com.huawei.hms.feature.tasks.FeatureTask;
import com.huawei.hms.feature.tasks.listener.OnFeatureCompleteListener;
import com.huawei.hms.feature.tasks.listener.OnFeatureFailureListener;
import com.huawei.hms.feature.tasks.listener.OnFeatureSuccessListener;
import com.hw.dfdemo.R;

/**
 * Sample Entry.
 */
public class SampleEntry extends Activity {
    private static final String TAG = SampleEntry.class.getSimpleName();

    private ProgressBar progressBar;

    private TextView progressNumber;

    private TextView installStateInfo;

    private FeatureInstallManager mFeatureInstallManager;

    private int sessionId = 10086;

    private String stateInfo = "default state";

    private int processPercentage = 0;

    private InstallStateListener mStateUpdateListener = new InstallStateListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            Log.d(TAG, "install session state " + state);
            if (state.status() == FeatureInstallSessionStatus.REQUIRES_USER_CONFIRMATION) {
                try {
                    mFeatureInstallManager.triggerUserConfirm(state, SampleEntry.this, 1);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "user confirm, failed to triggerUserConfirm", e);
                }
                return;
            }

            if (state.status() == FeatureInstallSessionStatus.REQUIRES_PERSON_AGREEMENT) {
                try {
                    mFeatureInstallManager.triggerUserConfirm(state, SampleEntry.this, 1);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "person agreement, failed to triggerUserConfirm", e);
                }
                return;
            }

            if (state.status() == FeatureInstallSessionStatus.INSTALLED) {
                Log.i(TAG, "installed success, can use new feature");
                makeToast("installed success, can test new feature");
                return;
            }

            if (state.status() == FeatureInstallSessionStatus.UNKNOWN) {
                Log.e(TAG, "installed in unknown status");
                makeToast("installed in unknown status");
                return;
            }

            if (state.status() == FeatureInstallSessionStatus.DOWNLOADING) {
                long totalBytes = state.totalBytesToDownload();
                long process = totalBytes == 0 ? 0 : state.bytesDownloaded() * 100 / totalBytes;
                try {
                    processPercentage = Integer.parseInt(String.valueOf(process));
                } catch (NumberFormatException e) {
                    Log.e(TAG, "parse int failed", e);
                }
                progressBar.setProgress(processPercentage);
                progressNumber.setText("Progress: " + processPercentage + "%");
                progressNumber.setTextSize(18);
                progressNumber.setTextColor(Color.GREEN);
                Log.d(TAG, "downloading percentage: " + process);
                return;
            }

            if (state.status() == FeatureInstallSessionStatus.FAILED) {
                Log.e(TAG, "installed failed, errorCode: " + state.errorCode());
                makeToast("installed failed, errorCode: " + state.errorCode());
                return;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_bar);
        progressNumber = findViewById(R.id.progress_number);
        installStateInfo = findViewById(R.id.installStateInfo);
        mFeatureInstallManager = FeatureInstallManagerFactory.create(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFeatureInstallManager != null) {
            mFeatureInstallManager.registerInstallListener(mStateUpdateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mFeatureInstallManager != null) {
            mFeatureInstallManager.unregisterInstallListener(mStateUpdateListener);
        }
    }

    /**
     * install feature
     *
     * @param view the view
     */
    public void installFeature(View view) {
        if (mFeatureInstallManager == null) {
            return;
        }
        // start install
        FeatureInstallRequest request = FeatureInstallRequest.newBuilder()
                .addModule("SplitSampleFeature01")
                .build();
        final FeatureTask<Integer> task = mFeatureInstallManager.installFeature(request);
        task.addOnListener(new OnFeatureSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer integer) {
                Log.d(TAG, "load feature onSuccess, session id: " + integer);
            }
        });
        task.addOnListener(new OnFeatureFailureListener<Integer>() {
            @Override
            public void onFailure(Exception exception) {
                if (exception instanceof FeatureInstallException) {
                    int errorCode = ((FeatureInstallException) exception).getErrorCode();
                    Log.d(TAG, "load feature onFailure, errorCode: " + errorCode);
                } else {
                    Log.e(TAG, "fail to load feature", exception);
                }
            }
        });
        task.addOnListener(new OnFeatureCompleteListener<Integer>() {
            @Override
            public void onComplete(FeatureTask<Integer> featureTask) {
                if (featureTask.isComplete()) {
                    Log.d(TAG, "complete to start install");
                    if (featureTask.isSuccessful()) {
                        Integer result = featureTask.getResult();
                        sessionId = result;
                        Log.d(TAG, "succeed to start install, session id: " + result);
                    } else {
                        Exception exception = featureTask.getException();
                        Log.e(TAG, "fail to start install", exception);
                    }
                }
            }
        });
        Log.d(TAG, "start install func end");
    }

    /**
     * start feature
     *
     * @param view the view
     */
    public void startFeature01(View view) {
        // test getInstallModules
        Set<String> moduleNames = mFeatureInstallManager.getAllInstalledModules();
        Log.d(TAG, "getInstallModules: " + moduleNames);
        if (moduleNames != null && moduleNames.contains("SplitSampleFeature01")) {
            try {
                startActivity(new Intent(this, Class.forName(
                        "com.huawei.android.dynamicfeaturesplit.splitsamplefeature01.FeatureActivity")));
            } catch (Exception e) {
                Log.w(TAG, "startActivity failed", e);
            }
        }
    }

    /**
     * cancel install task
     *
     * @param view the view
     */
    public void abortInstallFeature(View view) {
        Log.d(TAG, "begin abort_install: " + sessionId);
        FeatureTask<Void> task = mFeatureInstallManager.abortInstallFeature(sessionId);
        task.addOnListener(new OnFeatureCompleteListener<Void>() {
            @Override
            public void onComplete(FeatureTask<Void> featureTask) {
                if (featureTask.isComplete()) {
                    Log.d(TAG, "complete to abort_install");
                    if (featureTask.isSuccessful()) {
                        Log.d(TAG, "succeed to abort_install");
                    } else {
                        Exception exception = featureTask.getException();
                        Log.e(TAG, "fail to abort_install", exception);
                    }
                }
            }
        });
    }

    /**
     * get install task state
     *
     * @param view the view
     */
    public void getInstallState(View view) {
        Log.d(TAG, "begin to get session state for: " + sessionId);
        FeatureTask<InstallState> task = mFeatureInstallManager.getInstallState(sessionId);
        task.addOnListener(new OnFeatureCompleteListener<InstallState>() {
            @Override
            public void onComplete(FeatureTask<InstallState> featureTask) {
                if (featureTask.isComplete()) {
                    Log.d(TAG, "complete to get session state");
                    if (featureTask.isSuccessful()) {
                        InstallState state = featureTask.getResult();
                        Log.d(TAG, "succeed to get session state");
                        if (!TextUtils.isEmpty(state.toString())) {
                            stateInfo = state.toString();
                        }
                        installStateInfo.setText(stateInfo);
                        installStateInfo.setTextSize(18);
                        Log.d(TAG, stateInfo);
                    } else {
                        Exception exception = featureTask.getException();
                        Log.e(TAG, "failed to get session state", exception);
                    }
                }
            }
        });
    }

    /**
     * get states of all install tasks
     *
     * @param view the view
     */
    public void getAllInstallStates(View view) {
        Log.d(TAG, "begin to get all session states");
        FeatureTask<List<InstallState>> task = mFeatureInstallManager.getAllInstallStates();
        task.addOnListener(new OnFeatureCompleteListener<List<InstallState>>() {
            @Override
            public void onComplete(FeatureTask<List<InstallState>> featureTask) {
                Log.d(TAG, "complete to get session states");
                if (featureTask.isSuccessful()) {
                    Log.d(TAG, "succeed to get session states");
                    List<InstallState> stateList = featureTask.getResult();
                    for (InstallState state : stateList) {
                        Log.d(TAG, state.toString());
                    }
                } else {
                    Exception exception = featureTask.getException();
                    Log.e(TAG, "fail to get session states", exception);
                }
            }
        });
    }

    /**
     * deffer to install features
     *
     * @param view the view
     */
    public void delayedInstallFeature(View view) {
        List<String> features = new ArrayList<>();
        features.add("SplitSampleFeature01");
        FeatureTask<Void> task = mFeatureInstallManager.delayedInstallFeature(features);

        task.addOnListener(new OnFeatureCompleteListener<Void>() {
            @Override
            public void onComplete(FeatureTask<Void> featureTask) {
                if (featureTask.isComplete()) {
                    Log.d(TAG, "complete to delayed_install");
                    if (featureTask.isSuccessful()) {
                        Log.d(TAG, "succeed to delayed_install");
                    } else {
                        Exception exception = featureTask.getException();
                        Log.e(TAG, "fail to delayed_install", exception);
                    }
                }
            }
        });
    }

    /**
     * uninstall features
     *
     * @param view the view
     */
    public void delayedUninstallFeature(View view) {
        List<String> features = new ArrayList<>();
        features.add("SplitSampleFeature01");
        FeatureTask<Void> task = mFeatureInstallManager.delayedUninstallFeature(features);
        task.addOnListener(new OnFeatureCompleteListener<Void>() {
            @Override
            public void onComplete(FeatureTask<Void> featureTask) {
                if (featureTask.isComplete()) {
                    Log.d(TAG, "complete to delayed_uninstall");
                    if (featureTask.isSuccessful()) {
                        Log.d(TAG, "succeed to delayed_uninstall");
                    } else {
                        Exception exception = featureTask.getException();
                        Log.e(TAG, "fail to delayed_uninstall", exception);
                    }
                }
            }
        });
    }

    /**
     * install languages
     *
     * @param view the view
     */
    public void loadLanguage(View view) {
        if (mFeatureInstallManager == null) {
            return;
        }
        // start install
        Set<String> languages = new HashSet<>();
        languages.add("fr-FR");
        FeatureInstallRequest.Builder builder = FeatureInstallRequest.newBuilder();
        for (String lang : languages) {
            builder.addLanguage(Locale.forLanguageTag(lang));
        }
        FeatureInstallRequest request = builder.build();

        FeatureTask<Integer> task = mFeatureInstallManager.installFeature(request);

        task.addOnListener(new OnFeatureSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer result) {
                Log.d(TAG, "onSuccess callback result " + result);
            }
        });
        task.addOnListener(new OnFeatureFailureListener<Integer>() {
            @Override
            public void onFailure(Exception exception) {
                if (exception instanceof FeatureInstallException) {
                    Log.d(TAG, "onFailure callback "
                            + ((FeatureInstallException) exception).getErrorCode());
                } else {
                    Log.e(TAG, "onFailure callback", exception);
                }
            }
        });
        task.addOnListener(new OnFeatureCompleteListener<Integer>() {
            @Override
            public void onComplete(FeatureTask<Integer> task) {
                Log.d(TAG, "onComplete callback");
            }
        });
    }

    private void makeToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}