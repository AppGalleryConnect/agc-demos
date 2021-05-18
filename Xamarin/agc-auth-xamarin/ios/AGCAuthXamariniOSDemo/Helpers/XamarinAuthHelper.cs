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
using Foundation;
using Huawei.Agconnect.Auth;
using UIKit;
using Xamarin.Auth;
using GoogleSignIn = Google.SignIn;

namespace AGCAuthXamariniOSDemo
{
    public class XamarinAuthHelper : NSObject
    {
        private UIViewController parent;
        private Action<AGCAuthCredential> authorizationCompleted;

        public XamarinAuthHelper(UIViewController parent)
        {
            this.parent = parent;
        }

        public void AuthorizeFacebook(Action<AGCAuthCredential> AuthorizationCompleted)
        {
            this.authorizationCompleted = AuthorizationCompleted;
            var auth = new OAuth2Authenticator(
              clientId: "FB_CLIENT_ID",
              scope: "public_profile, email",
              authorizeUrl: new Uri("https://m.facebook.com/dialog/oauth/"),
              redirectUrl: new Uri("https://developer.huawei.com"));

            var ui = auth.GetUI();

            auth.Completed += FacebookAuth_Completed;

            parent.PresentViewController(ui, true, null);
        }

        public void FacebookAuth_Completed(object sender, AuthenticatorCompletedEventArgs e)
        {
            if (!e.IsAuthenticated)
                return;

            var credential = AGCFacebookAuthProvider.CredentialWithToken(e.Account.Properties["access_token"]);
            authorizationCompleted.Invoke(credential);
            parent.DismissViewController(true, null);

        }

        public void AuthorizeTwitter(Action<AGCAuthCredential> AuthorizationCompleted)
        {
            this.authorizationCompleted = AuthorizationCompleted;
            var auth = new OAuth1Authenticator(
                    consumerKey: "CONSUMER_KEY",
                    consumerSecret: "CONSUMER_SECRET",
                    requestTokenUrl: new Uri("https://api.twitter.com/oauth/request_token"),
                    authorizeUrl: new Uri("https://api.twitter.com/oauth/authorize"),
                    accessTokenUrl: new Uri("https://api.twitter.com/oauth/access_token"),
                    callbackUrl: new Uri("http://mobile.twitter.com/home"));

            var ui = auth.GetUI();
            auth.Completed += TwitterAuth_Completed;
            parent.PresentViewController(ui, true, null);
        }

        private void TwitterAuth_Completed(object sender, AuthenticatorCompletedEventArgs e)
        {
            if (!e.IsAuthenticated)
                return;

            var credential = AGCTwitterAuthProvider.CredentialWithToken(e.Account?.Properties["oauth_token"]?.ToString() ?? "", e.Account?.Properties["oauth_token_secret"]?.ToString());

            authorizationCompleted.Invoke(credential);

            parent.DismissViewController(true, null);
        }

        internal void AuthorizeGoogle(Action<AGCAuthCredential> AuthorizationCompleted)
        {
            this.authorizationCompleted = AuthorizationCompleted;
            GoogleSignIn.SignIn.SharedInstance.PresentingViewController = parent;
            GoogleSignIn.SignIn.SharedInstance.SignInUser();
            GoogleSignIn.SignIn.SharedInstance.SignedIn += (googleSender, e) =>
            {
                // Perform any operations on signed in user here.
                if (e.User != null && e.Error == null)
                {
                    Console.WriteLine(string.Format("Signed in user: {0}", e.User.Profile.Name));
                    var credential = AGCGoogleAuthProvider.CredentialWithToken(e.User?.Authentication.IdToken ?? "");
                    authorizationCompleted.Invoke(credential);
                }

            };

            GoogleSignIn.SignIn.SharedInstance.Disconnected += (googleSender, e) =>
            {
                // Perform any operations when the user disconnects from app here.
                Console.WriteLine("Disconnected user");

            };

        }
    }
}