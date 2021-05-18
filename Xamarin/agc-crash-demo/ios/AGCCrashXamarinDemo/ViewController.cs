/*
       Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.

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
using Foundation;
using Huawei.Agconnect.Crash;
using System;
using System.Threading;
using UIKit;

namespace AGCCrashXamarinDemo
{
    public partial class ViewController : UIViewController
    {
        private AGCCrash crashInstance;

        public ViewController(IntPtr handle) : base(handle)
        {
            crashInstance = AGCCrash.GetSharedInstance();
            
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();
            crashInstance.EnableCrashCollection(switchCrashEnabled.On);
            // Perform any additional setup after loading the view, typically from a nib.
        }

        public override void DidReceiveMemoryWarning()
        {
            base.DidReceiveMemoryWarning();
            // Release any cached data, images, etc that aren't in use.
        }

        partial void EnableCrash_Changed(UISwitch sender)
        {
            crashInstance.EnableCrashCollection(sender.On);
        }

        partial void AddLog_TouchUpInside(UIButton sender)
        {
            crashInstance.LogWithLevel(AGCCrashLogLevel.Debug,"Debug log has been added.");
        }

        partial void NSException_TouchUpInside(UIButton sender)
        {
            if (!string.IsNullOrEmpty(txtValue.Text))
                crashInstance.SetCustomValue(new NSString(txtValue.Text), "TestKey");
            if (!string.IsNullOrEmpty(txtUserId.Text))
                crashInstance.SetUserId(txtUserId.Text);
            crashInstance.TestIt();
        }

        partial void NSInvalidArgumentException_TouchUpInside(UIButton sender)
        {
            //This will throw an Objective-C NSInvalidArgumentException
            var dict = new NSMutableDictionary();
            dict.LowlevelSetObject(IntPtr.Zero, IntPtr.Zero);
        }

        partial void RecordError_TouchUpInside(UIButton sender)
        {
            // catch the error thrown by your function
            // or generate the error as needed.
            NSString yourErrorDomain = new NSString("your_error_domain");
            nint yourErrorCode = 0;
            NSString yourErrorInfo = new NSString("record error test");
            var error = new NSError(domain: yourErrorDomain, code: yourErrorCode);
            // record error.
            crashInstance.RecordError(error);
        }

        partial void RecordException_TouchUpInside(NSObject sender, UIEvent @event)
        { 
            crashInstance.RecordExceptionModel(new AGCExceptionModel("Exception Name", "Exception Reason", "Stack Trace"));
        }
    }
}