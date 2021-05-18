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




using Android.App;
using Android.Content;
using Android.Net;
using Android.OS;
using Android.Runtime;
using Android.Support.V7.App;
using Android.Widget;
using Huawei.Agconnect.Applinking;

namespace AGCAppLinkingXamarinAndroidDemo
{
    [Activity(Name = "com.company.app.MainActivity", Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {

        protected override async void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);


            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.activity_main);
            FindViewById<Button>(Resource.Id.btnCreateAppLink).Click += CreateAppLink_Click;
        }

        private void CreateAppLink_Click(object sender, System.EventArgs e)
        {
            Intent intent = new Intent(this, typeof(AppLinkActivity));
            StartActivity(intent);
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);
            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}