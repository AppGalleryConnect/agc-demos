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
import android.util.Log;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.core.service.auth.OnTokenListener;
import com.huawei.agconnect.core.service.auth.TokenSnapshot;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Handle login events
 */
public class LoginHelper {
    private static final String TAG = "LoginHelper";

    private List<OnLoginEventCallBack> mLoginCallbacks = new ArrayList<>();

    private Activity mActivity;

    public LoginHelper(Activity activity) {
        mActivity = activity;
    }

    public void login() {
        AGConnectAuth auth = AGConnectAuth.getInstance();
        auth.signInAnonymously().addOnSuccessListener(mActivity, signInResult -> {
            Log.i(TAG, "addOnSuccessListener: " + signInResult.getUser().getDisplayName());
            for (OnLoginEventCallBack loginEventCallBack : mLoginCallbacks) {
                loginEventCallBack.onLogin(true, signInResult);
            }
        }).addOnFailureListener(mActivity, e -> {
            Log.w(TAG, "Sign in for agc failed: " + e.getMessage());
            for (OnLoginEventCallBack loginEventCallBack : mLoginCallbacks) {
                loginEventCallBack.onLogOut(false);
            }
        });
    }

    public void logOut() {
        AGConnectAuth auth = AGConnectAuth.getInstance();
        auth.signOut();
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
