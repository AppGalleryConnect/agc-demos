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
using Huawei.Agconnect.Auth;
using UIKit;

namespace AGCAuthXamariniOSDemo.Views
{
    public class LinkTableSource : UITableViewSource
    {

        protected LinkItem[] tableItems;
        protected string cellIdentifier = "LinkTableCell";
        LinkViewController owner;
        private XamarinAuthHelper xamarinAuthHelper;

        public LinkTableSource(LinkItem[] items, LinkViewController owner)
        {
            tableItems = items;
            this.owner = owner;
            xamarinAuthHelper = new XamarinAuthHelper(owner);
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

            var item = tableItems[indexPath.Row];

            if (item.IsLinked)
            {
                UnLinkAccount((AGCAuthProviderType)Enum.ToObject(typeof(AGCAuthProviderType), item.ProviderType));

            }
            else
            {
                Link(indexPath.Row);

            }
        }

        private void Link(int row)
        {
            switch (row)
            {
                case 0:
                    var weChatCredential = AGCWeiXinAuthProvider.CredentialWithToken("TOKEN", "OPEN_ID");
                    LinkAccount(weChatCredential);
                    break;
                case 1:
                    var qqCredential = AGCQQAuthProvider.CredentialWithToken("TOKEN", "OPEN_ID");
                    LinkAccount(qqCredential);
                    break;
                case 2:
                    var weiboCredential = AGCWeiboAuthProvider.CredentialWithToken("TOKEN", "UID");
                    LinkAccount(weiboCredential);
                    break;
                case 3:
                    xamarinAuthHelper.AuthorizeFacebook((credential) =>
                    {
                        if (credential != null)
                            LinkAccount(credential);
                    });
                    break;
                case 4:
                    xamarinAuthHelper.AuthorizeGoogle((credential) =>
                    {
                        if (credential != null)
                            LinkAccount(credential);
                    });
                    break;
                case 5:
                    xamarinAuthHelper.AuthorizeTwitter((credential) =>
                    {
                        if (credential != null)
                            LinkAccount(credential);
                    });
                    break;
                case 6:
                    LinkPhoneAccount();
                    break;
                case 7:
                    LinkEmailAccount();
                    break;
                case 8:
                    var appleCredential = AGCAppleIDAuthProvider.CredentialWithIdentityToken("IdentityToken", "RequestNonce");
                    LinkAccount(appleCredential);
                    break;
                default:
                    Console.WriteLine("none");
                    break;
            }
            owner.RefreshLinkState();
        }

        void LinkAccount(AGCAuthCredential credential)
        {
            if (credential != null)
            {
                var linkReq = AGCAuth.GetInstance().CurrentUser?.Link(credential);
                linkReq.AddOnSuccessCallback((result) =>
                {
                    Console.WriteLine("link success");
                    owner.RefreshLinkState();
                });
                linkReq.AddOnFailureCallback((error) =>
                {
                    Console.WriteLine("link failed-->" + error);
                });
            }
            else
                Console.WriteLine("link failed");
        }

        void UnLinkAccount(AGCAuthProviderType type)
        {
            var unLinkReq = AGCAuth.GetInstance().CurrentUser?.Unlink(type);

            unLinkReq.AddOnSuccessCallback((result) =>
            {
                Console.WriteLine("unlink success");
                owner.RefreshLinkState();
            });
            unLinkReq.AddOnFailureCallback((error) =>
            {
                Console.WriteLine("unlink failed");
            });
        }

        private void LinkEmailAccount()
        {
            CreateAlert("Send Verification Code", "", new[] { "Email" }, (resultArr) =>
            {
                var email = resultArr[0];
                BaseProvider.SendVerifyCodeWithEmail(email, AGCVerifyCodeAction.RegisterLogin);

                CreateAlert("Link Email Account", "", new[] { "Email", "Password", "Verification Code" }, (linkArr) =>
                 {
                     email = linkArr[0];
                     var password = linkArr[1];
                     var code = linkArr[2];
                     AGCAuthCredential credential;
                     if (string.IsNullOrEmpty(code))
                     {
                         // Generate a credential to link to email account with password
                         credential = AGCEmailAuthProvider.CredentialWithEmail(email, password: password);
                     }
                     else
                     {
                         // Generate a credential to link to email account with verification code
                         credential = AGCEmailAuthProvider.CredentialWithEmail(email, password: password, code);
                     }

                     var linkReq = AGCAuth.GetInstance().CurrentUser?.Link(credential);

                     linkReq.AddOnSuccessCallback((result) =>
                     {
                         Console.WriteLine("link success");
                         owner.RefreshLinkState();
                     });
                     linkReq.AddOnFailureCallback((error) =>
                     {
                         Console.WriteLine("link failed");
                     });
                 });
            });
        }

        private void LinkPhoneAccount()
        {
            CreateAlert("Send Verification Code", "", new[] { "Country code", "Phone number" }, (resultArr) =>
            {
                var countryCode = resultArr[0];
                var phoneNumber = resultArr[1];
                BaseProvider.SendVerifyCodeWithCountryCode(countryCode, phoneNumber, AGCVerifyCodeAction.RegisterLogin);
                CreateAlert("Link Phone Account", "", new[] { "Country code", "Phone number", "Password", "verification code" }, (updateArr) =>
                {
                    countryCode = resultArr[0];
                    phoneNumber = resultArr[1];
                    var password = updateArr[2];
                    var code = updateArr[3];
                    AGCAuthCredential credential;
                    if (string.IsNullOrEmpty(code))
                    {
                        // Generate a credential to link phone account with password
                        credential = AGCPhoneAuthProvider.CredentialWithCountryCode(countryCode, phoneNumber, password);
                    }
                    else
                    {
                        // Generate a credential to link phone account with verification code
                        credential = AGCPhoneAuthProvider.CredentialWithCountryCode(countryCode, phoneNumber, password, code);
                    }
                    // link phone account with credential
                    var linkReq = AGCAuth.GetInstance().CurrentUser?.Link(credential);

                    linkReq.AddOnSuccessCallback((result) =>
                    {
                        Console.WriteLine("link success");
                        owner.RefreshLinkState();
                    });
                    linkReq.AddOnFailureCallback((error) =>
                    {
                        Console.WriteLine("link failed");
                    });
                });
            });
        }


        public override UITableViewCell GetCell(UITableView tableView, NSIndexPath indexPath)
        {
            UITableViewCell cell = tableView.DequeueReusableCell(cellIdentifier);
            string item = tableItems[indexPath.Row].Name;

            if (cell == null)
            {
                cell = new UITableViewCell(UITableViewCellStyle.Default, cellIdentifier);
            }

            cell.TextLabel.Text = tableItems[indexPath.Row].Name;
            if (tableItems[indexPath.Row].IsLinked)
                cell.BackgroundColor = UIColor.Green;
            return cell;
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
