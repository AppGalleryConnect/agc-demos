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
using AuthenticationServices;
using Foundation;
using Huawei.Agconnect.AgconnectCore;
using Huawei.Agconnect.Auth;

using System;
using UIKit;

namespace AGCAuthXamariniOSDemo
{
    public partial class ViewController : UIViewController, IASAuthorizationControllerDelegate, IASAuthorizationControllerPresentationContextProviding
    {
        private XamarinAuthHelper xamarinAuthHelper;

        public ViewController(IntPtr handle) : base(handle)
        {
        }

        public override void ViewDidLoad()
        {
            base.ViewDidLoad(); 
            Title = "Sign In";
            appleLoginButton.TouchUpInside += AppleLoginButton_TouchUpInside;
            xamarinAuthHelper = new XamarinAuthHelper(this);
        }

        public override void ViewDidAppear(bool animated)
        {
            base.ViewDidAppear(animated);
            if (AGCAuth.GetInstance().CurrentUser != null)
            {
                OpenUserInfoView(AGCAuth.GetInstance().CurrentUser);
            }
        }

        private void OpenUserInfoView(AGCUser user)
        {
            var storyboard = UIStoryboard.FromName("Main", null);
            var secondViewController = storyboard.InstantiateViewController("InfoVC");

            this.NavigationController?.PushViewController(secondViewController, true);
        }

        partial void LoginSwitchChange(NSObject sender)
        {
            bool index = Convert.ToBoolean((sender as UISegmentedControl).SelectedSegment);
            countryCodeLabel.Hidden = index;
            accountText.Text = "";
            accountText.Placeholder = index ? "please enter email" : "please enter phone number";
        }

        partial void Login(NSObject sender)
        {
            nint index = loginTypeSwitch.SelectedSegment;
            if (index == 0)
            {
                PhoneLogin();
            }
            else
            {
                EmailLogin();
            }
        }

        private void EmailLogin()
        {
            AGCAuthCredential credential;
            string email = accountText.Text;
            string password = passwordText.Text;
            string verificationCode = codeText.Text;
            if (codeText.Text.Length == 0)
            {
                // Generate a credential to sign in to email account with password
                credential = AGCEmailAuthProvider.CredentialWithEmail(email, password);
            }
            else
            {
                // Generate a credential to sign in to email account with verification code
                credential = AGCEmailAuthProvider.CredentialWithEmail(email, password, verificationCode);
            }
            SignInWithCredential(credential);
        }

        private void PhoneLogin()
        {
            AGCAuthCredential credential;
            string countryCode = "90";
            string phoneNumber = accountText.Text;
            string password = passwordText.Text;
            string verificationCode = codeText.Text;
            if (codeText.Text.Length == 0)
            {
                // Generate a credential to sign in phone account with password
                credential = AGCPhoneAuthProvider.CredentialWithCountryCode(countryCode, phoneNumber, password);
            }
            else
            {
                // Generate a credential to sign in phone account with verification code
                credential = AGCPhoneAuthProvider.CredentialWithCountryCode(countryCode, phoneNumber, password, verificationCode);
            }
            SignInWithCredential(credential);
        }

        private void SignInWithCredential(AGCAuthCredential credential)
        {
            if (credential == null)
            {
                Console.WriteLine("no credential");
            }

            // send sign in request with credential
            HMFTask<NSObject> signIn = AGCAuth.GetInstance().SignIn(credential);

            signIn.AddOnSuccessCallback((result) =>
                {
                    Console.WriteLine("Sign in success");
                    OpenUserInfoView((result as AGCSignInResult).User);

                });
            signIn.AddOnFailureCallback((error) =>
                {

                    Console.WriteLine("Sign in failed -->" + error);


                });

            Console.WriteLine("sign in failed");
        }

        partial void Register(NSObject sender)
        {
            nint index = loginTypeSwitch.SelectedSegment;
            if (index == 0)
            {
                RegisterPhoneAccount();
            }
            else
            {
                RegisterEmailAccount();
            }
        }

        private void RegisterEmailAccount()
        {
            string email = accountText.Text;
            string password = passwordText.Text;
            string verificationCode = codeText.Text;
            // register email account
            HMFTask<NSObject> register = AGCAuth.GetInstance().CreateUserWithEmail(email, password, verificationCode);
            register.AddOnSuccessCallback((result) =>
            {
                AGCSignInResult user = result as AGCSignInResult;
                Console.WriteLine("Account register success");
                OpenUserInfoView(user.User);
            });
            register.AddOnFailureCallback((error) =>
            {
                Console.WriteLine("Email create failed");
            });
        }

        private void RegisterPhoneAccount()
        {
            string countryCode = "90";
            string phoneNumber = accountText.Text;
            string password = passwordText.Text;
            string verificationCode = codeText.Text;
            // register phone account
            HMFTask<NSObject> register = AGCAuth.GetInstance().CreateUserWithCountryCode(countryCode, phoneNumber, password, verificationCode);
            register.AddOnSuccessCallback((result) =>
            {
                AGCSignInResult user = result as AGCSignInResult;
                Console.WriteLine("account register success --- >" + user.User.DisplayName);
                OpenUserInfoView(user.User);
            });
            register.AddOnFailureCallback((error) =>
            {

                Console.WriteLine("account register failed-->" + error);


            });


        }

        partial void SendCode(NSObject sender)
        {
            nint index = loginTypeSwitch.SelectedSegment;
            if (index == 0)
            {
                if (accountText.Text.Length == 0)
                {
                    Console.WriteLine("Please enter phone number");
                }
                // send verification code to phone
                BaseProvider.SendVerifyCodeWithCountryCode(countryCodeLabel.Text, accountText.Text, AGCVerifyCodeAction.RegisterLogin);
            }
            else
            {
                if (accountText.Text.Length == 0)
                {
                    Console.WriteLine("please enter email");
                }
                // send verification code to email
                BaseProvider.SendVerifyCodeWithEmail(accountText.Text, AGCVerifyCodeAction.RegisterLogin);
            }
        }

        partial void AnonymousLogin(NSObject sender)
        {
            // sign in anonymously
            HMFTask<NSObject> signInAnonymously = AGCAuth.GetInstance().SignInAnonymously();
            signInAnonymously.AddOnSuccessCallback((result) =>
            {
                AGCSignInResult signInResult = result as AGCSignInResult;
                Console.WriteLine("AnonymousLogin success");
                OpenUserInfoView(signInResult.User);
            });
            signInAnonymously.AddOnFailureCallback((error) =>
            {
                Console.WriteLine("AnonymousLogin failed");
            });
        }
        
        partial void WeChatLogin(NSObject sender)
        {
            AGCAuthCredential weChatCredential = AGCWeiXinAuthProvider.CredentialWithToken("TOKEN", "OPEN_ID");
            SignIn(weChatCredential);
        }

        partial void WeiboLogin(NSObject sender)
        {
            AGCAuthCredential weiboCredential = AGCWeiboAuthProvider.CredentialWithToken("TOKEN", "UID");
            SignIn(weiboCredential);
        }

        partial void QQLogin(NSObject sender)
        {
            AGCAuthCredential qqCredential = AGCQQAuthProvider.CredentialWithToken("TOKEN", "OPEN_ID");
            SignIn(qqCredential);
        }

        partial void FacebookLogin(NSObject sender)
        {
            xamarinAuthHelper.AuthorizeFacebook(AuthorizationCompleted);
        }

        partial void GooglePlusLogin(NSObject sender)
        {
            xamarinAuthHelper.AuthorizeGoogle(AuthorizationCompleted);
        }

        partial void TwitterLogin(NSObject sender)
        {
            xamarinAuthHelper.AuthorizeTwitter(AuthorizationCompleted);
        }
        
        private void AuthorizationCompleted(AGCAuthCredential credential)
        {
            if (credential != null)
                SignIn(credential); 
        }

        private void UpdateSnapshotListener(AGCTokenSnapshot obj)
        {
            throw new NotImplementedException();
        }

        private void SignIn(AGCAuthCredential credential)
        {
            if (credential != null)
            {
                HMFTask<NSObject> signIn = AGCAuth.GetInstance().SignIn(credential);
                
                signIn.AddOnSuccessCallback((result) =>
                {
                    AGCSignInResult user = result as AGCSignInResult;
                    Console.WriteLine("sign in success");
                    OpenUserInfoView(user.User);

                });
                signIn.AddOnFailureCallback((error) =>
                {

                    Console.WriteLine("sign in failed-->" + error);
                });
            }
            else
            {
                Console.WriteLine("no credential");
            }
        }


       
        private void AppleLoginButton_TouchUpInside(object sender, EventArgs e)
        {
            AGCAuthCredential credential = AGCAppleIDAuthProvider.CredentialWithIdentityToken("IdentityToken", "RequestNonce");
            SignIn(credential);
        }


        public override void DidReceiveMemoryWarning()
        {
            base.DidReceiveMemoryWarning();
            // Release any cached data, images, etc that aren't in use.
        }

        public UIWindow GetPresentationAnchor(ASAuthorizationController controller)
        {
            throw new NotImplementedException();
        }
    }
}