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
using UIKit;

namespace AGCAppLinkingXamariniOSDemo
{
    public partial class ResultViewController : UIViewController
    {
        private AGCAppLinkingComponents appLinkingComponent;

        public ResultViewController(AGCAppLinkingComponents appLinkingComponent) : base("ResultViewController", null)
        {
            this.appLinkingComponent = appLinkingComponent;
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

        public override async void ViewDidAppear(bool animated)
        {
            base.ViewDidAppear(animated);
            await GetAppLink();
            
        }

        public async Task GetAppLink()
        {
            string longUrl = appLinkingComponent.BuildLongLink().ToString();
            AGCShortAppLinking shortUrl = null;
            try
            {
                shortUrl = await appLinkingComponent.BuildShortLinkAsync();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
            }
            string result = $"LONG LINK: {longUrl} \nSHORT LINK:{shortUrl?.Url} \nTEST LINK:{shortUrl?.TestUrl}";
            Console.WriteLine(result);
            txtAppLink.Text = result;
            
        }

        public void Share()
        {
            if (String.IsNullOrEmpty(appLinkingComponent?.DeepLink))
                return;

            var items = new NSObject[] { (NSString)appLinkingComponent.DeepLink };
            var shareDalog = new UIActivityViewController(items, null);
            this.PresentViewController(shareDalog, true, null);
        }
    }
}

