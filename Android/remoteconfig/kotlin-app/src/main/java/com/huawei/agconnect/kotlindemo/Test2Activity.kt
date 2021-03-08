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

package com.huawei.agconnect.kotlindemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.remoteconfig.AGConnectConfig
import kotlinx.android.synthetic.main.activity_test2.*
import java.util.*

class Test2Activity : AppCompatActivity() {
    private val config: AGConnectConfig = AGConnectConfig.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test1)
        val map: MutableMap<String, Any> = HashMap()
        map["test1"] = "test1"
        map["test2"] = "true"
        map["test3"] = 123
        map["test4"] = 123.456
        map["test5"] = "test-test"
        // Apply default parameter values
        config.applyDefault(map)
        // Obtains the cached data that is successfully fetched last time
        val configValues = config.loadLastFetched()
        // Make the configuration parameters take effect
        config.apply(configValues)
        // fetch configuration parameters from the cloud
        config.fetch().addOnSuccessListener {
            Toast.makeText(baseContext, "Fetch Success", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(baseContext, "Fetch Fail", Toast.LENGTH_LONG).show()
        }
        showAllValues()
    }

    private fun showAllValues() {
        // Merge the default parameters and the parameters fetched from the cloud
        val map = config.mergedAll
        val string = StringBuilder()
        for ((key, value) in map) {
            string.append(key)
            string.append(" : ")
            string.append(value)
            string.append("\n")
        }
        textView.text = string
    }
}