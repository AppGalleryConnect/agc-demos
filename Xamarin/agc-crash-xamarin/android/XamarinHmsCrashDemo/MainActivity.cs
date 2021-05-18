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
using Android.Views;
using Huawei.Agconnect.Crash;
using System.Collections.Generic;
using Huawei.Agconnect.Core;
using Huawei.Agconnect.Crash.Internal;
using Android.Media;
using Huawei.Hmf.Tasks;
using Huawei.Agconnect.Crash.Internal.Server;
using Huawei.Agconnect.Datastore.Core;
using Java.Lang;
using System.Runtime.InteropServices;
using System;
using Android.Opengl;
using Javax.Microedition.Khronos.Opengles;
using Android.Content;
using static Android.Provider.Settings;
using Android.Util;
using Org.Apache.Commons.Logging;
using Huawei.Agconnect.Crash.Internal.Bean;
using Huawei.Hms.Analytics;
using Huawei.Agconnect.Config;
using Android.Support.V4.App;
using Android.Content.PM;
using Huawei.Hms.Support.Log;

namespace XamarinHmsCrashDemo
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        const string TAG = "MainActivity";
        Switch swiEnableCollection, swiCustomBoolValue;
        EditText edtSetUserId, edtCustomValue;
        Spinner spnrCustomKey;
        Button btnAddLog, btnRecordException, btnCreateCrash;

        string customKey = "";
        bool customBoolValue = false;
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_main);

            ActivityCompat.RequestPermissions(this,
                    new string[] { Android.Manifest.Permission.WriteExternalStorage },
                    1);

            //Init view elements.
            btnCreateCrash = FindViewById<Button>(Resource.Id.btn_crash);
            btnAddLog = FindViewById<Button>(Resource.Id.btnSetLog);
            btnRecordException = FindViewById<Button>(Resource.Id.btnRecordException);
            swiEnableCollection = FindViewById<Switch>(Resource.Id.swiEnableCollection);
            edtSetUserId = FindViewById<EditText>(Resource.Id.edt_SetUserId);
            edtCustomValue = FindViewById<EditText>(Resource.Id.edt_CustomValue);
            spnrCustomKey = FindViewById<Spinner>(Resource.Id.spnr_CustomKey);
            swiCustomBoolValue = FindViewById<Switch>(Resource.Id.swiCustomBoolValue);

            //Events
            btnCreateCrash.Click += BtnCreateCrash_Click;
            btnAddLog.Click += BtnAddLog_Click;
            btnRecordException.Click += BtnRecordException_Click;
            swiEnableCollection.CheckedChange += SwiEnableCollection_CheckedChange;
            swiCustomBoolValue.CheckedChange += SwiCustomBoolValue_CheckedChange;


            spnrCustomKey.ItemSelected += new EventHandler<AdapterView.ItemSelectedEventArgs>(SpinnerItemSelected);
            var adapter = ArrayAdapter.CreateFromResource(
                    this, Resource.Array.customkey_arrays, Android.Resource.Layout.SimpleSpinnerItem);

            adapter.SetDropDownViewResource(Android.Resource.Layout.SimpleSpinnerDropDownItem);
            spnrCustomKey.Adapter = adapter;
            
        }

        private void SwiCustomBoolValue_CheckedChange(object sender, CompoundButton.CheckedChangeEventArgs e)
        {
            if (swiCustomBoolValue.Checked == true)
            {
                customBoolValue = true;
            }
            else
            {
                customBoolValue = false;
            }
        }

        private void SpinnerItemSelected(object sender, AdapterView.ItemSelectedEventArgs e)
        {
            Spinner spinner = (Spinner)sender;
            if (spinner.GetItemAtPosition(e.Position).Equals("Choose a Custom Key"))
            {
                Toast.MakeText(this, "Choose a Custom Key", ToastLength.Long).Show();
                customKey = "";
            }
            else if(spinner.GetItemAtPosition(e.Position).Equals("Int"))
            {
                string message = string.Format("The key is {0}", spinner.GetItemAtPosition(e.Position));
                Toast.MakeText(this, message, ToastLength.Short).Show();
                customKey = spinner.GetItemAtPosition(e.Position).ToString();
                edtCustomValue.InputType = Android.Text.InputTypes.ClassNumber;
                edtCustomValue.Text = int.MaxValue.ToString();
            }
            else if (spinner.GetItemAtPosition(e.Position).Equals("String"))
            {
                string message = string.Format("The key is {0}", spinner.GetItemAtPosition(e.Position));
                Toast.MakeText(this, message, ToastLength.Short).Show();
                customKey = spinner.GetItemAtPosition(e.Position).ToString();
                edtCustomValue.InputType = Android.Text.InputTypes.ClassText;
                edtCustomValue.Text = "example string";
            }
            else if (spinner.GetItemAtPosition(e.Position).Equals("Bool"))
            {
                string message = string.Format("The key is {0}", spinner.GetItemAtPosition(e.Position));
                Toast.MakeText(this, message, ToastLength.Short).Show();
                customKey = spinner.GetItemAtPosition(e.Position).ToString();
                edtCustomValue.Visibility = ViewStates.Gone;
                swiCustomBoolValue.Visibility = ViewStates.Visible;
            }
            else if (spinner.GetItemAtPosition(e.Position).Equals("Double"))
            {
                string message = string.Format("The key is {0}", spinner.GetItemAtPosition(e.Position));
                Toast.MakeText(this, message, ToastLength.Short).Show();
                customKey = spinner.GetItemAtPosition(e.Position).ToString();
                edtCustomValue.InputType = Android.Text.InputTypes.NumberFlagDecimal;
                edtCustomValue.Text = double.MaxValue.ToString();
            }
            else if (spinner.GetItemAtPosition(e.Position).Equals("Long"))
            {
                string message = string.Format("The key is {0}", spinner.GetItemAtPosition(e.Position));
                Toast.MakeText(this, message, ToastLength.Short).Show();
                customKey = spinner.GetItemAtPosition(e.Position).ToString();
                edtCustomValue.InputType = Android.Text.InputTypes.ClassText;
                edtCustomValue.Text = long.MaxValue.ToString();
            }
            else
            {
                string message = "Error";
                Toast.MakeText(this, message, ToastLength.Short).Show();
                customKey = "";
            }
        }
        private void SwiEnableCollection_CheckedChange(object sender, CompoundButton.CheckedChangeEventArgs e)
        {
            AGConnectCrash.Instance.EnableCrashCollection(swiEnableCollection.Checked);
            Toast.MakeText(this, "Enable crash collection is "+ swiEnableCollection.Checked, ToastLength.Short).Show();
        }

        private void BtnRecordException_Click(object sender, EventArgs e)
        {
            try
            {
                ArrayIndexOutOfBoundsException arrayIndexOutOfBoundsException = new ArrayIndexOutOfBoundsException();
                throw arrayIndexOutOfBoundsException;
            }
            catch (Java.Lang.Exception ex)
            {
                AGConnectCrash.Instance.RecordException(ex);
                Toast.MakeText(this, "Exception has been recorded.", ToastLength.Short).Show();
            }
        }

        private void BtnAddLog_Click(object sender, EventArgs e)
        {
            AGConnectCrash.Instance.Log("Crash service log example.");
            Toast.MakeText(this, "Log has been added.", ToastLength.Short).Show();
        }

        private void BtnCreateCrash_Click(object sender, EventArgs e)
        {
            string userId = edtSetUserId.Text.Trim();
            string customValue = edtCustomValue.Text.Trim();

            if (!userId.Equals(""))
            {
                AGConnectCrash.Instance.SetUserId(userId);
            }
            if (customKey.Equals("Bool"))
            {
                AGConnectCrash.Instance.SetCustomKey(customKey, customBoolValue);
            }
            else
            {
                if (!customValue.Equals(""))
                {
                    AGConnectCrash.Instance.SetCustomKey(customKey, customValue);
                }
            }

            AGConnectCrash.Instance.TestIt(this);
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Permission[] grantResults)
        {
            switch (requestCode)
            {
                case 1:
                    {
                        if (grantResults.Length > 0
                                  && grantResults[0] == Permission.Granted)
                        {
                            Toast.MakeText(this, "Permission granted to write your External storage", ToastLength.Short).Show();
                        }
                        else
                        {
                            Toast.MakeText(this, "Permission denied to write your External storage", ToastLength.Short).Show();
                        }
                        return;
                    }
            }
        }
    }

}