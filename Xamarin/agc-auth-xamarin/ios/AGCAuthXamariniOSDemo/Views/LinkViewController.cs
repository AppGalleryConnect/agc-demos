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
using System.Linq;
using Foundation;
using Huawei.Agconnect.Auth;
using UIKit;

namespace AGCAuthXamariniOSDemo.Views
{
    public class LinkItem
    {
        public string Name { get; set; }
        public int ProviderType { get; set; }
        public bool IsLinked { get; set; }
        public LinkItem(string name, AGCAuthProviderType providerType, bool isLinked)
        {
            Name = name;
            ProviderType = (int)providerType;
            IsLinked = IsLinked;
        }
    }
    public partial class LinkViewController : UIViewController
    {
        private LinkItem[] linkAccounts;

        public LinkViewController() : base("LinkViewController", null)
        {
        }
        
        public override void ViewDidLoad()
        {
            base.ViewDidLoad();
            Title = "Link Accounts";
            
            linkAccounts = new LinkItem[] {
                new LinkItem("Wechat", AGCAuthProviderType.WeiXin, false),
            new LinkItem("QQ", AGCAuthProviderType.Qq, false),
            new LinkItem("Weibo", AGCAuthProviderType.WeiBo, false),
            new LinkItem("Facebook", AGCAuthProviderType.Facebook, false),
            new LinkItem("Google", AGCAuthProviderType.Google, false),
            new LinkItem("Twitter", AGCAuthProviderType.Twitter, false),
            new LinkItem("Phone", AGCAuthProviderType.Phone, false),
            new LinkItem("Email", AGCAuthProviderType.Email, false),
            new LinkItem("Apple", AGCAuthProviderType.Apple, false)
         };
            linkTableView.Source = new LinkTableSource(linkAccounts, this);
            RefreshLinkState();
        }

        public void RefreshLinkState()
        {
            var providers = AGCAuth.GetInstance().CurrentUser.ProviderInfo;
            if (providers != null)
                foreach (var item in providers)
                {
                    string itemProvider = item["provider"].ToString();
                    int pr = Convert.ToInt32(itemProvider);
                    if (linkAccounts.FirstOrDefault(a => a.ProviderType == pr) is LinkItem lItem)
                        lItem.IsLinked = true;
                }

        }

        public override void DidReceiveMemoryWarning()
        {
            base.DidReceiveMemoryWarning();
            // Release any cached data, images, etc that aren't in use.
        }
    }
}

