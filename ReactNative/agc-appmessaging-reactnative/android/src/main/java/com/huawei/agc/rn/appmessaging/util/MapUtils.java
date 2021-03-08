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

package com.huawei.agc.rn.appmessaging.util;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;
import com.huawei.agconnect.appmessaging.model.AppMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.facebook.react.bridge.Arguments.createArray;
import static com.facebook.react.bridge.Arguments.createMap;

/**
 * MapUtil exposes a set of helper methods for working with
 * {@link ReadableMap}, {@link Map<>}.
 *
 * @since v.1.2.0
 */
public class MapUtils {

    public static final String TAG = "Response Success:: ";

    /**
     * Converts a Generic Object to a WritableMap.
     *
     * @param instance: Generic Object.
     * @param <T>:      Generic class type.
     * @return WritableMap
     */
    public static <T> WritableMap toWritableMap(final T instance) {
        WritableMap writableMap = createMap();
        if (instance instanceof AppMessage) {
            return AgcAppMessagingJsonUtils.toJSON((AppMessage) instance);
        }

        return writableMap;
    }

    // MARK: - Private Helper Methods

    /**
     * Converts a HashMap into a WritableMap.
     *
     * @param map: Map<String, Object> to be converted.
     * @return WritableMap
     */
    public static WritableMap toWritableMap(final Map<String, Object> map) {
        WritableMap writableMap = Arguments.createMap();
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> pair = iterator.next();
            Object value = pair.getValue();
            if (value == null) {
                writableMap.putNull(pair.getKey());
            } else if (value instanceof Boolean) {
                writableMap.putBoolean(pair.getKey(), (Boolean) value);
            } else if (value instanceof Double) {
                writableMap.putDouble(pair.getKey(), (Double) value);
            } else if (value instanceof Integer) {
                writableMap.putInt(pair.getKey(), (Integer) value);
            } else if (value instanceof String) {
                writableMap.putString(pair.getKey(), (String) value);
            } else if (value instanceof Map) {
                writableMap.putMap(pair.getKey(), toWritableMap(value));
            } else if (value.getClass().isArray()) {
                writableMap.putArray(pair.getKey(), toWritableArray((Object[]) value));
            }
            iterator.remove();
        }
        return writableMap;
    }

    /**
     * Converts a JSONObject instance to a WritableMap.
     *
     * @param jsonObject: JSONObject instance.
     * @return WritableMap
     */
    public static WritableMap toWritableMap(final JSONObject jsonObject) {
        WritableMap map = new WritableNativeMap();
        Iterator<String> iterator = jsonObject.keys();
        while (iterator.hasNext()) {
            String key = iterator.next();
            try {
                Object value = jsonObject.get(key);
                if (value instanceof JSONObject) {
                    map.putMap(key, toWritableMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    map.putArray(key, toWritableArray((JSONArray) value));
                } else if (value instanceof Boolean) {
                    map.putBoolean(key, (Boolean) value);
                } else if (value instanceof Integer) {
                    map.putInt(key, (Integer) value);
                } else if (value instanceof Double) {
                    map.putDouble(key, (Double) value);
                } else if (value instanceof String) {
                    map.putString(key, (String) value);
                } else {
                    map.putString(key, value.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    /**
     * Converts ReadableMap instance to a JSONObject.
     *
     * @param readableMap: ReadableMap instance.
     * @return JSONObject.
     */
    public static JSONObject toJson(final ReadableMap readableMap) {
        JSONObject object = new JSONObject();
        if (readableMap == null) {
            return object;
        }
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            try {
                switch (readableMap.getType(key)) {
                    case Null:
                        object.put(key, JSONObject.NULL);
                        break;
                    case Boolean:
                        object.put(key, readableMap.getBoolean(key));
                        break;
                    case Number:
                        object.put(key, readableMap.getDouble(key));
                        break;
                    case String:
                        object.put(key, readableMap.getString(key));
                        break;
                    case Map:
                        object.put(key, toJson(readableMap.getMap(key)));
                        break;
                    case Array:
                        object.put(key, toJson(readableMap.getArray(key)));
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    /**
     * Converts a Object[] array into a WritableArray.
     *
     * @param array: The Object[] array to be converted.
     * @return WritableArray
     */
    private static WritableArray toWritableArray(final Object[] array) {
        WritableArray writableArray = createArray();
        if (array == null) {
            return writableArray;
        }

        for (Object value : array) {
            if (value == null) {
                writableArray.pushNull();
            }
            if (value instanceof Boolean) {
                writableArray.pushBoolean((Boolean) value);
            }
            if (value instanceof Double) {
                writableArray.pushDouble((Double) value);
            }
            if (value instanceof Integer) {
                writableArray.pushInt((Integer) value);
            }
            if (value instanceof String) {
                writableArray.pushString((String) value);
            }
            if (value instanceof Map) {
                Map<String, Object> valueMap = (Map<String, Object>) value;
                writableArray.pushMap(toWritableMap(valueMap));
            }
            if (value != null && value.getClass().isArray()) {
                if (value instanceof Object[]) {
                    writableArray.pushArray(toWritableArray((Object[]) value));
                }
            }
        }
        return writableArray;
    }

    /**
     * toArray converts a ReadableArray into a Object[].
     *
     * @param readableArray: The ReadableArray to be converted.
     * @return Object[]
     */
    private static Object[] toArray(final ReadableArray readableArray) {
        if (readableArray == null || readableArray.size() == 0) {
            return new Object[0];
        }
        Object[] array = new Object[readableArray.size()];
        for (int i = 0; i < readableArray.size(); i++) {
            ReadableType type = readableArray.getType(i);

            switch (type) {
                case Null:
                    array[i] = null;
                    break;
                case Boolean:
                    array[i] = readableArray.getBoolean(i);
                    break;
                case Number:
                    array[i] = readableArray.getDouble(i);
                    break;
                case String:
                    array[i] = readableArray.getString(i);
                    break;
                case Map:
                    array[i] = toMap(readableArray.getMap(i));
                    break;
                case Array:
                    array[i] = toArray(readableArray.getArray(i));
                    break;
                default:
                    break;
            }
        }
        return array;
    }

    /**
     * Converts a ReadableMap into a HashMap.
     *
     * @param readableMap: The ReadableMap to be converted.
     * @return A HashMap containing the data that was in the ReadableMap.
     */
    private static Map<String, Object> toMap(final ReadableMap readableMap) {
        Map<String, Object> map = new HashMap<>();
        if (readableMap == null) {
            return map;
        }
        ReadableMapKeySetIterator iterator = readableMap.keySetIterator();
        while (iterator.hasNextKey()) {
            String key = iterator.nextKey();
            ReadableType type = readableMap.getType(key);
            switch (type) {
                case Null:
                    map.put(key, null);
                    break;
                case Boolean:
                    map.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    map.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    String valueStr = readableMap.getString(key);
                    if (valueStr != null) {
                        map.put(key, valueStr);
                        break;
                    }
                    break;
                case Map:
                    map.put(key, MapUtils.toMap(readableMap.getMap(key)));
                    break;
                case Array:
                    map.put(key, toArray(readableMap.getArray(key)));
                    break;
                default:
                    break;
            }
        }
        return map;
    }

    /**
     * Converts a JSONArray into a WritableArray.
     *
     * @param jsonArray: JSONArray instance.
     * @return WritableArray
     */
    private static WritableArray toWritableArray(final JSONArray jsonArray) {
        WritableArray array = new WritableNativeArray();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Object value = jsonArray.get(i);
                if (value instanceof JSONObject) {
                    array.pushMap(toWritableMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    array.pushArray(toWritableArray((JSONArray) value));
                } else if (value instanceof Boolean) {
                    array.pushBoolean((Boolean) value);
                } else if (value instanceof Integer) {
                    array.pushInt((Integer) value);
                } else if (value instanceof Double) {
                    array.pushDouble((Double) value);
                } else if (value instanceof String) {
                    array.pushString((String) value);
                } else {
                    array.pushString(value.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    /**
     * Converts a ReadableArray to a JSONArray.
     *
     * @param readableArray: ReadableArray instance.
     * @return JSONArray
     */
    private static JSONArray toJson(final ReadableArray readableArray) {
        JSONArray array = new JSONArray();
        if (readableArray == null) {
            return array;
        }
        for (int i = 0; i < readableArray.size(); i++) {
            try {
                switch (readableArray.getType(i)) {
                    case Boolean:
                        array.put(readableArray.getBoolean(i));
                        break;
                    case Number:
                        array.put(readableArray.getDouble(i));
                        break;
                    case String:
                        array.put(readableArray.getString(i));
                        break;
                    case Map:
                        array.put(toJson(readableArray.getMap(i)));
                        break;
                    case Array:
                        array.put(toJson(readableArray.getArray(i)));
                        break;
                    default:
                        break;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }
}
