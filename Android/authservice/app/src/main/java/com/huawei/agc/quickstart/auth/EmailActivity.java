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

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.TaskExecutors;

public class EmailActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_phone);
        findViewById(R.id.btn_link).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* create a email credential
                 * the credential used for link must contain a verify code.
                 * password is optional, if password is not null, both the password and verification code are verified.
                 */
                AGConnectAuthCredential credential = EmailAuthProvider.credentialWithVerifyCode("email", null, "verify code");
                AGConnectUser agcUser = AGConnectAuth.getInstance().getCurrentUser();
                if (agcUser != null) {
                    // link email
                    agcUser.link(credential)
                        .addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<SignInResult>() {
                            @Override
                            public void onSuccess(SignInResult signInResult) {
                                Toast.makeText(EmailActivity.this, "Link Email Success", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(EmailActivity.this, "Link Email fail:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                }
            }
        });

        findViewById(R.id.btn_unlink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AGConnectAuth.getInstance().getCurrentUser() != null) {
                    // unlink email
                    AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.Email_Provider);
                }
            }
        });
    }

}
