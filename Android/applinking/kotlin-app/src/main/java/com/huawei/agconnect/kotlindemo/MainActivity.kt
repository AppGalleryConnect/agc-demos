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
package com.huawei.agconnect.kotlindemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.applinking.AGConnectAppLinking
import com.huawei.agconnect.applinking.AppLinking
import com.huawei.agconnect.applinking.AppLinking.AndroidLinkInfo
import com.huawei.agconnect.applinking.AppLinking.CampaignInfo
import com.huawei.agconnect.applinking.ResolvedLinkData
import com.huawei.agconnect.applinking.ShortAppLinking
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        private const val DOMAIN_URI_PREFIX = "https://example.drcn.agconnect.link"
        private const val DEEP_LINK = "agckit://example.agconnect.cn/detail?id=123"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        create.setOnClickListener { createAppLinking() }
        shareShort.setOnClickListener { shareLink(shortLinkText.text.toString()) }
        shareLong.setOnClickListener { shareLink(longLinkText.text.toString()) }
        AGConnectAppLinking.getInstance()
                .getAppLinking(this)
                .addOnSuccessListener { resolvedLinkData: ResolvedLinkData? ->
                    var deepLink: Uri? = null
                    if (resolvedLinkData != null) {
                        deepLink = resolvedLinkData.deepLink
                    }
                    val textView = findViewById<TextView>(R.id.deepLink)
                    textView.text = deepLink?.toString() ?: ""
                    if (deepLink != null) {
                        val path = deepLink.lastPathSegment
                        if ("detail" == path) {
                            val intent = Intent(baseContext, DetailActivity::class.java)
                            for (name in deepLink.queryParameterNames) {
                                intent.putExtra(name, deepLink.getQueryParameter(name))
                            }
                            startActivity(intent)
                        }
                        if ("setting" == path) {
                            val intent = Intent(baseContext, SettingActivity::class.java)
                            for (name in deepLink.queryParameterNames) {
                                intent.putExtra(name, deepLink.getQueryParameter(name))
                            }
                            startActivity(intent)
                        }
                    }
                }
                .addOnFailureListener { e: Exception? -> Log.w("MainActivity", "getAppLinking:onFailure", e) }
    }

    private fun createAppLinking() {
        val builder = AppLinking.Builder()
                .setUriPrefix(DOMAIN_URI_PREFIX)
                .setDeepLink(Uri.parse(DEEP_LINK))
                .setAndroidLinkInfo(AndroidLinkInfo.Builder().build())
                .setCampaignInfo(
                        CampaignInfo.Builder()
                                .setName("HDC")
                                .setSource("Huawei")
                                .setMedium("App")
                                .build())
        builder.buildShortAppLinking(ShortAppLinking.LENGTH.SHORT)
                .addOnSuccessListener { shortAppLinking: ShortAppLinking -> shortLinkText.text = shortAppLinking.shortUrl.toString() }
                .addOnFailureListener { e: Exception -> showError(e.message) }
        longLinkText.text = builder.buildAppLinking().uri.toString()
    }

    private fun shareLink(appLinking: String?) {
        if (appLinking != null) {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, appLinking)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun showError(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}