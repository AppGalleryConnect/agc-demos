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
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.jos.JosApps;
import com.huawei.hms.jos.JosAppsClient;
import com.huawei.hms.jos.games.Games;
import com.huawei.hms.jos.games.PlayersClient;
import com.huawei.hms.jos.games.player.Player;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class HWGameActivity extends ThirdBaseActivity {
    private static final String TAG = HWGameActivity.class.getSimpleName();
    private static final int SIGN_CODE = 9901;
    private static final int LINK_CODE = 9902;
    private AccountAuthService accountAuthService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        AccountAuthParams authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM_GAME)
            .createParams();
        accountAuthService = AccountAuthManager.getService(HWGameActivity.this, authParams);
    }

    /**
     * init JosAppsClient
     */
    private void init() {
        JosAppsClient appsClient = JosApps.getJosAppsClient(this);
        appsClient.init();
    }

    @Override
    public void login() {
        startActivityForResult(accountAuthService.getSignInIntent(), SIGN_CODE);
    }

    @Override
    public void link() {
        startActivityForResult(accountAuthService.getSignInIntent(), LINK_CODE);
    }

    @Override
    public void unlink() {
        if (AGConnectAuth.getInstance().getCurrentUser() != null) {
            // unlink HW game
            AGConnectAuth.getInstance().getCurrentUser().unlink(AGConnectAuthCredential.HWGame_Provider);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null == data) {
            Log.i("TAG", "signIn intent is null");
            return;
        }
        Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
        authAccountTask
            .addOnSuccessListener(authAccount -> HWGameActivity.this.currentPlayerInfo(requestCode))
            .addOnFailureListener(e -> Log.e("TAG", "parseAuthResultFromIntent failed"));
    }

    private void currentPlayerInfo(int requestCode) {
        PlayersClient client = Games.getPlayersClient(this);
        //get player info
        Task<Player> task = client.getCurrentPlayer();
        task.addOnSuccessListener(player -> {
            Log.i(TAG, "getPlayerInfo Success, player info: " + player.toString());
            String imageUrl = null;
            if (player.hasHiResImage()) {
                imageUrl = player.getHiResImageUri().toString();
            } else if (player.hasIconImage()) {
                imageUrl = player.getIconImageUri().toString();
            }
            // create hw game credential
            AGConnectAuthCredential credential = new HWGameAuthProvider.Builder()
                .setPlayerSign(player.getPlayerSign())
                .setPlayerId(player.getPlayerId())
                .setDisplayName(player.getDisplayName())
                .setImageUrl(imageUrl)
                .setPlayerLevel(player.getLevel())
                .setSignTs(player.getSignTs())
                .build();
            if (requestCode == SIGN_CODE) {
                // sign in
                auth.signIn(credential)
                    .addOnSuccessListener(signInResult -> HWGameActivity.this.loginSuccess())
                    .addOnFailureListener(e -> {
                        HWGameActivity.this.showToast(e.getMessage());
                    });
            }

            if (requestCode == LINK_CODE) {
                if (auth.getCurrentUser() != null) {
                    // link
                    auth.getCurrentUser()
                        .link(credential)
                        .addOnSuccessListener(signInResult -> {
                            HWGameActivity.this.showToast("link success");
                        }).addOnFailureListener(e -> {
                        HWGameActivity.this.showToast(e.getMessage());
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                if (e instanceof ApiException) {
                    Log.e(TAG, "getPlayerInfo failed, status: " + ((ApiException) e).getStatusCode());
                }
            }
        });
    }

}
