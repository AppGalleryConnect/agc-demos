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

package com.huawei.quickstart.remoteconfig.slice;

import com.huawei.agconnect.remoteconfig.AGConnectConfig;
import com.huawei.quickstart.remoteconfig.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Text;
import ohos.agp.window.dialog.ToastDialog;

import java.util.Map;

public class Test1AbilitySlice extends AbilitySlice {
    private Text textView;
    private AGConnectConfig config;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_test1);
        textView = (Text) findComponentById(ResourceTable.Id_textView);

        config = AGConnectConfig.getInstance();
        // Apply default parameter values based on the resource file
        config.applyDefault(ResourceTable.Profile_configdefault);

        // fetch configuration parameters from the cloud
        long fetchInterval = 12 * 60 * 60L;
        ToastDialog toastDialog = new ToastDialog(this);
        config.fetch(fetchInterval).addOnSuccessListener(configValues -> {
            // Make the configuration parameters take effect
            config.apply(configValues);
            showAllValues();
            toastDialog.setText("Fetch Success").setDuration(1000).show();
        }).addOnFailureListener(e -> {
            toastDialog.setText("Fetch Fail").setDuration(1000).show();
        });
        showAllValues();
    }

    private void showAllValues() {
        // Merge the default parameters and the parameters fetched from the cloud
        Map<String, Object> map = config.getMergedAll();
        StringBuilder string = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            string.append(entry.getKey());
            string.append(" : ");
            string.append(entry.getValue());
            string.append("\n");
        }
        textView.setText(string.toString());
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }
}
