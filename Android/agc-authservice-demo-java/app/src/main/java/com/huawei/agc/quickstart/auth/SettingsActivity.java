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

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;

public class SettingsActivity extends AppCompatActivity {
    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        initView();
    }

    private void initView() {
        findViewById(R.id.layout_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AGConnectAuth.getInstance().deleteUser();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                SettingsActivity.this.finish();
            }
        });

        findViewById(R.id.layout_update_email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmail();
            }
        });

        findViewById(R.id.layout_update_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePhone();
            }
        });

        findViewById(R.id.layout_update_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AGConnectAuth.getInstance().getCurrentUser().updatePassword("new password", "verifyCode", AGConnectAuthCredential.Email_Provider);
                //or
                //AGConnectAuth.getInstance().getCurrentUser().updatePassword("new password", "verifyCode", AGConnectAuthCredential.Phone_Provider);
            }
        });

        findViewById(R.id.layout_reset_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AGConnectAuth.getInstance().resetPassword("email", "new password", "verifycode");
                //or
                //AGConnectAuth.getInstance().resetPassword("countryCode", "phoneNumber", "new password", "verifycode");
            }
        });
    }

    private void updatePhone() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_phone, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        EditText editCc = view.findViewById(R.id.et_country_code);
        EditText editAccount = view.findViewById(R.id.et_account);
        EditText editCode = view.findViewById(R.id.et_verify_code);
        Button send = view.findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cc = editCc.getText().toString();
                String phone = editAccount.getText().toString();
                VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                    .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                    .build();
                Task<VerifyCodeResult> task = PhoneAuthProvider.requestVerifyCode(cc, phone, settings);
                task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                    @Override
                    public void onSuccess(VerifyCodeResult verifyCodeResult) {
                    }
                });
            }
        });
        Button update = view.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cc = editCc.getText().toString();
                String phone = editAccount.getText().toString();
                String verifyCode = editCode.getText().toString();
                AGConnectAuth.getInstance().getCurrentUser().updatePhone(cc, phone, verifyCode)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "updatePhone success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "updatePhone fail" + e);
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void updateEmail() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_email, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();
        EditText editAccount = view.findViewById(R.id.et_account);
        EditText editCode = view.findViewById(R.id.et_verify_code);
        Button send = view.findViewById(R.id.btn_send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editAccount.getText().toString();
                VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                    .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                    .build();
                Task<VerifyCodeResult> task = EmailAuthProvider.requestVerifyCode(email, settings);
                task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                    @Override
                    public void onSuccess(VerifyCodeResult verifyCodeResult) {
                    }
                });
            }
        });
        Button update = view.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editAccount.getText().toString();
                String verifyCode = editCode.getText().toString();
                AGConnectAuth.getInstance().getCurrentUser().updateEmail(email, verifyCode)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "updateEmail success");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.d(TAG, "updateEmail fail" + e);
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}