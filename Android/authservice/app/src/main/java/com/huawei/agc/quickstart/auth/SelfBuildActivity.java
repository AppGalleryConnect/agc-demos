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

package com.huawei.agc.quickstart.auth;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.SelfBuildProvider;

public class SelfBuildActivity extends ThirdBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void login() {
        /* you must provider a Json Web Token
         * first, you need to generate a pair of key pairs.
         * then, upload the public key to the AGC website, use the private key to construct your JWT,
         * and send the generated JWT to the SDK.
         */
        String jwtToken = "your jwt token";
        AGConnectAuthCredential credential = SelfBuildProvider.credentialWithToken(jwtToken);
        auth.signIn(credential).addOnSuccessListener(signInResult -> {
            loginSuccess();
        }).addOnFailureListener(e -> {
            showToast(e.getMessage());
        });
    }

    @Override
    public void link() {
        String jwtToken = "your jwt token";
        AGConnectAuthCredential credential = SelfBuildProvider.credentialWithToken(jwtToken);
        if (auth.getCurrentUser() != null) {
            auth.getCurrentUser().link(credential)
                .addOnSuccessListener(signInResult -> {
                    showToast("link success");
                }).addOnFailureListener(e -> {
                showToast(e.getMessage());
            });
        }
    }

    @Override
    public void unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.SelfBuild_Provider);
        }
    }
}
