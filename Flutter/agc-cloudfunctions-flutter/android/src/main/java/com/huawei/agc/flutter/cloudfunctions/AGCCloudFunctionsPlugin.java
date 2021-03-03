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

package com.huawei.agc.flutter.cloudfunctions;

import androidx.annotation.NonNull;
import com.huawei.agconnect.AGConnectInstance;
import com.huawei.agconnect.function.AGConnectFunction;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class AGCCloudFunctionsPlugin implements FlutterPlugin {
    private MethodChannel channel;
    private AGConnectFunction function;

    public static void registerWith(final Registrar registrar) {
        final AGCCloudFunctionsPlugin instance = new AGCCloudFunctionsPlugin();
        instance.onAttachedToEngine(registrar.messenger());
    }

    private void onAttachedToEngine(@NonNull final BinaryMessenger messenger) {
        function = AGConnectFunction.getInstance();
        channel = new MethodChannel(messenger, "com.huawei.agc.flutter.cloudfunctions/MethodChannel");
        channel.setMethodCallHandler(new CloudFunctionsMethodCallHandler(function));
    }

    @Override
    public void onAttachedToEngine(@NonNull final FlutterPluginBinding binding) {
        if (AGConnectInstance.getInstance() == null) {
            AGConnectInstance.initialize(binding.getApplicationContext());
        }
        onAttachedToEngine(binding.getBinaryMessenger());
    }

    @Override
    public void onDetachedFromEngine(@NonNull final FlutterPluginBinding binding) {
        if (channel != null) {
            channel.setMethodCallHandler(null);
            channel = null;
        }
        function = null;
    }
}
