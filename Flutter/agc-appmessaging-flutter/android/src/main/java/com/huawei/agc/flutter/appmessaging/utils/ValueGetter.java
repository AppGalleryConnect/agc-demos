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

package com.huawei.agc.flutter.appmessaging.utils;

import java.util.Map;
import java.util.Objects;

import io.flutter.Log;

public interface ValueGetter {
    final String TAG = ValueGetter.class.getName();

    /**
     * Converts a Object into a String.
     *
     * @param key:   String key.
     * @param value: Object value.
     * @return map
     */
    static String getString(String key, Object value) {
        if (!(value instanceof String)) {
            Log.w(TAG, "toString | String value expected for " + key + ". ");
            return "";
        }
        return (String) value;
    }

    /**
     * Converts a Object into a Boolean.
     *
     * @param key:   String key.
     * @param value: Object value.
     * @return map
     */
    public static Boolean toBoolean(String key, Object value) {
        if (!(value instanceof Boolean)) {
            Log.w(TAG, "toBoolean | Boolean value expected for " + key + ". Returning false as default.");
            return false;
        }
        return (Boolean) value;
    }

    /**
     * Converts a Object into a Integer.
     *
     * @param key:   String key.
     * @param value: Object value.
     * @return map
     */
    public static Integer toInteger(String key, Object value) {
        if (!(value instanceof Integer)) {
            Log.w(TAG, "toInteger | Integer value expected for " + key);
            return null;
        }
        return (Integer) value;
    }
}
