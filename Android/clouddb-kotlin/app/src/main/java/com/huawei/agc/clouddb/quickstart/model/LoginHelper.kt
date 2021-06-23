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
package com.huawei.agc.clouddb.quickstart.model

import android.app.Activity
import android.util.Log
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.SignInResult
import java.util.*

/**
 * Handle login events
 */
class LoginHelper(private val mActivity: Activity) {
    private val mLoginCallbacks: MutableList<OnLoginEventCallBack> = ArrayList()
    fun login() {
        val auth = AGConnectAuth.getInstance()
        auth.signInAnonymously().addOnSuccessListener(mActivity) { signInResult: SignInResult ->
            Log.i(TAG, "addOnSuccessListener: " + signInResult.user.displayName)
            for (loginEventCallBack in mLoginCallbacks) {
                loginEventCallBack.onLogin(true, signInResult)
            }
        }.addOnFailureListener(mActivity) { e: Exception ->
            Log.w(TAG, "Sign in for agc failed: " + e.message)
            for (loginEventCallBack in mLoginCallbacks) {
                loginEventCallBack.onLogOut(false)
            }
        }
    }

    fun logOut() {
        val auth = AGConnectAuth.getInstance()
        auth.signOut()
    }

    fun addLoginCallBack(loginEventCallBack: OnLoginEventCallBack) {
        if (!mLoginCallbacks.contains(loginEventCallBack)) {
            mLoginCallbacks.add(loginEventCallBack)
        }
    }

    interface OnLoginEventCallBack {
        fun onLogin(showLoginUserInfo: Boolean, signInResult: SignInResult?)
        fun onLogOut(showLoginUserInfo: Boolean)
    }

    companion object {
        private const val TAG = "LoginHelper"
    }
}