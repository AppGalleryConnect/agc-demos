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

package com.huawei.android.dynamicfeaturesplit.splitsamplefeature01;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.huawei.hms.feature.dynamicinstall.FeatureCompat;

/**
 * Feature Activity.
 */
public class FeatureActivity extends Activity {
    private static final String TAG = FeatureActivity.class.getSimpleName();

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            FeatureCompat.install(newBase);
        } catch (Exception e) {
            Log.w(TAG, "failed to install", e);
        }
    }

    static {
        System.loadLibrary("feature-native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feature);
        ImageView mImageView = findViewById(R.id.iv_load_png);
        mImageView.setImageDrawable(getResources().getDrawable(R.mipmap.activity));
        Toast.makeText(this, "from Feature 01: " + stringFromJNI() + " apple is: " + getResources().getString(R.string.apple), Toast.LENGTH_LONG).show();
    }

    /**
     * String from jni string.
     *
     * @return the string
     */
    public native String stringFromJNI();
}
