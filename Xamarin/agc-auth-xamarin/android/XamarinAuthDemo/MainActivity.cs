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
using System;
using Huawei.Agconnect.Auth;
using AlertDialog = Android.Support.V7.App.AlertDialog;
using Android.Content;
using Android.Text;
using Android.Views;
using Square.Picasso;
using Huawei.Agconnect.Core.Service.Auth;
using System.Collections.Generic;
using Android.Util;

namespace XamarinAuthDemo
{
    [Activity(Label = "@string/app_name", Theme = "@style/ActionBarTheme")]
    public class MainActivity : AppCompatActivity
    {
        private ImageView imgPhoto;
        private TextView tvUid;
        private TextView tvNickName;
        private TextView tvEmail;
        private TextView tvPhone;
        private Button btnSignOut;
        private Button btnUpdateName;
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            Xamarin.Essentials.Platform.Init(this, savedInstanceState);
            // Set our view from the "main" layout resource
            SetContentView(Resource.Layout.activity_main);
            InitView();
            ShowUserInfo();
        }

        private void ShowUserInfo()
        {
            AGConnectUser agConnectUser = AGConnectAuth.Instance.CurrentUser;
            string uid = agConnectUser.Uid;
            string nickName = agConnectUser.DisplayName;
            string email = agConnectUser.Email;
            string phone = agConnectUser.Phone;
            string photoUrl = agConnectUser.PhotoUrl;

            tvUid.Text = uid;
            tvNickName.Text = nickName;
            tvEmail.Text = email;
            tvPhone.Text = phone;
            if (!TextUtils.IsEmpty(photoUrl))
            {
                Picasso.Get().Load(photoUrl).Into(imgPhoto);
            }
        }

        private void InitView()
        {
            imgPhoto = FindViewById<ImageView>(Resource.Id.imgPhoto);
            tvUid = FindViewById<TextView>(Resource.Id.tvUid);
            tvNickName = FindViewById<TextView>(Resource.Id.tvNickName);
            tvEmail = FindViewById<TextView>(Resource.Id.tvEmail);
            tvPhone = FindViewById<TextView>(Resource.Id.tvPhone);
            btnSignOut = FindViewById<Button>(Resource.Id.btnSignOut);
            btnUpdateName = FindViewById<Button>(Resource.Id.btnUpdateName);

            btnSignOut.Click += BtnSignOut_Click;
            imgPhoto.Click += ImgPhoto_Click;
            btnUpdateName.Click += BtnUpdateName_Click;
        }
        
        private void BtnUpdateName_Click(object sender, EventArgs e)
        {
            Update(new UpdateListener(this));        
        }
        private void Update(IUpdateInterface updateInterface)
        {
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.LayoutParameters = new LinearLayout.LayoutParams(Android.Views.ViewGroup.LayoutParams.MatchParent, Android.Views.ViewGroup.LayoutParams.WrapContent);
            linearLayout.Orientation = Orientation.Vertical;
            EditText editText = new EditText(this);
            linearLayout.AddView(editText);
            AlertDialog.Builder dialogBuilder =
            new AlertDialog.Builder(this).SetTitle("Update");
            dialogBuilder.SetPositiveButton("UPDATE", delegate
            {
                dialogBuilder.Dispose();
                // input photoUrl or display name
                string data = editText.Text.ToString().Trim();
                if (!TextUtils.IsEmpty(data))
                {
                    updateInterface.OnUpdate(data);
                }
            });
            dialogBuilder.SetView(linearLayout);
            dialogBuilder.Show();
        }
        private void ImgPhoto_Click(object sender, EventArgs e)
        {
            Update(new UpdateListener(this));
        }

        private void BtnSignOut_Click(object sender, EventArgs e)
        {
            if (AGConnectAuth.Instance.CurrentUser != null)
            {
                // Sign out
                AGConnectAuth.Instance.SignOut();
                StartActivity(new Android.Content.Intent(this, typeof(LoginActivity)));
                Finish();
            }
        }

        public override bool OnCreateOptionsMenu(IMenu menu)
        {
            // Set the menu layout on Main Activity  
            MenuInflater.Inflate(Resource.Menu.mainMenu, menu);
            return base.OnCreateOptionsMenu(menu);
        }

        public override bool OnOptionsItemSelected(IMenuItem item)
        {
            switch (item.ItemId)
            {
                case Resource.Id.menuSettings:
                    {
                        Intent settingsIntent = new Intent(this, typeof(SettingsActivity));
                        StartActivity(settingsIntent);
                        return true;
                    }
            }

            return base.OnOptionsItemSelected(item);
        }

        private class UpdateListener : IUpdateInterface
        {
            MainActivity mainActivity;
            public UpdateListener(MainActivity mainActivity)
            {
                this.mainActivity = mainActivity;
            }
            public async void OnUpdate(string data)
            {
                if (AGConnectAuth.Instance.CurrentUser != null)
                {
                    if (data.Contains("https"))
                    {
                        ProfileRequest profileRequest = new ProfileRequest.Builder()
                            .SetPhotoUrl(data)
                            .Build();

                        // Update profile
                        try
                        {
                            var updateProfileResult = AGConnectAuth.Instance.CurrentUser.UpdateProfileAsync(profileRequest);
                            await updateProfileResult;
                            if (updateProfileResult.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                            {
                                mainActivity.ShowUserInfo();
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.MakeText(mainActivity, "Update profile failed: " + ex.Message, ToastLength.Long).Show();
                        }
                    }
                    else
                    {
                        // create a profileRequest
                        ProfileRequest userProfile = new ProfileRequest.Builder()
                            .SetDisplayName(data)
                            .Build();

                        // Update profile
                        try
                        {
                            var updateProfileResult = AGConnectAuth.Instance.CurrentUser.UpdateProfileAsync(userProfile);
                            await updateProfileResult;
                            if (updateProfileResult.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                            {
                                mainActivity.ShowUserInfo();
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.MakeText(mainActivity, "Update profile failed: " + ex.Message, ToastLength.Long).Show();
                        }
                    }
                }
            }
        }
    }
}