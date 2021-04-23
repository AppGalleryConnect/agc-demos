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
import android.util.Log
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.HwIdAuthProvider
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.TaskExecutors
import com.huawei.hms.common.ApiException
import com.huawei.hms.support.account.AccountAuthManager
import com.huawei.hms.support.account.request.AccountAuthParams
import com.huawei.hms.support.account.request.AccountAuthParamsHelper
import com.huawei.hms.support.account.service.AccountAuthService

class HWIDActivity : ThirdBaseActivity() {
    private var service: AccountAuthService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authParams: AccountAuthParams = AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setAccessToken()
                .createParams();
        service = AccountAuthManager.getService(this@HWIDActivity, authParams)
    }

    override fun login() {
        startActivityForResult(service!!.signInIntent, SIGN_CODE)
    }

    override fun link() {
        startActivityForResult(service!!.signInIntent, LINK_CODE)
    }

    override fun unlink() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            // unlink huawei id
            AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.HMS_Provider)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_CODE) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                val authAccount = authAccountTask.result
                Log.i(TAG, "accessToken:" + authAccount.accessToken)

                // create huawei id credential
                val credential = HwIdAuthProvider.credentialWithToken(authAccount.accessToken)
                // sign in
                auth!!.signIn(credential)
                        .addOnSuccessListener { loginSuccess() }
                        .addOnFailureListener { e: Exception -> showToast(e.message) }
            } else {
                Log.e(TAG, "sign in failed : " + (authAccountTask.exception as ApiException).statusCode)
            }
        } else if (requestCode == LINK_CODE) {
            val authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data)
            if (authAccountTask.isSuccessful) {
                val authAccount = authAccountTask.result
                // create huawei id credential
                val credential = HwIdAuthProvider.credentialWithToken(authAccount.accessToken)
                if (auth!!.currentUser != null) {
                    // link huawei id
                    auth!!.currentUser.link(credential)
                            .addOnSuccessListener(TaskExecutors.uiThread(), OnSuccessListener { showToast("link success") })
                            .addOnFailureListener(TaskExecutors.uiThread(), OnFailureListener { e -> showToast(e.message) })
                }
            } else {
                Log.e(TAG, "sign in failed : " + (authAccountTask.exception as ApiException).statusCode)
            }
        }
    }

    companion object {
        private val TAG = HWIDActivity::class.java.simpleName
        private const val SIGN_CODE = 9901
        private const val LINK_CODE = 9902
    }
}