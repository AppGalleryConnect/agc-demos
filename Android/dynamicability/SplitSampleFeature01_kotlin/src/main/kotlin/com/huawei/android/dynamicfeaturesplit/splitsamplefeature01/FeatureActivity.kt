/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */
package com.huawei.android.dynamicfeaturesplit.splitsamplefeature01

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.huawei.hms.feature.dynamicinstall.FeatureCompat

/**
 * Feature Activity.
 *
 */
class FeatureActivity : Activity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        try {
            FeatureCompat.install(newBase)
        } catch (e: Exception) {
            Log.w(TAG, "failed to install", e)
        }
    }

    companion object {
        private const val TAG = "FeatureActivity"

        init {
            System.loadLibrary("feature-native-lib")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feature)
        val mImageView = findViewById<ImageView>(R.id.iv_load_png)
        mImageView.setImageDrawable(resources.getDrawable(R.mipmap.activity))
        Toast.makeText(this, "from Feature 01: " + stringFromJNI() + " banana is: " + resources.getString(R.string.banana), Toast.LENGTH_LONG).show()
    }

    /**
     * String from jni string.
     *
     * @return the string
     */
    external fun stringFromJNI(): String
}