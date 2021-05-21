# AGC Auth Cordova Demo

## Contents

- [1. Introduction](#1-introduction)
- [2. Installation Guide](#2-installation-guide)
  - [2.1. Creating a Project in AppGallery Connect](#21-creating-a-project-in-appgallery-connect)
  - [2.2. Obtaining agconnect-services.json and agconnect-services.plist](#22-obtaining-agconnect-servicesjson-and-agconnect-servicesplist)
  - [2.3. Cordova](#23-cordova)
    - [2.3.1. iOS App Development](#231-ios-app-development)
    - [2.3.2. Android App Development](#232-android-app-development)
- [3. Configuration and Description](#3-configuration-and-description)
- [4. Licencing and Terms](#4-licencing-and-terms)

---

## 1. Introduction

This demo application demonstrates the usage of AGC Auth Cordova plugin.

---

## 2. Installation Guide

Before you get started, you must register as a HUAWEI Developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/?ha_source=hms1) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/10104?ha_source=hms1).

### 2.1. Creating a Project in AppGallery Connect

Creating an app in AppGallery Connect is required in order to communicate with the Huawei services. To create an app, perform the following steps:

1. Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and select **My projects**.
2. Select your project from the project list or create a new one by clicking the **Add Project** button.
3. Go to **Project settings** > **General information**, and click **Add app**.
   If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.
4. On the **Add app** page, enter the app information, and click **OK**.

### 2.2. Obtaining agconnect-services.json and agconnect-services.plist

1. Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and select your project from **My Projects**. Then go to **Project settings** > **General information**. In the **App information** field,

   - If platform is Android, click **agconnect-services.json** button to download the configuration file.
   - If platform is iOS, click **agconnect-services.plist** button to download the configuration file.

### 2.3. Cordova

1. Install Cordova CLI.

   ```bash
   npm install -g cordova
   ```

2. Set preference in your Ionic project config.xml.

   ```xml
   <!--<platform name="ios">-->
    <preference name="deployment-target" value="11.0" />
    <preference name="SwiftVersion" value="5" />
   ```

3. Update the widget **`id`** property which is specified in the **`config.xml`** file. It must be same with **client > package_name** value of the **`agconnect-services.json`** and **`agconnect-services.plist`** files.

4. Add the **Android** or **iOS** platform to the project if haven't done before.

   ```bash
   cordova platform add android@9.0
   ```

   ```bash
   cordova platform add ios
   ```

5. Install `AGC Auth Cordova Plugin` to the project.

   - Run the following command in the root directory of your project to install it through **npm**.

     ```bash
     cordova plugin add @cordova-plugin-agconnect/auth
     ```

#### 2.3.1. iOS App Development

1. Add **`agconnect-services.plist`** file to the app's root directory of your Xcode project.

2. Check Signing & Capabilities tab page of your Xcode project.

3. Run the application.

   ```bash
   cordova run ios --device
   ```

#### 2.3.2. Android App Development

1. Copy **`agconnect-services.json`** file to **`<project_root>/platforms/android/app`** directory your Android project.

2. Run the application.

   ```bash
   cordova run android --device
   ```

---

## 3. Configuration and Description

### Third Party Authentications

#### Login With Twitter

For using login with twitter functionality of example app. You will need:

- A Twitter Developer Account If you need one, click [Apply for Twitter Developer Account](https://developer.twitter.com/en/apply-for-access)
- An API Key and API Secret Key for Your App. Twitter will provide these after an application created on twitter developer platform.

##### Enable Twitter authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Twitter Authentication Mode.

**Step 5:** Enter Twitter **API Key** and **API Secret Key**, then click **OK**

##### Integrating Twitter Login With Auth Plugin

- Obtain auth token and auth token secret from twitter authorization by official plugin or third party plugins.
- Call signIn function and pass provider type and credential object as parameter.

```javascript
AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.Twitter_Provider, {
  token: "Obtain from Twitter authorization token",
  secret: "Obtain from Twitter authorization secret",
  autoCreateUser: true,
})
  .then((result) => {
    console.log("logInWithTwitterLogIn :: Success");
    $("main").style.display = "none";
    $("userProfileScreen").style.display = "block";
    updateUserProfileLayout();
  })
  .catch((error) =>
    alert("logInWithTwitterLogIn :: Error! " + JSON.stringify(error, null, 1))
  );
```

#### Login With WeChat

For using login with WeChat functionality of example app. You will need:

- A WeChat Developer Account If you need one, click [here](https://open.weixin.qq.com/?lang=en) and create account.
- An App Id and App Secret for Your App. After an application created on WeChat platform, WeChat will provide an app id and app secret.

##### Enable WeChat authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on WeChat Authentication Mode.

**Step 5:** Enter WeChat **App Id** and **App Secret**, then click **OK**

##### Integrating WeChat Login With Auth Plugin

- Obtain auth access token and open id from wechat authorization by official plugin or third party plugins.
- Call signIn function and provider type and pass credential object as parameter.

```javascript
AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.WeiXin_Provider, {
  token: "Obtain from WeChat authorization token",
  openId: "Obtain from WeChat authorization secret",
  autoCreateUser: true,
})
  .then((result) => {
    console.log("logInWithWeChatLogIn :: Success");
    $("main").style.display = "none";
    $("userProfileScreen").style.display = "block";
    updateUserProfileLayout();
  })
  .catch((error) =>
    alert("logInWithWeChatLogIn :: Error! " + JSON.stringify(error, null, 1))
  );
```

#### Login With QQ

For using login with QQ functionality of example app. You will need:

- A QQ for Developers Account If you need one, click [Tencent Open Platform](https://open.qq.com/eng/) and create account.
- An App ID and App key for Your App. After an application created on QQ platform, QQ will provide an App Id and App Key.

##### Enable QQ authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on QQ Authentication Mode.

**Step 5:** Enter QQ **App ID** and **App Key**, then click **OK**

##### Integrating QQ Login With Auth Plugin

- Obtain auth access token and open id from QQ authorization by official plugin or third party plugins.
- Call signIn function and provider type and pass credential object as parameter.

```javascript
AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.QQ_Provider, {
  token: "Obtain from QQ authorization token",
  openId: "Obtain from QQ authorization openId",
  autoCreateUser: true,
})
  .then((result) => {
    console.log("logInWithQQLogIn :: Success");
    $("main").style.display = "none";
    $("userProfileScreen").style.display = "block";
    updateUserProfileLayout();
  })
  .catch((error) =>
    alert("logInWithQQLogIn :: Error! " + JSON.stringify(error, null, 1))
  );
```

#### Login With Weibo

For using login with Weibo functionality of example app. You will need:

- A Weibo for Developers Account If you need one, click [Tencent Open Platform](https://open.qq.com/eng/) and create account.
- An App Key and App Secret for Your App. After an application created on Weibo platform, Weibo will provide an App Key and App Secret.

##### Enable QQ authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Weibo Authentication Mode.

**Step 5:** Enter Weibo **App Key** and **App Secret**, then click **OK**

##### Integrating Weibo Login With Auth Plugin

- Obtain auth token and uid from Weibo authorization by official plugin or third party plugins.
- Call signIn function and provider type and pass credential object as parameter.

```javascript
AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.WeiBo_Provider, {
     token: "Obtain from WeiBo authorization token",
     uid: "Obtain from WeiBo authorization uid",
     autoCreateUser: true,
 }).then((result) => {
     console.log("logInWithWeiBoLogIn :: Success");
     $("main").style.display = "none";
     $("userProfileScreen").style.display = "block";
     updateUserProfileLayout();
 }).catch((error) => alert("logInWithWeiBoLogIn :: Error! " + JSON.stringify(error, null, 1)));
```

#### Login With Facebook

For using login with facebook functionality of example app. You will need:

- A Facebook for Developers Account If you need one, click [Create a Facebook for Developers Accont](https://developers.facebook.com)
- An App ID for Your App. [Get an App ID for your app](https://developers.facebook.com/apps/)

##### Enable Facebook authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Facebook Authentication Mode.

**Step 5:** Enter Facebook **App ID** and **App Secret**, then click **OK**

##### Integrating Facebook Login With Auth Plugin

- Obtain auth token from Facebook authorization by official plugin or third party plugins.
- Call signIn function and provider type and pass credential object as parameter.

```javascript
AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.Facebook_Provider, {
  token: "Obtain from Facebook authorization token",
  autoCreateUser: true,
})
  .then((result) => {
    console.log("logInWithFacebookLogIn :: Success");
    $("main").style.display = "none";
    $("userProfileScreen").style.display = "block";
    updateUserProfileLayout();
  })
  .catch((error) =>
    alert("logInWithFacebookLogIn :: Error! " + JSON.stringify(error, null, 1))
  );
```

#### Login With Apple Account

> Login With Apple Account only supports iOS platform.

For using login with Apple functionality of example app. You will need:

- An Apple for Developers Account If you need one, click [Create a Apple for Developers Accont](https://developers.apple.com)

##### Enable Apple Account authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Apple Account Authentication Mode.

##### Integrating Apple Login With Auth Plugin

- Obtain auth identity token and nonce Apple authorization by official plugin or third party plugins.
- Call signIn function and provider type and pass credential object as parameter.

```javascript
/// Generates a cryptographically secure random nonce, to be included in a '0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._'

/// for AppGallery Connect to perform sign-in authentication: a random character string 'rawNonce'

/// Generate 'nonce'
///add a random character string to the authentication request and encrypt it using the SHA-256 algorithm. A String value used to associate a client session and the identity token. nonce required when logging Apple authorization by official plugin or third party plugins.

AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.Apple_Provider, {
    identityToken: "Obtain from Apple authorization identityToken",
    nonce: "sha256 nonce",
    autoCreateUser: true,
 }).then((result) => {
    console.log("logInWithAppleLogIn :: Success");
    $("main").style.display = "none";
    $("userProfileScreen").style.display = "block";
    updateUserProfileLayout();
 }).catch((error) => alert("logInWithAppleLogIn :: Error! " + JSON.stringify(error, null, 1)));
```

#### Login With Huawei Account

##### Configuring the Signing Certificate Fingerprint

**Step 1:** Open terminal, change **/path/to/my/keystore.jks** with your keystore file's absolute path and run the command below.

```bash
keytool -keystore /path/to/my/keystore.jks -list -v
```

**Step 2:** Obtain **SHA256** finger print from the result.

**Step 3:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 4:** Find your **app** project and click the app.

**Step 5:** Go to **Project Settings** > **General Information** > **App Information** and enter **SHA-256 certificate fingerprint**.

##### Enable Huawei Account authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** You can find your **App ID** and **App Secret** on **Project Settings** > **General Information** > **App Information**

**Step 4:** On left menu go to **build** > **Auth Service**

**Step 5:** Click **enable** on Huawei Account Authentication Mode.

**Step 6:** Enter Huawei **App ID** and **App Secret**, then click **OK**

##### Integrate HMSAccount plugin
```bash
cordova plugin add @hmscore/cordova-plugin-hms-account
```

#### Login With Huawei Game Account

##### Configuring the Signing Certificate Fingerprint

**Step 1:** Open terminal, change **/path/to/my/keystore.jks** with your keystore file's absolute path and run the command below.

```bash
keytool -keystore /path/to/my/keystore.jks -list -v
```

**Step 2:** Obtain **SHA256** finger print from the result.

**Step 3:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 4:** Find your **app** project and click the app.

**Step 5:** Go to **Project Settings** > **General Information** > **App Information** and enter **SHA-256 certificate fingerprint**.

##### Enable Huawei Game Service authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** You can find your **App ID** and **App Secret** on **Project Settings** > **General Information** > **App Information**

**Step 4:** On left menu go to **build** > **Auth Service**

**Step 5:** Go to Build > Game Service and query the game public key and private key.

**Step 6:** Click **enable** on Huawei Game Service Authentication Mode.

**Step 7:** Enter Huawei **game public key** and **game private key**, then click **OK**

##### Integrate HMS Game Service plugin
```bash
cordova plugin add @hmscore/hms-js-gameservice
```

#### Login With Google

For using Log in with Google functionality you will need a **Firebase** app. Log in to [Firebase Console](https://console.firebase.google.com/) and create an app.

##### Enable Google authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Google Authentication Mode.

**Step 5:** Set **Client ID** and **Client Secret** to the values of Client ID and Client secret under Credentials > OAuth 2.0 client IDs > Web client (Auto-created for Google Sign-in) page at [Google API Console](https://console.developers.google.com/apis/credentials).

##### Integrating Google Login With Auth Plugin

- Obtain auth token from Google authorization by official plugin or third party plugins.
- Call signIn function and provider type and pass credential object as parameter.

```javascript
AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.Google_Provider, {
  idToken: "Obtain from Google authorization idToken",
  autoCreateUser: true,
})
  .then((result) => {
    console.log("logInWithGoogleLogIn :: Success");
    $("main").style.display = "none";
    $("userProfileScreen").style.display = "block";
    updateUserProfileLayout();
  })
  .catch((error) =>
    alert("logInWithGoogleLogIn :: Error! " + JSON.stringify(error, null, 1))
  );
```

#### Login With Google Game

For using Log in with Google functionality you will need a **Firebase** app. Log in to [Firebase Console](https://console.firebase.google.com/) and create an app.

##### Enable Google authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Google Play Games Authentication Mode.

**Step 5:** Set **Client ID** and **Client Secret** to the values of Client ID and Client secret under Credentials > OAuth 2.0 client IDs > Web client (Auto-created for Google Sign-in) page at [Google API Console](https://console.developers.google.com/apis/credentials).

##### Integrating Google Game Login With Auth Plugin

- Obtain auth token from Google Game authorization by official plugin or third party plugins.
- Call signIn function and provider type and pass credential object as parameter.

```javascript
AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.GoogleGame_Provider, {
  idToken: "Obtain from Google Game authorization idToken",
  autoCreateUser: true,
})
  .then((result) => {
    console.log("logInWithGoogleGameLogIn :: Success");
    $("main").style.display = "none";
    $("userProfileScreen").style.display = "block";
    updateUserProfileLayout();
  })
  .catch((error) =>
    alert(
      "logInWithGoogleGameLogIn :: Error! " + JSON.stringify(error, null, 1)
    )
  );
```
#### Login With Self-owned Account

If you have built an authentication system by yourself, you can connect this system to Auth Service to allow your users to use accounts provided by your authentication system for sign-in.

##### Enable Self-owned Account system authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Your own account system Authentication Mode.

**Step 5:** Set **Signature public key** to the values of interconnect with your account system to provide complete data security management for your users.

##### Integrating Self Build Login


- Obtain auth token from self build authorization.
- Call signIn function and provider type and pass credential object as parameter.

```javascript
AGCAuthPlugin.signIn(AGCAuthPlugin.Provider.SelfBuild_Provider, {
  token: "Obtain from Your Own Account System",
  autoCreateUser: true,
})
  .then((result) => {
    console.log("logInWithSelfBuildLogIn :: Success");
    $("main").style.display = "none";
    $("userProfileScreen").style.display = "block";
    updateUserProfileLayout();
  })
  .catch((error) =>
    alert("logInWithSelfBuildLogIn :: Error! " + JSON.stringify(error, null, 1));
  );
```

### Third Party Account Linking

- Obtain auth token from third party authorization by official plugin or third party plugins.
- Call link function and provider type and pass credential object as parameter.

```javascript
AGCAuthPlugin.getCurrentUser().then((currentUser) => {
   let twitterCredential = {
      token: "Obtain from Twitter authorization token",,
      secret: "Obtain from Twitter authorization secret",,
      autoCreateUser: true
   }
   currentUser.link(AGCAuthPlugin.Provider.Twitter_Provider, twitterCredential).then(() => {
      alert('Succesfully linked account.');
   }).catch((error) => alert("currentUser.link :: Error! " + JSON.stringify(error, null, 1)));
}).catch((error) => alert("getCurrentUser :: Error! " + JSON.stringify(error, null, 1)));
```

---

## 4. Licencing and Terms

AGC Auth Cordova Plugin is licensed under the [Apache 2.0 license](LICENCE).
