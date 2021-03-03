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

package com.huawei.agc.rn.appmessaging;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.huawei.agc.rn.appmessaging.presenter.AgcAppMessagingContract;
import com.huawei.agc.rn.appmessaging.util.AgcAppMessagingJsonUtils;
import com.huawei.agc.rn.appmessaging.viewmodel.AgcAppMessagingViewModel;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.appmessaging.AGConnectAppMessaging;
import com.huawei.agconnect.appmessaging.AGConnectAppMessagingCallback;
import com.huawei.agconnect.appmessaging.model.AppMessage;

import java.lang.ref.WeakReference;

/**
 * AgcAppMessagingModule class is the tool class of {@link AgcAppMessagingPackage}.
 *
 * @since v.1.2.0
 */
public class AgcAppMessagingModule extends ReactContextBaseJavaModule {

    private static ReactContext mContext;
    private static AGConnectAppMessagingCallback currentAgConnectAppMessagingCallback;
    private static AppMessage currentAppMessage;
    private static Boolean isCustomViewActivated = false;
    private final WeakReference<ReactContext> weakContext;
    //ViewModel instance
    private final AgcAppMessagingContract.Presenter viewModel;

    //AGConnectAppMessaging instance
    private final AGConnectAppMessaging agConnectAppMessaging;

    /**
     * Initialization of AgcAppMessagingModule in RN Side.
     *
     * @param reactContext: {@link ReactApplicationContext} instance.
     */
    public AgcAppMessagingModule(ReactApplicationContext reactContext) {
        super(reactContext);
        weakContext = new WeakReference<>(reactContext);
        setMContext(reactContext);
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(weakContext.get());
        }
        agConnectAppMessaging = AGConnectAppMessaging.getInstance();
        viewModel = new AgcAppMessagingViewModel(weakContext.get(), agConnectAppMessaging);
        addNotifications();
    }

    /**
     * Adds custom view.
     */
    @ReactMethod
    public static void addCustomView() {
        if (AGConnectInstance.getInstance() == null)
            AGConnectInstance.initialize(getMContext());
        AGConnectAppMessaging.getInstance().addCustomView((appMessage, agConnectAppMessagingCallback) -> {
            setIsCustomViewActivated(true);
            setCurrentAppMessage(appMessage);
            setAgConnectAppMessagingCallback(agConnectAppMessagingCallback);
            if (getMContext() == null || getMContext().getCurrentActivity() == null) {
                return;
            }
            mContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("customView", AgcAppMessagingJsonUtils.toJSON(appMessage));
        });
    }

    private static ReactContext getMContext() {
        return mContext;
    }

    private static void setMContext(ReactContext mContext) {
        AgcAppMessagingModule.mContext = mContext;
    }

    private static AGConnectAppMessagingCallback getAgConnectAppMessagingCallback() {
        return currentAgConnectAppMessagingCallback;
    }

    private static void setAgConnectAppMessagingCallback(AGConnectAppMessagingCallback agConnectAppMessagingCallback) {
        AgcAppMessagingModule.currentAgConnectAppMessagingCallback = agConnectAppMessagingCallback;
    }

    private static AppMessage getCurrentAppMessage() {
        return currentAppMessage;
    }

    private static void setCurrentAppMessage(AppMessage currentAppMessage) {
        AgcAppMessagingModule.currentAppMessage = currentAppMessage;
    }

    private static Boolean getIsCustomViewActivated() {
        return isCustomViewActivated;
    }

    private static void setIsCustomViewActivated(Boolean isCustomViewActivated) {
        AgcAppMessagingModule.isCustomViewActivated = isCustomViewActivated;
    }

    /**
     * Here we will call this AGCAppMessaging so that
     * we can access it through
     * React.NativeModules.AGCAppMessaging in RN Side.
     *
     * @return "AGCAppMessaging"
     */
    @Override
    public String getName() {
        return "AGCAppMessagingModule";
    }

    /**
     * Data synchronization from the AppGallery Connect server.
     *
     * @param enable:  Boolean value that refers to whether enable or disable fetch message feature.
     * @param promise: In the success scenario, Boolean instance, true, is returned.
     */
    @ReactMethod
    public void setFetchMessageEnable(final Boolean enable, final Promise promise) {
        viewModel.setFetchMessageEnable(enable);
        promise.resolve(true);
    }

    /**
     * Sets displayEnable in AGCAppMessaging.
     *
     * @param enable:  Boolean value that refers to whether enable or disable display feature.
     * @param promise: In the success scenario, Boolean instance, true, is returned.
     */
    @ReactMethod
    public void setDisplayEnable(final Boolean enable, final Promise promise) {
        viewModel.setDisplayEnable(enable);
        promise.resolve(true);
    }

    /**
     * Returns the result of isDisplayEnable.
     *
     * @param promise: In the success scenario, {@link Boolean} instance is returned, or {@link Exception} is returned in the failure scenario.
     */
    @ReactMethod
    public void isDisplayEnable(final Promise promise) {
        viewModel.isDisplayEnable(new AgcAppMessagingModule.AppMessagingResultHandler(promise));
    }

    /**
     * Sets whether to allow data synchronization from the AppGallery Connect server.
     *
     * @param promise: In the success scenario, {@link Boolean} instance is returned, or {@link Exception} is returned in the failure scenario.
     */
    @ReactMethod
    public void isFetchMessageEnable(final Promise promise) {
        viewModel.isFetchMessageEnable(new AgcAppMessagingModule.AppMessagingResultHandler(promise));
    }

    /**
     * Sets force fetch to data synchronization from the AppGallery Connect server.
     *
     * @param promise: In the success scenario, Boolean instance, true, is returned.
     */
    @ReactMethod
    public void setForceFetch(final Promise promise) {
        viewModel.setForceFetch();
        promise.resolve(true);
    }

    /**
     * Sets display location of appMessage whether at the bottom or the center.
     *
     * @param location: Location instance that will be get via Constants.
     * @param promise:  In the success scenario, Boolean instance, true, is returned.
     */
    @ReactMethod
    public void setDisplayLocation(final String location, final Promise promise) {
        int loc = 0;
        if (location.equals("CENTER")) {
            loc = 1;
        }
        viewModel.setDisplayLocation(loc);
        promise.resolve(true);
    }

    /**
     * Adds notifications in android side that can be listened in RN Side via event emitters.
     */
    private void addNotifications() {
        agConnectAppMessaging.addOnDisplayListener(appMessage -> weakContext.get().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onMessageDisplay", AgcAppMessagingJsonUtils.toJSON(appMessage))); //sendEvent("onMessageDisplay", appMessage));
        agConnectAppMessaging.addOnClickListener(appMessage -> weakContext.get().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onMessageClick", AgcAppMessagingJsonUtils.toJSON(appMessage))); //sendEvent("onMessageClick", appMessage));
        agConnectAppMessaging.addOnDismissListener((appMessage, dismissType) -> weakContext.get().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onMessageDismiss", AgcAppMessagingJsonUtils.toJSON(appMessage))); //sendEvent("onMessageDismiss", appMessage, dismissType));
        agConnectAppMessaging.addOnErrorListener(appMessage -> weakContext.get().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("onMessageError", AgcAppMessagingJsonUtils.toJSON(appMessage))); //sendEvent("onMessageError", appMessage));
    }

    /**
     * Removes Custom View.
     *
     * @param promise: In the success scenario, Boolean instance, true, is returned.
     */
    @ReactMethod
    public void removeCustomView(final Promise promise) {
        viewModel.removeCustomView();
        setIsCustomViewActivated(false);
        promise.resolve(true);
    }

    /**
     * Triggers message display.
     *
     * @param eventId: String instance that refers to eventId.
     * @param promise: In the success scenario, Boolean instance, true, is returned.
     */
    @ReactMethod
    public void trigger(final String eventId, final Promise promise) {
        viewModel.trigger(eventId);
        promise.resolve(true);
    }

    /**
     * When using custom app message layout, handle custom app message click events like below.
     *
     * @param readableMap: {@link ReadableMap} instance that takes eventType and dismissType.
     * @param promise:     In the success scenario, Boolean instance, true, is returned.
     */
    @ReactMethod
    public void handleCustomViewMessageEvent(final ReadableMap readableMap, final Promise promise) {
        if (getAgConnectAppMessagingCallback() == null || getCurrentAppMessage() == null) {
            return;
        }

        viewModel.handleCustomViewMessageEvent(readableMap, getAgConnectAppMessagingCallback(), getCurrentAppMessage());
        promise.resolve(true);
    }

    /* Private Inner Class */

    /**
     * AppMessagingResultHandler static nested class is a helper class for reaching {@link AgcAppMessagingContract.ResultListener}.
     */
    private static final class AppMessagingResultHandler implements AgcAppMessagingContract.ResultListener {

        private Promise promise;

        AppMessagingResultHandler(final Promise promise) {
            this.promise = promise;
        }

        @Override
        public void onSuccess(Object result) {
            promise.resolve(result);
        }

        @Override
        public void onFail(Exception exception) {
            promise.reject(exception);
        }
    }
}
