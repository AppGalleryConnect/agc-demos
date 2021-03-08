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

package com.huawei.agc.quickstart.crash

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.huawei.agconnect.crash.AGConnectCrash

class MainActivity : Activity() {
    companion object {
        init {
            System.loadLibrary("native_crash_create")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Record customized logs
        AGConnectCrash.getInstance().log("onCreate")

        // Record Custom Status
        AGConnectCrash.getInstance().setCustomKey("keyInt", 1)
        AGConnectCrash.getInstance().setCustomKey("keyString", "String value")

        // Set the user ID
        AGConnectCrash.getInstance().setUserId("123456789")

        try {
            throw NullPointerException()
        } catch (ex: NullPointerException) {
            AGConnectCrash.getInstance().log("catch exception")

            // Record non-fatal exception
            AGConnectCrash.getInstance().recordException(ex)
        }

        val btn_crash = findViewById<Button>(R.id.btn_crash)
        btn_crash.setOnClickListener {
            // Simulate a crash
            AGConnectCrash.getInstance().testIt(this@MainActivity)
        }

        findViewById<View>(R.id.enable_crash_off).setOnClickListener {
            AGConnectCrash.getInstance().enableCrashCollection(false)
        }

        findViewById<View>(R.id.enable_crash_on).setOnClickListener {
            AGConnectCrash.getInstance().enableCrashCollection(true)
        }

        findViewById<View>(R.id.ndk_crash).setOnClickListener {
            nativeCrashCreate()
        }
    }

    external fun nativeCrashCreate(): Int
}