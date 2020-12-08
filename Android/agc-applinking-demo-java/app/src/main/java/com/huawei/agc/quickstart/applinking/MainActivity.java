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

package com.huawei.agc.quickstart.applinking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.applinking.AGConnectAppLinking;
import com.huawei.agconnect.applinking.AppLinking;
import com.huawei.agconnect.applinking.ShortAppLinking;
import com.huawei.hmf.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {
    private TextView shortTextView;
    private TextView longTextView;

    private static final String DOMAIN_URI_PREFIX = "https://example.drcn.agconnect.link";
    private static final String DEEP_LINK = "agckit://example.agconnect.cn/detail?id=123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shortTextView = findViewById(R.id.shortLinkText);
        longTextView = findViewById(R.id.longLinkText);

        findViewById(R.id.create)
                .setOnClickListener(
                        view -> {
                            createAppLinking();
                        });

        findViewById(R.id.shareShort)
                .setOnClickListener(
                        view -> {
                            shareLink((String) shortTextView.getText());
                        });

        findViewById(R.id.shareLong)
                .setOnClickListener(
                        view -> {
                            shareLink((String) longTextView.getText());
                        });

        AGConnectAppLinking.getInstance()
                .getAppLinking(this)
                .addOnSuccessListener(
                        resolvedLinkData -> {
                            Uri deepLink = null;
                            if (resolvedLinkData != null) {
                                deepLink = resolvedLinkData.getDeepLink();
                            }

                            TextView textView = findViewById(R.id.deepLink);
                            textView.setText(deepLink != null ? deepLink.toString() : "");

                            if (deepLink != null) {
                                String path = deepLink.getLastPathSegment();
                                if ("detail".equals(path)) {
                                    Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                                    for (String name : deepLink.getQueryParameterNames()) {
                                        intent.putExtra(name, deepLink.getQueryParameter(name));
                                    }
                                    startActivity(intent);
                                }
                                if ("setting".equals(path)) {
                                    Intent intent = new Intent(getBaseContext(), SettingActivity.class);
                                    for (String name : deepLink.getQueryParameterNames()) {
                                        intent.putExtra(name, deepLink.getQueryParameter(name));
                                    }
                                    startActivity(intent);
                                }
                            }
                        })
                .addOnFailureListener(
                        e -> {
                            Log.w("MainActivity", "getAppLinking:onFailure", e);
                        });
    }

    private void createAppLinking() {
        AppLinking.Builder builder =
                new AppLinking.Builder()
                        .setUriPrefix(DOMAIN_URI_PREFIX)
                        .setDeepLink(Uri.parse(DEEP_LINK))
                        .setAndroidLinkInfo(new AppLinking.AndroidLinkInfo.Builder().build())
                        .setCampaignInfo(
                                new AppLinking.CampaignInfo.Builder()
                                        .setName("HDC")
                                        .setSource("Huawei")
                                        .setMedium("App")
                                        .build());
        builder.buildShortAppLinking(ShortAppLinking.LENGTH.SHORT)
                .addOnSuccessListener(shortAppLinking -> {
                    shortTextView.setText(shortAppLinking.getShortUrl().toString());
                })
                .addOnFailureListener(
                        e -> {
                            showError(e.getMessage());
                        });

        longTextView.setText(builder.buildAppLinking().getUri().toString());
    }

    private void shareLink(String appLinking) {
        if (appLinking != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, appLinking);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
