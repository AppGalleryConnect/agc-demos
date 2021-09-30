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

package com.example.applinkingdemo.slice;

import com.example.applinkingdemo.ResourceTable;
import com.huawei.agconnect.applinking.AGConnectAppLinking;
import com.huawei.agconnect.applinking.AppLinking;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.Button;
import ohos.agp.components.Text;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

public class MainAbilitySlice extends AbilitySlice {
    private static final HiLogLabel LABEL = new HiLogLabel(HiLog.LOG_APP, 0x0001,"AppLinking");
    private static final String DOMAIN_URI_PREFIX = "https://example.drcn.agconnect.link";
    private Text text;
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);

        AGConnectAppLinking.getInstance().getAppLinking(getAbility()).addOnSuccessListener(resolvedLinkData -> {
            HiLog.info(LABEL, resolvedLinkData.getDeepLink());
        }).addOnFailureListener(e -> {
            HiLog.error(LABEL, e.toString());
        });

        text = (Text) findComponentById(ResourceTable.Id_text_linking);
        Button createShort = (Button) findComponentById(ResourceTable.Id_button_createshort);
        createShort.setClickedListener(component -> {
            createShortLink();
        });
        Button createLong = (Button) findComponentById(ResourceTable.Id_button_createlong);
        createLong.setClickedListener(component -> {
            createLongLink();
        });
    }

    @Override
    public void onActive() {
        super.onActive();
    }

    @Override
    public void onForeground(Intent intent) {
        super.onForeground(intent);
    }

    private void createShortLink() {
        AppLinking.Builder builder = AppLinking.newBuilder()
                .setUriPrefix(DOMAIN_URI_PREFIX)
                .setDeepLink("https://www.example.com")
                .setHarmonyLinkInfo(AppLinking.HarmonyLinkInfo.newBuilder()
                        .setHarmonyDeepLink("agckit://example.com?appId=C5373&channelId=123412")
                        .build())
                .setCampaignInfo(AppLinking.CampaignInfo.newBuilder()
                        .setName("xinnian")
                        .setSource("huawei")
                        .setMedium("pic").build())
                .setIsShowPreview(true)
                .setPreviewType(AppLinking.LinkingPreviewType.AppInfo);
        builder.buildShortAppLinking().addOnSuccessListener(shortAppLinking -> {
            text.setText(shortAppLinking.getShortUrl());
        }).addOnFailureListener(e -> {
            HiLog.error(LABEL, e.toString());
        });
    }

    private void createLongLink() {
        AppLinking appLinking = AppLinking.newBuilder()
                .setUriPrefix(DOMAIN_URI_PREFIX)
                .setDeepLink("https://www.example.com")
                .setHarmonyLinkInfo(AppLinking.HarmonyLinkInfo.newBuilder()
                        .setHarmonyDeepLink("agckit://example.com?appId=C5373&channelId=123412")
                        .build())
                .setCampaignInfo(AppLinking.CampaignInfo.newBuilder()
                        .setName("xinnian")
                        .setSource("huawei")
                        .setMedium("pic").build())
                .setIsShowPreview(true)
                .setPreviewType(AppLinking.LinkingPreviewType.AppInfo)
                .buildAppLinking();
        text.setText(appLinking.getUri());
    }
}
