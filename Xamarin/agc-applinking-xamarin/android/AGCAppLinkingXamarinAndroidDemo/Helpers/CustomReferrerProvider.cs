/*
Copyright (c) Huawei Technologies Co., Ltd. Copyright 2020-2021. All rights reserved.

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


using Android.Content;
using Android.Net;
using Huawei.Agconnect.Applinking;
using Huawei.Hmf.Tasks;
using System;

namespace AGCAppLinkingXamarinAndroidDemo
{
    [BroadcastReceiver(Exported = true)]
    [Android.App.IntentFilter(new[] { "com.android.vending.INSTALL_REFERRER" })]

    internal class CustomReferrerProvider : BroadcastReceiver, IReferrerProvider
    {
        private string installReferrer;
        public override void OnReceive(Context context, Intent intent)
        {
            installReferrer = intent.GetStringExtra("referrer");
        }

        public Task CustomReferrer
        {
            get
            {
                Huawei.Hmf.Tasks.TaskCompletionSource source = new TaskCompletionSource();
                try
                {
                    source.SetResult(installReferrer);
                }
                catch (Java.Lang.Exception ex)
                {
                    source.SetException(ex);
                }
                catch (Exception ex)
                {
                    source.SetException(new Java.Lang.Exception(ex.Message));
                }
                return source.Task;
            }
        }
    }
}