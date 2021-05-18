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
using Huawei.Agconnect.Function;
using System;
using UIKit;

namespace AGCCloudFunctionsiOSDemo
{
    public partial class ViewController : UIViewController
    {
        public ViewController(IntPtr handle) : base(handle)
        {
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();
            // Perform any additional setup after loading the view, typically from a nib.
        }

        public override void DidReceiveMemoryWarning()
        {
            base.DidReceiveMemoryWarning();
            // Release any cached data, images, etc that aren't in use.
        }

        partial void Submit_Clicked(NSObject sender)
        {
            var parameters = NSDictionary<NSString, NSObject>.FromObjectAndKey(new NSString(txtName.Text), new NSString("name"));

            var callableWithoutObject = AGCFunction.GetInstance().Wrap("test-$latest");
            var callableWithObject = AGCFunction.GetInstance().Wrap("testwithobject-$latest");

            callableWithoutObject.Call().AddOnSuccessCallback(SuccessCallback);
            callableWithObject.CallWithObject(parameters).AddOnSuccessCallback(SuccessCallback);
        }

        private void SuccessCallback(NSObject obj)
        {
            var result = obj as AGCFunctionResult;
            Console.WriteLine(result.Value);
            lblName.Text += result.Value + "\n";

        }
    }
}