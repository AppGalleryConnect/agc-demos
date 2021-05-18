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

using Android.App;
using Android.Content;
using Android.Net;
using Android.OS;
using Android.Widget;
using Huawei.Agconnect.Applinking;

namespace AGCAppLinkingXamarinAndroidDemo
{
    [Activity(Name = "com.company.app.DetailActivity", Label = "DetailActivity", Theme = "@style/AppTheme", MainLauncher = true)]
    public class DetailActivity : Activity
    {
        protected async override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);

            SetContentView(Resource.Layout.activity_detail);


            try
            {

                //To receive links, initialize the AGConnectAppLinking instance.
                AGConnectAppLinking appLinkInstance = AGConnectAppLinking.Instance;

                //Before receiving a link of App Linking for the first time, set a custom referrer
                appLinkInstance.SetCustomReferrer(new CustomReferrerProvider());


                //Call GetAppLinkingAsync() to check for links of App Linking to be processed
                ResolvedLinkData resolvedLinkData = await appLinkInstance.GetAppLinkingAsync(this);

                string message = "";
                string campaign = "";
                string social = "";
                Uri deepLink = null;
                if (resolvedLinkData != null)
                {
                    message = $"AppLink: {resolvedLinkData.DeepLink?.ToString()}\nTime: {resolvedLinkData.ClickTimestamp.UnixTimeStampToDateTime()}\nMin App Version: {resolvedLinkData.MinimumAppVersion}";

                    campaign = $"Campaign Name:{resolvedLinkData.CampaignName} \n Campaign Medium:{resolvedLinkData.CampaignMedium} \n Campaign Source:{resolvedLinkData.CampaignSource}";

                    social = $"Social Title:{resolvedLinkData.SocialTitle} \n Social Description:{resolvedLinkData.SocialDescription} \n Social Image URL:{resolvedLinkData.SocialImageUrl}";

                    FindViewById<TextView>(Resource.Id.txtAppLink).Text = message;
                    FindViewById<TextView>(Resource.Id.txtCampaignInfo).Text = campaign;
                    FindViewById<TextView>(Resource.Id.txtSocialInfo).Text = social;

                }

                //Get detail applink
                if (Intent != null)
                {
                    deepLink = resolvedLinkData.DeepLink;
                    if (deepLink != null)
                    {
                        string path = deepLink.LastPathSegment;
                        if (path == "detail")
                        {
                            string detail = "";
                            foreach (var item in deepLink.QueryParameterNames)
                            {
                                detail += deepLink.GetQueryParameter(item);
                            }

                            FindViewById<TextView>(Resource.Id.txtDetailLink).Text = "id = " + detail;
                        }
                    }
                }

            }
            catch (System.Exception ex)
            {
                FindViewById<TextView>(Resource.Id.txtAppLink).Text = ex.Message;
            }
        }
    }
}