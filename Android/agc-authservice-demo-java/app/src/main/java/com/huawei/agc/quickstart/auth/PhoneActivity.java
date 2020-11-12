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
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.TaskExecutors;

public class PhoneActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        findViewById(R.id.btn_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AGConnectAuthCredential credential = PhoneAuthProvider.credentialWithVerifyCode(
                    "country code",
                    "phone number",
                    null, // password, can be null
                    "verify code");
                AGConnectUser agcUser = AGConnectAuth.getInstance().getCurrentUser();
                if (agcUser != null) {
                    agcUser.link(credential)
                        .addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<SignInResult>() {
                            @Override
                            public void onSuccess(SignInResult signInResult) {

                            }
                        })
                        .addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                }
            }
        });

        findViewById(R.id.btn_unlink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AGConnectUser agcUser = AGConnectAuth.getInstance().getCurrentUser();
                if (agcUser != null) {
                    agcUser.unlink(AGConnectAuthCredential.Phone_Provider);
                }
            }
        });
    }

}
