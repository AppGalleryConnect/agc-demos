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

package com.huawei.agc.quickstart.auth.wxapi;

import android.content.Context;

import androidx.annotation.Nullable;

import com.huawei.agc.quickstart.auth.R;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Field;
import retrofit2.http.POST;

public class WXHelper {
    public interface Callback {
        void onSuccess(String accessToken, String openId);
        void onFail();
    }

    private static IWXAPI api;
    private static String APP_ID;
    private static String SECRET_ID;
    private static Callback callback;

    public static void setContext(Context context) {
        APP_ID = context.getString(R.string.weixin_app_id);
        SECRET_ID = context.getString(R.string.weixin_secret_id);
        api = WXAPIFactory.createWXAPI(context, APP_ID, false);
        api.registerApp(APP_ID);
    }

    public static IWXAPI getApi() {
        return api;
    }

    public static void signIn(Callback callback) {
        WXHelper.callback = callback;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "none";
        api.sendReq(req);
    }

    public static void getToken(String code) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.weixin.qq.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        retrofit.create(WXTokenService.class).getToken(code, APP_ID, SECRET_ID).enqueue(new retrofit2.Callback<TokenResponse>() {
            @Override
            public void onResponse(@Nullable Call<TokenResponse> call, @Nullable Response<TokenResponse> response) {
                if (response != null && response.body() != null) {
                    callback.onSuccess(response.body().access_token, response.body().openid);
                } else {
                    callback.onFail();
                }
            }

            @Override
            public void onFailure(@Nullable Call<TokenResponse> call, @Nullable Throwable t) {
                callback.onFail();
            }
        });
    }

    public interface WXTokenService {
        @POST("sns/oauth2/access_token")
        Call<TokenResponse> getToken(@Field("code") String code, @Field("appid") String appid, @Field("secret") String secret);
    }

    public class TokenResponse {
        public String openid;
        public String access_token;
    }
}
