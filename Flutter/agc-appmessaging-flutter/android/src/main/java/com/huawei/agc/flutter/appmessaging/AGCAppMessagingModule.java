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

package com.huawei.agc.flutter.appmessaging;

import android.app.Activity;

import com.huawei.agc.flutter.appmessaging.presenter.AGCAppMessagingContract;
import com.huawei.agc.flutter.appmessaging.viewmodel.AGCAppMessagingViewModel;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.appmessaging.AGConnectAppMessaging;

import io.flutter.plugin.common.MethodChannel.Result;

public class AGCAppMessagingModule {
    //ViewModel instance
    private final AGCAppMessagingContract.Presenter viewModel;

    //AGConnectAppMessaging instance
    private final AGConnectAppMessaging agConnectAppMessaging;

    /**
     * Initialization of AgcAppMessagingModule in Flutter Side.
     *
     * @param activity: {@link Activity} instance.
     */
    public AGCAppMessagingModule(Activity activity) {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(activity.getApplicationContext());
        }
        agConnectAppMessaging = AGConnectAppMessaging.getInstance();
        viewModel = new AGCAppMessagingViewModel(activity, agConnectAppMessaging);
    }

    /**
     * Data synchronization from the AppGallery Connect server.
     *
     * @param enable: Boolean value that refers to whether enable or disable fetch message feature.
     * @param result: In the success scenario, Boolean instance, true, is returned.
     */
    public void setFetchMessageEnable(final Boolean enable, final Result result) {
        viewModel.setFetchMessageEnable(enable);
        result.success(true);
    }

    /**
     * Sets displayEnable in AGCAppMessaging.
     *
     * @param enable: Boolean value that refers to whether enable or disable display feature.
     * @param result: In the success scenario, Boolean instance, true, is returned.
     */
    public void setDisplayEnable(final Boolean enable, final Result result) {
        viewModel.setDisplayEnable(enable);
        result.success(true);
    }

    /**
     * Returns the result of isDisplayEnable.
     *
     * @param result: In the success scenario, {@link Boolean} instance is returned, or {@link Exception} is returned in
     *                the failure scenario.
     */
    public void isDisplayEnable(final Result result) {
        viewModel.isDisplayEnable(new AGCAppMessagingModule.AppMessagingResultHandler(result));
    }

    /**
     * Sets whether to allow data synchronization from the AppGallery Connect server.
     *
     * @param result: In the success scenario, {@link Boolean} instance is returned, or {@link Exception} is returned in
     *                the failure scenario.
     */
    public void isFetchMessageEnable(final Result result) {
        viewModel.isFetchMessageEnable(new AGCAppMessagingModule.AppMessagingResultHandler(result));
    }

    /**
     * Sets force fetch to data synchronization from the AppGallery Connect server.
     *
     * @param result: In the success scenario, Boolean instance, true, is returned.
     */
    public void setForceFetch(final Result result) {
        viewModel.setForceFetch();
        result.success(true);
    }

    /**
     * Sets display location of appMessage whether at the bottom or the center.
     *
     * @param location: Location instance that will be get via Constants.
     * @param result:   In the success scenario, Boolean instance, true, is returned.
     */
    public void setDisplayLocation(final int location, final Result result) {
        viewModel.setDisplayLocation(location);
        result.success(true);
    }

    /**
     * Removes Custom View.
     *
     * @param result: In the success scenario, Boolean instance, true, is returned.
     */

    public void removeCustomView(final Result result) {
        viewModel.removeCustomView();
        result.success(true);
    }

    /**
     * Triggers message display.
     *
     * @param eventId: String instance that refers to eventId.
     * @param result:  In the success scenario, Boolean instance, true, is returned.
     */

    public void trigger(final String eventId, final Result result) {
        viewModel.trigger(eventId);
        result.success(true);
    }

    /* Private Inner Class */

    /**
     * AppMessagingResultHandler static nested class is a helper class for reaching {@link
     * AGCAppMessagingContract.ResultListener}.
     */
    private static final class AppMessagingResultHandler implements AGCAppMessagingContract.ResultListener {

        private Result mResult;

        AppMessagingResultHandler(final Result result) {
            this.mResult = result;
        }

        @Override
        public void onSuccess(Object result) {
            mResult.success(result);
        }

        @Override
        public void onFail(Exception exception) {
            mResult.error("", exception.getMessage(), exception.getLocalizedMessage());
        }
    }

}