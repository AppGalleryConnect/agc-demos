/*
       Copyright 2020-2021. Huawei Technologies Co., Ltd. All rights reserved.

       Licensed under the Apache License, Version 2.0 (the "License");
       you may not use this file except in compliance with the License.
       You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing, software
       distributed under the License is distributed on an "AS IS" BASIS,
       WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
       See the License for the specific language governing permissions and
       limitations under the License.
*/
using Android.App;
using Android.Content;
using Android.OS;
using Android.Provider;
using Android.Runtime;
using Android.Util;
using Android.Views;
using Android.Widget;
using Huawei.Agconnect.Apms.Custom;
using Huawei.Agconnect.Apms.Instrument.Okhttp3;
using Huawei.Agconnect.Apms;
using Java.Net;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using Square.OkHttp3;

namespace XamarinApmsDemo
{
    public class HttpUtil
    {
        const string TAG = "HttpUtil";
        static string CUSTOMURL = "https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-apm-introduction-0000001052247254";
        static string URL = "https://developer.huawei.com/consumer/en/";
        static string REQUESTBODY = "apms http request test";
        public HttpUtil()
        {}
       
        public static void CreateRequest()
        {

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                .Url(URL)
                .Post(RequestBody.Create(Square.OkHttp3.MediaType.Parse("text/x-markdown; charset=utf-8"), REQUESTBODY))
                .Build();

            OkHttp3Instrumentation.newCall(okHttpClient, request).Enqueue(new OnresponseOkHttpClient());
            
        }

        public async void CustomNetworkEvent()
        {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
                .Url(CUSTOMURL)
                .Post(RequestBody.Create(Square.OkHttp3.MediaType.Parse("text/x-markdown; charset=utf-8"), REQUESTBODY))
                .Build();

            // Define custom network event
            NetworkMeasure networkMeasure = APMS.Instance.CreateNetworkMeasure(CUSTOMURL, "POST");
            networkMeasure.SetBytesSent(request.Headers().ByteCount());
            long bytesReceive = 0L;

            networkMeasure.Start();

            try
            {
                Response response = await okHttpClient.NewCall(request).ExecuteAsync();
                networkMeasure.SetStatusCode(response.Code());

                if (response.Body() != null)
                {
                    networkMeasure.SetBytesReceived(response.Body().ContentLength());
                    networkMeasure.SetContentType(response.Body().ContentType().ToString());
                    bytesReceive = DealResponseBody(response.Body());
                    response.Body().Close();
                }
                networkMeasure.PutProperty("Property", bytesReceive.ToString());
                networkMeasure.Stop();

            }
            catch (Exception ex)
            {
                networkMeasure.SetStatusCode(0);
                networkMeasure.PutProperty("Error Message", ex.Message);
                networkMeasure.PutProperty("Bytes", bytesReceive.ToString());
                networkMeasure.Stop();
            }
        }
        private static long DealResponseBody(ResponseBody body)
        {
            Stream inputStream = body.ByteStream();
            byte[] result = new byte[1000];
            long readBytes = 0;
            readBytes = inputStream.Read(result);
            return readBytes;
        }

        public class OnresponseOkHttpClient : Java.Lang.Object, ICallback
        {
            public void OnFailure(ICall call, Java.IO.IOException exception)
            {
                Log.Debug(TAG, "OnFailure: " + exception.Message);
            }

            public void OnResponse(ICall call, Response response)
            {
                Log.Debug(TAG, "OnResponse IsSuccessful: " + response.IsSuccessful);
            }
        }
    }
}