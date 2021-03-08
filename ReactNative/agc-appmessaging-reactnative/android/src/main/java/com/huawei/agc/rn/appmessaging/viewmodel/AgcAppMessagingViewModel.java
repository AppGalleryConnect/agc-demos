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

package com.huawei.agc.rn.appmessaging.viewmodel;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableMap;
import com.huawei.agc.rn.appmessaging.AgcAppMessagingModule;
import com.huawei.agc.rn.appmessaging.presenter.AgcAppMessagingContract;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.appmessaging.AGConnectAppMessaging;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingCallback;
import com.huawei.agconnect.appmessaging.Location;
import com.huawei.agconnect.appmessaging.model.AppMessage;

import java.util.Objects;

/**
 * AgcAppMessagingViewModel works as a mediator between {@link AgcAppMessagingContract.Presenter} and {@link AgcAppMessagingModule}.
 *
 * <p>
 * It fetches data from the {@link AppMessage}, formats and returns to the {@link AgcAppMessagingModule}.
 *
 * @since v.1.2.0
 */
public class AgcAppMessagingViewModel implements AgcAppMessagingContract.Presenter {

    //AGConnectAppMessaging instance
    private final AGConnectAppMessaging agConnectAppMessaging;

    public AgcAppMessagingViewModel(@Nullable ReactContext context, AGConnectAppMessaging agConnectAppMessaging) {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(context);
        }
        this.agConnectAppMessaging = agConnectAppMessaging;
    }

    /**
     * Data synchronization from the AppGallery Connect server.
     *
     * @param enabled: Boolean value that refers to whether enable or disable fetch message feature.
     */
    @Override
    public void setFetchMessageEnable(Boolean enabled) {
        agConnectAppMessaging.setFetchMessageEnable(enabled);
    }

    /**
     * Sets displayEnable in AGCAppMessaging.
     *
     * @param enabled: Boolean value that refers to whether enable or disable display feature.
     */
    @Override
    public void setDisplayEnable(Boolean enabled) {
        agConnectAppMessaging.setDisplayEnable(enabled);
    }

    /**
     * Returns the result of isDisplayEnable.
     *
     * @param resultListener: In the success scenario, {@link AgcAppMessagingContract.ResultListener<Boolean>} instance is returned via listener.
     */
    @Override
    public void isDisplayEnable(AgcAppMessagingContract.ResultListener<Boolean> resultListener) {
        boolean enabled = agConnectAppMessaging.isDisplayEnable();
        resultListener.onSuccess(enabled);
    }

    /**
     * Sets whether to allow data synchronization from the AppGallery Connect server.
     *
     * @param resultListener: In the success scenario, {@link AgcAppMessagingContract.ResultListener<Boolean>} instance is returned via listener.
     */
    @Override
    public void isFetchMessageEnable(AgcAppMessagingContract.ResultListener<Boolean> resultListener) {
        boolean enabled = agConnectAppMessaging.isFetchMessageEnable();
        resultListener.onSuccess(enabled);
    }

    /**
     * Sets force fetch to data synchronization from the AppGallery Connect server.
     */
    @Override
    public void setForceFetch() {
        agConnectAppMessaging.setForceFetch();
    }

    /**
     * Sets display location of appMessage whether at the bottom or the center.
     *
     * @param locationVal: Location instance that will be get via Constants.
     */
    @Override
    public void setDisplayLocation(int locationVal) {
        if (locationVal == 0) {
            agConnectAppMessaging.setDisplayLocation(Location.BOTTOM);
        } else {
            agConnectAppMessaging.setDisplayLocation(Location.CENTER);
        }
    }

    /**
     * When using custom app message layout, handle custom app message click events like below.
     *
     * @param readableMap:                   {@link ReadableMap} instance that takes eventType and dismissType.
     * @param agConnectAppMessagingCallback: AGConnectAppMessagingCallback instance.
     * @param appMessage:                    AppMessage instance.
     */
    @Override
    public void handleCustomViewMessageEvent(ReadableMap readableMap, AGConnectAppMessagingCallback agConnectAppMessagingCallback, AppMessage appMessage) {
        String eventType = "";
        String dismissType = "";

        if (readableMap.hasKey("eventType")) {
            eventType = readableMap.getString("eventType");
        }

        if (readableMap.hasKey("dismissType")) {
            dismissType = readableMap.getString("dismissType");
        }

        if (eventType == null || eventType.isEmpty()) {
            return;
        }

        switch (Objects.requireNonNull(eventType)) {
            case "onMessageDisplay":
                agConnectAppMessagingCallback.onMessageDisplay(appMessage);
                break;
            case "onMessageClick":
                agConnectAppMessagingCallback.onMessageClick(appMessage);
                break;
            case "onMessageDismiss":
                agConnectAppMessagingCallback.onMessageDismiss(appMessage, getDismissType(dismissType));
                break;
            case "onMessageError":
                agConnectAppMessagingCallback.onMessageError(appMessage);
                break;
            default:
                break;
        }
    }

    /**
     * Removes Custom View.
     */
    @Override
    public void removeCustomView() {
        agConnectAppMessaging.removeCustomView();
    }

    /**
     * Triggers message display.
     *
     * @param eventId: String instance that refers to eventId.
     */
    @Override
    public void trigger(final String eventId) {
        agConnectAppMessaging.trigger(eventId);
    }

    private AGConnectAppMessagingCallback.DismissType getDismissType(String dismissType) {
        AGConnectAppMessagingCallback.DismissType mDismissType = AGConnectAppMessagingCallback.DismissType.UNKNOWN_DISMISS_TYPE;

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
}
