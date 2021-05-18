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
using CoreGraphics;
using Foundation;
using Huawei.Agconnect.Auth;
using UIKit;

namespace AGCAuthXamariniOSDemo.Views
{
    public partial class SettingsViewController : UIViewController
    {
        public SettingsViewController() : base("SettingsViewController", null)
        {
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();

            tableView.Source = new SettingsTableSource(this);
        }

        public override void ViewDidDisappear(bool animated)
        {
            base.ViewDidDisappear(animated);

        }
    }
}

