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

package com.huawei.quickstart.cloudfunction.slice;

import com.huawei.agconnect.common.AGCLogger;
import com.huawei.agconnect.function.AGConnectFunction;
import com.huawei.agconnect.function.FunctionResult;
import com.huawei.hmf.tasks.HarmonyTask;
import com.huawei.hmf.tasks.OnHarmonyCompleteListener;
import com.huawei.quickstart.cloudfunction.ResourceTable;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Text;
import ohos.agp.components.TextField;

import java.util.HashMap;

import static ohos.data.search.schema.PhotoItem.TAG;

public class MainAbilitySlice extends AbilitySlice {
    TextField textFieldNum1;
    TextField textFieldNum2;
    Text textView;

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        textFieldNum1 = (TextField)findComponentById(ResourceTable.Id_editText_number1);
        textFieldNum2 = (TextField)findComponentById(ResourceTable.Id_editText_number2);
        textView = (Text)findComponentById(ResourceTable.Id_textView_result);
        Button btn_add = (Button)findComponentById(ResourceTable.Id_btn_add);
        btn_add.setClickedListener(listener -> testFunctionAdd());
    }

    public void testFunctionAdd() {
        HashMap<String, Integer> number = new HashMap();
        number.put("number1", Integer.parseInt(textFieldNum1.getText()));
        number.put("number2", Integer.parseInt(textFieldNum2.getText()));
        // call cloud function
        // You need to configure related processing functions on the web page,
        // Function corresponding to the current demo. For details, see the handler.js file.
        AGConnectFunction function = AGConnectFunction.getInstance();
        HarmonyTask<FunctionResult> task = function.wrap("add-$latest").call(number);
        task.addOnCompleteListener(new OnHarmonyCompleteListener<FunctionResult>() {
            @Override
            public void onComplete(HarmonyTask<FunctionResult> task) {
                if (task.isSuccessful()) {
                    String val = task.getResult().getValue(String.class);
                    textView.setText(val);
                } else {
                    textView.setText("");
                    AGCLogger.e(TAG, "exception", task.getException().getMessage());
                }
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
