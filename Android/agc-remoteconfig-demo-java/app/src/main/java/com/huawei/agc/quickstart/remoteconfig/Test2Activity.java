/*
Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.huawei.agc.quickstart.remoteconfig;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.remoteconfig.AGConnectConfig;
import com.huawei.agconnect.remoteconfig.ConfigValues;

import java.util.HashMap;
import java.util.Map;

public class Test2Activity extends AppCompatActivity {

    private TextView textView;
    private AGConnectConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        textView = findViewById(R.id.textView);

        config = AGConnectConfig.getInstance();
        Map<String, Object> map = new HashMap<>();
        map.put("test1", "test1");
        map.put("test2", "true");
        map.put("test3", 123);
        map.put("test4", 123.456);
        map.put("test5", "test-test");
        config.applyDefault(map);
        ConfigValues configValues = config.loadLastFetched();
        config.apply(configValues);
        config.fetch().addOnSuccessListener(configValues1 -> {
            Toast.makeText(getBaseContext(), "Fetch Success", Toast.LENGTH_LONG).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getBaseContext(), "Fetch Fail", Toast.LENGTH_LONG).show();
        });
        showAllValues();
    }

    private void showAllValues() {
        Map<String, Object> map = config.getMergedAll();
        StringBuilder string = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            string.append(entry.getKey());
            string.append(" : ");
            string.append(entry.getValue());
            string.append("\n");
        }
        textView.setText(string);
    }
}
