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
import com.huawei.agconnect.auth.TwitterAuthProvider;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class TwitterActivity extends ThirdBaseActivity {
    private TwitterAuthClient twitterAuthClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(
            getString(R.string.twitter_app_id),
            getString(R.string.twitter_app_secret));
        TwitterConfig twitterConfig = new TwitterConfig
            .Builder(this)
            .twitterAuthConfig(authConfig)
            .build();
        Twitter.initialize(twitterConfig);
        twitterAuthClient = new TwitterAuthClient();
    }

    @Override
    public void login() {
        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                String token = result.data.getAuthToken().token;
                String secret = result.data.getAuthToken().secret;
                AGConnectAuthCredential credential = TwitterAuthProvider.credentialWithToken(token, secret);
                auth.signIn(credential)
                    .addOnSuccessListener(signInResult -> loginSuccess())
                    .addOnFailureListener(e -> showToast(e.getMessage()));
            }

            @Override
            public void failure(TwitterException exception) {
                showToast(exception.getMessage());
            }
        });
    }

    @Override
    public void link() {
        twitterAuthClient.authorize(this, new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                String token = result.data.getAuthToken().token;
                String secret = result.data.getAuthToken().secret;
                AGConnectAuthCredential credential = TwitterAuthProvider.credentialWithToken(token, secret);
                auth.getCurrentUser().link(credential).addOnSuccessListener(signInResult -> {
                    showToast("link success");
                }).addOnFailureListener(e -> {
                    showToast(e.getMessage());
                });
            }

            @Override
            public void failure(TwitterException exception) {
                showToast(exception.getMessage());
            }
        });
    }

    @Override
    public void unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.Twitter_Provider);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        twitterAuthClient.onActivityResult(requestCode, resultCode, data);
    }
}
