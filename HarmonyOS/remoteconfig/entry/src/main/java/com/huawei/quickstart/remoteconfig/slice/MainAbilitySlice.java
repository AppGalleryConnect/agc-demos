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
import ohos.agp.components.Button;

public class MainAbilitySlice extends AbilitySlice {

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        Button fetchButton = (Button) findComponentById(ResourceTable.Id_fetch);
        fetchButton.setClickedListener(listener -> present(new Test1AbilitySlice(), new Intent()));

        Button saveButton = (Button) findComponentById(ResourceTable.Id_save);
        saveButton.setClickedListener(listener -> present(new Test2AbilitySlice(), new Intent()));

        Button clearButton = (Button) findComponentById(ResourceTable.Id_clear);
        clearButton.setClickedListener(listener -> AGConnectConfig.getInstance().clearAll());
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
