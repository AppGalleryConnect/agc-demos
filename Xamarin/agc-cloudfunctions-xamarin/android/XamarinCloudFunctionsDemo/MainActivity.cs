/*
        Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

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
using Huawei.Agconnect.Function;
using Android.Util;
using System;
using Newtonsoft.Json;
using Android.Views;
using Java.Util.Concurrent;

namespace XamarinCloudFunctionsDemo
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        private const string TAG = "MainACtivity";
        Button BtnSum;
        EditText EdtNumberOne, EdtNumberTwo;
        TextView TvResult;
        AGConnectFunction Function;
        JavaDictionary ParametersDictionary;
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);

            SetContentView(Resource.Layout.activity_main);

            //Init View
            BtnSum = FindViewById<Button>(Resource.Id.btnSum);
            EdtNumberOne = FindViewById<EditText>(Resource.Id.edtNumberOne);
            EdtNumberTwo = FindViewById<EditText>(Resource.Id.edtNumberTwo);
            TvResult = FindViewById<TextView>(Resource.Id.tvResult);
            Function = AGConnectFunction.Instance;

            ParametersDictionary = new JavaDictionary();

            //Click Event
            BtnSum.Click += BtnSum_Click;
        }

        private async void BtnSum_Click(object sender, System.EventArgs e)
        {
            int numberOneVal = Convert.ToInt32(EdtNumberOne.Text.Trim().ToString());
            int numberTwoVal = Convert.ToInt32(EdtNumberTwo.Text.Trim().ToString());

            // Add the parameters that will be sent to the function in the cloud 
            ParametersDictionary["NumberOne"] = numberOneVal;
            ParametersDictionary["NumberTwo"] = numberTwoVal;

            // Replace "withparameter-$latest" with the name of the function defined in AGConnect
            IFunctionCallable FunctionCallable = Function.Wrap("withparameter-$latest");

            FunctionCallable.SetTimeout(3, TimeUnit.Seconds);
            var CallTask = FunctionCallable.CallAsync(ParametersDictionary);

            FindViewById(Resource.Id.progressBar1).Visibility = ViewStates.Visible;
            FindViewById(Resource.Id.content).Visibility = ViewStates.Gone;
            try
            {
                IFunctionResult FunctionResult = await CallTask;
                if (CallTask.IsCompleted && CallTask.Exception == null)
                {
                    string JsonResult = FunctionResult.Value.ToString();
                    Log.Debug(TAG, JsonResult);

                    dynamic Result = JsonConvert.DeserializeObject(JsonResult);

                    string Sum = Result.result;
                    TvResult.Text = Sum;
                }
                else 
                    Log.Error(TAG, "Call Failed: " + CallTask.Exception.Message);
            }
            catch (Exception ex)
            {
                Log.Error(TAG, "Call Failed: " + ex.Message);
            }
            FindViewById(Resource.Id.progressBar1).Visibility = ViewStates.Gone;
            FindViewById(Resource.Id.content).Visibility = ViewStates.Visible;
        }
    }
}