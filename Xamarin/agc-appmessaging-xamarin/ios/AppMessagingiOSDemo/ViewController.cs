/*
*       Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.

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

namespace AGCAppLinkingXamariniOSDemo
{
    public partial class ViewController : UIViewController
    {
        private NSDate selectedDate;

        public ViewController(IntPtr handle) : base(handle)
        {
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();

            var agcInstance = AGCInstance.SharedInstance();
            Console.WriteLine("AAID: " + agcInstance.Config.AppId);
            lblAaid.Text = "AAID: " + agcInstance.Config.AppId;

            AppDelegate.AppMessagingInstance.IsFetchMessageEnable = true;
            AppDelegate.AppMessagingInstance.IsDisplayEnable = true;
            //Trigger custom event
            AppDelegate.AppMessagingInstance.TriggerEvent("CustomEvent");
        }

        public override void DidReceiveMemoryWarning()
        {
            base.DidReceiveMemoryWarning();
            // Release any cached data, images, etc that aren't in use.
        }


        //Uncomment this if your app use Analytics Kit
        // public void SetBirthday()
        // {
        //     HiAnalytics.SetUserProfile("BirthdayAttr", "OK");
        //     var keys = new[]
        //    {
        //              new NSString("Birthday"),
        //          };

        //     var objects = new NSObject[]
        //     {
        //            new NSString(selectedDate!=null?selectedDate.ToString():DateTime.Now.ToString()),
        //     };
        //     var dictionary = new NSDictionary<NSString, NSObject>(keys, objects);
        //     HiAnalytics.OnEvent("CelebrateEvent", dictionary);
        // }

        internal void SetAppMessageDetail(AGCAppMessagingDisplayMessage message)
        {

            lblEventType.Text = "Message Type: " + message.MessageType.ToString();
            lblStartTime.Text = "Start Time: " + message.StartTime?.ToString();
            lblEndTime.Text = "End Time: " + message?.EndTime?.ToString() ?? "None";
            lblFreqType.Text = "Frequency Type: " + message.FrequencyType.ToString();
            lblFreqValue.Text = "Frequency Value: " + message.FrequencyValue.ToString();
            lblDismissType.Text = "";
        }

        internal void SetDismissType(AGCAppMessagingDismissType dismissType)
        {
            lblDismissType.Text = dismissType.ToString();
        }
    }
}