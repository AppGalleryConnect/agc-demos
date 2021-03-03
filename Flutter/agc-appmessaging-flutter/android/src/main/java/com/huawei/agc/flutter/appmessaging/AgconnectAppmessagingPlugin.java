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

import androidx.annotation.NonNull;

import com.huawei.agc.flutter.appmessaging.handlers.AGCAppMessagingCustomEventStreamHandler;
import com.huawei.agc.flutter.appmessaging.handlers.AGCAppMessagingOnMessageClickStreamHandler;
import com.huawei.agc.flutter.appmessaging.handlers.AGCAppMessagingOnMessageDismissStreamHandler;
import com.huawei.agc.flutter.appmessaging.handlers.AGCAppMessagingOnMessageDisplayStreamHandler;
import com.huawei.agc.flutter.appmessaging.handlers.AGCAppMessagingOnMessageErrorStreamHandler;
import com.huawei.agc.flutter.appmessaging.handlers.AppMessagingMethodHandler;
import com.huawei.agconnect.AGConnectInstance;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class AgconnectAppmessagingPlugin implements FlutterPlugin, ActivityAware {
    private static final CountDownLatch LATCH = new CountDownLatch(1);

    private MethodChannel methodChannel;
    private EventChannel eventChannelOnMessageDismiss;
    private EventChannel eventChannelOnMessageClick;
    private EventChannel eventChannelOnMessageDisplay;
    private EventChannel eventChannelOnMessageError;
    private EventChannel eventChannelCustomEvent;
    private FlutterPluginBinding flutterPluginBinding;
    private AppMessagingMethodHandler appMessagingMethodHandler;
    private AGCAppMessagingModule agcAppMessagingModule;
    private EventChannel.StreamHandler onMessageDismissStreamHandler;
    private EventChannel.StreamHandler onMessageClickStreamHandler;
    private EventChannel.StreamHandler onMessageDisplayStreamHandler;
    private EventChannel.StreamHandler onMessageErrorStreamHandler;
    private EventChannel.StreamHandler customEventStreamHandler;

    public static void registerWith(Registrar registrar) {
        AgconnectAppmessagingPlugin agconnectAppmessagingPlugin = new AgconnectAppmessagingPlugin();
        registrar.publish(agconnectAppmessagingPlugin);
        agconnectAppmessagingPlugin.initializeEngine(registrar.messenger(), registrar.activity());
    }

    private void initializeEngine(final BinaryMessenger messenger, final Activity activity) {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(activity.getApplicationContext());
        }

        initializeChannels(messenger);
        setHandlers(activity);
        LATCH.countDown();
    }

    private void initializeChannels(final BinaryMessenger messenger) {
        methodChannel = new MethodChannel(messenger, "com.huawei.agc.flutter.appmessaging_methodchannel");
        eventChannelOnMessageDismiss = new EventChannel(messenger,
            "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageDismiss");
        eventChannelOnMessageClick = new EventChannel(messenger,
            "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageClick");
        eventChannelOnMessageDisplay = new EventChannel(messenger,
            "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageDisplay");
        eventChannelOnMessageError = new EventChannel(messenger,
            "com.huawei.agc.flutter.appmessaging_eventchannel_onMessageError");
        eventChannelCustomEvent = new EventChannel(messenger,
            "com.huawei.agc.flutter.appmessaging_eventchannel_customEvent");
    }

    private void setHandlers(Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<Activity>(activity);
        agcAppMessagingModule = new AGCAppMessagingModule(activity);
        appMessagingMethodHandler = new AppMessagingMethodHandler(agcAppMessagingModule);
        methodChannel.setMethodCallHandler(appMessagingMethodHandler);
        onMessageClickStreamHandler = new AGCAppMessagingOnMessageClickStreamHandler(activity);
        onMessageDismissStreamHandler = new AGCAppMessagingOnMessageDismissStreamHandler(activity);
        onMessageDisplayStreamHandler = new AGCAppMessagingOnMessageDisplayStreamHandler(activity);
        onMessageErrorStreamHandler = new AGCAppMessagingOnMessageErrorStreamHandler(activity);
        customEventStreamHandler = new AGCAppMessagingCustomEventStreamHandler(activityWeakReference, LATCH);

        eventChannelOnMessageDismiss.setStreamHandler(onMessageDismissStreamHandler);
        eventChannelOnMessageClick.setStreamHandler(onMessageClickStreamHandler);
        eventChannelOnMessageDisplay.setStreamHandler(onMessageDisplayStreamHandler);
        eventChannelOnMessageError.setStreamHandler(onMessageErrorStreamHandler);
        eventChannelCustomEvent.setStreamHandler(customEventStreamHandler);
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        this.flutterPluginBinding = flutterPluginBinding;

    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        removeChannels();
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        if (flutterPluginBinding != null) {
            initializeEngine(flutterPluginBinding.getBinaryMessenger(), binding.getActivity());
        }
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        onDetachedFromActivity();
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        onAttachedToActivity(binding);
    }

    @Override
    public void onDetachedFromActivity() {
        methodChannel.setMethodCallHandler(null);
        eventChannelOnMessageDismiss.setStreamHandler(null);
        eventChannelCustomEvent.setStreamHandler(null);
        eventChannelOnMessageDisplay.setStreamHandler(null);
        eventChannelOnMessageClick.setStreamHandler(null);
        eventChannelOnMessageError.setStreamHandler(null);
    }

    private void removeChannels() {
        onMessageClickStreamHandler = null;
        onMessageErrorStreamHandler = null;
        onMessageDisplayStreamHandler = null;
        onMessageDismissStreamHandler = null;
        customEventStreamHandler = null;
        appMessagingMethodHandler = null;
        agcAppMessagingModule = null;
        methodChannel = null;
        eventChannelOnMessageDismiss = null;
        eventChannelOnMessageError = null;
        eventChannelOnMessageDisplay = null;
        eventChannelOnMessageClick = null;
        eventChannelCustomEvent = null;
    }
}
