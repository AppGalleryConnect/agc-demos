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
import com.huawei.agconnect.auth.SignInResult
import com.huawei.agconnect.auth.WeiboAuthProvider
import com.sina.weibo.sdk.WbSdk
import com.sina.weibo.sdk.auth.AuthInfo
import com.sina.weibo.sdk.auth.Oauth2AccessToken
import com.sina.weibo.sdk.auth.WbAuthListener
import com.sina.weibo.sdk.auth.WbConnectErrorMessage
import com.sina.weibo.sdk.auth.sso.SsoHandler

class WeiboActivity : ThirdBaseActivity() {
    private var ssoHandler: SsoHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mAuthInfo = AuthInfo(
                this,
                getString(R.string.weibo_app_id),
                getString(R.string.weibo_redirect_url),
                "all")
        WbSdk.install(this, mAuthInfo)
        ssoHandler = SsoHandler(this@WeiboActivity)
    }

    override fun login() {
        ssoHandler!!.authorize(object : WbAuthListener {
            override fun onSuccess(oauth2AccessToken: Oauth2AccessToken) {
                // create Weibo credential
                val credential = WeiboAuthProvider.credentialWithToken(oauth2AccessToken.token, oauth2AccessToken.uid)
                // sign in
                auth!!.signIn(credential)
                        .addOnSuccessListener { signInResult: SignInResult? -> loginSuccess() }
                        .addOnFailureListener { e: Exception -> showToast(e.message) }
            }

            override fun cancel() {
                showToast("Cancel")
            }

            override fun onFailure(wbConnectErrorMessage: WbConnectErrorMessage) {
                showToast(wbConnectErrorMessage.errorMessage)
            }
        })
    }

    override fun link() {
        ssoHandler!!.authorize(object : WbAuthListener {
            override fun onSuccess(oauth2AccessToken: Oauth2AccessToken) {
                // create Weibo credential
                val credential = WeiboAuthProvider.credentialWithToken(oauth2AccessToken.token, oauth2AccessToken.uid)
                if (auth!!.currentUser != null) {
                    auth!!.currentUser.link(credential).addOnSuccessListener { signInResult: SignInResult? -> showToast("link success") }.addOnFailureListener { e: Exception -> showToast(e.message) }
                }
            }

            override fun cancel() {
                showToast("Cancel")
            }

            override fun onFailure(wbConnectErrorMessage: WbConnectErrorMessage) {
                showToast(wbConnectErrorMessage.errorMessage)
            }
        })
    }

    override fun unlink() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            // unlink weibo
            AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.WeiBo_Provider)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (ssoHandler != null) {
            ssoHandler!!.authorizeCallBack(requestCode, resultCode, data)
        }
    }
}