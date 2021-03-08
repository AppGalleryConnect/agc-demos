/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.agc.quickstart.remoteconfig;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.agconnect.remoteconfig.AGConnectConfig;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.push.HmsMessaging;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getToken();

        HiAnalyticsInstance hiAnalytics = HiAnalytics.getInstance(this);
        hiAnalytics.setUserProfile("favorite_color", "red");
        hiAnalytics.setUserProfile("favorite_number", "five");
        hiAnalytics.setUserProfile("favorite_food", "banana");

        findViewById(R.id.fetch)
                .setOnClickListener(
                        view -> {
                            Intent intent = new Intent(getBaseContext(), Test1Activity.class);
                            startActivity(intent);
                        });
        findViewById(R.id.save)
                .setOnClickListener(
                        view -> {
                            Intent intent = new Intent(getBaseContext(), Test2Activity.class);
                            startActivity(intent);
                        });
        findViewById(R.id.clear)
                .setOnClickListener(
                        view -> {
                            AGConnectConfig.getInstance().clearAll();
                        });
        Log.d(TAG, "AAID : " + HmsInstanceId.getInstance(getBaseContext()).getId());
    }

    private void getToken() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String app_id = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                    String token = HmsInstanceId.getInstance(MainActivity.this).getToken(app_id,"HCM");
                    Log.i(TAG,"Push Token : " + token);
                    HmsMessaging.getInstance(MainActivity.this).subscribe("PUSH_REMOTE_CONFIG").addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "subscribe Complete");
                        } else {
                            Log.e(TAG, "subscribe failed: cause=" + task.getException().getMessage());
                        }
                    });
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
