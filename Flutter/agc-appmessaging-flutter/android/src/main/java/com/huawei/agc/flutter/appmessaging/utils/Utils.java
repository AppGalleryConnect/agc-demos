/*
    Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

    Licensed under the Apache License, Version 2.0 (the "License")
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/

package com.huawei.agc.flutter.appmessaging.utils;

import com.huawei.agconnect.appmessaging.AGConnectAppMessagingCallback;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingCallback.DismissType;
import com.huawei.agconnect.appmessaging.model.AppMessage;
import com.huawei.agconnect.appmessaging.model.BannerMessage;
import com.huawei.agconnect.appmessaging.model.CardMessage;
import com.huawei.agconnect.appmessaging.model.PictureMessage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public interface Utils {
    /**
     * Converts a AppMessage into a map.
     *
     * @param appMessage:  AppMessage to be converted.
     * @return map
     */
    static Map<String, Object> fromAppMessageToMap(AppMessage appMessage) {
        if (appMessage == null) {
            return Collections.emptyMap();
        }
        final Map<String, Object> map = new HashMap<>();
        final Map<String, Object> base = new HashMap<>();
        base.put("id", "" + appMessage.getId());
        base.put("startTime", appMessage.getStartTime());
        base.put("endTime", appMessage.getEndTime());
        base.put("frequencyType", appMessage.getFrequencyType());
        base.put("frequencyValue", appMessage.getFrequencyValue());
        base.put("testFlag", appMessage.getTestFlag());
        base.put("triggerEvents", appMessage.getTriggerEvents());
        base.put("messageType", appMessage.getMessageType().name());
        map.put("base", base);
        if (appMessage instanceof CardMessage) {
            CardMessage cardMessage = (CardMessage) appMessage;
            map.put("card", fromCardToMap(cardMessage.getCard()));
        } else if (appMessage instanceof BannerMessage) {
            BannerMessage bannerMessage = (BannerMessage) appMessage;
            map.put("banner", fromBannerToMap(bannerMessage.getBanner()));
        } else if (appMessage instanceof PictureMessage) {
            PictureMessage pictureMessage = (PictureMessage) appMessage;
            map.put("picture", fromPictureToMap(pictureMessage.getPicture()));
        }
        return map;
    }

    /**
     * Converts a AppMessage into a map.
     *
     * @param appMessage:  AppMessage to be converted.
     * @param dismissType: messageType added map.
     * @return map
     */
    static Map<String, Object> fromAppMessageToMap(AppMessage appMessage, String dismissType) {
        if (appMessage == null) {
            return Collections.emptyMap();
        }
        final Map<String, Object> map = new HashMap<>();
        final Map<String, Object> base = new HashMap<>();
        map.put("dismissType", dismissType);
        base.put("id", "" + appMessage.getId());
        base.put("startTime", appMessage.getStartTime());
        base.put("endTime", appMessage.getEndTime());
        base.put("frequencyType", appMessage.getFrequencyType());
        base.put("frequencyValue", appMessage.getFrequencyValue());
        base.put("testFlag", appMessage.getTestFlag());
        base.put("triggerEvents", appMessage.getTriggerEvents());
        base.put("messageType", appMessage.getMessageType().name());
        map.put("base", base);
        if (appMessage instanceof CardMessage) {
            CardMessage cardMessage = (CardMessage) appMessage;
            map.put("card", fromCardToMap(cardMessage.getCard()));
        } else if (appMessage instanceof BannerMessage) {
            BannerMessage bannerMessage = (BannerMessage) appMessage;
            map.put("banner", fromBannerToMap(bannerMessage.getBanner()));
        } else if (appMessage instanceof PictureMessage) {
            PictureMessage pictureMessage = (PictureMessage) appMessage;
            map.put("picture", fromPictureToMap(pictureMessage.getPicture()));
        }
        return map;
    }

    /**
     * Converts a CardMessage into a map.
     *
     * @param card: CardMessage to be converted.
     * @return map
     */
    static Map<String, Object> fromCardToMap(CardMessage.Card card) {
        if (card == null) {
            return Collections.emptyMap();
        }
        final Map<String, Object> map = new HashMap<>();
        map.put("title", card.getTitle());
        map.put("titleColor", card.getTitleColor());
        map.put("titleColorOpenness", card.getTitleColorOpenness());
        map.put("body", card.getBody());
        map.put("bodyColor", card.getBodyColor());
        map.put("bodyColorOpenness", card.getBodyColorOpenness());
        map.put("backgroundColor", card.getBackgroundColor());
        map.put("backgroundColorOpenness", card.getBackgroundColorOpenness());
        map.put("portraitPictureURL", card.getPortraitPictureUrl());
        map.put("landscapePictureURL", card.getLandscapePictureUrl());
        map.put("majorButton", fromButtonToMap(card.getMajorButton()));
        map.put("minorButton", fromButtonToMap(card.getMinorButton()));
        return map;
    }

    /**
     * Converts a BannerMessage into a map.
     *
     * @param banner: BannerMessage to be converted.
     * @return map
     */
    static Map<String, Object> fromBannerToMap(BannerMessage.Banner banner) {
        if (banner == null) {
            return Collections.emptyMap();
        }
        final Map<String, Object> map = new HashMap<>();
        map.put("title", banner.getTitle());
        map.put("titleColor", banner.getTitleColor());
        map.put("titleColorOpenness", banner.getTitleColorOpenness());
        map.put("body", banner.getBody());
        map.put("bodyColor", banner.getBodyColor());
        map.put("bodyColorOpenness", banner.getBodyColorOpenness());
        map.put("backgroundColor", banner.getBackgroundColor());
        map.put("backgroundColorOpenness", banner.getBackgroundColorOpenness());
        map.put("pictureURL", banner.getPictureUrl());
        map.put("actionURL", banner.getActionUrl());
        return map;
    }

    /**
     * Converts a PictureMessage into a map.
     *
     * @param picture: PictureMessage to be converted.
     * @return map
     */
    static Map<String, Object> fromPictureToMap(PictureMessage.Picture picture) {
        if (picture == null) {
            return Collections.emptyMap();
        }
        final Map<String, Object> map = new HashMap<>();
        map.put("pictureURL", picture.getPictureUrl());
        map.put("actionURL", picture.getActionUrl());
        return map;
    }

    /**
     * Converts a Button into a map.
     *
     * @param button: Button to be converted.
     * @return map
     */
    static Map<String, Object> fromButtonToMap(CardMessage.Button button) {
        if (button == null) {
            return Collections.emptyMap();
        }
        final Map<String, Object> map = new HashMap<>();
        map.put("text", button.getText());
        map.put("textColor", button.getTextColor());
        map.put("textColorOpenness", button.getTextColorOpenness());
        map.put("actionURL", button.getActionUrl());

        return map;
    }
}
