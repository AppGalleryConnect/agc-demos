/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */
package com.huawei.android.dynamicfeaturesplit

import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.huawei.hms.feature.install.FeatureInstallManager
import com.huawei.hms.feature.install.FeatureInstallManagerFactory
import com.huawei.hms.feature.listener.InstallStateListener
import com.huawei.hms.feature.model.FeatureInstallException
import com.huawei.hms.feature.model.FeatureInstallRequest
import com.huawei.hms.feature.model.FeatureInstallSessionStatus
import com.huawei.hms.feature.model.InstallState
import com.huawei.hms.feature.tasks.FeatureTask
import com.huawei.hms.feature.tasks.listener.OnFeatureCompleteListener
import com.huawei.hms.feature.tasks.listener.OnFeatureFailureListener
import com.huawei.hms.feature.tasks.listener.OnFeatureSuccessListener
import com.hw.dfdemo.kotlin.R
import java.util.*

/**
 * Sample Entry.
 */
class SampleEntry : Activity() {

    companion object {
        private const val TAG = "SampleEntry"
    }

    private lateinit var progressBar: ProgressBar

    private lateinit var progressNumber: TextView

    private lateinit var installStateInfo: TextView

    private lateinit var mFeatureInstallManager: FeatureInstallManager

    private var sessionId = 10086

    private var stateInfo = "default state"

    private var processPercentage = 0

    private val mStateUpdateListener: InstallStateListener = InstallStateListener { state ->
        Log.d(TAG, "install session state $state")
        if (state.status() == FeatureInstallSessionStatus.REQUIRES_USER_CONFIRMATION) {
            try {
                mFeatureInstallManager.triggerUserConfirm(state, this@SampleEntry, 1)
            } catch (e: SendIntentException) {
                Log.e(TAG, "user confirm, failed to triggerUserConfirm", e)
            }
            return@InstallStateListener
        }

        if (state.status() == FeatureInstallSessionStatus.REQUIRES_PERSON_AGREEMENT) {
            try {
                mFeatureInstallManager.triggerUserConfirm(state, this@SampleEntry, 1)
            } catch (e: SendIntentException) {
                Log.e(TAG, "person agreement, failed to triggerUserConfirm", e)
            }
            return@InstallStateListener
        }

        if (state.status() == FeatureInstallSessionStatus.INSTALLED) {
            Log.i(TAG, "installed success, can use new feature")
            makeToast("installed success, can test new feature")
            return@InstallStateListener
        }

        if (state.status() == FeatureInstallSessionStatus.UNKNOWN) {
            Log.e(TAG, "installed in unknown status")
            makeToast("installed in unknown status")
            return@InstallStateListener
        }

        if (state.status() == FeatureInstallSessionStatus.DOWNLOADING) {
            val totalBytes = state.totalBytesToDownload()
            val process = if (totalBytes == 0L) 0 else state.bytesDownloaded() * 100 / totalBytes
            processPercentage = process.toInt()
            progressBar.progress = processPercentage
            progressNumber.text = "Progress: $processPercentage%"
            progressNumber.textSize = 18f
            progressNumber.setTextColor(Color.GREEN)
            Log.d(TAG, "downloading percentage: $process")
            return@InstallStateListener
        }

        if (state.status() == FeatureInstallSessionStatus.FAILED) {
            Log.e(TAG, "installed failed, errorCode: " + state.errorCode())
            makeToast("installed failed, errorCode: " + state.errorCode())
            return@InstallStateListener
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progress_bar)
        progressNumber = findViewById(R.id.progress_number)
        installStateInfo = findViewById(R.id.installStateInfo)
        mFeatureInstallManager = FeatureInstallManagerFactory.create(this)
    }

    override fun onResume() {
        super.onResume()
        if (mFeatureInstallManager != null) {
            mFeatureInstallManager.registerInstallListener(mStateUpdateListener)
        }
    }

    override fun onPause() {
        super.onPause()
        if (mFeatureInstallManager != null) {
            mFeatureInstallManager.unregisterInstallListener(mStateUpdateListener)
        }
    }

    /**
     * install feature
     *
     * @param view the view
     */
    fun installFeature(view: View?) {
        if (mFeatureInstallManager == null) {
            return
        }
        // start install
        val request = FeatureInstallRequest.newBuilder()
                .addModule("SplitSampleFeature01_kotlin")
                .build()
        val task = mFeatureInstallManager.installFeature(request)
        task.addOnListener(object : OnFeatureSuccessListener<Int>() {
            override fun onSuccess(sessionId: Int) {
                Log.d(TAG, "load feature onSuccess, session id: $sessionId")
            }
        })
        task.addOnListener(object : OnFeatureFailureListener<Int>() {
            override fun onFailure(exception: Exception) {
                if (exception is FeatureInstallException) {
                    val errorCode = exception.errorCode
                    Log.d(TAG, "load feature onFailure, errorCode: $errorCode")
                } else {
                    Log.e(TAG, "fail to load feature", exception)
                }
            }
        })
        task.addOnListener(object : OnFeatureCompleteListener<Int>() {
            override fun onComplete(featureTask: FeatureTask<Int>) {
                if (featureTask.isComplete) {
                    Log.d(TAG, "complete to start install")
                    if (featureTask.isSuccessful) {
                        val result = featureTask.result
                        sessionId = result
                        Log.d(TAG, "succeed to start install, session id: $result")
                    } else {
                        val exception = featureTask.exception
                        Log.e(TAG, "fail to start install", exception)
                    }
                }
            }
        })
        Log.d(TAG, "start install func end")
    }

    /**
     * start feature
     *
     * @param view the view
     */
    fun startFeature01(view: View?) {
        // test getInstallModules
        val moduleNames = mFeatureInstallManager.allInstalledModules
        Log.d(TAG, "getInstallModules: $moduleNames")
        if (moduleNames != null && moduleNames.contains("SplitSampleFeature01_kotlin")) {
            try {
                startActivity(Intent(this, Class.forName(
                        "com.huawei.android.dynamicfeaturesplit.splitsamplefeature01.FeatureActivity")))
            } catch (e: Exception) {
                Log.w(TAG, "startActivity failed", e)
            }
        }
    }

    /**
     * cancel install task
     *
     * @param view the view
     */
    fun abortInstallFeature(view: View?) {
        Log.d(TAG, "begin abort_install: $sessionId")
        val task = mFeatureInstallManager.abortInstallFeature(sessionId)
        task.addOnListener(object : OnFeatureCompleteListener<Void>() {
            override fun onComplete(featureTask: FeatureTask<Void>) {
                if (featureTask.isComplete) {
                    Log.d(TAG, "complete to abort_install")
                    if (featureTask.isSuccessful) {
                        Log.d(TAG, "succeed to abort_install")
                    } else {
                        val exception = featureTask.exception
                        Log.e(TAG, "fail to abort_install", exception)
                    }
                }
            }
        })
    }

    /**
     * get install task state
     *
     * @param view the view
     */
    fun getInstallState(view: View?) {
        Log.d(TAG, "begin to get session state for: $sessionId")
        val task = mFeatureInstallManager.getInstallState(sessionId)
        task.addOnListener(object : OnFeatureCompleteListener<InstallState>() {
            override fun onComplete(featureTask: FeatureTask<InstallState>) {
                if (featureTask.isComplete) {
                    Log.d(TAG, "complete to get session state")
                    if (featureTask.isSuccessful) {
                        val state = featureTask.result
                        Log.d(TAG, "succeed to get session state")
                        if (!TextUtils.isEmpty(state.toString())) {
                            stateInfo = state.toString()
                        }
                        installStateInfo.text = stateInfo
                        installStateInfo.textSize = 18f
                        Log.d(TAG, stateInfo)
                    } else {
                        val exception = featureTask.exception
                        Log.e(TAG, "failed to get session state", exception)
                    }
                }
            }
        })
    }

    /**
     * get states of all install tasks
     *
     * @param view the view
     */
    fun getAllInstallStates(view: View?) {
        Log.d(TAG, "begin to get all session states")
        val task = mFeatureInstallManager.allInstallStates
        task.addOnListener(object : OnFeatureCompleteListener<MutableList<InstallState>>() {
            override fun onComplete(featureTask: FeatureTask<MutableList<InstallState>>) {
                Log.d(TAG, "complete to get session states")
                if (featureTask.isSuccessful) {
                    Log.d(TAG, "succeed to get session states")
                    val stateList = featureTask.result
                    for (state in stateList) {
                        Log.d(TAG, state.toString())
                    }
                } else {
                    val exception = featureTask.exception
                    Log.e(TAG, "fail to get session states", exception)
                }
            }
        })
    }

    /**
     * deffer to install features
     *
     * @param view the view
     */
    fun delayedInstallFeature(view: View?) {
        val features: MutableList<String> = ArrayList()
        features.add("SplitSampleFeature01_kotlin")
        val task = mFeatureInstallManager.delayedInstallFeature(features)
        task.addOnListener(object : OnFeatureCompleteListener<Void>() {
            override fun onComplete(featureTask: FeatureTask<Void>) {
                if (featureTask.isComplete) {
                    Log.d(TAG, "complete to delayed_install")
                    if (featureTask.isSuccessful) {
                        Log.d(TAG, "succeed to delayed_install")
                    } else {
                        Log.d(TAG, "fail to delayed_install.")
                        val exception = featureTask.exception
                        Log.e(TAG, "fail to delayed_install", exception)
                    }
                }
            }
        })
    }

    /**
     * uninstall features
     *
     * @param view the view
     */
    fun delayedUninstallFeature(view: View?) {
        val features: MutableList<String> = ArrayList()
        features.add("SplitSampleFeature01_kotlin")
        val task = mFeatureInstallManager.delayedUninstallFeature(features)
        task.addOnListener(object : OnFeatureCompleteListener<Void>() {
            override fun onComplete(featureTask: FeatureTask<Void>) {
                if (featureTask.isComplete) {
                    Log.d(TAG, "complete to delayed_uninstall")
                    if (featureTask.isSuccessful) {
                        Log.d(TAG, "succeed to delayed_uninstall")
                    } else {
                        val exception = featureTask.exception
                        Log.e(TAG, "fail to delayed_uninstall", exception)
                    }
                }
            }
        })
    }

    /**
     * install languages
     *
     * @param view the view
     */
    fun loadLanguage(view: View?) {
        if (mFeatureInstallManager == null) {
            return
        }
        // start install
        val languages: MutableSet<String> = HashSet()
        languages.add("fr-FR")
        val builder = FeatureInstallRequest.newBuilder()
        for (lang in languages) {
            builder.addLanguage(Locale.forLanguageTag(lang))
        }
        val request = builder.build()
        val task = mFeatureInstallManager.installFeature(request)
        task.addOnListener(object : OnFeatureSuccessListener<Int>() {
            override fun onSuccess(result: Int) {
                Log.d(TAG, "onSuccess callback result $result")
            }
        })
        task.addOnListener(object : OnFeatureFailureListener<Int>() {
            override fun onFailure(exception: Exception) {
                if (exception is FeatureInstallException) {
                    Log.d(TAG, "onFailure callback "
                            + exception.errorCode)
                } else {
                    Log.e(TAG, "onFailure callback", exception)
                }
            }
        })
        task.addOnListener(object : OnFeatureCompleteListener<Int>() {
            override fun onComplete(task: FeatureTask<Int>) {
                Log.d(TAG, "onComplete callback")
            }
        })
    }

    private fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}