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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.PhoneUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText countryCodeEdit;
    private EditText accountEdit;
    private EditText passwordEdit;
    private EditText verifyCodeEdit;

    private LoginActivity.Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        type = (LoginActivity.Type) getIntent().getSerializableExtra("registerType");
        initView();
    }

    private void initView() {
        TextView accountTv = findViewById(R.id.tv_account);
        countryCodeEdit = findViewById(R.id.et_country_code);
        accountEdit = findViewById(R.id.et_account);
        passwordEdit = findViewById(R.id.et_password);
        verifyCodeEdit = findViewById(R.id.et_verify_code);
        ViewGroup countryCodeLayout = findViewById(R.id.layout_cc);
        if (type == LoginActivity.Type.EMAIL) {
            accountTv.setText(R.string.email);
            countryCodeLayout.setVisibility(View.INVISIBLE);
        } else {
            accountTv.setText(R.string.phone);
            countryCodeLayout.setVisibility(View.VISIBLE);
        }

        Button registerBtn = findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(this);

        Button send = findViewById(R.id.btn_send);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                login();
                break;
            case R.id.btn_send:
                sendVerificationCode();
                break;
        }
    }

    private void login() {
        if (type == LoginActivity.Type.EMAIL) {
            String email = accountEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();
            String verifyCode = verifyCodeEdit.getText().toString().trim();

            EmailUser emailUser = new EmailUser.Builder()
                .setEmail(email)
                .setPassword(password)//optional
                .setVerifyCode(verifyCode)
                .build();
            AGConnectAuth.getInstance().createUser(emailUser)
                .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                    @Override
                    public void onSuccess(SignInResult signInResult) {
                        startActivity(new Intent(RegisterActivity.this, AuthMainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(RegisterActivity.this, "createUser fail:" + e, Toast.LENGTH_SHORT).show();
                    }
                });

        } else {
            String countryCode = countryCodeEdit.getText().toString().trim();
            String phoneNumber = accountEdit.getText().toString().trim();
            String password = passwordEdit.getText().toString().trim();
            String verifyCode = verifyCodeEdit.getText().toString().trim();
            PhoneUser phoneUser = new PhoneUser.Builder()
                .setCountryCode(countryCode)
                .setPhoneNumber(phoneNumber)
                .setPassword(password)//optional
                .setVerifyCode(verifyCode)
                .build();
            AGConnectAuth.getInstance().createUser(phoneUser)
                .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                    @Override
                    public void onSuccess(SignInResult signInResult) {
                        startActivity(new Intent(RegisterActivity.this, AuthMainActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(RegisterActivity.this, "createUser fail:" + e, Toast.LENGTH_SHORT).show();
                    }
                });
        }
    }

    private void sendVerificationCode() {
        VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
            .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
            .sendInterval(30) //shortest send interval ，30-120s
            .locale(Locale.SIMPLIFIED_CHINESE) //optional,must contain country and language eg:zh_CN
            .build();
        if (type == LoginActivity.Type.EMAIL) {
            String email = accountEdit.getText().toString().trim();
            Task<VerifyCodeResult> task = EmailAuthProvider.requestVerifyCode(email, settings);
            task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                @Override
                public void onSuccess(VerifyCodeResult verifyCodeResult) {
                    //You need to get the verification code from your email
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(RegisterActivity.this, "requestVerifyCode fail:" + e, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            String countryCode = countryCodeEdit.getText().toString().trim();
            String phoneNumber = accountEdit.getText().toString().trim();
            Task<VerifyCodeResult> task = PhoneAuthProvider.requestVerifyCode(countryCode, phoneNumber, settings);
            task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                @Override
                public void onSuccess(VerifyCodeResult verifyCodeResult) {
                    //You need to get the verification code from your phone
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(RegisterActivity.this, "requestVerifyCode fail:" + e, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
