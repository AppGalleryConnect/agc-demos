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

import android.content.Intent
import android.os.Bundle
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.QQAuthProvider
import com.huawei.agconnect.auth.SignInResult
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONObject

class QQActivity : ThirdBaseActivity() {
    private var tencent: Tencent? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tencent = Tencent.createInstance(getString(R.string.qq_app_id), this.applicationContext)
    }

    override fun login() {
        tencent!!.login(this, "all", object : IUiListener {
            override fun onComplete(o: Any) {
                val jsonObject = o as JSONObject
                val accessToken = jsonObject.optString("access_token")
                val openId = jsonObject.optString("openid")
                // create qq credential
                val credential = QQAuthProvider.credentialWithToken(accessToken, openId)
                // sign in
                auth!!.signIn(credential)
                        .addOnSuccessListener { signInResult: SignInResult? -> loginSuccess() }.addOnFailureListener { e: Exception -> showToast(e.message) }
            }

            override fun onError(uiError: UiError) {
                showToast(uiError.toString())
            }

            override fun onCancel() {
                showToast("Cancel")
            }
        })
    }

    override fun link() {
        tencent!!.login(this, "all", object : IUiListener {
            override fun onComplete(o: Any) {
                val jsonObject = o as JSONObject
                val accessToken = jsonObject.optString("access_token")
                val openId = jsonObject.optString("openid")
                // create qq credential
                val credential = QQAuthProvider.credentialWithToken(accessToken, openId)
                if (auth!!.currentUser != null) {
                    // link qq
                    auth!!.currentUser.link(credential)
                            .addOnSuccessListener { signInResult: SignInResult? -> showToast("link success") }.addOnFailureListener { e: Exception -> showToast(e.message) }
                }
            }

            override fun onError(uiError: UiError) {
                showToast(uiError.toString())
            }

            override fun onCancel() {
                showToast("Cancel")
            }
        })
    }

    override fun unlink() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.QQ_Provider)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Tencent.onActivityResultData(requestCode, resultCode, data, null)
    }
}