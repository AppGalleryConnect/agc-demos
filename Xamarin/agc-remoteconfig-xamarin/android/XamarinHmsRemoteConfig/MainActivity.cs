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
using Huawei.Agconnect.Remoteconfig;
using Huawei.Agconnect.Config;
using Android.Content;
using Android.Util;
using System.Collections.Generic;
using Huawei.Hmf.Tasks;
using Huawei.Hms.Aaid;
using Android.Text.Method;
using Huawei.Hms.Analytics;

namespace XamarinHmsRemoteConfig
{
    [Activity(Name = "Com.XamarinRemoteConfig.MainActivity", Label = "@string/app_name", Theme = "@style/AppTheme", MainLauncher =true)]
    public class MainActivity : AppCompatActivity
    {
        HiAnalyticsInstance instance;

        private static readonly string TAG = "RemoteConfig";

        public static AGConnectConfig AGCRemoteConfig;

        private Button FetchApplyButton;

        private Button FetchSaveButton;

        private Button ClearButton;

        private Switch DeveloperMode;

        private TextView ResultLog;

        public static  bool ShowLogOnSuccess;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            SetContentView(Resource.Layout.activity_main);

            FetchApplyButton = FindViewById<Button>(Resource.Id.FetchApply);
            FetchSaveButton = FindViewById<Button>(Resource.Id.FetchSave);
            ClearButton = FindViewById<Button>(Resource.Id.Clear);
            DeveloperMode = FindViewById<Switch>(Resource.Id.DeveloperMode);
            ResultLog = (TextView)FindViewById(Resource.Id.log_info);
            ResultLog.MovementMethod = ScrollingMovementMethod.Instance;

            //Set User attributes defined in AGConnect Console
            instance = HiAnalytics.GetInstance(this);
            instance.SetUserProfile("favorite_color", "Black");
            instance.SetUserProfile("favorite_food", "banana");

            AGCRemoteConfig = AGConnectConfig.Instance;

            if (AGCRemoteConfig != null)
            {
                Log.Info("RC", "AGConnectConfig instance cratead successfully.");
            }

            FetchApplyButton.Click += delegate { FetchApply(); };
            FetchSaveButton.Click += delegate { LoadLastFetchedConfig(); };
            ClearButton.Click += delegate { 
                AGCRemoteConfig.ClearAll();
                Logger("All Values Cleared", TAG);
                ApplyDefualtByMap();
                ShowAllValues();
            };

            DeveloperMode.CheckedChange += delegate (object sender, CompoundButton.CheckedChangeEventArgs e)
            {
                AGCRemoteConfig.SetDeveloperMode(e.IsChecked);
                if (e.IsChecked)
                Toast.MakeText(this, "DeveloperMode Enabled", ToastLength.Long).Show();
                else Toast.MakeText(this, "DeveloperMode Disabled", ToastLength.Long).Show();
            };

            ApplyDefualtByFile();
            ShowAllValues();
            GetToken();

        }
        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Xamarin.Essentials.Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        protected override void AttachBaseContext(Context context)
        {
            base.AttachBaseContext(context);
            AGConnectServicesConfig config = AGConnectServicesConfig.FromContext(context);
            config.OverlayWith(new HmsLazyInputStream(context));
        }

        public void FetchApply() {
            ShowLogOnSuccess = true;
            Fetch();
        }

        public void LoadLastFetchedConfig()
        {
            IConfigValues configValues = AGCRemoteConfig.LoadLastFetched();
            if (configValues.ContainKey("IsEnglish") || configValues.ContainKey("Region")|| configValues.ContainKey("Food_Preference") || configValues.ContainKey("Color_Preference"))
            AGCRemoteConfig.Apply(configValues);
            ShowAllValues();
            ShowLogOnSuccess = false;
            Fetch();
        }


        private void ApplyDefualtByMap()
        {
            IDictionary<string, Java.Lang.Object> ConfigVariables = new Dictionary<string, Java.Lang.Object>();
            ConfigVariables.Add("Region", "Default");
            ConfigVariables.Add("Food_Preference", "Default");
            ConfigVariables.Add("IsEnglish", "Default");
            ConfigVariables.Add("Color_Preference", "Default");
            AGCRemoteConfig.ApplyDefault(ConfigVariables);
        }

        private void ApplyDefualtByFile()
        {
            AGCRemoteConfig.ApplyDefault(Resource.Xml.RemoteConfig);
        }
        public void Fetch()
        {
            long fetchInterval;
            fetchInterval = 12 * 60 * 60;

            ISharedPreferences sharedPreferences = this.GetSharedPreferences("Remote_Config", FileCreationMode.Private);
            if (sharedPreferences.GetBoolean("DATA_OLD", false))
            {
                fetchInterval = 0;

            }
            
            AGCRemoteConfig.Fetch(fetchInterval).AddOnSuccessListener(new TaskListener(this)).AddOnFailureListener(new TaskListener(this));

        }

        public void Logger(string str, string tag)
        {
            Log.Info(tag, str);
            ResultLog.Append(str + System.Environment.NewLine);
            int offset = ResultLog.LineCount * ResultLog.LineHeight;
            if (offset > ResultLog.Height)
            {
                ResultLog.ScrollTo(0, offset - ResultLog.Height);
            }
        }
        public void ShowAllValues()
        {
            IDictionary<string, Java.Lang.Object> RCVariables = new Dictionary<string, Java.Lang.Object>();
            RCVariables = AGCRemoteConfig.MergedAll;

            string values = "";
            values = values + "Region" + " : " + AGCRemoteConfig.GetValueAsString("Region")+ " \\ Source: " + AGCRemoteConfig.GetSource("Region") + "\n";
            values = values + "Food_Preference" + " : " + AGCRemoteConfig.GetValueAsString("Food_Preference") + " \\ Source: " + AGCRemoteConfig.GetSource("Food_Preference") + "\n"; 
            values = values + "IsEnglish" + " : " + AGCRemoteConfig.GetValueAsBoolean("IsEnglish").ToString() + " \\ Source: " + AGCRemoteConfig.GetSource("IsEnglish") + "\n"; 
            values = values + "Color_Preference" + " : " + AGCRemoteConfig.GetValueAsString("Color_Preference") + " \\ Source: " + AGCRemoteConfig.GetSource("Color_Preference") + "\n"; 
            
            //Another way to display all value by using foreach
            /*foreach (KeyValuePair<string, Java.Lang.Object> kvp in RCVariables)
            {
                values = values + kvp.Key + " : " + kvp.Value + " Source: " + AGCRemoteConfig.GetSource(kvp.Key)+ "\n";
            }*/

            Logger(values, TAG);

        }

        private void GetToken()
        {
            System.Threading.Thread thread = new System.Threading.Thread(() =>
            {
                try
                {
                    string appid = AGConnectServicesConfig.FromContext(this).GetString("client/app_id");
                    string token = HmsInstanceId.GetInstance(this).GetToken(appid, "HCM");
                    Log.Info(TAG, "token = " + token);


                }
                catch (Java.Lang.Exception e)
                {
                    Log.Info(TAG, e.ToString());
                }
            }
                );
            Log.Info(TAG, "start the thread");
            thread.Start();
        }

        public class TaskListener : Java.Lang.Object, IOnSuccessListener, IOnFailureListener
        {
            MainActivity Context;

            public TaskListener(MainActivity context)
            {

                this.Context = context;
            }
            public void OnSuccess(Java.Lang.Object result)
            {
                if (result == null)
                {
                    Context.Logger("result is null", MainActivity.TAG);
                    return;
                }
                Context.Logger("Fetch Success",MainActivity.TAG);
                IConfigValues configValues = (IConfigValues)result;
                MainActivity.AGCRemoteConfig.Apply(configValues);
                if (MainActivity.ShowLogOnSuccess)
                Context.ShowAllValues();
                ISharedPreferences sharedPreferences = Context.GetSharedPreferences("Remote_Config", FileCreationMode.Private);
                ISharedPreferencesEditor editor = sharedPreferences.Edit();
                editor.PutBoolean("DATA_OLD", false).Apply();
            }

            public void OnFailure(Java.Lang.Exception e)
            {
                Context.Logger("Fetch Field", MainActivity.TAG);
                Context.Logger(e.Message,MainActivity.TAG);
            }
        }
    }

}