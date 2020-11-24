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

package com.huawei.agc.quickstart.function;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.huawei.agconnect.function.AGConnectFunction;
import com.huawei.agconnect.function.FunctionResult;
import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    Button btn_add;
    EditText editTextNum1;
    EditText editTextNum2;
    TextView textView;
    AGConnectFunction function;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_add = findViewById(R.id.btn_add);
        editTextNum1 = findViewById(R.id.editText_number1);
        editTextNum2 = findViewById(R.id.editText_number2);
        textView = findViewById(R.id.textView_result);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        });

        function = AGConnectFunction.getInstance();
    }

    private void add(){
        int number1Val = Integer.parseInt(editTextNum1.getText().toString());
        int number2Val = Integer.parseInt(editTextNum2.getText().toString());
        Number number = new Number();
        number.number1 = number1Val;
        number.number2 = number2Val;
        Log.i(TAG,"number1:"+number1Val+",number2:"+number2Val);
        Task<FunctionResult> task = function.wrap("add-$latest").call(number);
        task.addOnCompleteListener(new OnCompleteListener<FunctionResult>() {
            @Override
            public void onComplete(Task<FunctionResult> task) {
                if(task.isSuccessful()) {
                    Sum val = task.getResult().getValue(Sum.class);
                    int sumVal = val.getResult();
                    textView.setText(String.valueOf(sumVal));
                } else {
                    String exception = task.getException().getMessage();
                    textView.setText(exception);
                }
            }
        });
    }
}
