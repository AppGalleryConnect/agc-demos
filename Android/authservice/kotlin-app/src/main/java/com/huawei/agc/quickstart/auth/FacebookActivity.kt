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
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.FacebookAuthProvider
import com.huawei.agconnect.auth.SignInResult
import java.util.*

class FacebookActivity : ThirdBaseActivity() {
    private val callbackManager = CallbackManager.Factory.create()
    override fun login() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val token = loginResult.accessToken.token
                // create facebook credential
                val credential = FacebookAuthProvider.credentialWithToken(token)
                // signIn
                auth!!.signIn(credential)
                        .addOnSuccessListener { signInResult: SignInResult? -> loginSuccess() }
                        .addOnFailureListener { e: Exception -> showToast(e.message) }
            }

            override fun onCancel() {
                showToast("Cancel")
            }

            override fun onError(error: FacebookException) {
                showToast(error.message)
            }
        })
    }

    override fun link() {
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val token = loginResult.accessToken.token
                // create facebook credential
                val credential = FacebookAuthProvider.credentialWithToken(token)
                if (auth!!.currentUser != null) {
                    // link facebook
                    auth!!.currentUser
                            .link(credential)
                            .addOnSuccessListener { signInResult: SignInResult? -> showToast("link success") }
                            .addOnFailureListener { e: Exception -> showToast(e.message) }
                }
            }

            override fun onCancel() {
                showToast("Cancel")
            }

            override fun onError(error: FacebookException) {
                showToast(error.message)
            }
        })
    }

    override fun unlink() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            // unlink facebook,make sure you have already linked facebook
            AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.Facebook_Provider)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
}