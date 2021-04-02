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
using Huawei.Agconnect.Remoteconfig;
using System;
using UIKit;

namespace AGCRemoteConfigXamarinDemo
{
    public partial class ViewController : UIViewController
    {
        public ViewController(IntPtr handle) : base(handle)
        {
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();
        }

        public override void DidReceiveMemoryWarning()
        {
            base.DidReceiveMemoryWarning();
            // Release any cached data, images, etc that aren't in use.
        }


        partial void Mode1_TouchUpInside(UIButton sender)
        {
            //Fetch the remote configuration after startup, and apply the configuration immediately when success.
            this.NavigationController.PushViewController(new FirstModeViewController(), true);
        }

        partial void Mode2_TouchUpInside(UIButton sender)
        {
            //Fetch the remote configuration after startup, and apply the configuration after the next startup.
            this.NavigationController.PushViewController(new SecondModeViewController(), true);
        }

        partial void Clear_TouchUpInside(UIButton sender)
        {
            AGCRemoteConfig.GetSharedInstance().ClearAll();
        }
    }
}