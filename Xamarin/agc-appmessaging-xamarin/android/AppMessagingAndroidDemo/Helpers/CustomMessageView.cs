/*
*        Copyright 2020-2021. Huawei Technologies Co., Ltd. All rights reserved.

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
using System;
using Android.App;
using Android.Views;
using Android.Widget;
using Huawei.Agconnect.Appmessaging;
using Huawei.Agconnect.Appmessaging.Model;

namespace AppMessagingAndroidDemo.Helpers
{
    public class CustomMessageView : Java.Lang.Object, IAGConnectAppMessagingDisplay
    {
        private Activity activity;
        private IAGConnectAppMessagingCallback callback;
        private AppMessage appMessage;
        private AlertDialog dialog;

        public CustomMessageView(Activity activity)
        {
            this.activity = activity;
        }
        public void DisplayMessage(AppMessage appMessage, IAGConnectAppMessagingCallback callback)
        {
            View view = LayoutInflater.From(activity).Inflate(Resource.Layout.custom_view, null, false);
            AlertDialog dialog = new AlertDialog.Builder(activity).SetView(view).Create();
            Button clickType = view.FindViewById<Button>(Resource.Id.click_type);
            Button dismissType = view.FindViewById<Button>(Resource.Id.dismiss_type);
            TextView id = view.FindViewById<TextView>(Resource.Id.id);
            this.callback = callback;
            this.appMessage = appMessage;
            this.dialog = dialog;
            clickType.Click += ClickTypeClick;
            dismissType.Click += DismissType_Click;

            dialog.Show();
            dialog.Window.SetLayout((activity.Resources.DisplayMetrics.WidthPixels / 4 * 3), LinearLayout.LayoutParams.WrapContent);

            //Set callback for custom app message
            callback.OnMessageDisplay(appMessage);
        }

        private void DismissType_Click(object sender, EventArgs e)
        {
            callback.OnMessageDismiss(appMessage, AGConnectAppMessagingCallbackDismissType.Click);
            dialog.Dismiss();
        }

        private void ClickTypeClick(object sender, EventArgs e)
        {
            callback.OnMessageClick(this.appMessage);
            callback.OnMessageDismiss(appMessage, AGConnectAppMessagingCallbackDismissType.Click);
            dialog.Dismiss();
        }
    }
}