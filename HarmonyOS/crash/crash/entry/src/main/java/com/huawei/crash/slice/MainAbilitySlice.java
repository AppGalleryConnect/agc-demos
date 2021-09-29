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
 
package com.huawei.crash.slice;

import com.huawei.agconnect.crash.AGConnectCrash;
import com.huawei.crash.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Component;

public class MainAbilitySlice extends AbilitySlice {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button btn_crash = (Button) findComponentById(ResourceTable.Id_Make_Crash);
        btn_crash.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                // Record customized logs
                AGConnectCrash.getInstance().log("onCreate");

                // Record Custom Status
                AGConnectCrash.getInstance().setCustomKey("keyInt", 1);
                AGConnectCrash.getInstance().setCustomKey("keyString", "String value");

                // Set the user ID
                AGConnectCrash.getInstance().setUserId("123456789");

                try {
                    throw new NullPointerException();
                } catch (NullPointerException ex) {
                    AGConnectCrash.getInstance().log("catch exception");

                    // Record non-fatal exception
                    AGConnectCrash.getInstance().recordException(ex);
                }

                AGConnectCrash.getInstance().testIt(MainAbilitySlice.this);
            }
        });
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

