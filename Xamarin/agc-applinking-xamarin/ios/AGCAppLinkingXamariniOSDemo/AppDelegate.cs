/*
Copyright (c) Huawei Technologies Co., Ltd. 2012-2020. All rights reserved.

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
using System.Threading.Tasks;
using Foundation;
using Huawei.Agconnect.Applinking;
using Huawei.Agconnect.AgconnectCore;
using UIKit;

namespace AGCAppLinkingXamariniOSDemo
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
            //Initialize the AppGallery Connect SDK in the system startup method.
            AGCInstance.StartUp();

            //Obtain the singleton object
            AGCAppLinking appLinking = AGCAppLinking.GetSharedInstance();

            //Handle app link receiving
            appLinking.HandleAppLinking(AppLinkReceivedCallback);

            return true;
        }

        private void AppLinkReceivedCallback(AGCResolvedLink link, NSError error)
        {

            if (error != null)
                Console.WriteLine("Error occured: " + error.Description);

            if (link != null)
            {
                //Display alert to show app link detail
                DisplayAlert(link);

                Console.WriteLine("App Link handled");
            }
        }

        private static void DisplayAlert(AGCResolvedLink link)
        {
            string appLinkInfo = $"App Link: {link?.DeepLink} \n Time: {link?.ClickTime}";

            string campaignInfo = $"Campaign Name: {link?.CampaignName} \n Campaign Medium: {link?.CampaignMedium} \n Campaign Source: {link?.CampaignSource}";

            string socialInfo = $"Social Title: {link?.SocialTitle} \n Social Description: {link?.SocialDescription} \n Social ImageUrl: {link?.SocialImageUrl}";

            var alert = UIAlertController.Create("App Link Received", appLinkInfo + "\n" + campaignInfo + "\n" + socialInfo, UIAlertControllerStyle.Alert);
            var defaultAction = UIAlertAction.Create("OK", UIAlertActionStyle.Default, null);
            alert.AddAction(defaultAction);

            (UIApplication.SharedApplication.KeyWindow.RootViewController as ViewController)?.PresentViewController(alert, true, null);
        }

        // UISceneSession Lifecycle

        [Export("application:configurationForConnectingSceneSession:options:")]
        public UISceneConfiguration GetConfiguration(UIApplication application, UISceneSession connectingSceneSession, UISceneConnectionOptions options)
        {
            // Called when a new scene session is being created.
            // Use this method to select a configuration to create the new scene with.
            return UISceneConfiguration.Create("Default Configuration", connectingSceneSession.Role);
        }

        [Export("application:didDiscardSceneSessions:")]
        public void DidDiscardSceneSessions(UIApplication application, NSSet<UISceneSession> sceneSessions)
        {
            // Called when the user discards a scene session.
            // If any sessions were discarded while the application was not running, this will be called shortly after `FinishedLaunching`.
            // Use this method to release any resources that were specific to the discarded scenes, as they will not return.
        }

        [Export("application:openURL:options:")]
        public bool OpenUrl(UIApplication app, NSUrl url, NSDictionary options)
        {
            return true;
        }
    }
}

