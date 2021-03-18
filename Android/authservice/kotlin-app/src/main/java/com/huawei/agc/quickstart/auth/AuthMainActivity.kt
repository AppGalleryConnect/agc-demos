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
import android.text.TextUtils
import android.view.*
import android.widget.*
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.ProfileRequest
import com.squareup.picasso.Picasso
import java.util.*

class AuthMainActivity : Activity(), View.OnClickListener {
    var layoutSingOut: LinearLayout? = null
    var imgViewPhoto: ImageView? = null
    var txtViewUid: TextView? = null
    var txtViewNickName: TextView? = null
    var txtViewEmail: TextView? = null
    var txtViewPhone: TextView? = null
    var btnSignOut: Button? = null
    var btnUpdateName: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_main)
        initView()
        showUserInfo()
    }

    private fun initView() {
        layoutSingOut = findViewById(R.id.layout_SingOut)
        btnSignOut = findViewById(R.id.btn_Signout)
        btnSignOut?.setOnClickListener(this)
        imgViewPhoto = findViewById(R.id.imgView_photo)
        imgViewPhoto?.setOnClickListener(this)
        txtViewUid = findViewById(R.id.txtView_uid)
        txtViewNickName = findViewById(R.id.txtView_NickName)
        txtViewEmail = findViewById(R.id.txtView_email)
        txtViewPhone = findViewById(R.id.txtView_phone)
        btnUpdateName = findViewById(R.id.btn_update_name)
        btnUpdateName?.setOnClickListener(this)
        findViewById<View>(R.id.btn_link).setOnClickListener(this)
        findViewById<View>(R.id.txtView_settings).setOnClickListener(this)
    }

    private fun showUserInfo() {
        val agConnectUser = AGConnectAuth.getInstance().currentUser
        val uid = agConnectUser.uid
        val nickname = agConnectUser.displayName
        val email = agConnectUser.email
        val phone = agConnectUser.phone
        val photoUrl = agConnectUser.photoUrl
        txtViewUid!!.text = uid
        txtViewNickName!!.text = nickname
        txtViewEmail!!.text = email
        txtViewPhone!!.text = phone
        if (!TextUtils.isEmpty(photoUrl)) {
            Picasso.get().load(photoUrl).into(imgViewPhoto)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_Signout -> if (AGConnectAuth.getInstance().currentUser != null) {
                // signOut
                AGConnectAuth.getInstance().signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            R.id.imgView_photo -> updateAvatar()
            R.id.btn_update_name -> updateDisplayName()
            R.id.btn_link -> link()
            R.id.txtView_settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun link() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_link, null, false)
        val dialog = AlertDialog.Builder(this).setView(view).create()
        val listView = view.findViewById<ListView>(R.id.list_view)
        val linkEntityList = initLink()
        val linkAdapter = LinkAdapter(dialog!!.context, R.layout.list_link_item, linkEntityList, object : LinkClickCallback {
            override fun click() {
                dialog?.dismiss()
            }
        })
        listView.adapter = linkAdapter
        dialog.show()
    }

    interface LinkClickCallback {
        fun click()
    }

    private fun initLink(): List<LinkEntity> {
        val linkEntityList: MutableList<LinkEntity> = ArrayList()
        linkEntityList.add(LinkEntity("Huawei ID", R.mipmap.huawei, HWIDActivity::class.java))
        linkEntityList.add(LinkEntity("Facebook ", R.mipmap.facebook, FacebookActivity::class.java))
        linkEntityList.add(LinkEntity("Twitter", R.mipmap.twitter, TwitterActivity::class.java))
        linkEntityList.add(LinkEntity("WeiXin", R.mipmap.wechat, WeiXinActivity::class.java))
        linkEntityList.add(LinkEntity("HWGame", R.mipmap.huawei, HWGameActivity::class.java))
        linkEntityList.add(LinkEntity("QQ", R.mipmap.qq, QQActivity::class.java))
        linkEntityList.add(LinkEntity("WeiBo", R.mipmap.weibo, WeiboActivity::class.java))
        linkEntityList.add(LinkEntity("Google ", R.mipmap.google_plus, GoogleActivity::class.java))
        linkEntityList.add(LinkEntity("GoogleGame", R.mipmap.google_plus, GooglePlayActivity::class.java))
        linkEntityList.add(LinkEntity("SelfBuild ", R.mipmap.huawei, SelfBuildActivity::class.java))
        linkEntityList.add(LinkEntity("Phone", R.mipmap.huawei, PhoneActivity::class.java))
        linkEntityList.add(LinkEntity("Email", R.mipmap.huawei, EmailActivity::class.java))
        return linkEntityList
    }

    private fun updateDisplayName() {
        update(object : UpdateCallback {
            override fun onUpdate(data: String?) {
                if (AGConnectAuth.getInstance().currentUser != null) {
                    // create a profileRequest
                    val userProfile = ProfileRequest.Builder()
                            .setDisplayName(data)
                            .build()
                    // update profile
                    AGConnectAuth.getInstance().currentUser.updateProfile(userProfile)
                            .addOnSuccessListener { showUserInfo() }
                            .addOnFailureListener { e -> Toast.makeText(this@AuthMainActivity, "update display name fail:$e", Toast.LENGTH_SHORT).show() }
                }
            }
        })
    }

    private fun updateAvatar() {
        update(object : UpdateCallback {
            override fun onUpdate(data: String?) {
                if (AGConnectAuth.getInstance().currentUser != null) {
                    // create a profileRequest
                    val userProfile = ProfileRequest.Builder()
                            .setPhotoUrl(data)
                            .build()
                    // update profile
                    AGConnectAuth.getInstance().currentUser.updateProfile(userProfile)
                            .addOnSuccessListener { showUserInfo() }
                            .addOnFailureListener { e -> Toast.makeText(this@AuthMainActivity, "update photo Url fail:$e", Toast.LENGTH_SHORT).show() }
                }
            }
        })
    }

    private fun update(callback: UpdateCallback) {
        val layout = LinearLayout(this@AuthMainActivity)
        layout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layout.orientation = LinearLayout.VERTICAL
        val editText = EditText(this@AuthMainActivity)
        layout.addView(editText)
        val dialogBuilder = AlertDialog.Builder(this@AuthMainActivity).setTitle("Update")
        dialogBuilder.setPositiveButton("UPDATE") { dialog, which -> // input photoUrl or display name
            val data = editText.text.toString().trim { it <= ' ' }
            if (!TextUtils.isEmpty(data)) {
                callback.onUpdate(data)
            }
        }
        dialogBuilder.setView(layout)
        dialogBuilder.show()
    }

    internal interface UpdateCallback {
        fun onUpdate(data: String?)
    }
}