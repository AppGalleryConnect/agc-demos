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

import androidx.annotation.NonNull;

import com.huawei.agc.flutter.appmessaging.AGCAppMessagingModule;
import com.huawei.agc.flutter.appmessaging.constants.Method;
import com.huawei.agc.flutter.appmessaging.utils.ValueGetter;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

public class AppMessagingMethodHandler implements MethodCallHandler {
    private final AGCAppMessagingModule agcAppMessagingModule;

    public AppMessagingMethodHandler(AGCAppMessagingModule module) {
        this.agcAppMessagingModule = module;
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        switch (call.method) {
            case Method.SET_FETCH_MESSAGE_ENABLE:
                boolean enable = ValueGetter.toBoolean("enable", call.arguments);
                agcAppMessagingModule.setFetchMessageEnable(enable, result);
                break;
            case Method.SET_DISPLAY_ENABLE:
                boolean enabled = ValueGetter.toBoolean("enable", call.arguments);
                agcAppMessagingModule.setDisplayEnable(enabled, result);
                break;
            case Method.REMOVE_CUSTOM_VIEW:
                agcAppMessagingModule.removeCustomView(result);
                break;
            case Method.TRIGGER:
                String eventId = ValueGetter.getString("eventId", call.arguments);
                agcAppMessagingModule.trigger(eventId, result);
                break;
            case Method.SET_DISPLAY_LOCATION:
                int locationValue = ValueGetter.toInteger("locationConstant", call.arguments);
                agcAppMessagingModule.setDisplayLocation(locationValue, result);
                break;
            case Method.SET_FORCE_FETCH:
                agcAppMessagingModule.setForceFetch(result);
                break;
            case Method.IS_DISPLAY_ENABLE:
                agcAppMessagingModule.isDisplayEnable(result);
                break;
            case Method.IS_FETCH_MESSAGE_ENABLE:
                agcAppMessagingModule.isFetchMessageEnable(result);
                break;
            case Method.HANDLE_CUSTOM_VIEW_MESSAGE_EVENT:
                AGCAppMessagingCustomEventStreamHandler.handleCustomViewMessageEvent(call);
                break;
            default:
                result.notImplemented();
                break;
        }
    }
}
