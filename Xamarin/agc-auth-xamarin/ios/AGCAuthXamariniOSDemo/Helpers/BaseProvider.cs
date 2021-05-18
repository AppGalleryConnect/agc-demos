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
using Foundation;
using Huawei.Agconnect.AgconnectCore;
using Huawei.Agconnect.Auth;
using UIKit;

namespace AGCAuthXamariniOSDemo
{
    public class BaseProvider
    {
        internal static void SendVerifyCodeWithCountryCode(string countryCode, string phoneNumber, AGCVerifyCodeAction action)
        {
            AGCVerifyCodeSettings setting = new AGCVerifyCodeSettings(action, null, 30);
            HMFTask<NSObject> verifyCode = AGCPhoneAuthProvider.RequestVerifyCodeWithCountryCode(countryCode, phoneNumber, setting);

            verifyCode.AddOnSuccessCallback((result) =>
            {
                AGCVerifyCodeResult code = result as AGCVerifyCodeResult;
                Console.WriteLine("Verification code created successfully.");
                CreateAlert();

            });
            verifyCode.AddOnFailureCallback((error) =>
            {

                Console.WriteLine("Verification code created failed." + error);
            });
        }

        internal static void SendVerifyCodeWithEmail(string email, AGCVerifyCodeAction action)
        {
            AGCVerifyCodeSettings setting = new AGCVerifyCodeSettings(action, null, 30);
            HMFTask<NSObject> verifyCode = AGCEmailAuthProvider.RequestVerifyCodeWithEmail(email, setting);
            verifyCode.AddOnSuccessCallback((result) =>
            {
                AGCVerifyCodeResult code = result as AGCVerifyCodeResult;
                Console.WriteLine("Verification code created successfully.");
                CreateAlert();

            });
            verifyCode.AddOnFailureCallback((error) =>
            {
                Console.WriteLine("Verification code created failed." + error);
            });
        }




        private static void CreateAlert()
        {
            var window = UIApplication.SharedApplication.KeyWindow;
            var vc = window.RootViewController;
            while (vc.PresentedViewController != null)
            {
                vc = vc.PresentedViewController;
            }


            var okAlertController = UIAlertController.Create("Title", "Verification code has been sent.", UIAlertControllerStyle.Alert);

            okAlertController.AddAction(UIAlertAction.Create("OK", UIAlertActionStyle.Default, null));

            vc.PresentViewController(okAlertController, true, null);

        }
    }
}