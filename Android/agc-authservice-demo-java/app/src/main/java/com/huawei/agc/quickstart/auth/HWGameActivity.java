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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.HWGameAuthProvider;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.jos.JosApps;
import com.huawei.hms.jos.JosAppsClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;

public class HWGameActivity extends ThirdBaseActivity {
    private static final String TAG = HWGameActivity.class.getSimpleName();
    private static final int SIGN_CODE = 9901;
    private static final int LINK_CODE = 9902;
    private HuaweiIdAuthService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        HuaweiIdAuthParams authParams = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME)
            .createParams();
        service = HuaweiIdAuthManager.getService(this, authParams);
    }

    private void init() {
        JosAppsClient appsClient = JosApps.getJosAppsClient(this, null);
        appsClient.init();
    }

    @Override
    public void login() {
        startActivityForResult(service.getSignInIntent(), SIGN_CODE);
    }

    @Override
    public void link() {
        startActivityForResult(service.getSignInIntent(), LINK_CODE);
    }

    @Override
    public void unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.HWGame_Provider);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (SIGN_CODE == requestCode) {
            if (null == data) {
                Log.i("TAG", "signIn intent is null");
                return;
            }
            Task<AuthHuaweiId> task = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            task.addOnSuccessListener(authHuaWeiId -> currentPlayerInfo(requestCode, authHuaWeiId))
                .addOnFailureListener(e -> Log.e("TAG", "parseAuthResultFromIntent failed"));
        }
    }

    private void currentPlayerInfo(int requestCode, AuthHuaweiId authHuaweiId) {
        PlayersClient playersClient = Games.getPlayersClient(this, authHuaweiId);
        playersClient.getCurrentPlayer()
            .addOnSuccessListener(player -> {
                Log.i(TAG, "getPlayerInfo Success, player info: " + player.toString());
                String imageUrl = player.hasHiResImage() ? player.getHiResImageUri().toString()
                    : player.getIconImageUri().toString();
                AGConnectAuthCredential credential = new HWGameAuthProvider.Builder()
                    .setPlayerSign(player.getPlayerSign())
                    .setPlayerId(player.getPlayerId())
                    .setDisplayName(player.getDisplayName())
                    .setImageUrl(imageUrl)
                    .setPlayerLevel(player.getLevel())
                    .setSignTs(player.getSignTs())
                    .build();
                if (requestCode == SIGN_CODE) {
                    auth.signIn(credential)
                        .addOnSuccessListener(signInResult -> loginSuccess())
                        .addOnFailureListener(e -> showToast(e.getMessage()));
                }

                if (requestCode == LINK_CODE) {
                    if (auth.getCurrentUser() != null) {
                        auth.getCurrentUser().link(credential).addOnSuccessListener(signInResult -> {
                            showToast("link success");
                        }).addOnFailureListener(e -> {
                            showToast(e.getMessage());
                        });
                    }
                }

            })
            .addOnFailureListener(e -> {
                if (e instanceof ApiException) {
                    Log.e(TAG, "getPlayerInfo failed, status: " + ((ApiException) e).getStatusCode());
                }
            });
    }

}
