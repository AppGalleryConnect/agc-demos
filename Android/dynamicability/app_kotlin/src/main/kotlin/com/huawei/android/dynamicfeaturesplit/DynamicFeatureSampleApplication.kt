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