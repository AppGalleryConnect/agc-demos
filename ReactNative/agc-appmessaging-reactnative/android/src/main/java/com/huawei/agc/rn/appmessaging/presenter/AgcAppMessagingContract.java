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

package com.huawei.agc.rn.appmessaging.presenter;

import com.facebook.react.bridge.ReadableMap;
import com.huawei.agc.rn.appmessaging.AgcAppMessagingModule;
import com.huawei.agc.rn.appmessaging.viewmodel.AgcAppMessagingViewModel;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingCallback;
import com.huawei.agconnect.appmessaging.model.AppMessage;

/**
 * AgcAppMessagingContract defines a blueprint of {@link AgcAppMessagingModule} methods that will be exposed to RN Side.
 *
 * @since v.1.2.0
 */
public interface AgcAppMessagingContract {

    /**
     * Defines blueprints of {@link AgcAppMessagingViewModel} methods
     */
    interface Presenter {


        /**
         * Data synchronization from the AppGallery Connect server.
         *
         * @param enabled: Boolean value that refers to whether enable or disable fetch message feature.
         */
        void setFetchMessageEnable(final Boolean enabled);

        /**
         * Sets displayEnable in AGCAppMessaging.
         *
         * @param enabled: Boolean value that refers to whether enable or disable display feature.
         */
        void setDisplayEnable(final Boolean enabled);


        /**
         * Returns the result of isDisplayEnable.
         *
         * @param resultListener: In the success scenario, {@link AgcAppMessagingContract.ResultListener<Boolean>} instance is returned via listener.
         */
        void isDisplayEnable(final AgcAppMessagingContract.ResultListener<Boolean> resultListener);

        /**
         * Sets whether to allow data synchronization from the AppGallery Connect server.
         *
         * @param resultListener: In the success scenario, {@link AgcAppMessagingContract.ResultListener<Boolean>} instance is returned via listener.
         */
        void isFetchMessageEnable(final AgcAppMessagingContract.ResultListener<Boolean> resultListener);

        /**
         * Sets force fetch to data synchronization from the AppGallery Connect server.
         */
        void setForceFetch();

        /**
         * Removes Custom View.
         */
        void removeCustomView();

        /**
         * Sets display location of appMessage whether at the bottom or the center.
         *
         * @param locationVal: Location instance that will be get via Constants.
         */
        void setDisplayLocation(final int locationVal);

        /**
         * Triggers message display.
         *
         * @param eventId: String instance that refers to eventId.
         */
        void trigger(final String eventId);


        /**
         * When using custom app message layout, handle custom app message click events like below.
         *
         * @param readableMap:                   {@link ReadableMap} instance that takes eventType and dismissType.
         * @param agConnectAppMessagingCallback: AGConnectAppMessagingCallback instance.
         * @param appMessage:                    AppMessage instance.
         */
        void handleCustomViewMessageEvent(final ReadableMap readableMap, AGConnectAppMessagingCallback agConnectAppMessagingCallback, AppMessage appMessage);

    }

    /**
     * ResultListener
     *
     * @param <T>: Generic Instance.
     */
    interface ResultListener<T> {
        /**
         * Presents the success scenario, Generic result instance is returned.
         *
         * @param result: Result instance.
         */
        void onSuccess(T result);

        /**
         * Presents the failure scenario, Exception instance is returned.
         *
         * @param exception: Exception instance.
         */
        void onFail(Exception exception);
    }
}
