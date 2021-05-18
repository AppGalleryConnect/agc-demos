/*
*       Copyright 2020-2021. Huawei Technologies Co., Ltd. All rights reserved.

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
using Huawei.Agconnect.Appmessaging;
using Huawei.Agconnect.AgconnectCore;
using UIKit;
//using Huawei.Hms.Analytics;

namespace AGCAppLinkingXamariniOSDemo
{
    // The UIApplicationDelegate for the application. This class is responsible for launching the
    // User Interface of the application, as well as listening (and optionally responding) to application events from iOS.
    [Register("AppDelegate")]
    public class AppDelegate : UIResponder, IUIApplicationDelegate, IAGCAppMessagingDelegate
    {

        [Export("window")]
        public UIWindow Window { get; set; }
        public static AGCAppMessaging AppMessagingInstance { get; set; }
        public static AGCAppMessagingDefaultDisplay DisplayMessagingInstance { get; set; }

        [Export("application:didFinishLaunchingWithOptions:")]
        public bool FinishedLaunching(UIApplication application, NSDictionary launchOptions)
        {
            // Override point for customization after application launch.
            // If not required for your application you can safely delete this method

            AGCInstance.StartUp();

            AppMessagingInstance = AGCAppMessaging.GetSharedInstance();
            AppMessagingInstance.Delegate = this;

            //Uncomment for customize app message and implement IAGCAppMessagingDisplayDelegate
            //DisplayMessagingInstance = AGCAppMessagingDefaultDisplay.GetSharedInstance();
            //AppMessagingInstance.DisplayComponent = this;
            
            //HiAnalytics.Config();
            return true;
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

        void SetMessageDetail(AGCAppMessagingDisplayMessage message)
        {
            (UIApplication.SharedApplication.KeyWindow.RootViewController as ViewController).SetAppMessageDetail(message);

        }

        void SetDismissType(AGCAppMessagingDismissType dismissType)
        {
            (UIApplication.SharedApplication.KeyWindow.RootViewController as ViewController).SetDismissType(dismissType);

        }

        [Export("appMessagingOnDismiss:dismissType:")]
        public void AppMessagingOnDismiss(AGCAppMessagingDisplayMessage message, AGCAppMessagingDismissType dismissType)
        {
            SetMessageDetail(message);
            SetDismissType(dismissType);
            Console.WriteLine("AppMessagingOnDisplay: " + message.MessageType + "--- Dismiss Type: " + dismissType.ToString());
        }

       
        [Export("appMessagingOnClick:button:")]
        public void AppMessagingOnClick(AGCAppMessagingDisplayMessage message, AGCAppMessagingActionButton button)
        {
            SetMessageDetail(message);
            Console.WriteLine("AppMessagingOnClick: " + message.MessageType);
        }

        [Export("appMessagingOnDisplay:")]
        public void AppMessagingOnDisplay(AGCAppMessagingDisplayMessage message)
        {
            SetMessageDetail(message);
            Console.WriteLine("AppMessagingOnDisplay: " + message.MessageType);
        }

        [Export("appMessagingOnError:")]
        public void AppMessagingOnError(AGCAppMessagingDisplayMessage message)
        {
            SetMessageDetail(message);
            Console.WriteLine("AppMessagingOnError: " + message.MessageType);
        }

        //public void SetDisplayDelegate(AGCAppMessagingDisplayMessage message, IAGCAppMessagingDelegate displayDelegate)
        //{

        //    if (message is AGCAppMessagingCardDisplay)
        //    {
        //        AGCAppMessagingCardDisplay card = new AGCAppMessagingCardDisplay();
        //        card = (AGCAppMessagingCardDisplay)message;
        //        var alert = UIAlertController.Create(card.Title, card.Body, UIAlertControllerStyle.Alert);
        //        if (card.MajorButton != null)
        //        {
        //            alert.AddAction(UIAlertAction.Create(card.MajorButton.Text, UIAlertActionStyle.Default, (action) =>
        //            {
        //                if (card.MajorButton.ActionURL != null)
        //                    UIApplication.SharedApplication.OpenUrl(card.MajorButton.ActionURL);
        //                else
        //                    alert.DismissViewController(true, null);
        //            }));
        //        }


        //        if (card.MinorButton != null)
        //        {
        //            alert.AddAction(UIAlertAction.Create(card.MinorButton.Text, UIAlertActionStyle.Default, (action) =>
        //            {
        //                if (card.MinorButton.ActionURL != null)
        //                    UIApplication.SharedApplication.OpenUrl(card.MinorButton.ActionURL);
        //                else
        //                    alert.DismissViewController(true, null);
        //            }));
        //        }

        //        UIApplication.SharedApplication.KeyWindow.RootViewController.PresentViewController(alert, true, null);
        //    }
        //    else if (message is AGCAppMessagingBannerDisplay)
        //    {
        //        AGCAppMessagingBannerDisplay banner = new AGCAppMessagingBannerDisplay();
        //        banner = (AGCAppMessagingBannerDisplay)message;

        //        string title = banner.Title;
        //        UIColor titleColor = banner.TitleColor;
        //        NSUrl actionUrl = banner.ActionURL;
        //        string body = banner.Body;
        //        UIColor bodyColor = banner.BodyColor;
        //        UIColor backgroundColor = banner.BackgroundColor;
        //        NSUrl pictureUrl = banner.PictureURL;
        //    }

        //}
    }
}

