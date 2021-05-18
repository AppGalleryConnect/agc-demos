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
using Android.Support.V7.App;
using Android.Views;
using Android.Widget;
using Huawei.Agconnect.Auth;
using Java.Util;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using ActionBar = Android.Support.V7.App.ActionBar;

namespace XamarinAuthDemo
{
    [Activity(Label = "RegisterActivity", Theme = "@style/ActionBarTheme")]
    public class RegisterActivity : AppCompatActivity
    {
        private EditText edtCountryCode;
        private EditText edtAccount;
        private EditText edtPassword;
        private EditText edtVerifyCode;
        private TextView tvAccount;
        private Type type;
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.activity_register);
            type = (Type)Intent.GetByteExtra("registerType",0);
            InitView();
        }

        private void InitView()
        {
            tvAccount = FindViewById<TextView>(Resource.Id.tvAccount);
            edtCountryCode = FindViewById<EditText>(Resource.Id.edtCountryCode);
            edtAccount = FindViewById<EditText>(Resource.Id.edtAccount);
            edtPassword = FindViewById<EditText>(Resource.Id.edtPassword);
            edtVerifyCode = FindViewById<EditText>(Resource.Id.edtVerifyCode);
            LinearLayout layoutCountryCode = FindViewById<LinearLayout>(Resource.Id.layoutCountryCode);
            if(type == Type.Email)
            {
                tvAccount.Text = GetString(Resource.String.email);
                layoutCountryCode.Visibility = ViewStates.Gone;
            }
            else
            {
                tvAccount.Text = GetString(Resource.String.phone);
                layoutCountryCode.Visibility = ViewStates.Visible;
            }

            Button btnRegister = FindViewById<Button>(Resource.Id.btnRegister);
            Button btnSend = FindViewById<Button>(Resource.Id.btnSend);

            btnSend.Click += BtnSend_Click;
            btnRegister.Click += BtnRegister_Click;
        }
        public override bool OnOptionsItemSelected(IMenuItem item)
        {
            switch (item.ItemId)
            {
                case Android.Resource.Id.Home:
                    Finish();
                    return true;

                default:
                    return base.OnOptionsItemSelected(item);
            }
        }
        private void BtnSend_Click(object sender, EventArgs e)
        {
            SendVerificationCode();
        }

        private async void BtnRegister_Click(object sender, EventArgs e)
        {
            if(type == Type.Email)
            {
                string email = edtAccount.Text.ToString().Trim();
                string password = edtPassword.Text.ToString().Trim();
                string verifyCode = edtVerifyCode.Text.ToString().Trim();

                // Build e-mail user.
                EmailUser emailUser = new EmailUser.Builder()
                    .SetEmail(email)
                    .SetPassword(password)//optional,if you set a password, you can log in directly using the password next time.
                    .SetVerifyCode(verifyCode)
                    .Build();
                try
                {
                    // Create e-mail user.
                    var emailUserResult = AGConnectAuth.Instance.CreateUserAsync(emailUser);
                    ISignInResult signInResult = await emailUserResult;
                    if (emailUserResult.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                    {
                        // After a user is created, the user has logged in by default.
                        StartActivity(new Intent(this, typeof(MainActivity)));
                    }
                }
                catch (Exception ex)
                {
                    Toast.MakeText(this, "Create User Fail:" + ex.Message, ToastLength.Long).Show();
                }
            }
            else
            {
                string countryCode = edtCountryCode.Text.ToString().Trim();
                string phoneNumber = edtAccount.Text.ToString().Trim();
                string password = edtPassword.Text.ToString().Trim();
                string verifyCode = edtVerifyCode.Text.ToString().Trim();

                // Build phone user.
                PhoneUser phoneUser = new PhoneUser.Builder()
                    .SetCountryCode(countryCode)
                    .SetPhoneNumber(phoneNumber)
                    .SetPassword(password)
                    .SetVerifyCode(verifyCode)
                    .Build();

                try
                {
                    // Create phoneUser user.
                    var phoneUserResult = AGConnectAuth.Instance.CreateUserAsync(phoneUser);
                    ISignInResult signInResult = await phoneUserResult;
                    if (phoneUserResult.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                    {
                        // After a user is created, the user has logged in by default.
                        StartActivity(new Intent(this, typeof(MainActivity)));
                    }
                }
                catch (Exception ex)
                {
                    Toast.MakeText(this,"Create User Fail:" + ex.Message, ToastLength.Long).Show();
                }
            }
        }
        private async void SendVerificationCode()
        {
            VerifyCodeSettings settings = VerifyCodeSettings.NewBuilder()
                .Action(VerifyCodeSettings.ActionRegisterLogin)
                .SendInterval(30)
                .Locale(Locale.English)
                .Build();

            if (type == Type.Email)
            {
                string email = edtAccount.Text.ToString().Trim();

                try
                {
                    var requestVerifyCode = AGConnectAuth.Instance.RequestVerifyCodeAsync(email, settings);
                    VerifyCodeResult verifyCodeResult = await requestVerifyCode;
                    if (requestVerifyCode.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                    {
                        Toast.MakeText(this, "Send email verify code success! ", ToastLength.Short).Show();
                    }
                }
                catch (Exception ex)
                {
                    Toast.MakeText(this, ex.Message, ToastLength.Long).Show();
                }

            }
            else
            {
                string countryCode = edtCountryCode.Text.ToString().Trim();
                string phoneNumber = edtAccount.Text.ToString().Trim();

                try
                {
                    var requestVerifyCode = AGConnectAuth.Instance.RequestVerifyCodeAsync(countryCode, phoneNumber, settings);
                    VerifyCodeResult verifyCodeResult = await requestVerifyCode;
                    if (requestVerifyCode.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                    {
                        Toast.MakeText(this, "Send email verify code success! ", ToastLength.Short).Show();
                    }
                }
                catch (Exception ex)
                {
                    Toast.MakeText(this, ex.Message, ToastLength.Long).Show();
                }
            }
        }
    }
}