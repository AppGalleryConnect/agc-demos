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
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.huawei.agconnect.auth.*
import com.huawei.hmf.tasks.OnFailureListener
import com.huawei.hmf.tasks.OnSuccessListener
import com.huawei.hmf.tasks.TaskExecutors
import java.util.*

class LoginActivity : Activity(), View.OnClickListener {
    private var accountTv: TextView? = null
    private var countryCodeEdit: EditText? = null
    private var accountEdit: EditText? = null
    private var passwordEdit: EditText? = null
    private var verifyCodeEdit: EditText? = null
    private var countryCodeLayout: ViewGroup? = null
    private var galleryLayout: LinearLayout? = null
    private var type = Type.EMAIL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (AGConnectAuth.getInstance().currentUser != null) {
            startActivity(Intent(this, AuthMainActivity::class.java))
            finish()
        }
        initView()
    }

    private fun initView() {
        accountTv = findViewById(R.id.tv_account)
        countryCodeEdit = findViewById(R.id.et_country_code)
        accountEdit = findViewById(R.id.et_account)
        passwordEdit = findViewById(R.id.et_password)
        verifyCodeEdit = findViewById(R.id.et_verify_code)
        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radiobutton_email -> type = Type.EMAIL
                R.id.radiobutton_phone -> type = Type.PHONE
            }
            updateView()
        }
        countryCodeLayout = findViewById(R.id.layout_cc)
        val loginBtn = findViewById<Button>(R.id.btn_login)
        loginBtn.setOnClickListener(this)
        val registerBtn = findViewById<Button>(R.id.btn_register)
        registerBtn.setOnClickListener(this)
        val send = findViewById<Button>(R.id.btn_send)
        send.setOnClickListener(this)
        galleryLayout = findViewById(R.id.id_gallery)
        findViewById<View>(R.id.btn_login_anonymous).setOnClickListener(this)
        initThird()
    }

    private fun initThird() {
        val ids = resources.getIntArray(R.array.ids)
        val typedArray = resources.obtainTypedArray(R.array.ids) //获得任意类型
        val names = resources.getStringArray(R.array.names)
        val inflater = LayoutInflater.from(this)
        for (i in ids.indices) {
            val view = inflater.inflate(R.layout.layout_imge, galleryLayout, false)
            val img = view.findViewById<ImageView>(R.id.image)
            img.setImageResource(typedArray.getResourceId(i, R.mipmap.huawei))
            val text = view.findViewById<TextView>(R.id.text)
            text.text = names[i]
            view.setOnClickListener(ThirdListener(names[i]))
            galleryLayout!!.addView(view)
        }
    }

    inner class ThirdListener internal constructor(private val name: String) : View.OnClickListener {
        override fun onClick(v: View) {
            var intent: Intent? = null
            when (name) {
                "Huawei ID" -> intent = Intent(this@LoginActivity, HWIDActivity::class.java)
                "Huawei Game" -> intent = Intent(this@LoginActivity, HWGameActivity::class.java)
                "Facebook" -> intent = Intent(this@LoginActivity, FacebookActivity::class.java)
                "Twitter" -> intent = Intent(this@LoginActivity, TwitterActivity::class.java)
                "Google" -> intent = Intent(this@LoginActivity, GoogleActivity::class.java)
                "Google Play" -> intent = Intent(this@LoginActivity, GooglePlayActivity::class.java)
                "WeChat" -> intent = Intent(this@LoginActivity, WeiXinActivity::class.java)
                "Weibo" -> intent = Intent(this@LoginActivity, WeiboActivity::class.java)
                "QQ" -> intent = Intent(this@LoginActivity, QQActivity::class.java)
                "Self Build" -> intent = Intent(this@LoginActivity, SelfBuildActivity::class.java)
            }
            intent?.let { startActivity(it) }
        }

    }

    private fun updateView() {
        if (type == Type.EMAIL) {
            accountTv!!.setText(R.string.email)
            countryCodeLayout!!.visibility = View.GONE
        } else {
            accountTv!!.setText(R.string.phone)
            countryCodeLayout!!.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_login -> login()
            R.id.btn_register -> {
                val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                intent.putExtra("registerType", type)
                startActivity(intent)
            }
            R.id.btn_send -> sendVerificationCode()
            R.id.btn_login_anonymous -> loginAnonymous()
        }
    }

    private fun sendVerificationCode() {
        val settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30) //shortest send interval ，30-120s
                .locale(Locale.SIMPLIFIED_CHINESE) //optional,must contain country and language eg:zh_CN
                .build()
        if (type == Type.EMAIL) {
            val email = accountEdit!!.text.toString().trim { it <= ' ' }
            val task = AGConnectAuth.getInstance().requestVerifyCode(email, settings)
            task.addOnSuccessListener(TaskExecutors.uiThread(), OnSuccessListener {
                Toast.makeText(this@LoginActivity, "send email verify code success", Toast.LENGTH_SHORT).show()
                //You need to get the verification code from your email
            }).addOnFailureListener(TaskExecutors.uiThread(), OnFailureListener { e -> Toast.makeText(this@LoginActivity, "requestVerifyCode fail:$e", Toast.LENGTH_SHORT).show() })
        } else {
            val countryCode = countryCodeEdit!!.text.toString().trim { it <= ' ' }
            val phoneNumber = accountEdit!!.text.toString().trim { it <= ' ' }
            val task = AGConnectAuth.getInstance().requestVerifyCode(countryCode, phoneNumber, settings)
            task.addOnSuccessListener(TaskExecutors.uiThread(), OnSuccessListener {
                Toast.makeText(this@LoginActivity, "send phone verify code success", Toast.LENGTH_SHORT).show()
                //You need to get the verification code from your phone
            }).addOnFailureListener(TaskExecutors.uiThread(), OnFailureListener { e -> Toast.makeText(this@LoginActivity, "requestVerifyCode fail:$e", Toast.LENGTH_SHORT).show() })
        }
    }

    private fun login() {
        if (type == Type.EMAIL) {
            val email = accountEdit!!.text.toString().trim { it <= ' ' }
            val password = passwordEdit!!.text.toString().trim { it <= ' ' }
            val verifyCode = verifyCodeEdit!!.text.toString().trim { it <= ' ' }
            val credential: AGConnectAuthCredential
            credential = if (TextUtils.isEmpty(verifyCode)) {
                EmailAuthProvider.credentialWithPassword(email, password)
            } else {
                //If you do not have a password, param password can be null
                EmailAuthProvider.credentialWithVerifyCode(email, password, verifyCode)
            }
            signIn(credential)
        } else {
            val countryCode = countryCodeEdit!!.text.toString().trim { it <= ' ' }
            val phoneNumber = accountEdit!!.text.toString().trim { it <= ' ' }
            val password = passwordEdit!!.text.toString().trim { it <= ' ' }
            val verifyCode = verifyCodeEdit!!.text.toString().trim { it <= ' ' }
            val credential: AGConnectAuthCredential
            credential = if (TextUtils.isEmpty(verifyCode)) {
                PhoneAuthProvider.credentialWithPassword(countryCode, phoneNumber, password)
            } else {
                //If you do not have a password, param password can be null
                PhoneAuthProvider.credentialWithVerifyCode(countryCode, phoneNumber, password, verifyCode)
            }
            signIn(credential)
        }
    }

    private fun signIn(credential: AGConnectAuthCredential) {
        AGConnectAuth.getInstance().signIn(credential)
                .addOnSuccessListener {
                    startActivity(Intent(this@LoginActivity, AuthMainActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e -> Toast.makeText(this@LoginActivity, "signIn fail:$e", Toast.LENGTH_SHORT).show() }
    }

    private fun loginAnonymous() {
        // signIn anonymously
        AGConnectAuth.getInstance().signInAnonymously()
                .addOnSuccessListener {
                    startActivity(Intent(this@LoginActivity, AuthMainActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e -> Toast.makeText(this@LoginActivity, "login Anonymous fail:$e", Toast.LENGTH_SHORT).show() }
    }

    internal enum class Type {
        EMAIL, PHONE
    }
}