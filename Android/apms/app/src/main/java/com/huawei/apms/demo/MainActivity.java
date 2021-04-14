/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
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
import com.huawei.util.HttpUtil;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize button event
        initNetworkEventButton();
        initApmsSwitchButton();
        initCustomEventButton();
    }

    // define custom event by annotation
    @AddCustomTrace(name = "自定义事件2")
    public void customEventHandle() {
    }

    // define custom event by code
    public void sendCustomEvent() {
        CustomTrace customTrace = APMS.getInstance().createCustomTrace("自定义事件1");
        customTrace.start();
        // code you want trace
        businessLogicStart(customTrace);
        businessLogicEnd(customTrace);
        customTrace.stop();
    }

    private void sendCustomEventByAnnotation() {
        customEventHandle();
    }

    public void businessLogicStart(CustomTrace customTrace) {
        customTrace.putMeasure("处理次数", 0);
        for (int i = 0; i < 5; i++) {
            customTrace.incrementMeasure("处理次数", 1);
        }
    }

    public void businessLogicEnd(CustomTrace customTrace) {
        customTrace.putProperty("处理结果", "成功");
        customTrace.putProperty("状态", "正常");
    }


    public void initApmsSwitchButton() {
        findViewById(R.id.enable_apms_off).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "disable apms.");
                APMS.getInstance().enableCollection(false);
            }
        });
        findViewById(R.id.enable_apms_on).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "enable apms.");
                APMS.getInstance().enableCollection(true);
            }
        });
    }

    public void initNetworkEventButton() {
        Log.d("apmsAndroidDemo", "apms demo start.");
        Button sendNetworkRequestBtn = findViewById(R.id.btn_network);
        sendNetworkRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("apmsAndroidDemo", "send network request.");
                HttpUtil.oneRequest();
            }
        });
    }

    public void initCustomEventButton() {
        findViewById(R.id.custom_normal_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "send a custom event");
                sendCustomEvent();
            }
        });
        findViewById(R.id.custom_normal_event_by_annotation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "send a custom event by annotation");
                sendCustomEventByAnnotation();
            }
        });
        findViewById(R.id.custom_network_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("apmsAndroidDemo", "send a custom network event");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtil.customNetworkEvent();
                    }
                }).start();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
