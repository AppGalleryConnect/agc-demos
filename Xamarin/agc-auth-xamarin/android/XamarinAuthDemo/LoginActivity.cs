/*
       Copyright 2020-2021. Huawei Technologies Co., Ltd. All rights reserved.

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
using Android.Content.Res;
using Android.Gms.Auth.Api;
using Android.Gms.Auth.Api.SignIn;
using Android.Gms.Common;
using Android.Gms.Common.Apis;
using Android.OS;
using Android.Runtime;
using Android.Support.V7.App;
using Android.Text;
using Android.Util;
using Android.Views;
using Android.Widget;
using Huawei.Agconnect;
using Huawei.Agconnect.Auth;
using Huawei.Agconnect.Config;
using Huawei.Hmf.Tasks;
using Huawei.Hms.Common;
using Huawei.Hms.Support.Account;
using Huawei.Hms.Support.Account.Result;
using Huawei.Hms.Support.Api.Entity.Hwid;
using Huawei.Hms.Support.Hwid;
using Huawei.Hms.Support.Hwid.Request;
using Huawei.Hms.Support.Hwid.Service;
using Java.Util;
using Org.Json;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Xamarin.Auth;
using Xamarin.Facebook;
using Xamarin.Facebook.AppEvents;
using Xamarin.Facebook.Login;
using Scope = Huawei.Hms.Support.Api.Entity.Auth.Scope;

namespace XamarinAuthDemo
{
    [Activity(Theme = "@style/AppTheme", MainLauncher = true)]
    public class LoginActivity : AppCompatActivity
    {
        private const string TAG = "LoginActivity";
        private TextView tvEmail;
        private EditText edtCountryCode;
        private EditText edtAccount;
        private EditText edtPassword;
        private EditText edtVerifyCode;
        private ViewGroup layoutCountryCode;
        private Type type = Type.Email;
        private AGConnectUser agConnectUser;
        private ImageView imgHuaweiId, imgTwitter, imgFacebook, imgGoogle, imgGooglePlay;
        private ICallbackManager callBackManager;

        private const int SignCode = 8888;
        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.activity_login);
            try
            {
                agConnectUser = AGConnectAuth.Instance.CurrentUser;
            }
            catch (Exception ex)
            {
                Log.Error(TAG, ex.Message);
            }

            if (agConnectUser != null)
            {
                StartActivity(new Intent(this, typeof(MainActivity)));
                Finish();
            }
            InitView();
        }
        protected override void AttachBaseContext(Context context)
        {
            base.AttachBaseContext(context);
            AGConnectServicesConfig config = AGConnectServicesConfig.FromContext(context);
            config.OverlayWith(new HmsLazyInputStream(context));
        }

        private void InitView()
        {
            tvEmail = FindViewById<TextView>(Resource.Id.tvEmail);
            edtCountryCode = FindViewById<EditText>(Resource.Id.edtCountryCode);
            edtAccount = FindViewById<EditText>(Resource.Id.edtAccount);
            edtPassword = FindViewById<EditText>(Resource.Id.edtPassword);
            edtVerifyCode = FindViewById<EditText>(Resource.Id.edtVerifyCode);
            layoutCountryCode = FindViewById<LinearLayout>(Resource.Id.layout_cc);
            imgHuaweiId = FindViewById<ImageView>(Resource.Id.imageHuawei);
            imgTwitter = FindViewById<ImageView>(Resource.Id.imageTwitter);
            imgFacebook = FindViewById<ImageView>(Resource.Id.imageFacebook);
            imgGoogle = FindViewById<ImageView>(Resource.Id.imageGoogle);
            imgGooglePlay = FindViewById<ImageView>(Resource.Id.imageGooglePlay);

            Button btnLogin = FindViewById<Button>(Resource.Id.btnLogin);
            Button btnRegister = FindViewById<Button>(Resource.Id.btnRegister);
            Button btnSend = FindViewById<Button>(Resource.Id.btnSend);
            Button btnLoginAnonymous = FindViewById<Button>(Resource.Id.btnLoginAnonymous);

            RadioGroup radioGroup = FindViewById<RadioGroup>(Resource.Id.radiogroup);
            radioGroup.CheckedChange += RadioGroup_CheckedChange;

            // Click events.
            btnLoginAnonymous.Click += BtnLoginAnonymous_Click;
            btnSend.Click += BtnSend_Click;
            btnRegister.Click += BtnRegister_Click;
            btnLogin.Click += BtnLogin_Click;
            imgHuaweiId.Click += ImgHuaweiId_Click;
            imgTwitter.Click += ImgTwitter_Click;
            imgFacebook.Click += ImgFacebook_Click;
            imgGoogle.Click += ImgGoogle_Click;
            imgGooglePlay.Click += ImgGooglePlay_Click;
        }

        private void RadioGroup_CheckedChange(object sender, RadioGroup.CheckedChangeEventArgs e)
        {
            switch (e.CheckedId)
            {
                case Resource.Id.radiobutton_email:
                    type = Type.Email;
                    break;
                case Resource.Id.radiobutton_phone:
                    type = Type.Phone;
                    break;
            }
            UpdateView();
        }

        private void UpdateView()
        {
            if (type == Type.Email)
            {
                tvEmail.Text = GetString(Resource.String.email);
                layoutCountryCode.Visibility = ViewStates.Gone;
            }
            else
            {
                tvEmail.Text = GetString(Resource.String.phone);
                layoutCountryCode.Visibility = ViewStates.Visible;
            }
        }

        private void BtnLogin_Click(object sender, EventArgs e)
        {
            if (type == Type.Email)
            {
                string email = edtAccount.Text.ToString().Trim();
                string password = edtPassword.Text.ToString().Trim();
                string verifyCode = edtVerifyCode.Text.ToString().Trim();
                IAGConnectAuthCredential credential;
                if (TextUtils.IsEmpty(verifyCode))
                {
                    credential = EmailAuthProvider.CredentialWithPassword(email, password);
                }
                else
                {
                    credential = EmailAuthProvider.CredentialWithVerifyCode(email, password, verifyCode);
                }
                SignIn(credential);
            }
            else
            {
                string countryCode = edtCountryCode.Text.ToString().Trim();
                string phoneNumber = edtAccount.Text.ToString().Trim();
                string password = edtPassword.Text.ToString().Trim();
                string verifyCode = edtVerifyCode.Text.ToString().Trim();
                IAGConnectAuthCredential credential;
                if (TextUtils.IsEmpty(verifyCode))
                {
                    credential = PhoneAuthProvider.CredentialWithPassword(countryCode, phoneNumber, password);
                }
                else
                {
                    credential = PhoneAuthProvider.CredentialWithVerifyCode(countryCode, phoneNumber, password, verifyCode);
                }
                SignIn(credential);
            }
        }
        private async void SignIn(IAGConnectAuthCredential credential)
        {
            try
            {
                AGConnectAuth connectAuth = AGConnectAuth.Instance;
                var signInResult = AGConnectAuth.Instance.SignInAsync(credential);

                ISignInResult result = await signInResult;

                if (signInResult.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                {
                    Log.Debug(TAG, signInResult.Result.ToString());
                    StartActivity(new Intent(this, typeof(MainActivity)));
                    Finish();
                }
            }
            catch (Exception ex)
            {
                Log.Error(TAG, ex.Message);
                Toast.MakeText(this, "SignIn failed: " + ex.Message, ToastLength.Long).Show();
            }

        }

        private async void SignInAnonymous()
        {
            try
            {
                var signInAnonymous = AGConnectAuth.Instance.SignInAnonymouslyAsync();
                ISignInResult result = await signInAnonymous;

                if (signInAnonymous.Status.Equals(System.Threading.Tasks.TaskStatus.RanToCompletion))
                {
                    Log.Debug(TAG, "Is Anonymous:" + result.User.IsAnonymous.ToString());
                    StartActivity(new Intent(this, typeof(MainActivity)));
                    Finish();
                }
            }
            catch (Exception ex)
            {
                Log.Error(TAG, ex.Message);
            }
        }

        private void BtnRegister_Click(object sender, EventArgs e)
        {
            Intent intent = new Intent(this, typeof(RegisterActivity));
            intent.PutExtra("registerType", (sbyte)type);
            StartActivity(intent);
        }

        private void BtnSend_Click(object sender, EventArgs e)
        {
            SendVerificationCode();
        }

        private void BtnLoginAnonymous_Click(object sender, EventArgs e)
        {
            SignInAnonymous();
        }
        private void SendVerificationCode()
        {
            VerifyCodeSettings settings = VerifyCodeSettings.NewBuilder()
                .Action(VerifyCodeSettings.ActionRegisterLogin)
                .SendInterval(30)
                .Locale(Locale.English)
                .Build();

            if (type == Type.Email)
            {
                string email = edtAccount.Text.ToString().Trim();

                try
                {
                    EmailAuthProvider.RequestVerifyCode(email, settings);
                    Log.Info(TAG, "RequestVerifyCode called successfully.");
                }
                catch (Exception ex)
                {
                    Toast.MakeText(this, ex.Message, ToastLength.Long).Show();
                }

            }
            else
            {
                string countryCode = edtCountryCode.Text.ToString().Trim();
                string phoneNumber = edtAccount.Text.ToString().Trim();

                try
                {
                    PhoneAuthProvider.RequestVerifyCode(countryCode, phoneNumber, settings);
                    Log.Info(TAG, "RequestVerifyCode function called successfully.");
                }
                catch (Exception ex)
                {
                    Toast.MakeText(this, ex.Message, ToastLength.Long).Show();
                }
            }
        }

        // Third partys
        private void ImgGooglePlay_Click(object sender, EventArgs e)
        {
            string serverClientId = Resources.GetString(Resource.String.server_client_id);
            GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DefaultGamesSignIn)
                .RequestServerAuthCode(serverClientId)
                .Build();

            GoogleSignInClient googleSignInClient = GoogleSignIn.GetClient(this, googleSignInOptions);
            Intent signInIntent = googleSignInClient.SignInIntent;

            StartActivityForResult(signInIntent, 9003);
        }

        private void ImgGoogle_Click(object sender, EventArgs e)
        {
            string serverClientId = Resources.GetString(Resource.String.server_client_id);
            const int RcSignIn = 9001;
            // Configure sign-in to request the user's ID, email address, and basic
            // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DefaultSignIn)
                    .RequestProfile()
                    .RequestIdToken(serverClientId)
                    .Build();

            GoogleSignInClient googleSignClient = GoogleSignIn.GetClient(this, gso);
            Intent signInIntent = googleSignClient.SignInIntent;

            StartActivityForResult(signInIntent, RcSignIn);

        }

        private void ImgFacebook_Click(object sender, EventArgs e)
        {
            callBackManager = CallbackManagerFactory.Create();
            ICollection<string> collection = new List<string>();
            collection.Add("public_profile");
            collection.Add("user_friends");
            LoginManager.Instance.LogInWithReadPermissions(this, collection);
            LoginManager.Instance.RegisterCallback(callBackManager, new FacebookCallback(this));
        }

        private void ImgTwitter_Click(object sender, EventArgs e)
        {
            var auth = new OAuth1Authenticator(
                consumerKey: "enteryourappkey", // For Twitter login, for configure refer http://www.c-sharpcorner.com/article/register-identity-provider-for-new-oauth-application/  
                consumerSecret: "enteryoursecreykey", // For Twitter login, for configure refer http://www.c-sharpcorner.com/article/register-identity-provider-for-new-oauth-application/  
                requestTokenUrl: new Uri("https://api.twitter.com/oauth/request_token"), // These values do not need changing  
                authorizeUrl: new Uri("https://api.twitter.com/oauth/authorize"), // These values do not need changing  
                accessTokenUrl: new Uri("https://api.twitter.com/oauth/access_token"), // These values do not need changing  
                callbackUrl: new Uri("http://mobile.twitter.com/home") // Set this property to the location the user will be redirected too after successfully authenticating
         );
            auth.AllowCancel = true;
            auth.Completed += Auth_Completed;
            
            StartActivity(auth.GetUI(this));
            
        }

        private void Auth_Completed(object sender, AuthenticatorCompletedEventArgs e)
        {
            if (e.IsAuthenticated)
            {
                string defaultToken = "";
                string defaultSecret = "";
                Account account = e.Account;
                if(account.Properties.TryGetValue("oauth_token", out defaultToken) && account.Properties.TryGetValue("oauth_token_secret", out defaultSecret))
                {
                    Log.Info(TAG, "Twitter login success. Accesstoken: " + defaultToken);
                    IAGConnectAuthCredential twitterCredential = TwitterAuthProvider.CredentialWithToken(defaultToken, defaultSecret);
                    SignIn(twitterCredential);
                }
                else
                {
                    Log.Error(TAG, "Cannot find value of oauth_token or oauth_token_secret");
                }
                
                
            }
        }

        private void ImgHuaweiId_Click(object sender, EventArgs e)
        {
            HuaweiIdAuthParamsHelper huaweiIdAuthParamsHelper = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DefaultAuthRequestParam);
            List<Scope> scopeList = new List<Scope>();
            scopeList.Add(new Scope(HwIDConstant.SCOPE.AccountBaseprofile));
            huaweiIdAuthParamsHelper.SetScopeList(scopeList);
            HuaweiIdAuthParams authParams = huaweiIdAuthParamsHelper.SetAccessToken().CreateParams();
            IHuaweiIdAuthService service = HuaweiIdAuthManager.GetService(this, authParams);
            StartActivityForResult(service.SignInIntent,SignCode);
        }
        protected override void OnActivityResult(int requestCode, [GeneratedEnum] Result resultCode, Intent data)
        {
            base.OnActivityResult(requestCode, resultCode, data);
            if (requestCode == SignCode)
            {
                try
                {
                    Task authAccountTask = AccountAuthManager.ParseAuthResultFromIntent(data);
                    if (authAccountTask.IsSuccessful)
                    {
                        AuthAccount authAccount = (AuthAccount)authAccountTask.Result;
                        Log.Info(TAG, "Huawei ID login success. Access token: " + authAccount.AccessToken);
                        // Create Huawei Id credential
                        IAGConnectAuthCredential credential = HwIdAuthProvider.CredentialWithToken(authAccount.AccessToken);
                        // sign In
                        SignIn(credential);
                    }
                }
                catch (Java.Lang.Exception ex)
                {
                    Log.Error(TAG, "Failed: " + ex.Message);
                }
            }
            else if (requestCode == 64206)
            {
                callBackManager.OnActivityResult(requestCode, (int)resultCode, data);
            }
            else if (requestCode == 9001)
            {
                Android.Gms.Tasks.Task task = GoogleSignIn.GetSignedInAccountFromIntent(data);
                task.AddOnSuccessListener(new GoogleSignInListener(this));
                task.AddOnFailureListener(new GoogleSignInListener(this));
            }else if (requestCode == 9003)
            {
                Android.Gms.Tasks.Task task = GoogleSignIn.GetSignedInAccountFromIntent(data);
                task.AddOnSuccessListener(new GooglePlaySignInListener(this));
                task.AddOnFailureListener(new GooglePlaySignInListener(this));
            }
        }
        private class GoogleSignInListener : Java.Lang.Object, Android.Gms.Tasks.IOnSuccessListener, Android.Gms.Tasks.IOnFailureListener
        {
            LoginActivity loginActivity;
            public GoogleSignInListener(LoginActivity loginActivity)
            {
                this.loginActivity = loginActivity;
            }
            public void OnFailure(Java.Lang.Exception error)
            {
                Log.Error("GoogleSignInListener", error.Message);
            }

            public void OnSuccess(Java.Lang.Object signInAccount)
            {
                GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount)signInAccount;
                string idToken = googleSignInAccount.IdToken;
                Log.Debug("GoogleSignInlistener", "Successfuly. Access token: " + idToken);
                IAGConnectAuthCredential credential = GoogleAuthProvider.CredentialWithToken(idToken);
                loginActivity.SignIn(credential);
            }
        }
        private class GooglePlaySignInListener : Java.Lang.Object, Android.Gms.Tasks.IOnSuccessListener, Android.Gms.Tasks.IOnFailureListener
        {
            LoginActivity loginActivity;
            public GooglePlaySignInListener(LoginActivity loginActivity)
            {
                this.loginActivity = loginActivity;
            }
            public void OnFailure(Java.Lang.Exception error)
            {
               
                Log.Error("GooglePlaySignInListener", error.Message);
            }

            public void OnSuccess(Java.Lang.Object signInAccount)
            {
                GoogleSignInAccount googleSignInAccount = (GoogleSignInAccount)signInAccount;
                string authCode = googleSignInAccount.ServerAuthCode;
                Log.Debug("GooglePlaySignInListener", "Successfuly. Auth code: " + authCode);
                IAGConnectAuthCredential credential = GoogleGameAuthProvider.CredentialWithToken(authCode);
                loginActivity.SignIn(credential);
            }
        }
        private class FacebookCallback : Java.Lang.Object, IFacebookCallback
        {
            LoginActivity loginActivity;
            public FacebookCallback(LoginActivity loginActivity)
            {
                this.loginActivity = loginActivity;
            }
            public void OnCancel()
            {
                Log.Debug("IFacebookCallback", "Cancelled.");
            }

            public void OnError(FacebookException error)
            {
                Log.Error("IFacebookCallback", "Failed: " + error.Message);
            }

            public void OnSuccess(Java.Lang.Object result)
            {
                LoginResult loginResult = (LoginResult)result;

                Log.Debug("IFacebookCallback", "Facebook login successfuly. Access token: " + loginResult.AccessToken.Token);
                IAGConnectAuthCredential credential = FacebookAuthProvider.CredentialWithToken(loginResult.AccessToken.Token);
                loginActivity.SignIn(credential);

            }
        }
    }
}