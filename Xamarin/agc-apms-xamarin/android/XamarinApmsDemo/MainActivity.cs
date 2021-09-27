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
using Android.OS;
using Android.Support.V7.App;
using Android.Runtime;
using Android.Widget;
using Huawei.Agconnect.Apms;
using Huawei.Agconnect.Apms.Custom;
using Huawei.Agconnect.Apms.Instrument;
using Huawei.Hms.Analytics;
using Android.Util;
using Android.Content;
using Huawei.Agconnect.Config;


namespace XamarinApmsDemo
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        const string TAG = "MainActivity";
        Button btnNetwork, btnOpenAnrActivity, btnOpenSecondActivity, btnCustomNormalEvent, btnCustomNetworkEvent;
        Switch swiEnableApms;
        protected override void OnCreate(Bundle savedInstanceState)
        {
            AppInstrumentation.ApplicationCreateBegin(this);
            AppInstrumentation.OnActivityCreateBegin(this.GetType().Name);
            
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.activity_main);

            //Enable ANR Monitor
            APMS.Instance.EnableAnrMonitor(true);

            //Init views
            btnNetwork = FindViewById<Button>(Resource.Id.btnNetworkrequest);
            btnCustomNormalEvent = FindViewById<Button>(Resource.Id.btnCustomEvent);
            btnCustomNetworkEvent = FindViewById<Button>(Resource.Id.btnCustomNetworkEvent);
            btnOpenAnrActivity = FindViewById<Button>(Resource.Id.btnOpenAnrActivity);
            btnOpenSecondActivity = FindViewById<Button>(Resource.Id.btnOpenSecondActivity);
            swiEnableApms = FindViewById<Switch>(Resource.Id.swiEnableCollection);

            //Events
            btnNetwork.Click += BtnNetwork_Click;
            btnCustomNormalEvent.Click += BtnCustomNormalEvent_Click;
            btnCustomNetworkEvent.Click += BtnCustomNetworkEvent_Click;
            btnOpenAnrActivity.Click += BtnOpenAnrActivity_Click;
            btnOpenSecondActivity.Click += BtnOpenSecondActivity_Click;
            swiEnableApms.CheckedChange += SwiEnableApms_CheckedChange;

           

            AppInstrumentation.OnActivityCreateEnd();
            AppInstrumentation.ApplicationCreateEnd();
        }
        private void SwiEnableApms_CheckedChange(object sender, CompoundButton.CheckedChangeEventArgs e)
        {
            APMS.Instance.EnableCollection(swiEnableApms.Checked);
            Log.Info(TAG, "Enable APMS collection is " + swiEnableApms.Checked);
            Toast.MakeText(this, "Enable APMS collection is " + swiEnableApms.Checked, ToastLength.Short).Show();
        }
        protected override void OnStart()
        {
            AppInstrumentation.OnActivityStartBegin(this.GetType().Name);
            base.OnStart();
            AppInstrumentation.OnActivityStartEnd();
        }
        protected override void OnResume()
        {
            AppInstrumentation.OnActivityResumeBegin(this.GetType().Name);
            base.OnResume();
            AppInstrumentation.OnActivityResumeEnd();
        }
        protected override void OnRestart()
        {
            AppInstrumentation.OnActivityRestartBegin(this.GetType().Name);
            base.OnResume();
            AppInstrumentation.OnActivityRestartEnd();
        }
        private void BtnOpenSecondActivity_Click(object sender, System.EventArgs e)
        {
            // To trigger ActivityRender events.
            Intent secondActivityIntent = new Intent(this, typeof(SecondActivity));
            StartActivity(secondActivityIntent);
        }
        protected override void AttachBaseContext(Context context)
        {
            AppInstrumentation.AttachBaseContextBegin(context);
            base.AttachBaseContext(context);
            AppInstrumentation.AttachBaseContextEnd();

        }
        private void BtnOpenAnrActivity_Click(object sender, System.EventArgs e)
        {
            Intent anrActivityIntent = new Intent(this, typeof(AnrActivity));
            StartActivity(anrActivityIntent);
        }
        private void BtnNetwork_Click(object sender, System.EventArgs e)
        {
            HttpUtil.CreateRequest();
        }
        private void BtnCustomNetworkEvent_Click(object sender, System.EventArgs e)
        {
            HttpUtil httpUtil = new HttpUtil();
            httpUtil.CustomNetworkEvent();
        }

        private void BtnCustomNormalEvent_Click(object sender, System.EventArgs e)
        {
            CustomTrace customTrace = APMS.Instance.CreateCustomTrace("testCustomTrace");
            customTrace.Start();
            // code you want trace
            BusinessLogicStart(customTrace);
            BusinessLogicEnd(customTrace);
            customTrace.Stop();
        }

        public void BusinessLogicStart(CustomTrace customTrace)
        {
            customTrace.PutMeasure("PutMeasure", 0);
            for (int i = 0; i < 5; i++)
            {
                customTrace.IncrementMeasure("IncrementMeasure", 1);
            }
        }

        public void BusinessLogicEnd(CustomTrace customTrace)
        {
            customTrace.PutProperty("PutProperty", "propertyValue");
            customTrace.PutProperty("key", "value");
        }
    }
}