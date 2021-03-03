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
import com.huawei.agconnect.function.AGCFunctionException;
import com.huawei.agconnect.function.AGConnectFunction;
import com.huawei.agconnect.function.FunctionCallable;
import com.huawei.agconnect.function.FunctionResult;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel.Result;
import java.util.concurrent.TimeUnit;

final class FunctionUtils {
    private static final long DEFAULT_TIMEOUT = 70L;
    private static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;
    private static final TimeUnit[] TIMEUNITS = TimeUnit.values();

    private FunctionUtils() {
    }

    static <T> long getTimeout(final T argument) {
        if (argument != null) {
            if (argument instanceof Integer) {
                return objectCast(argument, Integer.class);
            } else {
                return objectCast(argument, Long.class);
            }
        } else {
            return DEFAULT_TIMEOUT;
        }
    }

    static <T> TimeUnit getTimeUnit(final T argument) {
        if (argument != null) {
            final int timeUnitAsInt = objectCast(argument, Integer.class);
            return TIMEUNITS[timeUnitAsInt];
        } else {
            return DEFAULT_TIMEUNIT;
        }
    }

    static FunctionCallable getFunctionCallable(@NonNull final MethodCall call,
                                                @NonNull final AGConnectFunction function) {
        final String httpTriggerURI = call.argument("httpTriggerURI");
        final long timeout = getTimeout(call.argument("timeout"));
        final TimeUnit units = getTimeUnit(call.argument("units"));

        final FunctionCallable functionCallable = function.wrap(httpTriggerURI);
        functionCallable.setTimeout(timeout, units);

        return functionCallable;
    }

    static void sendSuccess(@NonNull final Result result, @NonNull final FunctionResult functionResult) {
        result.success(functionResult.getValue());
    }

    static void sendError(@NonNull final Result result, @NonNull final Exception e) {
        if (e instanceof AGCFunctionException) {
            final AGCFunctionException functionException = (AGCFunctionException) e;
            final int errorCodeValue = functionException.getCode();

            final String errorCode = String.valueOf(errorCodeValue);
            final String errorMessage = functionException.getMessage();

            result.error(errorCode, errorMessage, null);
        } else {
            result.error("-1", e.toString(), null);
        }
    }

    static <S, D> D objectCast(@NonNull final S source, @NonNull final Class<D> clazz) {
        return clazz.cast(source);
    }
}
