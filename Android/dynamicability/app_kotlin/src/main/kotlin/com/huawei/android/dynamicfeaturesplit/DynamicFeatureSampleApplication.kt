/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2012-2019. All rights reserved.
 */
package com.huawei.android.dynamicfeaturesplit

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.huawei.hms.feature.dynamicinstall.FeatureCompat

/**
 * Dynamic Feature Sample Application.
 *
 */
class DynamicFeatureSampleApplication : Application() {
    @SuppressLint("LongLogTag")
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        try {
            FeatureCompat.install(base)
        } catch (e: Exception) {
            Log.w(TAG, "install failed", e);
        }
    }

    companion object {
        /**
         * The constant TAG.
         */
        private const val TAG = "DynamicFeatureSampleApplication"
    }
}