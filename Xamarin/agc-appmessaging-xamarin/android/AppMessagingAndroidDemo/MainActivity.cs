/*
*       Copyright 2020-2021. Huawei Technologies Co., Ltd. All rights reserved.

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
using System.Threading.Tasks;
using Android.App;
using Android.OS;
using Android.Runtime;
using Android.Support.V7.App;
using Android.Widget;
using AppMessagingAndroidDemo.Helpers;
using Huawei.Agconnect.Appmessaging;
using Huawei.Agconnect.Appmessaging.Model;
using Huawei.Hms.Aaid;
using Huawei.Hms.Aaid.Entity;
using static Huawei.Agconnect.Appmessaging.Model.BannerMessage;
using static Huawei.Agconnect.Appmessaging.Model.CardMessage;
using static Huawei.Agconnect.Appmessaging.Model.PictureMessage;
using Button = Android.Widget.Button;
//using Huawei.Hms.Analytics;
//using Huawei.Hms.Analytics.Type; //optional

namespace AppMessagingAndroidDemo
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme.NoActionBar", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        private AGConnectAppMessaging appMessaging;
        private LinearLayout mainLayout;
        private TextView txtResult;
        //private HiAnalyticsInstance analyticsInstance; //Optional
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_main);

            //Optionally activate analytics kit for report app messaging activities
            //analyticsInstance = HiAnalytics.GetInstance(this);
            //analyticsInstance.SetAnalyticsEnabled(true);


            //Initialize the AGConnectAppMessaging instance
            appMessaging = AGConnectAppMessaging.Instance;

            mainLayout = FindViewById<LinearLayout>(Resource.Id.main_layout);
            txtResult = FindViewById<TextView>(Resource.Id.result);


            GetAAID();
            SetAppMessaging();



            FindViewById<Button>(Resource.Id.add_custom_view).Click += AddCustomView;
            FindViewById<Button>(Resource.Id.send_evet).Click += SendEvent;
            FindViewById<Button>(Resource.Id.remove_custom_view).Click += RemoveCustomView;



        }

        //Optional
        private void SendEvent(object sender, EventArgs e)
        {
            //var bundle = new Bundle();
            //bundle.PutInt("Score", 90);
            //analyticsInstance.OnEvent(HAEventType.Submitscore, bundle);
        }

        private void RemoveCustomView(object sender, EventArgs e)
        {
            appMessaging.RemoveCustomView();
        }

        private void AddCustomView(object sender, EventArgs e)
        {
            appMessaging.AddCustomView(new CustomMessageView(this));
        }

        public async void GetAAID()
        {
            HmsInstanceId hmsInstanceId = HmsInstanceId.GetInstance(this);
            Task<AAIDResult> idTask = hmsInstanceId.GetAAIDAsync();
            var result = await idTask;
            if (idTask.IsCompleted && idTask.Result != null)
            {
                txtResult.Text = "Your Id: " + result.Id;
                Console.WriteLine("AAID retrieved successfully: " + result.Id);
            }
            else
            {
                Toast.MakeText(this, "Cannot retrieved AAID: " + idTask.Result, ToastLength.Long);
                Console.WriteLine("AAID retrieved failed: " + idTask.Result);
            }
        }

        public void SetAppMessaging()
        {
            try
            {
                //Set whether to allow data synchronization from the AppGallery Connect server
                appMessaging.FetchMessageEnable = true;

                //Set whether to enable message display
                appMessaging.DisplayEnable = true;

                //Get the in-app message data from AppGallery Connect server in real time by force.
                appMessaging.SetForceFetch();

                //Set the appmessage location to bottom of the screen
                appMessaging.SetDisplayLocation(Location.Bottom);

                appMessaging.Trigger("EVENT_ID");

                //Subscribe click event of app meessage
                appMessaging.Click += AppMessaging_Click;
                //Subscribe display event of app meessage
                appMessaging.Display += AppMessaging_Display;
                //Subscribe dismiss event of app meessage
                appMessaging.Dismiss += AppMessaging_Dismiss;
                //Subscribe error event of app meessage
                appMessaging.Error += AppMessaging_Error;

                Console.WriteLine("AppMessaging configuration success");
            }
            catch (Exception ex)
            {
                Console.WriteLine("AppMessaging configuration failed: " + ex.ToString());
            }
        }

        private void AppMessaging_Error(object sender, AGConnectAppMessagingOnErrorEventArgs e)
        {
            //You can get App message data from AGConnectAppMessagingOnDisplayEventArgs
            AppMessage message = e.AppMessage;
            SetAppMessageDetails(message);
        }

        private void AppMessaging_Dismiss(object sender, AGConnectAppMessagingOnDismissEventArgs e)
        {
            //You can get App message data from AGConnectAppMessagingOnDisplayEventArgs
            AppMessage message = e.AppMessage;
            SetAppMessageDetails(message);

            //You can get dismiss type of app message  from AGConnectAppMessagingOnDisplayEventArgs
            AGConnectAppMessagingCallbackDismissType callbackDismissType = e.DismissType;
            string dismissType = callbackDismissType.ToString();
            FindViewById<TextView>(Resource.Id.txtDismissType).Text = "Dismisssed Type: " + dismissType;
        }

        private void SetAppMessageDetails(AppMessage message)
        {
            long startTime = message.StartTime;
            long endTime = message.EndTime;
            MessageType messageType = message.MessageType;
            long id = message.Id;
            int frequencyValue = message.FrequencyValue;
            int frequencyType = message.FrequencyTypeProperty;

            FindViewById<TextView>(Resource.Id.txtEventType).Text = "Message Type: " + messageType.ToString();
            FindViewById<TextView>(Resource.Id.txtId).Text = "Message ID: " + id;
            FindViewById<TextView>(Resource.Id.txtStartTime).Text = "Start Time: " + startTime.UnixTimeStampToDateTime();
            FindViewById<TextView>(Resource.Id.txtEndTime).Text = "Start Time: " + startTime.UnixTimeStampToDateTime();
            FindViewById<TextView>(Resource.Id.txtFreqValue).Text = "Frequency Value: " + frequencyValue;
            FindViewById<TextView>(Resource.Id.txtFreqType).Text = "Frequency Type: " + frequencyType;
        }

        private void AppMessaging_Display(object sender, AGConnectAppMessagingOnDisplayEventArgs e)
        {
            //You can get App message data from AGConnectAppMessagingOnDisplayEventArgs
            AppMessage message = e.AppMessage;
            SetAppMessageDetails(message);
        }

        private void AppMessaging_Click(object sender, AGConnectAppMessagingOnClickEventArgs e)
        {
            //You can get App message data from AGConnectAppMessagingOnDisplayEventArgs
            AppMessage message = e.AppMessage;
            if (message.MessageType == MessageType.Banner)
            {
                //Cast app message to BannerMessage
                BannerMessage banner = (BannerMessage)message;
                //Get banner detail 
                Banner detail = banner.GetBanner();

                string title = detail.Title;
                string pictureUrl = detail.PictureUrl;
                int bodyColorOpen = detail.BodyColorOpenness;
                string bodyColor = detail.BodyColor;
                string body = detail.Body;
                int backgroundColorOpen = detail.BackgroundColorOpenness;
                string actionUrl = detail.ActionUrl;
                string titleColor = detail.TitleColor;
                string backgroundColor = detail.BackgroundColor;
                int titleColorOpen = detail.TitleColorOpenness;
            }
            else if (message.MessageType == MessageType.Picture)
            {
                //Cast app message to PictureMessage
                PictureMessage banner = (PictureMessage)message;
                //Get picture detail 
                Picture detail = banner.GetPicture();

                string pictureUrl = detail.PictureUrl;
                string sctionUrl = detail.ActionUrl;
            }
            else if (message.MessageType == MessageType.Card)
            {
                //Cast app message to CardMessage
                CardMessage banner = (CardMessage)message;
                //Get card detail 
                Card detail = banner.GetCard();

                CardMessage.Button majorButton = detail.MajorButton;
                CardMessage.Button minorButton = detail.MinorButton;

                string text = majorButton.Text;
                string actionUrl = majorButton.ActionUrl;
                string textColor = majorButton.TextColor;
                int textColorOpenness = majorButton.TextColorOpenness;
            }
            SetAppMessageDetails(message);
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }
}
