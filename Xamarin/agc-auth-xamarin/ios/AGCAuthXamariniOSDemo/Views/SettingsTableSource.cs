/*
 * Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

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
using System.Collections.Generic;
using System.Linq;
using Foundation;
using Huawei.Agconnect.AgconnectCore;
using Huawei.Agconnect.Auth;
using UIKit;

namespace AGCAuthXamariniOSDemo.Views
{
    public class SettingsTableSource : UITableViewSource
    {

        protected string[] tableItems;
        protected string cellIdentifier = "TableCell";
        SettingsViewController owner;

        public SettingsTableSource(SettingsViewController owner)
        {
            this.owner = owner;

            var currentProvider = AGCAuth.GetInstance().CurrentUser?.ProviderId;
            if (currentProvider == AGCAuthProviderType.Email)
            {
                tableItems = new string[] { "Update User Profile", "Update Email", "Update Email Password" };
            }
            else if (currentProvider == AGCAuthProviderType.Phone)
            {
                tableItems = new string[] { "Update User Profile", "Update Phone", "Update Phone Password" };
            }
            else
            {
                tableItems = new string[] { "Update User Profile" };
            }
        }



        public override nint NumberOfSections(UITableView tableView)
        {
            return 1;
        }


        public override nint RowsInSection(UITableView tableview, nint section)
        {
            return tableItems.Length;
        }


        public override void RowSelected(UITableView tableView, NSIndexPath indexPath)
        {
            tableView.DeselectRow(indexPath, true);
            var title = tableItems[indexPath.Row];
            switch (title)
            {
                case "Update User Profile":
                    UpdateProfile();
                    break;
                case "Update Email":
                    UpdateEmail();
                    break;
                case "Update Phone":
                    UpdatePhone();
                    break;
                case "Update Email Password":
                    UpdateEmailPassword();
                    break;
                case "Update Phone Password":
                    UpdatePhonePassword();
                    break;
                default:
                    Console.WriteLine("none");
                    break;
            }
        }


        public override UITableViewCell GetCell(UITableView tableView, NSIndexPath indexPath)
        {
            UITableViewCell cell = tableView.DequeueReusableCell(cellIdentifier);
            string item = tableItems[indexPath.Row];

            if (cell == null)
            { cell = new UITableViewCell(UITableViewCellStyle.Default, cellIdentifier); }

            cell.TextLabel.Text = item;

            return cell;
        }


        private void UpdateProfile()
        {
            CreateAlert("Update Profile", "", new[] { "DisplayName", "PhotoUrl" }, (resultArr) =>
             {
                 var request = new AGCProfileRequest();
                 request.DisplayName = resultArr[0];
                 request.PhotoUrl = resultArr[1];
                 // update the profile of the current user
                 var profileReq = AGCAuth.GetInstance().CurrentUser?.UpdateProfile(request);

                 profileReq.AddOnSuccessCallback((result) =>
                 {
                     Console.WriteLine("profile update success");
                 });
                 profileReq.AddOnFailureCallback((error) =>
                 {
                     Console.WriteLine("profile update failed");
                 });
             });
        }

        private void UpdateEmail()
        {
            CreateAlert("Send Verification Code", "", new[] { "Email" }, (resultArr) =>
            {
                var email = resultArr[0];
                BaseProvider.SendVerifyCodeWithEmail(email, AGCVerifyCodeAction.RegisterLogin);
                CreateAlert("Update Email", "", new[] { "Email", "Verification Code" }, (updateArr) =>
                {
                    var updatedEmail = updateArr[0];
                    var code = updateArr[1];
                    HMFTask<NSObject> emailReq = AGCAuth.GetInstance().CurrentUser?.UpdateEmail(updatedEmail, code);
                    emailReq.AddOnSuccessCallback((result) =>
                    {
                        Console.WriteLine("email update success");
                    });
                    emailReq.AddOnFailureCallback((error) =>
                    {
                        Console.WriteLine("email update failed");
                    });
                });
            });


            

        }

        private void UpdatePhone()
        {
            CreateAlert("Send Verification Code", "", new[] { "Country Code", "Phone number" }, (resultArr) =>
            {
                var countryCode = resultArr[0];
                var phoneNumber = resultArr[1];
                BaseProvider.SendVerifyCodeWithCountryCode(countryCode, phoneNumber, AGCVerifyCodeAction.RegisterLogin);
                CreateAlert("Update Phone", "", new[] { "Country code", "Phone number", "Verification Code" }, (updateArr) =>
                {
                    countryCode = updateArr[0];
                    phoneNumber = updateArr[1];
                    var code = updateArr[2];

                    // update the phone number of the current user
                    HMFTask<NSObject> phoneReq = AGCAuth.GetInstance().CurrentUser?.UpdatePhoneWithCountryCode(countryCode, phoneNumber, code);

                    phoneReq.AddOnSuccessCallback((result) =>
                    {
                        Console.WriteLine("phone update success");
                    });
                    phoneReq.AddOnFailureCallback((error) =>
                    {
                        Console.WriteLine("phone update failed");
                    });
                });
            });
        }

        private void UpdateEmailPassword()
        {
            CreateAlert("Send Verification Code", "", new[] {  "Email" }, (resultArr) =>
            {
                var email = resultArr[0];
                BaseProvider.SendVerifyCodeWithEmail(email, AGCVerifyCodeAction.SetPassword);

                CreateAlert("Update Email Password", "", new[] { "Password", "Verification Code" }, (updateArr) =>
                {
                    var password = updateArr[0];
                    var code = updateArr[1];

                    // update the password of the current user
                    HMFTask<NSObject> passwordReq = AGCAuth.GetInstance().CurrentUser?.UpdatePassword(password, code, (int)AGCAuthProviderType.Email);

                    passwordReq.AddOnSuccessCallback((result) =>
                    {
                        Console.WriteLine("password update success");
                    });
                    passwordReq.AddOnFailureCallback((error) =>
                    {
                        Console.WriteLine("password update failed");
                    });
                });
            });


           
        }

        private void UpdatePhonePassword()
        {
            CreateAlert("Send Verification Code", "", new[] { "Country code", "Phone number" }, (resultArr) =>
            {
                var countryCode = resultArr[0];
                var phoneNumber = resultArr[1];
                BaseProvider.SendVerifyCodeWithCountryCode(countryCode, phoneNumber, AGCVerifyCodeAction.SetPassword);
                CreateAlert("Update Phone Password", "", new[] { "New Password", "Verification Code" }, (updateArr) =>
                {
                    var password = updateArr[0];
                    var code = updateArr[1];

                    // update the password of the current user
                    HMFTask<NSObject> passwordReq = AGCAuth.GetInstance().CurrentUser?.UpdatePassword(password, code, (int)AGCAuthProviderType.Phone);

                    passwordReq.AddOnSuccessCallback((result) =>
                    {
                        Console.WriteLine("password update success");
                    });
                    passwordReq.AddOnFailureCallback((error) =>
                    {
                        Console.WriteLine("password update failed--" + error);
                    });
                });
            });


          
        }

        private void CreateAlert(string title, string message, string[] inputs, Action<List<string>> completeHandler)
        {
            var okAlertController = UIAlertController.Create(title, message, UIAlertControllerStyle.Alert);

            for (int i = 0; i < inputs.Length; i++)
            {
                okAlertController.AddTextField((obj) =>
                {
                    obj.Font = UIFont.SystemFontOfSize(20);
                    obj.TextAlignment = UITextAlignment.Center;
                    obj.Placeholder = inputs[i];

                });
            }

            okAlertController.AddAction(UIAlertAction.Create("OK", UIAlertActionStyle.Default, (alert) =>
            {
                var resultArray = new List<string>();
                resultArray.AddRange(okAlertController.TextFields.Select(a => a.Text).ToList());
                completeHandler.Invoke(resultArray);

            }));

            // Present Alert
            owner.PresentViewController(okAlertController, true, null);
        }
    }
}
