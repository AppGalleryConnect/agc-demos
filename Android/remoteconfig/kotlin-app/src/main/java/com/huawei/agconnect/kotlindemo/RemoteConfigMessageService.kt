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

import android.util.Log
import com.huawei.hmf.tasks.Task
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.HmsMessaging
import com.huawei.hms.push.RemoteMessage

class RemoteConfigMessageService : HmsMessageService() {
    override fun onMessageReceived(message: RemoteMessage) {
        Log.i(TAG, "onMessageReceived is called")
        if (message == null) {
            Log.e(TAG, "Received message entity is null!")
            return
        }
        if (message.dataOfMap.containsKey("DATA_STATE")) {
            val preferences = this.applicationContext.getSharedPreferences("Remote_Config", MODE_PRIVATE)
            preferences.edit().putBoolean("DATA_OLD", true).apply()
        }
    }

    override fun onNewToken(s: String) {
        super.onNewToken(s)
        Log.d(TAG, "Push Token : $s")
        HmsMessaging.getInstance(this).subscribe("PUSH_REMOTE_CONFIG").addOnCompleteListener { task: Task<Void?> ->
            if (task.isSuccessful) {
                Log.i(TAG, "subscribe Complete")
            } else {
                Log.e(TAG, "subscribe failed: cause=" + task.exception.message)
            }
        }
    }

    companion object {
        private const val TAG = "MessageService"
    }
}