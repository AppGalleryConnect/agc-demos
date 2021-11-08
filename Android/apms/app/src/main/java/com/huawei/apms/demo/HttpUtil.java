/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package com.huawei.apms.demo;

import android.util.Log;

import com.huawei.agconnect.apms.APMS;
import com.huawei.agconnect.apms.custom.NetworkMeasure;

import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class HttpUtil {
    static private String URL = "https://developer.huawei.com/consumer/cn/";
    static private MediaType MEDIATYPE = MediaType.parse("text/x-markdown; charset=utf-8");
    static private String REQUESTBODY = "apms http request test";

    static public void oneRequest() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .post(RequestBody.create(MEDIATYPE, REQUESTBODY))
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("apmsAndroidDemo", "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("apmsAndroidDemo", "onResponse: Success");
            }
        });
    }

    static public void customNetworkEvent() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URL)
                .post(RequestBody.create(MEDIATYPE, REQUESTBODY))
                .build();
        // define custom network event
        NetworkMeasure networkMeasure = APMS.getInstance().createNetworkMeasure(URL, "POST");
        networkMeasure.setBytesSent(request.headers().byteCount());
        long bytesRecive = 0L;
        networkMeasure.start();
        try {
            Response response = okHttpClient.newCall(request).execute();
            networkMeasure.setStatusCode(response.code());
            if (response.body() != null) {
                networkMeasure.setBytesReceived(response.body().contentLength());
                networkMeasure.setContentType(Objects.requireNonNull(response.body().contentType()).toString());
                bytesRecive = dealResponseBody(response.body());
                response.body().close();
            }
            networkMeasure.putProperty("TransferBytes", String.valueOf(bytesRecive));
            networkMeasure.stop();
        } catch (IOException e) {
            networkMeasure.setStatusCode(0);
            classErrorMessage(networkMeasure, e);
            networkMeasure.putProperty("ErrorMessage", e.getMessage());
            networkMeasure.putProperty("Transfer Bytes", String.valueOf(bytesRecive));
            networkMeasure.stop();
        }
    }

    private static void classErrorMessage(NetworkMeasure networkMeasure, Exception e) {
        if (e instanceof UnknownHostException) {
            networkMeasure.putProperty("ErrorMessage", "DNS resolution failure");
        }
    }

    private static long dealResponseBody(ResponseBody body) throws IOException {
        InputStream inputStream = body.byteStream();
        byte[] result = new byte[1000];
        long len = 0;
        long readBytes = 0;
        while ((len = inputStream.read(result)) != -1) {
            readBytes += len;
        }
        return readBytes;
    }
}
