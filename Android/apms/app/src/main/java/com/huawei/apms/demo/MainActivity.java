/*
 * Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.
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
 package com.huawei.apms.demo;

import android.os.Bundle;
import com.huawei.agconnect.apms.APMS;
import com.huawei.agconnect.apms.androiddemo.R;
import com.huawei.agconnect.apms.custom.CustomTrace;
import com.huawei.agconnect.apms.instrument.AddCustomTrace;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendNetworkRequestBtn = findViewById(R.id.btn_network);
        sendNetworkRequestBtn.setOnClickListener(view -> {
            Log.d("apmsAndroidDemo", "send network request.");
            HttpUtil.oneRequest();
        });

        this.findViewById(R.id.anr_test).setOnClickListener(v -> {
            Log.d("apmsAndroidDemo", "trigger ANR");
            anrTestEnable = true;
        });

        initCustomEventButton();

        initApmsSwitchButton();
        initApmsAnrSwitchButton();

    }

    public void initCustomEventButton() {
        findViewById(R.id.custom_normal_event).setOnClickListener(v -> {
            Log.d("apmsAndroidDemo", "send a custom event");
            sendCustomEvent();
        });
        findViewById(R.id.custom_normal_event_by_annotation).setOnClickListener(v -> {
            Log.d("apmsAndroidDemo", "send a custom event by annotation");
            sendCustomEventByAnnotation();
        });
        findViewById(R.id.custom_network_event).setOnClickListener(v -> {
            Log.d("apmsAndroidDemo", "send a custom network event");
            new Thread(HttpUtil::customNetworkEvent).start();
        });
    }

    public void initApmsSwitchButton() {
        findViewById(R.id.enable_apms_on).setOnClickListener(v -> {
            Log.d("apmsAndroidDemo", "enable Collection");
            APMS.getInstance().enableCollection(true);
        });

        findViewById(R.id.enable_apms_off).setOnClickListener(v -> {
            Log.d("apmsAndroidDemo", "disable Collection");
            APMS.getInstance().enableCollection(false);
        });
    }

    public void initApmsAnrSwitchButton() {
        findViewById(R.id.enable_apms_anr_on).setOnClickListener(v -> {
            Log.d("apmsAndroidDemo", "enable ANR Collection");
            APMS.getInstance().enableAnrMonitor(true);
        });

        findViewById(R.id.enable_apms_anr_off).setOnClickListener(v -> {
            Log.d("apmsAndroidDemo", "disable ANR Collection");
            APMS.getInstance().enableAnrMonitor(false);
        });
    }

    private boolean anrTestEnable = false;
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (anrTestEnable) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (anrTestEnable) {
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void sendCustomEventByAnnotation() {
        customEventHandle();
    }
    // define custom event by annotation
    @AddCustomTrace(name = "CustomEvent2")
    public void customEventHandle() {
    }

    // define custom event by code
    public void sendCustomEvent() {
        CustomTrace customTrace = APMS.getInstance().createCustomTrace("CustomEvent1");
        customTrace.start();
        // code you want trace
        businessLogicStart(customTrace);
        businessLogicEnd(customTrace);
        customTrace.stop();
    }

    public void businessLogicStart(CustomTrace customTrace) {
        customTrace.putMeasure("ProcessingTimes", 0);
        for (int i = 0; i < 5; i++) {
            customTrace.incrementMeasure("ProcessingTimes", 1);
        }
    }

    public void businessLogicEnd(CustomTrace customTrace) {
        customTrace.putProperty("ProcessingResult", "Success");
        customTrace.putProperty("Status", "Normal");
    }

}