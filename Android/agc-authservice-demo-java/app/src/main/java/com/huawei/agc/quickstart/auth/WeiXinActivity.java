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

import com.huawei.agc.quickstart.auth.wxapi.WXHelper;
import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.WeixinAuthProvider;

public class WeiXinActivity extends ThirdBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WXHelper.setContext(this);
    }

    public void login() {
        WXHelper.signIn(new WXHelper.Callback() {
            @Override
            public void onSuccess(String accessToken, String openId) {
                AGConnectAuthCredential credential = WeixinAuthProvider.credentialWithToken(accessToken, openId);
                auth.signIn(credential)
                    .addOnSuccessListener(signInResult -> loginSuccess())
                    .addOnFailureListener(e -> showToast(e.getMessage()));
            }

            @Override
            public void onFail() {
                showToast("WX Fail");
            }
        });
    }

    @Override
    public void link() {
        WXHelper.signIn(new WXHelper.Callback() {
            @Override
            public void onSuccess(String accessToken, String openId) {
                AGConnectAuthCredential credential = WeixinAuthProvider.credentialWithToken(accessToken, openId);
                auth.getCurrentUser().link(credential).addOnSuccessListener(signInResult -> {
                    showToast("link success");
                }).addOnFailureListener(e -> {
                    showToast(e.getMessage());
                });
            }

            @Override
            public void onFail() {
                showToast("WX Fail");
            }
        });
    }

    @Override
    public void unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.WeiXin_Provider);
        }
    }

}
