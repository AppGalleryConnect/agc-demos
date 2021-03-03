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

package com.huawei.agc.flutter.appmessaging.handlers;

import android.app.Activity;

import com.huawei.agc.flutter.appmessaging.utils.Utils;
import com.huawei.agc.flutter.appmessaging.utils.ValueGetter;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.appmessaging.AGConnectAppMessaging;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingCallback;
import com.huawei.agconnect.appmessaging.model.AppMessage;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

import io.flutter.Log;
import io.flutter.plugin.common.EventChannel.EventSink;
import io.flutter.plugin.common.EventChannel.StreamHandler;
import io.flutter.plugin.common.MethodCall;

public class AGCAppMessagingCustomEventStreamHandler implements StreamHandler {
    private static WeakReference<Activity> mActivity;
    private static CountDownLatch LATCH;
    private static AppMessage mAppMessage;
    private static AGConnectAppMessagingCallback mAgConnectAppMessagingCallback;

    public AGCAppMessagingCustomEventStreamHandler(final WeakReference<Activity> activity, CountDownLatch latch) {
        setmActivity(activity);
        setLATCH(latch);
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(getActivity().getApplicationContext());
        }
    }

    public static void addCustomView() {
        AGConnectAppMessaging.getInstance()
            .addCustomView((appMessage, agConnectAppMessagingCallback) -> new Thread(() -> {
                try {
                    LATCH.await();
                    getActivity().runOnUiThread(() -> {
                        mAppMessage = appMessage;
                        mAgConnectAppMessagingCallback = agConnectAppMessagingCallback;
                        agConnectAppMessagingCallback.onMessageDisplay(appMessage);
                    });

                } catch (InterruptedException e) {
                    Log.d("AGCAppMessagingCustomEventStreamHandler", e.getLocalizedMessage());
                }
            }).start());
    }

    public static void handleCustomViewMessageEvent(MethodCall call) {
        String eventType = "";
        String dismissType = "";

        Map<String, Object> map = call.arguments();

        if (map.containsKey("eventType")) {
            eventType = ValueGetter.getString("eventType", map.get("eventType"));
        }

        if (map.containsKey("dismissType")) {
            dismissType = ValueGetter.getString("dismissType", map.get("eventType"));
        }

        switch (Objects.requireNonNull(eventType)) {
            case "onMessageDisplay":
                mAgConnectAppMessagingCallback.onMessageDisplay(mAppMessage);
                break;
            case "onMessageClick":
                mAgConnectAppMessagingCallback.onMessageClick(mAppMessage);
                break;
            case "onMessageDismiss":
                mAgConnectAppMessagingCallback.onMessageDismiss(mAppMessage, getDismissType(dismissType));
                break;
            case "onMessageError":
                mAgConnectAppMessagingCallback.onMessageError(mAppMessage);
                break;
            default:
                break;
        }
    }

    private static AGConnectAppMessagingCallback.DismissType getDismissType(String dismissType) {
        AGConnectAppMessagingCallback.DismissType mDismissType
            = AGConnectAppMessagingCallback.DismissType.UNKNOWN_DISMISS_TYPE;

        if (dismissType == null || dismissType.isEmpty()) {
            return mDismissType;
        }

        switch (dismissType) {
            case "dismissTypeClick":
                mDismissType = AGConnectAppMessagingCallback.DismissType.CLICK;
                break;
            case "dismissTypeClickOutside":
                mDismissType = AGConnectAppMessagingCallback.DismissType.CLICK_OUTSIDE;
                break;
            case "dismissTypeBack":
                mDismissType = AGConnectAppMessagingCallback.DismissType.BACK_BUTTON;
                break;
            case "dismissTypeAuto":
                mDismissType = AGConnectAppMessagingCallback.DismissType.AUTO;
                break;
            default:
                break;
        }
        return mDismissType;
    }

    @Override
    public void onListen(final Object arguments, final EventSink eventSink) {
        eventSink.success(Utils.fromAppMessageToMap(mAppMessage));
    }

    @Override
    public void onCancel(Object arguments) {

    }

    public static void setmActivity(WeakReference<Activity> mActivity) {
        AGCAppMessagingCustomEventStreamHandler.mActivity = mActivity;
    }

    public static void setLATCH(CountDownLatch LATCH) {
        AGCAppMessagingCustomEventStreamHandler.LATCH = LATCH;
    }
    private static Activity getActivity() {
        return mActivity.get();    }
}
