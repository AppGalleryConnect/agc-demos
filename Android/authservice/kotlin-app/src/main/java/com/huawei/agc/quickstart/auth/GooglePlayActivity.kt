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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.GoogleGameAuthProvider

class GooglePlayActivity constructor() : ThirdBaseActivity() {
    private var client: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestServerAuthCode(getString(R.string.google_client_id))
                .build()
        client = GoogleSignIn.getClient(this, options)
    }

    public override fun login() {
        startActivityForResult(client!!.signInIntent, SIGN_CODE)
    }

    public override fun link() {
        startActivityForResult(client!!.signInIntent, LINK_CODE)
    }

    public override fun unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            // unlink google game , make sure you have already linked google game
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.GoogleGame_Provider)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_CODE) {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(com.google.android.gms.tasks.OnSuccessListener { googleSignInAccount: GoogleSignInAccount ->
                        // create google game credential
                        val credential: AGConnectAuthCredential = GoogleGameAuthProvider.credentialWithToken(googleSignInAccount.serverAuthCode)
                        // signIn with google game credential
                        auth!!.signIn(credential)
                                .addOnSuccessListener(com.huawei.hmf.tasks.OnSuccessListener { loginSuccess() })
                                .addOnFailureListener(com.huawei.hmf.tasks.OnFailureListener { e: Exception -> showToast(e.message) })
                    })
                    .addOnFailureListener(com.google.android.gms.tasks.OnFailureListener { e: Exception -> showToast(e.message) })
        } else if (requestCode == LINK_CODE) {
            GoogleSignIn.getSignedInAccountFromIntent(data).addOnSuccessListener(com.google.android.gms.tasks.OnSuccessListener { googleSignInAccount: GoogleSignInAccount ->
                // create google game credential
                val credential: AGConnectAuthCredential = GoogleGameAuthProvider.credentialWithToken(googleSignInAccount.serverAuthCode)
                if (auth!!.currentUser != null) {
                    // link google game credential
                    auth!!.currentUser.link(credential)
                            .addOnSuccessListener(com.huawei.hmf.tasks.OnSuccessListener { showToast("link success") })
                            .addOnFailureListener(com.huawei.hmf.tasks.OnFailureListener { e: Exception -> showToast(e.message) })
                }
            }).addOnFailureListener(com.google.android.gms.tasks.OnFailureListener { e: Exception -> showToast(e.message) })
        }
    }

    companion object {
        private val SIGN_CODE: Int = 9901
        private val LINK_CODE: Int = 9902
    }
}