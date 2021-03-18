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
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.SelfBuildProvider
import com.huawei.agconnect.auth.SignInResult
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener

class SelfBuildActivity constructor() : ThirdBaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    public override fun login() {
        /* you must provider a Json Web Token
         * first, you need to generate a pair of key pairs.
         * then, upload the public key to the AGC website, use the private key to construct your JWT,
         * and send the generated JWT to the SDK.
         */
        val jwtToken: String = "your jwt token"
        val credential: AGConnectAuthCredential = SelfBuildProvider.credentialWithToken(jwtToken)
        auth!!.signIn(credential).addOnSuccessListener(OnSuccessListener({ signInResult: SignInResult? -> loginSuccess() })).addOnFailureListener(OnFailureListener({ e: Exception -> showToast(e.message) }))
    }

    public override fun link() {
        val jwtToken: String = "your jwt token"
        val credential: AGConnectAuthCredential = SelfBuildProvider.credentialWithToken(jwtToken)
        if (auth!!.getCurrentUser() != null) {
            auth!!.getCurrentUser().link(credential)
                    .addOnSuccessListener(OnSuccessListener({ signInResult: SignInResult? -> showToast("link success") })).addOnFailureListener(OnFailureListener({ e: Exception -> showToast(e.message) }))
        }
    }

    public override fun unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.SelfBuild_Provider)
        }
    }
}