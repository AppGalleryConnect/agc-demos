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
import com.huawei.agconnect.auth.WeiboAuthProvider;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

public class WeiboActivity extends ThirdBaseActivity {
    private SsoHandler ssoHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AuthInfo mAuthInfo = new AuthInfo(
            this,
            getString(R.string.weibo_app_id),
            getString(R.string.weibo_redirect_url),
            "all");
        WbSdk.install(this, mAuthInfo);
        ssoHandler = new SsoHandler(WeiboActivity.this);
    }

    @Override
    public void login() {
        ssoHandler.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                // create Weibo credential
                AGConnectAuthCredential credential =
                    WeiboAuthProvider.credentialWithToken(oauth2AccessToken.getToken(), oauth2AccessToken.getUid());
                // sign in
                auth.signIn(credential)
                    .addOnSuccessListener(signInResult -> loginSuccess())
                    .addOnFailureListener(e -> showToast(e.getMessage()));
            }

            @Override
            public void cancel() {
                showToast("Cancel");
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                showToast(wbConnectErrorMessage.getErrorMessage());
            }
        });
    }

    @Override
    public void link() {
        ssoHandler.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
                // create Weibo credential
                AGConnectAuthCredential credential =
                    WeiboAuthProvider.credentialWithToken(oauth2AccessToken.getToken(), oauth2AccessToken.getUid());
                if (auth.getCurrentUser() != null) {
                    auth.getCurrentUser().link(credential).addOnSuccessListener(signInResult -> {
                        showToast("link success");
                    }).addOnFailureListener(e -> {
                        showToast(e.getMessage());
                    });
                }
            }

            @Override
            public void cancel() {
                showToast("Cancel");
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                showToast(wbConnectErrorMessage.getErrorMessage());
            }
        });
    }

    @Override
    public void unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            // unlink weibo
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.WeiBo_Provider);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
