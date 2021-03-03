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

package com.huawei.agc.quickstart.function

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.huawei.agconnect.function.AGConnectFunction
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_add.setOnClickListener { add() }
    }

    private fun add() {
        val number1Val = Integer.parseInt(editText_number1.text.toString())
        val number2Val = Integer.parseInt(editText_number2.text.toString())
        val number = Number()
        number.number1 = number1Val
        number.number2 = number2Val
        Log.i(TAG, "number1:$number1Val,number2:$number2Val")
        // call cloud function
        val addTask = AGConnectFunction.getInstance().wrap("add-${'$'}latest").call(number)
        addTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val sumResult = task.result.getValue(Sum::class.java)
                val sumVal = sumResult.getResult()
                textView_result.text = sumVal.toString()
            } else {
                val exception = task.exception.message
                textView_result.text = exception
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}
