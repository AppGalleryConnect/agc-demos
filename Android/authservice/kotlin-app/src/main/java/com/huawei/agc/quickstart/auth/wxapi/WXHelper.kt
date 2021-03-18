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
package com.huawei.agc.quickstart.auth.wxapi

import android.content.Context
import com.huawei.agc.quickstart.auth.R
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.POST

object WXHelper {
    var api: IWXAPI? = null
        private set
    private var APP_ID: String? = null
    private var SECRET_ID: String? = null
    private var callback: Callback? = null
    fun setContext(context: Context) {
        APP_ID = context.getString(R.string.weixin_app_id)
        SECRET_ID = context.getString(R.string.weixin_secret_id)
        api = WXAPIFactory.createWXAPI(context, APP_ID, false)
        api?.registerApp(APP_ID)
    }

    fun signIn(callback: Callback?) {
        WXHelper.callback = callback
        val req = SendAuth.Req()
        req.scope = "snsapi_userinfo"
        req.state = "none"
        api!!.sendReq(req)
    }

    fun getToken(code: String?) {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.weixin.qq.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retrofit.create(WXTokenService::class.java).getToken(code, APP_ID, SECRET_ID).enqueue(object : retrofit2.Callback<TokenResponse?> {
            override fun onResponse(call: Call<TokenResponse?>?, response: Response<TokenResponse?>?) {
                if (response?.body() != null) {
                    callback!!.onSuccess(response.body()!!.access_token, response.body()!!.openid)
                } else {
                    callback!!.onFail()
                }
            }

            override fun onFailure(call: Call<TokenResponse?>?, t: Throwable?) {
                callback!!.onFail()
            }
        })
    }

    interface Callback {
        fun onSuccess(accessToken: String?, openId: String?)
        fun onFail()
    }

    interface WXTokenService {
        @POST("sns/oauth2/access_token")
        fun getToken(@Field("code") code: String?, @Field("appid") appid: String?, @Field("secret") secret: String?): Call<TokenResponse?>
    }

    class TokenResponse {
        var openid: String? = null
        var access_token: String? = null
    }
}