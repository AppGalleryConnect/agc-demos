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
package com.huawei.agc.quickstart.auth

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.PhoneAuthProvider
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.TaskExecutors

class PhoneActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_phone)
        findViewById<View>(R.id.btn_link).setOnClickListener { /* create a phone credential
                 * the credential used for link must contain a verify code.
                 * password is optional, if password is not null, both the password and verification code are verified.
                 */
            val credential = PhoneAuthProvider.credentialWithVerifyCode(
                    "country code",
                    "phone number",
                    null,
                    "verify code")
            if (AGConnectAuth.getInstance().currentUser != null) {
                AGConnectAuth.getInstance().currentUser.link(credential)
                        .addOnSuccessListener(TaskExecutors.uiThread(), OnSuccessListener { Toast.makeText(this@PhoneActivity, "Link phone Success", Toast.LENGTH_SHORT).show() })
                        .addOnFailureListener(TaskExecutors.uiThread(), OnFailureListener { e -> Toast.makeText(this@PhoneActivity, "Link phone fail:" + e.message, Toast.LENGTH_SHORT).show() })
            }
        }
        findViewById<View>(R.id.btn_unlink).setOnClickListener {
            if (AGConnectAuth.getInstance().currentUser != null) {
                // unlink phone
                AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.Phone_Provider)
            }
        }
    }
}