/*
    Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

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

package com.huawei.agc.rn.appmessaging.util;

import android.util.Log;

import com.facebook.react.bridge.WritableMap;
import com.huawei.agconnect.appmessaging.model.AppMessage;
import com.huawei.agconnect.appmessaging.model.BannerMessage;
import com.huawei.agconnect.appmessaging.model.CardMessage;
import com.huawei.agconnect.appmessaging.model.PictureMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

/**
 * {@link AgcAppMessagingJsonUtils} simply converts Objects into a WritableMap instances, so that RN Side can read the objects with proper keys.
 *
 * @since v.1.2.0
 */
public enum AgcAppMessagingJsonUtils {
    INSTANCE;
    public static final String TAG = AgcAppMessagingJsonUtils.class.getSimpleName();

    public synchronized static WritableMap toJSON(AppMessage message) {
        Log.d(TAG, "call -> convertAppMessageToJSON");

        WritableMap writableMap = null;
        JSONObject json = new JSONObject();
        try {
            JSONObject base = new JSONObject()
                    .put("id", "" + message.getId())
                    .put("startTime", message.getStartTime())
                    .put("endTime", message.getEndTime())
                    .put("frequencyType", message.getFrequencyType())
                    .put("frequencyValue", message.getFrequencyValue())
                    .put("testFlag", message.getTestFlag())
                    .put("triggerEvents", message.getTriggerEvents())
                    .put("messageType", message.getMessageType().name());
            json.put("base", base);
            if (message instanceof CardMessage) {
                CardMessage cardMessage = (CardMessage) message;
                json.put("card", convertCardToJSON(cardMessage.getCard()));
            } else if (message instanceof BannerMessage) {
                BannerMessage bannerMessage = (BannerMessage) message;
                json.put("banner", convertBannerToJSON(bannerMessage.getBanner()));
            } else if (message instanceof PictureMessage) {
                PictureMessage pictureMessage = (PictureMessage) message;
                json.put("picture", convertPictureToJSON(pictureMessage.getPicture()));
            }
        } catch (JSONException e) {
            Log.d(TAG, e.getMessage());
        }
        writableMap = MapUtils.toWritableMap(json);
        return writableMap;
    }

    public synchronized static JSONObject convertBannerToJSON(BannerMessage.Banner banner) throws JSONException {
        JSONObject json = new JSONObject();
        if (banner != null) {
            json
                    .put("title", banner.getTitle())
                    .put("titleColor", banner.getTitleColor())
                    .put("titleColorOpenness", banner.getTitleColorOpenness())
                    .put("body", banner.getBody())
                    .put("bodyColor", banner.getBodyColor())
                    .put("bodyColorOpenness", banner.getBodyColorOpenness())
                    .put("backgroundColor", banner.getBackgroundColor())
                    .put("backgroundColorOpenness", banner.getBackgroundColorOpenness())
                    .put("pictureURL", banner.getPictureUrl())
                    .put("actionURL", banner.getActionUrl());
        }
        return json;
    }

    public synchronized static JSONObject convertPictureToJSON(PictureMessage.Picture picture)
            throws JSONException {
        JSONObject json = new JSONObject();
        if (picture != null) {
            json
                    .put("pictureURL", picture.getPictureUrl())
                    .put("actionURL", picture.getActionUrl());
        }
        return json;
    }

    public synchronized static JSONObject convertCardToJSON(CardMessage.Card card) throws JSONException {
        JSONObject json = new JSONObject();
        if (card != null) {
            json
                    .put("title", card.getTitle())
                    .put("titleColor", card.getTitleColor())
                    .put("titleColorOpenness", card.getTitleColorOpenness())
                    .put("body", card.getBody())
                    .put("bodyColor", card.getBodyColor())
                    .put("bodyColorOpenness", card.getBodyColorOpenness())
                    .put("backgroundColor", card.getBackgroundColor())
                    .put("backgroundColorOpenness", card.getBackgroundColorOpenness())
                    .put("portraitPictureURL", card.getPortraitPictureUrl())
                    .put("landscapePictureURL", card.getLandscapePictureUrl())
                    .put("majorButton", convertButtonToJSON(card.getMajorButton()))
                    .put("minorButton", convertButtonToJSON(card.getMinorButton()));
        }
        return json;
    }


    public synchronized static JSONObject convertButtonToJSON(CardMessage.Button button) throws JSONException {
        JSONObject json = new JSONObject();
        if (button != null) {
            json
                    .put("text", button.getText())
                    .put("textColor", button.getTextColor())
                    .put("textColorOpenness", button.getTextColorOpenness())
                    .put("actionURL", button.getActionUrl());
        }
        return json;
    }

    public synchronized static String createAppMessageObject(AppMessage message, String name) {
        return removeQMsFromNullValue(String.format(Locale.ENGLISH,
                "new AGCAppMessaging.%s(%s)", name, toJSON(message)));
    }

    private synchronized static String removeQMsFromNullValue(String s) {
        return s.replace("'null'", "null");
    }

}
