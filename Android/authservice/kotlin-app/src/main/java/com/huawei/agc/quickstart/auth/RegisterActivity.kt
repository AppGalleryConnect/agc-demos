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
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.huawei.agconnect.auth.*
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.TaskExecutors
import java.util.*

class RegisterActivity() : Activity(), View.OnClickListener {
    private var countryCodeEdit: EditText? = null
    private var accountEdit: EditText? = null
    private var passwordEdit: EditText? = null
    private var verifyCodeEdit: EditText? = null
    private var type: LoginActivity.Type? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        type = intent.getSerializableExtra("registerType") as LoginActivity.Type
        initView()
    }

    private fun initView() {
        val accountTv = findViewById<TextView>(R.id.tv_account)
        countryCodeEdit = findViewById(R.id.et_country_code)
        accountEdit = findViewById(R.id.et_account)
        passwordEdit = findViewById(R.id.et_password)
        verifyCodeEdit = findViewById(R.id.et_verify_code)
        val countryCodeLayout = findViewById<ViewGroup>(R.id.layout_cc)
        if (type == LoginActivity.Type.EMAIL) {
            accountTv.setText(R.string.email)
            countryCodeLayout.visibility = View.INVISIBLE
        } else {
            accountTv.setText(R.string.phone)
            countryCodeLayout.visibility = View.VISIBLE
        }
        val registerBtn = findViewById<Button>(R.id.btn_register)
        registerBtn.setOnClickListener(this)
        val send = findViewById<Button>(R.id.btn_send)
        send.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_register -> login()
            R.id.btn_send -> sendVerificationCode()
        }
    }

    private fun login() {
        if (type == LoginActivity.Type.EMAIL) {
            val email = accountEdit!!.text.toString().trim { it <= ' ' }
            val password = passwordEdit!!.text.toString().trim { it <= ' ' }
            val verifyCode = verifyCodeEdit!!.text.toString().trim { it <= ' ' }
            // build email user
            val emailUser = EmailUser.Builder()
                    .setEmail(email)
                    .setPassword(password) //optional,if you set a password, you can log in directly using the password next time.
                    .setVerifyCode(verifyCode)
                    .build()
            // create email user
            AGConnectAuth.getInstance().createUser(emailUser)
                    .addOnSuccessListener(OnSuccessListener { // After a user is created, the user has logged in by default.
                        startActivity(Intent(this@RegisterActivity, AuthMainActivity::class.java))
                    })
                    .addOnFailureListener(object : OnFailureListener {
                        override fun onFailure(e: Exception) {
                            Toast.makeText(this@RegisterActivity, "createUser fail:$e", Toast.LENGTH_SHORT).show()
                        }
                    })
        } else {
            val countryCode = countryCodeEdit!!.text.toString().trim { it <= ' ' }
            val phoneNumber = accountEdit!!.text.toString().trim { it <= ' ' }
            val password = passwordEdit!!.text.toString().trim { it <= ' ' }
            val verifyCode = verifyCodeEdit!!.text.toString().trim { it <= ' ' }
            // build phone user
            val phoneUser = PhoneUser.Builder()
                    .setCountryCode(countryCode)
                    .setPhoneNumber(phoneNumber)
                    .setPassword(password) //optional
                    .setVerifyCode(verifyCode)
                    .build()
            // create phone user
            AGConnectAuth.getInstance().createUser(phoneUser)
                    .addOnSuccessListener(object : OnSuccessListener<SignInResult?> {
                        override fun onSuccess(signInResult: SignInResult?) {
                            // After a user is created, the user has logged in by default.
                            startActivity(Intent(this@RegisterActivity, AuthMainActivity::class.java))
                        }
                    })
                    .addOnFailureListener(object : OnFailureListener {
                        override fun onFailure(e: Exception) {
                            Toast.makeText(this@RegisterActivity, "createUser fail:$e", Toast.LENGTH_SHORT).show()
                        }
                    })
        }
    }

    private fun sendVerificationCode() {
        val settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN) // action type
                .sendInterval(30) //shortest send interval ，30-120s
                .locale(Locale.SIMPLIFIED_CHINESE) //optional,must contain country and language eg:zh_CN
                .build()
        if (type == LoginActivity.Type.EMAIL) {
            val email = accountEdit!!.text.toString().trim { it <= ' ' }
            // apply for a verification code by email, indicating that the email is owned by you.
            val task = AGConnectAuth.getInstance().requestVerifyCode(email, settings)
            task.addOnSuccessListener(TaskExecutors.uiThread(), object : OnSuccessListener<VerifyCodeResult?> {
                override fun onSuccess(verifyCodeResult: VerifyCodeResult?) {
                    Toast.makeText(this@RegisterActivity, "send email verify code success", Toast.LENGTH_SHORT).show()
                    //You need to get the verification code from your email
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    Toast.makeText(this@RegisterActivity, "requestVerifyCode fail:$e", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            val countryCode = countryCodeEdit!!.text.toString().trim { it <= ' ' }
            val phoneNumber = accountEdit!!.text.toString().trim { it <= ' ' }
            // apply for a verification code by phone, indicating that the phone is owned by you.
            val task = AGConnectAuth.getInstance().requestVerifyCode(countryCode, phoneNumber, settings)
            task.addOnSuccessListener(TaskExecutors.uiThread(), object : OnSuccessListener<VerifyCodeResult?> {
                override fun onSuccess(verifyCodeResult: VerifyCodeResult?) {
                    Toast.makeText(this@RegisterActivity, "send phone verify code success", Toast.LENGTH_SHORT).show()
                    //You need to get the verification code from your phone
                }
            }).addOnFailureListener(TaskExecutors.uiThread(), object : OnFailureListener {
                override fun onFailure(e: Exception) {
                    Toast.makeText(this@RegisterActivity, "requestVerifyCode fail:$e", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}