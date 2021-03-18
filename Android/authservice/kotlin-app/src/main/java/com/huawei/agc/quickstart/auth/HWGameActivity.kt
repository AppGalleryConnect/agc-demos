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

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.huawei.agconnect.auth.AGConnectAuth
import com.huawei.agconnect.auth.AGConnectAuthCredential
import com.huawei.agconnect.auth.HWGameAuthProvider
import com.huawei.agconnect.auth.SignInResult
import com.huawei.hms.common.ApiException
import com.huawei.hms.jos.JosApps
import com.huawei.hms.jos.games.Games
import com.huawei.hms.support.hwid.HuaweiIdAuthManager
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper
import com.huawei.hms.support.hwid.result.AuthHuaweiId
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService

class HWGameActivity : ThirdBaseActivity() {
    private var service: HuaweiIdAuthService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        val authParams = HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME)
                .createParams()
        service = HuaweiIdAuthManager.getService(this, authParams)
    }

    private fun init() {
        val appsClient = JosApps.getJosAppsClient(this, null)
        appsClient.init()
    }

    override fun login() {
        startActivityForResult(service!!.signInIntent, SIGN_CODE)
    }

    override fun link() {
        startActivityForResult(service!!.signInIntent, LINK_CODE)
    }

    override fun unlink() {
        if (AGConnectAuth.getInstance().currentUser != null) {
            // unlink HW game
            AGConnectAuth.getInstance().currentUser.unlink(AGConnectAuthCredential.HWGame_Provider)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (SIGN_CODE == requestCode) {
            if (null == data) {
                Log.i("TAG", "signIn intent is null")
                return
            }
            val task = HuaweiIdAuthManager.parseAuthResultFromIntent(data)
            task.addOnSuccessListener { authHuaWeiId -> currentPlayerInfo(requestCode, authHuaWeiId) }.addOnFailureListener { Log.e("TAG", "parseAuthResultFromIntent failed") }
        }
    }

    private fun currentPlayerInfo(requestCode: Int, authHuaweiId: AuthHuaweiId) {
        val playersClient = Games.getPlayersClient(this, authHuaweiId)
        playersClient.currentPlayer
                .addOnSuccessListener { player ->
                    Log.i(TAG, "getPlayerInfo Success, player info: $player")
                    var imageUrl : String? = null
                    if (player.hasHiResImage()) {
                        imageUrl = player.hiResImageUri.toString()
                    }else if (player.hasIconImage()) {
                        imageUrl = player.iconImageUri.toString()
                    }
                    // create hw game credential
                    val credential = HWGameAuthProvider.Builder()
                            .setPlayerSign(player.playerSign)
                            .setPlayerId(player.playerId)
                            .setDisplayName(player.displayName)
                            .setImageUrl(imageUrl)
                            .setPlayerLevel(player.level)
                            .setSignTs(player.signTs)
                            .build()
                    if (requestCode == SIGN_CODE) {
                        // sign in
                        auth!!.signIn(credential)
                                .addOnSuccessListener { signInResult: SignInResult? -> loginSuccess() }
                                .addOnFailureListener { e: Exception -> showToast(e.message) }
                    }
                    if (requestCode == LINK_CODE) {
                        if (auth!!.currentUser != null) {
                            // link
                            auth!!.currentUser
                                    .link(credential)
                                    .addOnSuccessListener { signInResult: SignInResult? -> showToast("link success") }.addOnFailureListener { e: Exception -> showToast(e.message) }
                        }
                    }
                }.addOnFailureListener { e ->
                    if (e is ApiException) {
                        Log.e(TAG, "getPlayerInfo failed, status: " + e.statusCode)
                    }
                }
    }

    companion object {
        private val TAG = HWGameActivity::class.java.simpleName
        private const val SIGN_CODE = 9901
        private const val LINK_CODE = 9902
    }
}