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
import com.huawei.agconnect.function.AGConnectFunction;
import com.huawei.agconnect.function.FunctionCallable;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

final class CloudFunctionsMethodCallHandler implements MethodCallHandler {
    private final AGConnectFunction function;

    CloudFunctionsMethodCallHandler(@NonNull final AGConnectFunction function) {
        this.function = function;
    }

    private void callFunction(@NonNull final MethodCall call, @NonNull final Result result) {
        final FunctionCallable functionCallable = FunctionUtils.getFunctionCallable(call, function);
        final Object functionParameters = call.argument("functionParameters");

        functionCallable.call(functionParameters)
            .addOnSuccessListener(functionResult -> FunctionUtils.sendSuccess(result, functionResult))
            .addOnFailureListener(e -> FunctionUtils.sendError(result, e));
    }

    @Override
    public void onMethodCall(@NonNull final MethodCall call, @NonNull final Result result) {
        if ("callFunction".equals(call.method)) {
            callFunction(call, result);
        } else {
            result.notImplemented();
        }
    }
}
