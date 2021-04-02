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
using Android.Util;
using Android.Widget;
using Huawei.Hms.Push;

namespace XamarinHmsRemoteConfig
{
    [Service]
    [IntentFilter(new[] { "com.huawei.push.action.MESSAGING_EVENT" })]
    public class RemoteConfigMessageService : HmsMessageService
    {

        private static readonly string TAG = "My Messaging Service";

        public override void OnNewToken(string token)
        {
            base.OnNewToken(token);
            Log.Info(TAG, "receive token :" + token);

        }


        public override void OnMessageReceived(RemoteMessage message)
        {
            Log.Info(TAG, "onMessageReceived is called");
            if (message == null)
            {
                Log.Error(TAG, "Received message entity is null!");
                return;
            }

            Log.Info(TAG, "getCollapseKey: " + message.CollapseKey
                    + "\n getData: " + message.Data
                    + "\n getFrom: " + message.From
                    + "\n getTo: " + message.To
                    + "\n getMessageId: " + message.MessageId
                    + "\n getOriginalUrgency: " + message.OriginalUrgency
                    + "\n getUrgency: " + message.Urgency
                    + "\n getSendTime: " + message.SentTime
                    + "\n getMessageType: " + message.MessageType
                    + "\n getTtl: " + message.Ttl);

            RemoteMessage.Notification notification = message.GetNotification();
            if (notification != null)
            {
                Log.Info(TAG, "\n getImageUrl: " + notification.ImageUrl
                        + "\n getTitle: " + notification.Title
                        + "\n getTitleLocalizationKey: " + notification.TitleLocalizationKey
                        + "\n getBody: " + notification.Body
                        + "\n getBodyLocalizationKey: " + notification.BodyLocalizationKey
                        + "\n getIcon: " + notification.Icon
                        + "\n getSound: " + notification.Sound
                        + "\n getTag: " + notification.Tag
                        + "\n getColor: " + notification.Color
                        + "\n getClickAction: " + notification.ClickAction
                        + "\n getChannelId: " + notification.ChannelId
                        + "\n getLink: " + notification.Link
                        + "\n getNotifyId: " + notification.NotifyId);
            }

            if (message.DataOfMap.ContainsKey("DATA_STATE"))
            {
                Log.Info(TAG, "DATA_STATE Exist");
                ISharedPreferences sharedPreferences = this.GetSharedPreferences("Remote_Config", FileCreationMode.Private);
                ISharedPreferencesEditor editor = sharedPreferences.Edit();
                editor.PutBoolean("DATA_OLD", true).Apply();
                Toast.MakeText(this, "The Configuration will be refreshed", ToastLength.Short).Show();

            }


        }



    }
}