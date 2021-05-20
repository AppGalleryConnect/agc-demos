# AppGallery Connect Auth Service Flutter Plugin - Demo

---

## Contents

- [Introduction](#1-introduction)
- [Installation](#2-installation)
- [Configuration](#3-configuration)
- [Licensing and Terms](#4-licensing-and-terms)

---

## 1. Introduction

This demo project is an example to demonstrate the features of the **AppGallery Connect Flutter Auth Service
** Plugin.

---

## 2. Installation

Before you get started, you must register as a HUAWEI developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/10104).

### Creating a Project in AppGallery Connect

Creating an app in AppGallery Connect is required in order to communicate with the Huawei services. To create an app, perform the following steps:

**Step 1.** Set an unique **Application ID** on the app level build gradle file located on **example/android/app/build.gradle**. You should also change the **package names** for the manifest files in the **/example/android/app/src/** directory and on the **MainActivity.java** to match with the Application ID.

```gradle
<!-- Other configurations ... -->
  defaultConfig {
    // Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html). You may need to change the package name on AndroidManifest.xml and MainActivity.java respectively.
    // The Application ID here should match with the Package Name on the AppGalleryConnect
    applicationId "<Enter_your_package_name_here>" // For ex: "com.developer.appmessaging"
    minSdkVersion 19
    <!-- Other configurations ... -->
}
```

**Step 2.** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select **My projects**.

**Step 3.** Select your project from the project list or create a new one by clicking the **Add Project** button.

**Step 4.** Go to **Project Setting** > **General information**, and click **Add app**.
If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.

**Step 5.** On the **Add app** page, enter the **Application ID** you've defined before as the **Package Name** here, then fill the necessary fields and click **OK**.

### Enabling Auth Service

You need to enable App Messaging before using it. If you have enabled it, skip this step.

**Step 1.** In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), find your project, and click the app for which you want to use Auth Service.

**Step 2.** Select any menu under **Auth Service** and click **Enable Auth Services**.

### Build & Run the project

**Step 1:** Run the following command to update package info.

```
[project_path]> flutter pub get
```

**Step 2:** Run the following command to start the demo app.

```
[project_path]> flutter run
```

---

## 3. Configuration

### Third Party Authentications

#### Login With Twitter

For using login with twitter functionality of example app. You will need:

- A Twitter Developer Account If you need one, click [Apply for Twitter Developer Account](https://developer.twitter.com/en/apply-for-access)
- An API Key and API Secret Key for Your App. Twitter will provide these after an application created on twitter developer platform.

##### Enable Twitter authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Twitter Authentication Mode.

**Step 5:** Enter Twitter **API Key** and **API Secret Key**, then click **OK**

##### Integrating Twitter Login With Auth Plugin

- Go to example>lib>main.dart and find **Twitter Login** function. Delete **\_showDialog** function.
- Obtain auth token and auth token secret from twitter authorization by official plugin or third party plugins.
- Create credential object by calling **credential** function from **TwitterAuthProvider** and pass auth token and auth token secret as parameters.
- Call signIn function and pass credential object as parameter.

```dart
String authToken = "Obtain from Twitter authorization";
String authTokenSecret = "Obtain from Twitter authorization";
AGCAuthCredential credential = TwitterAuthProvider.credentialWithToken(
            authToken, authTokenSecret);
AGCAuth.instance.signIn(credential);
```

#### Login With WeChat

For using login with WeChat functionality of example app. You will need:

- A WeChat Developer Account If you need one, click [here](https://open.weixin.qq.com/?lang=en) and create account.
- An App Id and App Secret for Your App. After an application created on WeChat platform, WeChat will provide an app id and app secret.

##### Enable WeChat authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on WeChat Authentication Mode.

**Step 5:** Enter WeChat **App Id** and **App Secret**, then click **OK**

##### Integrating WeChat Login With Auth Plugin

- Go to example>lib>main.dart and find **WeChat Login** function. Delete **\_showDialog** function.
- Obtain auth access token and open id from wechat authorization by official plugin or third party plugins.
- Create credential object by calling **credential** function from **WeixinAuthProvider** and pass access token and open id as parameters.
- Call signIn function and pass credential object as parameter.

```dart
String accessToken = "Obtain from WeChat authorization";
String openId = "Obtain from WeChat authorization";
AGCAuthCredential credential = WeiXinAuthProvider.credentialWithToken(
            accessToken, openId);
AGCAuth.instance.signIn(credential);
```

#### Login With QQ

For using login with QQ functionality of example app. You will need:

- A QQ for Developers Account If you need one, click [Tencent Open Platform](https://open.qq.com/eng/) and create account.
- An App ID and App key for Your App. After an application created on QQ platform, QQ will provide an App Id and App Key.

##### Enable QQ authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on QQ Authentication Mode.

**Step 5:** Enter QQ **App ID** and **App Key**, then click **OK**

##### Integrating QQ Login With Auth Plugin

- Go to example>lib>main.dart and find **QQ Login** function. Delete **\_showDialog** function and comments.
- Obtain auth access token and open id from QQ authorization by official plugin or third party plugins.
- Create credential object by calling **credential** function from **QQAuthProvider** and pass access token and open id as parameters.
- Call signIn function and pass credential object as parameter.

```dart
String accessToken = "Obtain from QQ authorization";
String openId = "Obtain from QQ authorization";
AGCAuthCredential credential = QQAuthProvider.credentialWithToken(
            accessToken, openId);
AGCAuth.instance.signIn(credential);
```

#### Login With Weibo

For using login with Weibo functionality of example app. You will need:

- A Weibo for Developers Account If you need one, click [Tencent Open Platform](https://open.qq.com/eng/) and create account.
- An App Key and App Secret for Your App. After an application created on Weibo platform, Weibo will provide an App Key and App Secret.

##### Enable Weibo authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Weibo Authentication Mode.

**Step 5:** Enter Weibo **App Key** and **App Secret**, then click **OK**

##### Integrating Weibo Login With Auth Plugin

- Go to example>lib>main.dart and find **WeiBo Login** function. Delete **\_showDialog** function and comments.
- Obtain auth token and uid from Weibo authorization by official plugin or third party plugins.
- Create credential object by calling **credential** function from **WeiboAuthProvider** and pass token and uid as parameters.
- Call signIn function and pass credential object as parameter.

```dart
String token = "Obtain from Weibo authorization";
String uid = "Obtain from Weibo authorization";
AGCAuthCredential credential = WeiBoAuthProvider.credentialWithToken(
            token, uid);
AGCAuth.instance.signIn(credential);
```

#### Login With Facebook

For using login with facebook functionality of example app. You will need:

- A Facebook for Developers Account If you need one, click [Create a Facebook for Developers Accont](https://developers.facebook.com)
- An App ID for Your App. [Get an App ID for your app](https://developers.facebook.com/apps/)

##### Enable Facebook authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Facebook Authentication Mode.

**Step 5:** Enter Facebook **App ID** and **App Secret**, then click **OK**

##### Android

Go to example>android>app>src>main>res>values>strings.xml file (If there is no strings.xml file, create it yourself) and enter your facebook app Id.

```xml
<resources>
    <string name="app_name">example</string>
    <string name="facebook_app_id">FACEBOOK_APP_ID</string>
</resources>
```

##### IOS

Go to example>ios>Runner>info.plist file. Enter your facebook app Id and display name.

```XML
<key>CFBundleURLTypes</key>
<array>
  <dict>
  <key>CFBundleURLSchemes</key>
  <array>
    <string>fb[FACEBOOK_APP_ID]</string>
  </array>
  </dict>
</array>
<key>FacebookAppID</key>
<string>[FACEBOOK_APP_ID]</string>
<key>FacebookDisplayName</key>
<string>[FACEBOOK_DISPLAY_NAME]</string>
```

##### Integrating Facebook Login With Auth Plugin

- Go to example>lib>main.dart and find **Facebook Login** function. Delete **\_showDialog** function and comments.
- Obtain auth token from Facebook authorization by official plugin or third party plugins.
- Create credential object by calling **credential** function from **FacebookAuthProvider** and pass token as parameters.
- Call signIn function and pass credential object as parameter.

```dart
String token = "Obtain from Facebook authorization";
 AGCAuthCredential credential =  FacebookAuthProvider.credentialWithToken(accessToken.token);
AGCAuth.instance.signIn(credential);
```

#### Login With Apple Account

> IOS Only

##### Enable Apple Account authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Apple Account Authentication Mode.

##### Integrating Apple Login With Auth Plugin

- Go to example>lib>main.dart and find **Apple Login** function. Delete **\_showDialog** function and comments.
- Obtain auth identity token and nonce Apple authorization by official plugin or third party plugins.
- Create credential object by calling **credential** function from **AppleIDAuthProvider** and pass identity token and nonce as parameters.
- Call signIn function and pass credential object as parameter.

```dart
/// Generates a cryptographically secure random nonce, to be included in a
/// credential request.
String generateNonce([int length = 32]) {
  final charset =
      '0123456789ABCDEFGHIJKLMNOPQRSTUVXYZabcdefghijklmnopqrstuvwxyz-._';
  final random = Random.secure();
  return List.generate(length, (_) => charset[random.nextInt(charset.length)])
      .join();
}

/// Returns the sha256 hash of [input] in hex notation.
String sha256ofString(String input) {
  final bytes = utf8.encode(input);
  final digest = sha256.convert(bytes);
  return digest.toString();
}
/// Generate nonce
///add a random character string to the authentication request and encrypt it using the SHA-256 algorithm

/// for AppGallery Connect to perform sign-in authentication
final rawNonce = generateNonce();
/// A String value used to associate a client session and the identity token. nonce required when logging Apple authorization by official plugin or third party plugins.
final nonce = sha256ofString(rawNonce);

String identityToken = "Obtain from Apple authorization";
AGCAuthCredential credential = AppleAuthProvider.credentialWithToken(
        credentialiOS.identityToken, rawNonce);
AGCAuth.instance.signIn(credential);
```

#### Login With Huawei Account

##### Configuring the Signing Certificate Fingerprint

**Step 1:** Open terminal, change **/path/to/my/keystore.jks** with your keystore file's absolute path and run the command below.

```bash
keytool -keystore /path/to/my/keystore.jks -list -v
```

**Step 2:** Obtain **SHA256** finger print from the result.

**Step 3:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 4:** Find your **app** project and click the app.

**Step 5:** Go to **Project Settings** > **General Information** > **App Information** and enter **SHA-256 certificate fingerprint**.

##### Enable Huawei Account authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** You can find your **App ID** and **App Secret** on **Project Settings** > **General Information** > **App Information**

**Step 4:** On left menu go to **build** > **Auth Service**

**Step 5:** Click **enable** on Huawei Account Authentication Mode.

**Step 6:** Enter Huawei **App ID** and **App Secret**, then click **OK**

#### Login With Huawei Game Account

##### Configuring the Signing Certificate Fingerprint

**Step 1:** Open terminal, change **/path/to/my/keystore.jks** with your keystore file's absolute path and run the command below.

```bash
keytool -keystore /path/to/my/keystore.jks -list -v
```

**Step 2:** Obtain **SHA256** finger print from the result.

**Step 3:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 4:** Find your **app** project and click the app.

**Step 5:** Go to **Project Settings** > **General Information** > **App Information** and enter **SHA-256 certificate fingerprint**.

##### Enable Huawei Account authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** You can find your **App ID** and **App Secret** on **Project Settings** > **General Information** > **App Information**

**Step 4:** On left menu go to **build** > **Auth Service**

**Step 5:** Go to Build > Game Service and query the game public key and private key.

**Step 6:** Click **enable** on Huawei Game Service Authentication Mode.

**Step 7:** Enter Huawei **game public key** and **game private key**, then click **OK**

#### Login With Google

For using Log in with Google functionality you will need a **Firebase** app. Log in to [Firebase Console](https://console.firebase.google.com/) and create an app.

##### Enable Google authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Google Authentication Mode.

**Step 5:** Set **Client ID** and **Client Secret** to the values of Client ID and Client secret under Credentials > OAuth 2.0 client IDs > Web client (Auto-created for Google Sign-in) page at [Google API Console](https://console.developers.google.com/apis/credentials).

#### Login With Google Game

For using Log in with Google functionality you will need a **Firebase** app. Log in to [Firebase Console](https://console.firebase.google.com/) and create an app.

##### Enable Google authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Google Play Games Authentication Mode.

**Step 5:** Set **Client ID** and **Client Secret** to the values of Client ID and Client secret under Credentials > OAuth 2.0 client IDs > Web client (Auto-created for Google Sign-in) page at [Google API Console](https://console.developers.google.com/apis/credentials).

#### Integrating Self Build Login

If you have built an authentication system by yourself, you can connect this system to Auth Service to allow your users to use accounts provided by your authentication system for sign-in.

##### Enable Self Build authentication mode from AppGallery Connect

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 2:** Find your **app** project and click the app.

**Step 3:** On left menu go to **build** > **Auth Service**

**Step 4:** Click **enable** on Your own account system Authentication Mode.

**Step 5:** Set **Signature public key** to the values of interconnect with your account system to provide complete data security management for your users.

```dart
  _signIn() async {
    bool result = await _showSelfBuildDialog(VerifyCodeAction.registerLogin);
    if (result == null) {
      print("cancel");
      return;
    }
    String token = _selfBuildController.text;
    AGCAuthCredential credential =
        SelfBuildAuthProvider.credentialWithToken(token);
    AGCAuth.instance.signIn(credential).then((value) {
      setState(() {
        _log = 'signIn = ${value.user.uid} , ${value.user.providerId}';
      });
    });
  }
```

## 4. Licensing and Terms

AppGallery Connect Auth Service Flutter Plugin Demo is licensed under [Apache 2.0 license](../LICENSE)
