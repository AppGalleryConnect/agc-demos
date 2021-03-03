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

package com.huawei.agc.flutter.appmessaging.viewmodel;

import android.app.Activity;

import com.huawei.agc.flutter.appmessaging.presenter.AGCAppMessagingContract;
import com.huawei.agc.flutter.appmessaging.presenter.AGCAppMessagingContract.ResultListener;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.appmessaging.AGConnectAppMessaging;
import com.huawei.agconnect.appmessaging.Location;

public class AGCAppMessagingViewModel implements AGCAppMessagingContract.Presenter {
    private final Activity mActivity;
    //AGCAppMessagingViewModel instance
    private final AGConnectAppMessaging instance;

    public AGCAppMessagingViewModel(Activity activity, AGConnectAppMessaging agConnectAppMessaging) {
        this.mActivity = activity;
        initAGConnectInstance();
        this.instance = agConnectAppMessaging;
    }

    private void initAGConnectInstance() {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(mActivity.getApplicationContext());
        }
    }

    @Override
    public void removeCustomView() {
        instance.removeCustomView();
    }

    @Override
    public void setDisplayLocation(int locationValue) {
        if (locationValue == 0) {
            instance.setDisplayLocation(Location.BOTTOM);
        } else {
            instance.setDisplayLocation(Location.CENTER);
        }
    }

    @Override
    public void trigger(String eventId) {
        instance.trigger(eventId);
    }

    @Override
    public void setFetchMessageEnable(Boolean enabled) {
        instance.setFetchMessageEnable(enabled);
    }

    @Override
    public void setDisplayEnable(Boolean enabled) {
        instance.setDisplayEnable(enabled);
    }

    @Override
    public void isDisplayEnable(ResultListener<Boolean> resultListener) {
        boolean enabled = instance.isDisplayEnable();
        resultListener.onSuccess(enabled);
    }

    @Override
    public void isFetchMessageEnable(ResultListener<Boolean> resultListener) {
        boolean enabled = instance.isFetchMessageEnable();
        resultListener.onSuccess(enabled);
    }

    @Override
    public void setForceFetch() {
        instance.setForceFetch();
    }

}
