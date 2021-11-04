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

package com.example.appmessaging.demo.slice;

import com.example.appmessaging.demo.ResourceTable;
import com.huawei.agconnect.appmessaging.AGConnectAppMessaging;
import com.huawei.agconnect.common.api.AGCInstanceID;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;

public class MainAbilitySlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button createShort = (Button) findComponentById(ResourceTable.Id_button_debug);
        createShort.setClickedListener(component -> {
            debugMessage();
        });
    }

    private void debugMessage() {
        /*
         * Step 1
         * Obtain the AAID of the device and add the AAID to the AppGallery Connect server. For details, please refer to the development guide.
         */
        String aaid =  AGCInstanceID.getInstance().getId();

        /*
         *  Step 2
         *  Call the setForceFetch API.
         *  Note: The API can be called only during version tests. Do not use it for officially released versions.
         */
        AGConnectAppMessaging.getInstance().setForceFetch();

        /*
         * Step 3
         * Trigger an event so the SDK can synchronize test device data from the AppGallery Connect server. (Ensure that the AAID of the test device has been added to the AppGallery Connect server.）
         * For example, you can press Home to exit the app and open the app again at least one second later.
         */

        /*
         * Step 4
         * Check logs to see if the test device has been added successfully.
         * Log: This device is a test device
         */
    }
}
