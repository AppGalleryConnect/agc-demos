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
using Huawei.Agconnect.Auth;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace XamarinAuthDemo
{
    [Activity(Label = "SettingsActivity")]
    public class SettingsActivity : Activity
    {
        private const string TAG = "SettingsActivity";
        private RelativeLayout deleteUser;
        private RelativeLayout updateEmail;
        private RelativeLayout updatePhone;
        private RelativeLayout updatePassword;
        private RelativeLayout resetPassword;
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.activity_settings);
            InitView();
        }

        private void InitView()
        {
            deleteUser = FindViewById<RelativeLayout>(Resource.Id.layoutDelete);
            updateEmail = FindViewById<RelativeLayout>(Resource.Id.layoutUpdateEmail);
            updatePhone = FindViewById<RelativeLayout>(Resource.Id.layoutUpdatePhone);
            updatePassword = FindViewById<RelativeLayout>(Resource.Id.layoutUpdatePassword);
            resetPassword = FindViewById<RelativeLayout>(Resource.Id.layoutResetPassword);

            deleteUser.Click += DeleteUser_Click;
            updateEmail.Click += UpdateEmail_Click;
            updatePhone.Click += UpdatePhone_Click;
            updatePassword.Click += UpdatePassword_Click;
            resetPassword.Click += ResetPassword_Click;
        }

        private void ResetPassword_Click(object sender, EventArgs e)
        {
            View view = LayoutInflater.From(this).Inflate(Resource.Layout.dialog_reset_password, null, false);
            AlertDialog dialog = new AlertDialog.Builder(this).SetView(view).Create();
            EditText edtEmail = view.FindViewById<EditText>(Resource.Id.edtEmail);
            EditText edtNewPassword = view.FindViewById<EditText>(Resource.Id.edtNewPassword);
            EditText edtVerifyCode = view.FindViewById<EditText>(Resource.Id.edtVerifyCode);
            Button btnSend = view.FindViewById<Button>(Resource.Id.btnSend);
            Button btnUpdate = view.FindViewById<Button>(Resource.Id.btnUpdate);

            btnSend.Click += async delegate
            {
                string email = edtEmail.Text.ToString().Trim();
                // Build a verify code settings.
                VerifyCodeSettings settings = VerifyCodeSettings.NewBuilder().Action(VerifyCodeSettings.ActionResetPassword).Build();
                // Request verify code,and waiting for the verification code to be sent to your mobile phone
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
            };

            btnUpdate.Click += async delegate
            {
                string email = edtEmail.Text.ToString().Trim();
                string newPassword = edtNewPassword.Text.ToString().Trim();
                string verifyCode = edtVerifyCode.Text.ToString().Trim();
                // Reset password 
                try
                {
                    var resetPasswordResult = AGConnectAuth.Instance.ResetPasswordAsync(email, newPassword, verifyCode);
                    await resetPasswordResult;
                    if (resetPasswordResult.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                    {
                        Toast.MakeText(this, "Password has been reset.", ToastLength.Short).Show();
                    }
                }
                catch (Exception ex)
                {
                    Toast.MakeText(this, "Reset password failed: " + ex.Message, ToastLength.Long).Show();
                }
                dialog.Dismiss();
            };
            dialog.Show();
        }

        private void UpdatePassword_Click(object sender, EventArgs e)
        {
            View view = LayoutInflater.From(this).Inflate(Resource.Layout.dialog_password, null, false);
            AlertDialog dialog = new AlertDialog.Builder(this).SetView(view).Create();
            EditText edtNewPassword = view.FindViewById<EditText>(Resource.Id.edtNewPassword);
            EditText edtVerifyCode = view.FindViewById<EditText>(Resource.Id.edtVerifyCode);
            Button btnSend = view.FindViewById<Button>(Resource.Id.btnSend);
            Button btnUpdate = view.FindViewById<Button>(Resource.Id.btnUpdate);
            if (AGConnectAuth.Instance.CurrentUser != null)
            {
                btnSend.Click += async delegate
                {
                    // Build a verify code settings.
                    VerifyCodeSettings settings = VerifyCodeSettings.NewBuilder().Action(VerifyCodeSettings.ActionResetPassword).Build();
                    // Request verify code,and waiting for the verification code to be sent to your mobile phone
                    try
                    {
                        var requestVerifyCode = AGConnectAuth.Instance.RequestVerifyCodeAsync(AGConnectAuth.Instance.CurrentUser.Email, settings);
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
                };

                btnUpdate.Click += async delegate
                {
                    string newPassword = edtNewPassword.Text.ToString().Trim();
                    string verifyCode = edtVerifyCode.Text.ToString().Trim();
                    // Update password
                    try
                        {
                            var updatePasswordResult = AGConnectAuth.Instance.CurrentUser.UpdatePasswordAsync(newPassword, verifyCode, AGConnectAuthCredential.EmailProvider);
                            await updatePasswordResult;
                            if (updatePasswordResult.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                            {
                                Toast.MakeText(this, "Password has been updated.", ToastLength.Short).Show();
                            }
                        }
                        catch (Exception ex)
                        {
                            Toast.MakeText(this, "Update password failed: " + ex.Message, ToastLength.Long).Show();
                        }
                    dialog.Dismiss();
                };
            }
            dialog.Show();
        }

        private void UpdatePhone_Click(object sender, EventArgs e)
        {
            View view = LayoutInflater.From(this).Inflate(Resource.Layout.dialog_phone, null, false);
            AlertDialog dialog = new AlertDialog.Builder(this).SetView(view).Create();
            EditText edtCountryCode = view.FindViewById<EditText>(Resource.Id.edtCountryCode);
            EditText edtAccount = view.FindViewById<EditText>(Resource.Id.edtAccount);
            EditText edtVerifyCode = view.FindViewById<EditText>(Resource.Id.edtVerifyCode);
            Button btnSend = view.FindViewById<Button>(Resource.Id.btnSend);
            Button btnUpdate = view.FindViewById<Button>(Resource.Id.btnUpdate);

            btnSend.Click += async delegate
            {
                string countryCode = edtCountryCode.Text.ToString().Trim();
                string phoneNumber = edtAccount.Text.ToString().Trim();

                // Build a verify code settings.
                VerifyCodeSettings settings = VerifyCodeSettings.NewBuilder().Action(VerifyCodeSettings.ActionRegisterLogin).Build();
                // Request verify code,and waiting for the verification code to be sent to your mobile phone
                try
                {
                    var requestVerifyCode = AGConnectAuth.Instance.RequestVerifyCodeAsync(countryCode, phoneNumber, settings);
                    VerifyCodeResult verifyCodeResult = await requestVerifyCode;
                    if (requestVerifyCode.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                    {
                        Toast.MakeText(this, "Send phone verify code success! ", ToastLength.Short).Show();
                    }
                }
                catch (Exception ex)
                {
                    Toast.MakeText(this, ex.Message, ToastLength.Long).Show();
                }
            };

            btnUpdate.Click += async delegate
            {
                string countryCode = edtCountryCode.Text.ToString().Trim();
                string phoneNumber = edtAccount.Text.ToString().Trim();
                string verifyCode = edtVerifyCode.Text.ToString().Trim();
                if (AGConnectAuth.Instance.CurrentUser != null)
                {
                    // Update phone
                    try
                    {
                        var requestUpdatePhone = AGConnectAuth.Instance.CurrentUser.UpdatePhoneAsync(countryCode, phoneNumber, verifyCode);
                        await requestUpdatePhone;
                        if (requestUpdatePhone.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                        {
                            Toast.MakeText(this, "Update phone success! ", ToastLength.Short).Show();
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.MakeText(this, "Update phone failed! "+ ex.Message, ToastLength.Short).Show();
                    }
                }
                dialog.Dismiss();
            };
            dialog.Show();

        }

        private void UpdateEmail_Click(object sender, EventArgs e)
        {
            View view = LayoutInflater.From(this).Inflate(Resource.Layout.dialog_email, null, false);
            AlertDialog dialog = new AlertDialog.Builder(this).SetView(view).Create();
            EditText edtAccount = view.FindViewById<EditText>(Resource.Id.edtAccount);
            EditText edtVerifyCode = view.FindViewById<EditText>(Resource.Id.edtVerifyCode);
            Button btnSend = view.FindViewById<Button>(Resource.Id.btnSend);
            Button btnUpdate = view.FindViewById<Button>(Resource.Id.btnUpdate);

            btnSend.Click += async delegate
            {
                string email = edtAccount.Text.ToString().Trim();

                // Build a verify code settings.
                VerifyCodeSettings settings = VerifyCodeSettings.NewBuilder().Action(VerifyCodeSettings.ActionRegisterLogin).Build();
                // Request verify code,and waiting for the verification code to be sent to your mobile phone
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
            };

            btnUpdate.Click += async delegate
            {
                string email = edtAccount.Text.ToString().Trim();
                string verifyCode = edtVerifyCode.Text.ToString().Trim();
                if (AGConnectAuth.Instance.CurrentUser != null)
                {
                    // Update phone
                    try
                    {
                        var requestUpdateEmail = AGConnectAuth.Instance.CurrentUser.UpdateEmailAsync(email, verifyCode);
                        await requestUpdateEmail;
                        if (requestUpdateEmail.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                        {
                            Toast.MakeText(this, "Update email success! ", ToastLength.Short).Show();
                        }
                    }
                    catch (Exception ex)
                    {
                        Toast.MakeText(this, "Update email failed! " + ex.Message, ToastLength.Short).Show();
                    }
                }
                dialog.Dismiss();
            };
            dialog.Show();
        }

        private async void DeleteUser_Click(object sender, EventArgs e)
        {
            // Delete user
            try
            {
                var deleteUserRequest = AGConnectAuth.Instance.DeleteUserAsync();
                await deleteUserRequest;
                if (deleteUserRequest.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                {
                    Toast.MakeText(this, "Delete user success! ", ToastLength.Short).Show();
                    StartActivity(new Intent(this, typeof(LoginActivity)));
                    Finish();
                }
            }
            catch (Exception ex)
            {
                Toast.MakeText(this, "Delete user failed! " + ex.Message,ToastLength.Long).Show();
            }
        }
    }
}