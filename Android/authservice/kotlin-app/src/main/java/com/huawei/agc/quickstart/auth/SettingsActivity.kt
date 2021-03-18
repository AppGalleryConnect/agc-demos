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
package com.huawei.agc.quickstart.auth

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.VerifyCodeResult
import com.huawei.agconnect.auth.VerifyCodeSettings
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.Task
import com.huawei.hmf.tasks.TaskExecutors

class SettingsActivity constructor() : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        initView()
    }

    private fun initView() {
        findViewById<View>(R.id.layout_delete).setOnClickListener { // delete user
            AGConnectAuth.getInstance().deleteUser()
                    .addOnSuccessListener(TaskExecutors.uiThread(), OnSuccessListener<Void?> {
                        startActivity(Intent(this@SettingsActivity, LoginActivity::class.java))
                        finish()
                    }).addOnFailureListener(TaskExecutors.uiThread(), OnFailureListener {
                        e -> Toast.makeText(this@SettingsActivity, "delete user fail:" + e.message, Toast.LENGTH_SHORT).show() })
        }
        findViewById<View>(R.id.layout_update_email).setOnClickListener { updateEmail() }
        findViewById<View>(R.id.layout_update_phone).setOnClickListener { updatePhone() }
        findViewById<View>(R.id.layout_update_password).setOnClickListener {
            // update password
            if (AGConnectAuth.getInstance().currentUser != null) {
                AGConnectAuth.getInstance().currentUser.updatePassword("new password", "verify code", AGConnectAuthCredential.Email_Provider)
                //or
                //AGConnectAuth.getInstance().getCurrentUser().updatePassword("new password", "verify code", AGConnectAuthCredential.Phone_Provider);
            }
        }
        findViewById<View>(R.id.layout_reset_password).setOnClickListener {
            // reset password
            AGConnectAuth.getInstance().resetPassword("email", "new password", "verify code")
            //or
            //AGConnectAuth.getInstance().resetPassword("countryCode", "phoneNumber", "new password", "verifycode");
        }
    }

    private fun updatePhone() {
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_phone, null, false)
        val dialog: AlertDialog = AlertDialog.Builder(this).setView(view).create()
        val editCc: EditText = view.findViewById(R.id.et_country_code)
        val editAccount: EditText = view.findViewById(R.id.et_account)
        val editCode: EditText = view.findViewById(R.id.et_verify_code)
        val send: Button = view.findViewById(R.id.btn_send)
        send.setOnClickListener {
            val countryCode: String = editCc.text.toString()
            val phoneNumber: String = editAccount.text.toString()
            //build a verify code settings
            val settings: VerifyCodeSettings = VerifyCodeSettings.newBuilder()
                    .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                    .build()
            // request verify code,and waiting for the verification code to be sent to your mobile phone
            val task: Task<VerifyCodeResult> = AGConnectAuth.getInstance().requestVerifyCode(countryCode, phoneNumber, settings)
            task.addOnSuccessListener(TaskExecutors.uiThread(), object : OnSuccessListener<VerifyCodeResult?> {
                public override fun onSuccess(verifyCodeResult: VerifyCodeResult?) {
                    Toast.makeText(this@SettingsActivity, "request verify code success", Toast.LENGTH_SHORT).show()
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), object : OnFailureListener {
                public override fun onFailure(e: Exception) {
                    Toast.makeText(this@SettingsActivity, "request verify code fail:" + e.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
        val update: Button = view.findViewById(R.id.update)
        update.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val countryCode: String = editCc.getText().toString()
                val phoneNumber: String = editAccount.getText().toString()
                val verifyCode: String = editCode.getText().toString()
                if (AGConnectAuth.getInstance().getCurrentUser() != null) {
                    // update phone
                    AGConnectAuth.getInstance().getCurrentUser().updatePhone(countryCode, phoneNumber, verifyCode)
                            .addOnSuccessListener(TaskExecutors.uiThread(), OnSuccessListener<Void?> {
                                Log.d(TAG, "updatePhone success")
                                Toast.makeText(this@SettingsActivity, "updatePhone success", Toast.LENGTH_SHORT).show()
                            }).addOnFailureListener(TaskExecutors.uiThread(), OnFailureListener { e -> Toast.makeText(this@SettingsActivity, "updatePhone fail:" + e.message, Toast.LENGTH_SHORT).show() })
                }
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    private fun updateEmail() {
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_email, null, false)
        val dialog: AlertDialog = AlertDialog.Builder(this).setView(view).create()
        val editAccount: EditText = view.findViewById(R.id.et_account)
        val editCode: EditText = view.findViewById(R.id.et_verify_code)
        val send: Button = view.findViewById(R.id.btn_send)
        send.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                //build a verify code settings
                val email: String = editAccount.getText().toString()
                val settings: VerifyCodeSettings = VerifyCodeSettings.newBuilder()
                        .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                        .build()
                val task: Task<VerifyCodeResult> = AGConnectAuth.getInstance().requestVerifyCode(email, settings)
                task.addOnSuccessListener(TaskExecutors.uiThread(), object : OnSuccessListener<VerifyCodeResult?> {
                    public override fun onSuccess(verifyCodeResult: VerifyCodeResult?) {
                        Toast.makeText(this@SettingsActivity, "request verify code success", Toast.LENGTH_SHORT).show()
                    }
                }).addOnFailureListener(TaskExecutors.uiThread(), object : OnFailureListener {
                    public override fun onFailure(e: Exception) {
                        Toast.makeText(this@SettingsActivity, "request verify code fail:" + e.message, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        })
        val update: Button = view.findViewById(R.id.update)
        update.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val email: String = editAccount.getText().toString()
                val verifyCode: String = editCode.getText().toString()
                if (AGConnectAuth.getInstance().getCurrentUser() != null) {
                    AGConnectAuth.getInstance().getCurrentUser().updateEmail(email, verifyCode)
                            .addOnSuccessListener(TaskExecutors.uiThread(), object : OnSuccessListener<Void?> {
                                public override fun onSuccess(aVoid: Void?) {
                                    Toast.makeText(this@SettingsActivity, "updateEmail success", Toast.LENGTH_SHORT).show()
                                }
                            }).addOnFailureListener(TaskExecutors.uiThread(), object : OnFailureListener {
                                public override fun onFailure(e: Exception) {
                                    Toast.makeText(this@SettingsActivity, "updateEmail fail:" + e.message, Toast.LENGTH_SHORT).show()
                                }
                            })
                }
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    companion object {
        private val TAG: String = SettingsActivity::class.java.getSimpleName()
    }
}