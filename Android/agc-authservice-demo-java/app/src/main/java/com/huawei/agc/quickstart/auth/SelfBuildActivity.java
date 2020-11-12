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

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.SelfBuildProvider;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SelfBuildActivity extends ThirdBaseActivity {
    private Retrofit retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrofit = new Retrofit.Builder().baseUrl("https://lfdswebdevapi01.hwcloudtest.cn:17062").build();
    }

    @Override
    public void login() {
        retrofit.create(JWTService.class)
            .getJwt("98341621547925918", "testId", "testDisplayName", "https://example.com/test.png")
            .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String responseString = null;
                    try {
                        ResponseBody body = response.body();
                        if (body != null) {
                            responseString = body.string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AGConnectAuthCredential credential = SelfBuildProvider.credentialWithToken(responseString);
                    auth.signIn(credential).addOnSuccessListener(signInResult -> {
                        loginSuccess();
                    }).addOnFailureListener(e -> {
                        showToast(e.getMessage());
                    });
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showToast(t.getMessage());
                }
            });
    }

    @Override
    public void link() {
        retrofit.create(JWTService.class)
            .getJwt("98341621547925918", "testId", "testDisplayName", "https://example.com/test.png")
            .enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String responseString = null;
                    try {
                        ResponseBody body = response.body();
                        if (body != null) {
                            responseString = body.string();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AGConnectAuthCredential credential = SelfBuildProvider.credentialWithToken(responseString);
                    auth.getCurrentUser().link(credential)
                        .addOnSuccessListener(signInResult -> {
                            showToast("link success");
                        }).addOnFailureListener(e -> {
                        showToast(e.getMessage());
                    });
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    showToast(t.getMessage());
                }
            });
    }

    @Override
    public void unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.SelfBuild_Provider);
        }
    }

    public interface JWTService {
        @GET("/agc/cpService/getCpJwt")
        Call<ResponseBody> getJwt(@Query("productId") String productId, @Query("uid") String uid,
            @Query("displayName") String displayName, @Query("photeUrl") String photoUrl);
    }
}
