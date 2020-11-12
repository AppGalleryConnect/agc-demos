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

package com.huawei.agc.clouddb.quickstart.model;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.HwIdAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.auth.api.signin.HuaweiIdSignIn;
import com.huawei.hms.auth.api.signin.HuaweiIdSignInClient;
import com.huawei.hms.support.api.hwid.HuaweiId;
import com.huawei.hms.support.api.hwid.HuaweiIdSignInOptions;
import com.huawei.hms.support.api.hwid.SignInHuaweiId;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle login events
 */
public class LoginHelper {
    private static final String TAG = "LoginHelper";

    private static final int REQUEST_CODE_SIGNIN_HWID = 888;

    private List<OnLoginEventCallBack> mLoginCallbacks = new ArrayList<>();

    private Activity mActivity;

    private HuaweiIdSignInClient mClient;

    public LoginHelper(Activity activity) {
        mActivity = activity;
        HuaweiIdSignInOptions signInOptions = new HuaweiIdSignInOptions.Builder(
            HuaweiIdSignInOptions.DEFAULT_SIGN_IN).requestScopes(HuaweiId.HUAEWEIID_BASE_SCOPE)
            .requestAccessToken()
            .build();
        mClient = HuaweiIdSignIn.getClient(mActivity, signInOptions);
    }

    public void login() {
        // Sign out from agc first
        logOut();
        mActivity.startActivityForResult(mClient.getSignInIntent(), REQUEST_CODE_SIGNIN_HWID);
    }

    public void logOut() {
        AGConnectAuth auth = AGConnectAuth.getInstance();
        auth.signOut();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.w(TAG, "onActivityResult: " + requestCode + " resultCode = " + resultCode);
        if (requestCode == REQUEST_CODE_SIGNIN_HWID && resultCode == Activity.RESULT_OK) {
            Task<SignInHuaweiId> signInHuaweiIdTask = HuaweiIdSignIn.getSignedInAccountFromIntent(data);
            signInHuaweiIdTask.addOnSuccessListener(signInHuaweiId -> {
                String accessToken = signInHuaweiId.getAccessToken();
                Log.w(TAG, "accessToken: " + accessToken);
                AGConnectAuth auth = AGConnectAuth.getInstance();
                AGConnectAuthCredential credential = HwIdAuthProvider.credentialWithToken(accessToken);
                auth.signIn(credential).addOnSuccessListener(signInResult -> {
                    for (OnLoginEventCallBack loginEventCallBack : mLoginCallbacks) {
                        loginEventCallBack.onLogin(true, signInResult);
                    }
                }).addOnFailureListener(e -> {
                    Log.w(TAG, "sign in for agc failed: " + e.getMessage());
                    for (OnLoginEventCallBack loginEventCallBack : mLoginCallbacks) {
                        loginEventCallBack.onLogOut(false);
                    }
                });
            }).addOnFailureListener(e -> Log.w(TAG, "sign in hwid faild: " + e.getMessage()));
        }
    }

    public void addLoginCallBack(OnLoginEventCallBack loginEventCallBack) {
        if (!mLoginCallbacks.contains(loginEventCallBack)) {
            mLoginCallbacks.add(loginEventCallBack);
        }
    }

    public interface OnLoginEventCallBack {
        void onLogin(boolean showLoginUserInfo, SignInResult signInResult);

        void onLogOut(boolean showLoginUserInfo);
    }
}
