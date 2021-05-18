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
using Facebook.CoreKit;
using Foundation;
using Google.SignIn;
using Huawei.Agconnect.AgconnectCore;
using UIKit;

namespace AGCAuthXamariniOSDemo
{
    // The UIApplicationDelegate for the application. This class is responsible for launching the
    // User Interface of the application, as well as listening (and optionally responding) to application events from iOS.
    [Register("AppDelegate")]
    public class AppDelegate : UIResponder, IUIApplicationDelegate
    {

        [Export("window")]
        public UIWindow Window { get; set; }

        [Export("application:didFinishLaunchingWithOptions:")]
        public bool FinishedLaunching(UIApplication application, NSDictionary launchOptions)
        {
            //AGC
            AGCInstance.StartUp();

            //Google
            // You can get the GoogleService-Info.plist file at https://developers.google.com/mobile/add
            var googleServiceDictionary = NSDictionary.FromFile("GoogleService-Info.plist");
            SignIn.SharedInstance.ClientId = googleServiceDictionary["CLIENT_ID"].ToString();

            return ApplicationDelegate.SharedInstance.FinishedLaunching(application, launchOptions);
        }

        [Export("application:openURL:sourceApplication:annotation:")]
        public bool OpenUrl(UIApplication application, NSUrl url, string sourceApplication, NSObject annotation)
        {
            return ApplicationDelegate.SharedInstance.OpenUrl(application, url, sourceApplication, annotation) || SignIn.SharedInstance.HandleUrl(url);
        }


        [Export("applicationDidBecomeActive:")]
        public void OnActivated(UIApplication application)
        {
            // Call the 'ActivateApp' method to log an app event for use
            // in analytics and advertising reporting. This is optional
            AppEvents.ActivateApp();
        }
    }
}

