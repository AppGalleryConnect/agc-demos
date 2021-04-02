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
using System;
using Foundation;
using Huawei.Agconnect.Remoteconfig;
using UIKit;

namespace AGCRemoteConfigXamarinDemo
{
    public partial class FirstModeViewController : UIViewController
    {
        private AGCRemoteConfig remoteInstance;

        public FirstModeViewController() : base("FirstModeViewController", null)
        {
        }

        public async override void ViewDidLoad()
        {
            base.ViewDidLoad();
            // the following code demonstrates how to fetch config and apply immediately.

            // apply default config if you want
            var keys = new[]
           {
                    new NSString("test1"),
                    new NSString("test2")
                };

            var objects = new NSObject[]
            {
                  new NSString("value1"),
                  new NSNumber(2),
            };

            var dictionary = new NSDictionary<NSString, NSObject>(keys, objects);
            remoteInstance = AGCRemoteConfig.GetSharedInstance();
            remoteInstance.ApplyDefaults(dictionary);



            // fetch config
            var resultTask = remoteInstance.Fetch().AddOnSuccessCallbackAsync();
            await resultTask;
            if (resultTask.IsCompleted)
            {
                Console.Write("fetch success");
                if (resultTask.Result != null)
                    remoteInstance.Apply((AGCConfigValues)resultTask.Result);  // apply the config when fetch is successful

                ShowAllValues(); // get all applied config and show it in label
            }
            else
                Console.Write("fetch failed");
        }

        private void ShowAllValues()
        {
            NSDictionary values = remoteInstance.MergedAll;
            foreach (var item in values)
            {
                label.Text += $"Parameter:{item.Key} \n Value:{item.Value} \n Source:{remoteInstance.GetSource(item.Key.ToString())}  \n ------------ \n";
            }
        }

        public override void DidReceiveMemoryWarning()
        {
            base.DidReceiveMemoryWarning();
            // Release any cached data, images, etc that aren't in use.
        }
    }
}

