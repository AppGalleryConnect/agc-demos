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
using Android.Runtime;
using Android.Views;
using Android.Widget;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace XamarinApmsDemo
{
    [Activity(Label = "AnrActivity")]
    public class AnrActivity : Activity
    {
        Button btnCreateAnr;
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.activity_anr);
            // Init views
            btnCreateAnr = FindViewById<Button>(Resource.Id.btnCreateAnr);

            // Events
            btnCreateAnr.Click += BtnCreateAnr_Click;
        }

        private void BtnCreateAnr_Click(object sender, EventArgs e)
        {
            Task.Delay(60000).Wait();
        }
    }
}