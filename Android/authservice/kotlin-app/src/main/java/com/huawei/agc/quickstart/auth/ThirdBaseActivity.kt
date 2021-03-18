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
import android.widget.Button
import android.widget.Toast
import com.huawei.agconnect.auth.AGConnectAuth

abstract class ThirdBaseActivity constructor() : Activity() {
    protected var auth: AGConnectAuth? = null
    private var link: Boolean = false
    private var loginBtn: Button? = null
    private var linkBtn: Button? = null
    private var unlinkBtn: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_base)
        link = intent.getBooleanExtra("link", false)
        auth = AGConnectAuth.getInstance()
        loginBtn = findViewById(R.id.btn_third_login)
        loginBtn?.setOnClickListener { login() }
        linkBtn = findViewById(R.id.link)
        linkBtn?.setOnClickListener { link() }
        unlinkBtn = findViewById(R.id.unlink)
        unlinkBtn?.setOnClickListener { unlink() }
        if (link) {
            loginBtn?.visibility = View.INVISIBLE
        } else {
            linkBtn?.visibility = View.INVISIBLE
            unlinkBtn?.visibility = View.INVISIBLE
        }
    }

    abstract fun login()
    abstract fun link()
    abstract fun unlink()
    protected fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    protected fun loginSuccess() {
        startActivity(Intent(this, AuthMainActivity::class.java))
        finish()
    }
}