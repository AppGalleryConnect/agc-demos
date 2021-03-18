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
import com.huawei.agconnect.auth.TwitterAuthProvider
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.identity.TwitterAuthClient

class TwitterActivity : ThirdBaseActivity() {
    private var twitterAuthClient: TwitterAuthClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authConfig = TwitterAuthConfig(
                getString(R.string.twitter_app_id),
                getString(R.string.twitter_app_secret))
        val twitterConfig = TwitterConfig.Builder(this)
                .twitterAuthConfig(authConfig)
                .build()
        Twitter.initialize(twitterConfig)
        twitterAuthClient = TwitterAuthClient()
    }

    override fun login() {
        twitterAuthClient!!.authorize(this, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                val token = result.data.authToken.token
                val secret = result.data.authToken.secret
                // create twitter credential
                val credential = TwitterAuthProvider.credentialWithToken(token, secret)
                // sign in
                auth!!.signIn(credential)
                        .addOnSuccessListener { signInResult: SignInResult? -> loginSuccess() }
                        .addOnFailureListener { e: Exception -> showToast(e.message) }
            }

            override fun failure(exception: TwitterException) {
                showToast(exception.message)
            }
        })
    }

    override fun link() {
        twitterAuthClient!!.authorize(this, object : Callback<TwitterSession>() {
            override fun success(result: Result<TwitterSession>) {
                val token = result.data.authToken.token
                val secret = result.data.authToken.secret
                // create twitter credential
                val credential = TwitterAuthProvider.credentialWithToken(token, secret)
                if (auth!!.currentUser != null) {
                    // link twitter
                    auth!!.currentUser.link(credential).addOnSuccessListener { signInResult: SignInResult? -> showToast("link success") }.addOnFailureListener { e: Exception -> showToast(e.message) }
                }
            }

            override fun failure(exception: TwitterException) {
                showToast(exception.message)
            }
        })
    }

    override fun unlink() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            // unlink twitter
            AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.Twitter_Provider)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        twitterAuthClient!!.onActivityResult(requestCode, resultCode, data)
    }
}