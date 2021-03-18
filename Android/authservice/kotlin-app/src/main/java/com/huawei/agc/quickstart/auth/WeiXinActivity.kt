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

import android.os.Bundle
import com.huawei.agc.quickstart.auth.wxapi.WXHelper
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.SignInResult
import com.huawei.agconnect.auth.WeixinAuthProvider

class WeiXinActivity : ThirdBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WXHelper.setContext(this)
    }

    override fun login() {
        WXHelper.signIn(object : WXHelper.Callback {
            override fun onSuccess(accessToken: String?, openId: String?) {
                // create weiXin credential
                val credential = WeixinAuthProvider.credentialWithToken(accessToken, openId)
                // sign in
                auth!!.signIn(credential)
                        .addOnSuccessListener { signInResult: SignInResult? -> loginSuccess() }
                        .addOnFailureListener { e: Exception -> showToast(e.message) }
            }

            override fun onFail() {
                showToast("WX Fail")
            }
        })
    }

    override fun link() {
        WXHelper.signIn(object : WXHelper.Callback {
            override fun onSuccess(accessToken: String?, openId: String?) {
                // create weiXin credential
                val credential = WeixinAuthProvider.credentialWithToken(accessToken, openId)
                if (auth!!.currentUser != null) {
                    // link weiXin
                    auth!!.currentUser.link(credential)
                            .addOnSuccessListener { signInResult: SignInResult? -> showToast("link success") }
                            .addOnFailureListener { e: Exception -> showToast(e.message) }
                }
            }

            override fun onFail() {
                showToast("WX Fail")
            }
        })
    }

    override fun unlink() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.WeiXin_Provider)
        }
    }
}