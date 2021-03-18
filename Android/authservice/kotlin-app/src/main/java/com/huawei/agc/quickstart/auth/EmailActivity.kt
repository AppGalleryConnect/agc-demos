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
import com.huawei.agconnect.auth.EmailAuthProvider
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.TaskExecutors

class EmailActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_phone)
        findViewById<View>(R.id.btn_link).setOnClickListener { /* create a email credential
                 * the credential used for link must contain a verify code.
                 * password is optional, if password is not null, both the password and verification code are verified.
                 */
            val credential = EmailAuthProvider.credentialWithVerifyCode("email", null, "verify code")
            val agcUser = AGConnectAuth.getInstance().currentUser
            agcUser?.link(credential)?.addOnSuccessListener(TaskExecutors.uiThread(), OnSuccessListener {
                Toast.makeText(this@EmailActivity, "Link Email Success", Toast.LENGTH_SHORT).show() })
                    ?.addOnFailureListener(TaskExecutors.uiThread(), OnFailureListener {
                        e -> Toast.makeText(this@EmailActivity, "Link Email fail:" + e.message, Toast.LENGTH_SHORT).show() })
        }
        findViewById<View>(R.id.btn_unlink).setOnClickListener {
            if (AGConnectAuth.getInstance().currentUser != null) {
                // unlink email
                AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.Email_Provider)
            }
        }
    }
}