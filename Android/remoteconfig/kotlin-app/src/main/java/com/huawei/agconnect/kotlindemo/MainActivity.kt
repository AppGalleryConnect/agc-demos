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

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.agconnect.remoteconfig.AGConnectConfig
import com.huawei.hmf.tasks.Task
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.analytics.HiAnalytics
import com.huawei.hms.common.ApiException
import com.huawei.hms.push.HmsMessaging
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getToken()
        val hiAnalytics = HiAnalytics.getInstance(this)
        hiAnalytics.setUserProfile("favorite_color", "red")
        hiAnalytics.setUserProfile("favorite_number", "five")
        hiAnalytics.setUserProfile("favorite_food", "banana")
        fetch.setOnClickListener {
            val intent = Intent(baseContext, Test1Activity::class.java)
            startActivity(intent)
        }

        save.setOnClickListener {
                    val intent = Intent(baseContext, Test2Activity::class.java)
                    startActivity(intent)
                }
        clear.setOnClickListener {
                    AGConnectConfig.getInstance().clearAll()
                }
        Log.d(TAG, "AAID : " + HmsInstanceId.getInstance(baseContext).id)
    }

    private fun getToken() {
            Thread {
                try {
                    val appId = AGConnectServicesConfig.fromContext(this@MainActivity).getString("client/app_id")
                    val token = HmsInstanceId.getInstance(this@MainActivity).getToken(appId, "HCM")
                    Log.i(TAG, "Push Token : $token")
                    HmsMessaging.getInstance(this@MainActivity).subscribe("PUSH_REMOTE_CONFIG").addOnCompleteListener { task: Task<Void?> ->
                        if (task.isSuccessful) {
                            Log.i(TAG, "subscribe Complete")
                        } else {
                            Log.e(TAG, "subscribe failed: cause=" + task.exception.message)
                        }
                    }
                } catch (e: ApiException) {
                    e.printStackTrace()
                }
            }.start()
        }

    companion object {
        private const val TAG = "MainActivity"
    }
}