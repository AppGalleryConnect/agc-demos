/*
Copyright (c) Huawei Technologies Co., Ltd. Copyright 2020-2021. All rights reserved.

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


using Android;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Widget;
using Huawei.Agconnect.Applinking;
using System;
using static Huawei.Agconnect.Applinking.AppLinking;
using Debug = System.Diagnostics.Debug;
using Uri = Android.Net.Uri;

namespace AGCAppLinkingXamarinAndroidDemo
{
    [Activity(Label = "AppLinkActivity")]
    public class AppLinkActivity : Activity
    {
        private AppLinking.Builder builder;
        private Uri longLink;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.applink_create);
            FindViewById<Button>(Resource.Id.btnAddCampaignInfo).Click += AddCampaignInfo_Click;
            FindViewById<Button>(Resource.Id.btnAddSocialInfo).Click += AddSocialInfo_Click;
            FindViewById<Button>(Resource.Id.btnAddCampaignInfo).Click += AddCampaignInfo_Click;
            FindViewById<Button>(Resource.Id.btnAddAndroidAppParameters).Click += AddAndroidAppParameters_Click;
            FindViewById<Button>(Resource.Id.btnAddiOSAppParameters).Click += AddiOSAppParameters_Click;
            FindViewById<Button>(Resource.Id.btnCreateShortAppLink).Click += CreateShortOrLongAppLink_Click;
            FindViewById<Button>(Resource.Id.btnCreateLongAppLink).Click += CreateShortOrLongAppLink_Click;
            FindViewById<Button>(Resource.Id.btnShareAppLink).Click += ShareShortAppLink_Click;
            FindViewById<Button>(Resource.Id.btnConvert).Click += Convert_Click;
            FindViewById<Button>(Resource.Id.btnClear).Click += Clear_Click;
            CreateAppLink();
        }


        public void CreateAppLink()
        {
            builder = new AppLinking.Builder();
            // Set URL Prefix
            builder.SetUriPrefix(Utility.UriPrefix);
            // Set Deep Link
            builder.SetDeepLink(Uri.Parse(Utility.OpenApp_Link));

            //Set the link preview type. If this method is not called, the preview page with app information is displayed by default.
            builder.SetPreviewType(AppLinking.LinkingPreviewType.AppInfo);

            // Set Android link behavior (Optional)
            var behaviorBuilder = new AppLinking.AndroidLinkInfo.Builder();
            // Set Min Version if user app's version less than version number or users direct to AppGallery
            behaviorBuilder.SetMinimumVersion(1);
            builder.SetAndroidLinkInfo(behaviorBuilder.Build());

            FindViewById<TextView>(Resource.Id.txtLink).Text = builder.BuildAppLinking().Uri.ToString();
        }


        private void Clear_Click(object sender, EventArgs e)
        {
            CreateAppLink();
            FindViewById<TextView>(Resource.Id.txtLink).Text = "";
            FindViewById<TextView>(Resource.Id.txtConvertedLink).Text = "";
            FindViewById<TextView>(Resource.Id.txtShortLink).Text = "";
            FindViewById<TextView>(Resource.Id.txtTestLink).Text = "";
        }


        //If you have created a long link and want to convert it to a short link, use the following method
        private async void Convert_Click(object sender, EventArgs e)
        {
            try
            {
                builder.SetLongLink(Uri.Parse(Utility.UriPrefix +"/?deeplink="+longLink));

                var result = await builder.BuildAppShortLinkingAsync();
                if (result != null)
                {
                    SendResult(result.ShortUrl);
                }
            }
            catch (Exception ex)
            {
                Debug.WriteLine("Error: " + ex.ToString());
            }
        }



        private async void CreateShortOrLongAppLink_Click(object sender, EventArgs e)
        {
            ShortAppLinking link = null;
            switch ((sender as Button).Id)
            {
                case Resource.Id.btnCreateLongAppLink: //Create Long Link
                    FindViewById<TextView>(Resource.Id.txtLinkTitle).Text = "Long Link";
                    FindViewById<Button>(Resource.Id.btnConvert).Enabled = true;
                    try
                    {
                        link = await builder.BuildAppShortLinkingAsync(ShortAppLinking.LENGTH.Long);
                        SendResult(new Uri[] { link.ShortUrl, link.TestUrl });
                        longLink = link.ShortUrl;
                    }
                    catch (Exception ex)
                    {
                        Debug.WriteLine("Error: " + ex.ToString());
                    }
                    break;
                case Resource.Id.btnCreateShortAppLink: //Create Short Link
                    FindViewById<Button>(Resource.Id.btnConvert).Enabled = false;
                    FindViewById<TextView>(Resource.Id.txtLinkTitle).Text = "Short Link";
                    try
                    {
                        link = await builder.BuildAppShortLinkingAsync(ShortAppLinking.LENGTH.Short);
                        SendResult(new Uri[] { link.ShortUrl, link.TestUrl });
                    }
                    catch (Exception ex)
                    {
                        Debug.WriteLine("Error: " + ex.ToString());
                    }
                    break;
                default:
                    break;
            }

        }

        private void ShareShortAppLink_Click(object sender, EventArgs e)
        {
            string agcLink = FindViewById<TextView>(Resource.Id.txtLink).Text;
            Intent intent = new Intent(Intent.ActionSend);
            intent.SetType("text/plain");
            intent.PutExtra(Intent.ExtraText, agcLink);
            intent.AddFlags(ActivityFlags.NewTask);
            StartActivity(intent);
        }



        private void AddSocialInfo_Click(object sender, EventArgs e)
        {
            // Set Sharing Identifier (Optional)
            // Your links appears with title, description and image url on Facebook, Twitter etc.
            SocialCardInfo.Builder socialCard = new AppLinking.SocialCardInfo.Builder();
            socialCard.SetImageUrl("IMAGE_URL");
            socialCard.SetDescription("AppLink Share Description");
            socialCard.SetTitle("AppLink Share Title");
            builder.SetSocialCardInfo(socialCard.Build());

            FindViewById<TextView>(Resource.Id.txtLink).Text = builder.BuildAppLinking().Uri.ToString();
        }

        private void AddCampaignInfo_Click(object sender, EventArgs e)
        {
            // Set Campaign Parameters (Optional)
            // You track the effectiveness of campaigns across traffic sources.
            CampaignInfo.Builder campaignBuilder = new AppLinking.CampaignInfo.Builder();
            campaignBuilder.SetName("Xamarin App Link Test");
            campaignBuilder.SetSource("AGC");
            campaignBuilder.SetMedium("AGC Xamarin App Link Test");
            builder.SetCampaignInfo(campaignBuilder.Build());

            FindViewById<TextView>(Resource.Id.txtLink).Text = builder.BuildAppLinking().Uri.ToString();
        }


        private void AddiOSAppParameters_Click(object sender, EventArgs e)
        {
            // Set iOS App Parameters (Optional)
            // If this parameters not set, the link will be opened in the browser by default. 
            var iOSInfoBuilder = new AppLinking.IOSLinkInfo.Builder();
            iOSInfoBuilder.SetBundleId("com.sample.ios");
            iOSInfoBuilder.SetFallbackUrl("IOS_FALLBACK_URL");
            iOSInfoBuilder.SetIOSDeepLink("applink://example/detail");
            iOSInfoBuilder.SetIPadBundleId("com.ipadsample.ios");
            iOSInfoBuilder.SetIPadFallbackUrl("IPAD_FALLBACK_URL");

            //Set iTunes Connect activity parameters.
            if (FindViewById<CheckBox>(Resource.Id.chkItunesCampaignInfo).Checked)
            {
                ITunesConnectCampaignInfo.Builder iTunesConnectCampaignInfo = new AppLinking.ITunesConnectCampaignInfo.Builder();
                iTunesConnectCampaignInfo.SetAffiliateToken("AffiliateToken");
                iTunesConnectCampaignInfo.SetCampaignToken("CampaignToken");
                iTunesConnectCampaignInfo.SetMediaType("MediaType");
                iTunesConnectCampaignInfo.SetProviderToken("ProviderToken");

                iOSInfoBuilder.SetITunesConnectCampaignInfo(iTunesConnectCampaignInfo.Build());
            }

            builder.SetIOSLinkInfo(iOSInfoBuilder.Build());
            FindViewById<TextView>(Resource.Id.txtLink).Text = builder.BuildAppLinking().Uri.ToString();
        }

        private void AddAndroidAppParameters_Click(object sender, EventArgs e)
        {
            // Set Android App Parameters (Optional)
            // If this parameters not set, the link will be opened in the browser by default.
            AndroidLinkInfo.Builder androidLinkInfo = new AppLinking.AndroidLinkInfo.Builder();
            androidLinkInfo.SetAndroidDeepLink("applink://example/detail");
            androidLinkInfo.SetFallbackUrl("ANDROID_FALLBACK_URL");
            androidLinkInfo.SetMinimumVersion(2);
            androidLinkInfo.SetOpenType(AppLinking.AndroidLinkInfo.AndroidOpenType.CustomUrl);

            builder.SetAndroidLinkInfo(androidLinkInfo.Build());
            FindViewById<TextView>(Resource.Id.txtLink).Text = builder.BuildAppLinking().Uri.ToString();
        }

        internal void SendResult(Uri[] uris)
        {
            FindViewById<TextView>(Resource.Id.txtShortLink).Text = uris[0].ToString();
            FindViewById<TextView>(Resource.Id.txtTestLink).Text = uris[1].ToString();
        }

        internal void SendResult(Uri shortUrl)
        {
            FindViewById<TextView>(Resource.Id.txtConvertedLink).Text = shortUrl.ToString();
        }
    }
}