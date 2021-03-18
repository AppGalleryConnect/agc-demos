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
import com.huawei.agconnect.auth.GoogleAuthProvider

class GoogleActivity : ThirdBaseActivity() {
    private var client: GoogleSignInClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_client_id))
                .build()
        client = GoogleSignIn.getClient(this, options)
    }

    override fun login() {
        startActivityForResult(client!!.signInIntent, SIGN_CODE)
    }

    override fun link() {
        startActivityForResult(client!!.signInIntent, LINK_CODE)
    }

    override fun unlink() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            // unlink google , make sure you have already linked google
            AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.Google_Provider)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_CODE) {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener { googleSignInAccount: GoogleSignInAccount ->
                        // create google credential
                        val credential = GoogleAuthProvider.credentialWithToken(googleSignInAccount.idToken)
                        // signIn with google credential
                        auth!!.signIn(credential)
                                .addOnSuccessListener { loginSuccess() }
                                .addOnFailureListener { e: Exception -> showToast(e.message) }
                    }
                    .addOnFailureListener { e: Exception -> showToast(e.message) }
        } else if (requestCode == LINK_CODE) {
            GoogleSignIn.getSignedInAccountFromIntent(data).addOnSuccessListener { googleSignInAccount: GoogleSignInAccount ->
                // create google credential
                val credential = GoogleAuthProvider.credentialWithToken(googleSignInAccount.idToken)
                if (auth!!.currentUser != null) {
                    // link google
                    auth!!.currentUser.link(credential)
                            .addOnSuccessListener { showToast("link success") }
                            .addOnFailureListener { e: Exception -> showToast(e.message) }
                }
            }.addOnFailureListener { e: Exception -> showToast(e.message) }
        }
    }

    companion object {
        private const val SIGN_CODE = 9901
        private const val LINK_CODE = 9902
    }
}