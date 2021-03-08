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
import com.huawei.agconnect.remoteconfig.ConfigValues
import kotlinx.android.synthetic.main.activity_test1.*

class Test1Activity : AppCompatActivity() {
    private val config: AGConnectConfig = AGConnectConfig.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test1)
        // Apply default parameter values based on the resource file
        config.applyDefault(R.xml.remote_config)
        val preferences = this.applicationContext.getSharedPreferences("Remote_Config", MODE_PRIVATE)
        var fetchInterval = 12 * 60 * 60L
        if (preferences.getBoolean("DATA_OLD", false)) {
            fetchInterval = 0
        }
        // fetch configuration parameters from the cloud
        config.fetch(fetchInterval).addOnSuccessListener { configValues: ConfigValues? ->
            // Make the configuration parameters take effect
            config.apply(configValues)
            showAllValues()
            preferences.edit().putBoolean("DATA_OLD", false).apply()
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