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
    public partial class FirstViewController : UIViewController
    {

        private AGCAppLinkingComponents appLinkingComponent;

        public FirstViewController() : base("FirstViewController", null)
        {
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad();

            CreateAppLink();

        }

        private void CreateAppLink()
        {
            appLinkingComponent = new AGCAppLinkingComponents
            {
                // Set URL Prefix
                UriPrefix = "URI_PREFIX",
                // Set Deep Link
                DeepLink = "https://open.iosdemoapp.com",

                //Set the link preview type. If this method is not called, the preview page with app information is displayed by default.
                PreviewType = AGCLinkingPreviewType.AppInfo
            };
        }

        public override void DidReceiveMemoryWarning()
        {
            base.DidReceiveMemoryWarning();
            // Release any cached data, images, etc that aren't in use.
        }

        partial void CampaignChanged(UISwitch sender)
        {
            // Set Campaign Parameters (Optional)
            // You track the effectiveness of campaigns across traffic sources.
            if (switchCampaign.On)
            {
                appLinkingComponent.CampaignName = "Xamarin App Link Test";
                appLinkingComponent.CampaignSource = "AGC";
                appLinkingComponent.CampaignMedium = "AGC Xamarin App Link Test";
            }
        }

        partial void SocialChanged(UISwitch sender)
        {
            // Set Sharing Identifier (Optional)
            // Your links appears with title, description and image url on Facebook, Twitter etc.
            if (switchSocial.On)
            {
                appLinkingComponent.SocialImageUrl = "IMAGE_URL";
                appLinkingComponent.SocialDescription = "AppLink Share Description";
                appLinkingComponent.SocialTitle = "AppLink Share Title";

            }
        }



        partial void AndroidParamsChanged(UISwitch sender)
        {
            // Set Android App Parameters (Optional)
            // If this parameters not set, the link will be opened in the browser by default.
            if (switchAndroidParams.On)
            {
                appLinkingComponent.AndroidDeepLink = ("opendemoapp://Hello");
                appLinkingComponent.AndroidFallbackUrl = "FALLBACK_URL";
                appLinkingComponent.ExpireMinute = 2;
                appLinkingComponent.AndroidPackageName = "com.xamarin.android.sample";
                appLinkingComponent.AndroidOpenType = AGCLinkingAndroidOpenType.CustomURL;
            }
        }

        partial void iOSParamsChanged(UISwitch sender)
        {
            // Set iOS App Parameters (Optional)
            // If this parameters not set, the link will be opened in the browser by default.
            if (switchiOSParams.On)
            {
                appLinkingComponent.IosBundleId = "com.xamarin.ios.sample";
                appLinkingComponent.IosDeepLink = "opendemoapp://Hello";
                appLinkingComponent.IosFallbackUrl = "FALLBACK_URL";

                //For ipad configuration
                appLinkingComponent.IpadBundleId = "com.xamarin.ios.sample";
                appLinkingComponent.IpadFallbackUrl = "FALLBACK_URL";

            }
        }

        partial void iTunesParamsChanged(UISwitch sender)
        {
            //Set iTunes Connect activity parameters.
            if (switchITunesParams.On)
            {
                appLinkingComponent.ITunesConnectAffiliateToken = "AffiliateToken";
                appLinkingComponent.ITunesConnectCampaignToken = "CampaignToken";
                appLinkingComponent.ITunesConnectMediaType = "MediaType";
                appLinkingComponent.ITunesConnectProviderToken = "ProviderToken";

            }
        }


        partial void ClearAll(UIButton sender)
        {
            switchAndroidParams.On = false;
            switchiOSParams.On = false;
            switchITunesParams.On = false;
            switchSocial.On = false;
            switchCampaign.On = false;
            CreateAppLink();
        }

        partial void CreateAppLink(UIButton sender)
        {

            PresentViewController(new ResultViewController(appLinkingComponent), true, null);
        }

        async partial void BtnConvert_TouchUpInside(UIButton sender)
        {
            appLinkingComponent.LongLink = appLinkingComponent.BuildLongLink().ToString();
            Task<AGCShortAppLinking> task = appLinkingComponent.BuildShortLinkAsync();
            AGCShortAppLinking result = await task;
            if (task.IsCompleted && result != null)
            {

                Console.WriteLine("Converted Result: " + task.Result.Url.ToString());

                var alert = UIAlertController.Create("Long Link Converted", "SHORT LINK: " + task.Result.Url.ToString() +" ", UIAlertControllerStyle.Alert);
                var defaultAction = UIAlertAction.Create("OK", UIAlertActionStyle.Default, null);
                alert.AddAction(defaultAction);

                PresentViewController(alert, true, null);
            }
        }


        public async void CreateShortAppLink()
        {
            //Build a short link
           Task<AGCShortAppLinking> shortLinkTask = appLinkingComponent.BuildShortLinkAsync();
           AGCShortAppLinking result = await shortLinkTask;
           if (shortLinkTask.IsCompleted && result != null)
           {
               NSUrl shortLink = result.Url;
               NSUrl testLink  = result.TestUrl;
           }
        }

        public void CreateLongAppLink()
        {
            //Build a long link
            NSUrl longLink = appLinkingComponent.BuildLongLink();
        }
    }
}

