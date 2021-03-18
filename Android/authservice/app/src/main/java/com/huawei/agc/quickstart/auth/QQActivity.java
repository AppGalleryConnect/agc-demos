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

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.QQAuthProvider;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

public class QQActivity extends ThirdBaseActivity {
    private Tencent tencent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tencent = Tencent.createInstance(getString(R.string.qq_app_id), this.getApplicationContext());
    }

    @Override
    public void login() {
        tencent.login(this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject jsonObject = (JSONObject) o;
                String accessToken = jsonObject.optString("access_token");
                String openId = jsonObject.optString("openid");
                // create qq credential
                AGConnectAuthCredential credential = QQAuthProvider.credentialWithToken(accessToken, openId);
                // sign in
                auth.signIn(credential)
                    .addOnSuccessListener(signInResult -> {
                        loginSuccess();
                    }).addOnFailureListener(e -> {
                    showToast(e.getMessage());
                });
            }

            @Override
            public void onError(UiError uiError) {
                showToast(uiError.toString());
            }

            @Override
            public void onCancel() {
                showToast("Cancel");
            }
        });
    }

    @Override
    public void link() {
        tencent.login(this, "all", new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject jsonObject = (JSONObject) o;
                String accessToken = jsonObject.optString("access_token");
                String openId = jsonObject.optString("openid");
                // create qq credential
                AGConnectAuthCredential credential = QQAuthProvider.credentialWithToken(accessToken, openId);
                if (auth.getCurrentUser() != null) {
                    // link qq
                    auth.getCurrentUser().link(credential)
                        .addOnSuccessListener(signInResult -> {
                            showToast("link success");
                        }).addOnFailureListener(e -> {
                        showToast(e.getMessage());
                    });
                }
            }

            @Override
            public void onError(UiError uiError) {
                showToast(uiError.toString());
            }

            @Override
            public void onCancel() {
                showToast("Cancel");
            }
        });
    }

    @Override
    public void unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.QQ_Provider);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, null);
    }
}
