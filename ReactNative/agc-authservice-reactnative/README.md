# React-Native AGC Auth - Demo

---

## Contents

- [Introduction](#1-introduction)
- [Installation](#2-installation)
- [Configuration](#3-configuration)
- [Licensing and Terms](#4-licensing-and-terms)

---

## 1. Introduction

This demo project is an example to demonstrate the features of the **Huawei React-Native Auth Kit** Plugin.

---

## 2. Installation

Before you get started, you must register as a HUAWEI developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/10104).

### Creating a Project in AppGallery Connect

Creating an app in AppGallery Connect is required in order to communicate with the Huawei services. To create an app, perform the following steps:

**Step 1.** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html)  and select **My projects**.

**Step 2.** Select your project from the project list or create a new one by clicking the **Add Project** button.

**Step 3.** Go to **Project Setting** > **General information**, and click **Add app**.
If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.

**Step 4.** On the **Add app** page, enter the app information, and click **OK**.

### Enabling Auth Service

**Step 1.** In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), find your project, and click the app for which you want to use Auth Service.

**Step 2.** Select any menu under **Auth Service** and click **Enable Auth Services**.

### Integrating the React-Native Auth Services Plugin

Before using **@react-native-agconnect/auth**, ensure that the ReactNative development environment has been installed.

#### Install via NPM

```
npm i @react-native-agconnect/auth
```

#### Android App Development

#### Integrating the React-Native AGConnect Auth Services into the Android Studio

**Step 1:** Set an unique **Application ID** on the app level build gradle file located on **example/android/app/build.gradle**. You should also change the **package names** for the manifest files in the **/example/android/app/src/** directory to match with the Application ID. 
  ```gradle
  <!-- Other configurations ... -->
    defaultConfig {
      // Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html). You may need to change the package name on AndroidManifest.xml and MainActivity.java respectively.
      // The Application ID here should match with the Package Name on the AppGalleryConnect
      applicationId "<Enter_Your_Package_Here>" // For ex: "com.example.auth"
      <!-- Other configurations ... -->
  }
  ```

**Step 2:** Copy the **agconnect-services.json** file to the app's root directory of your project.
    
**Package name must match with the _package_name_ entry in _agconnect-services.json_ file.**

**Step 3:** Configure the signature file.

```gradle
 signingConfigs {
        config {
            storeFile file('<keystore_file>')
            keyAlias '<key_alias>'
            keyPassword '<key_password>'
            storePassword '<keystore_password>'
        }
    }
```

---

#### iOS App Development

#### Integrating the React-Native AGC Auth Services into the Xcode Project

- Add the AppGallery Connect configuration file of the app to your Xcode project.

    **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.
    
    **Step 2:** Find your **app** project and click the app.
    
    **Step 3:** Go to **Project Setting > App information**. In the **App information** area, download the **agconnect-services.plist** file.
    
    **Step 4:** Copy the **agconnect-services.plist** file to the app's root directory of your Xcode project.
    
    Before obtaining the **agconnect-services.plist** file, ensure that you have enabled Auth Services. For details, please refer to [Enabling HUAWEI Auth Services](#enabling-cloud-functions-service).
    
    If you have made any changes on the Project Setting page, such as setting the data storage location and enabling or managing APIs, you need to download the latest **agconnect-services.plist** file and replace the existing file in the app's root directory.

- Navigate into example/ios and run 
    
    ```
    $ cd ios && pod install
    ```
    
- Initialize the AGC Auth Services SDK.

    After the **agconnect-services.plist** file is imported successfully, add the **agconnect-services.plist** file under example/ios folder. 

### Build & Run the project

#### Android

Run the following command to start the demo app.
```
[project_path]> npm run android
```

#### iOS

Run the following command to start the demo app.
```
[project_path]> npm run ios
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

 - Go to example>scr>screens>LoginScreen.js and find **loginWithTwitter** function. Delete **showUnimplementedAlert** function and comments.
 - Obtain auth token and auth token secret from twitter authorization by official plugin or third party plugins.
 - Create credential object by calling **credential** function from **TwitterAuthProvider** and pass auth token and auth token secret as parameters.
 - Call signIn function and pass credential object ass parameter.

```js
let authToken = "Obtain from Twitter authorization";
let authTokenSecret = "Obtain from Twitter authorization";
let credential = TwitterAuthProvider.credential(authToken, authTokenSecret)
signIn(credential)
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

 - Go to example>scr>screens>LoginScreen.js and find **loginWithWeChat** function. Delete **showUnimplementedAlert** function and comments.
 - Obtain auth access token and open id from wechat authorization by official plugin or third party plugins.
 - Create credential object by calling **credential** function from **WeixinAuthProvider** and pass access token and open id as parameters.
 - Call signIn function and pass credential object ass parameter.

```js
let accessToken = "Obtain from WeChat authorization";
let openId = "Obtain from WeChat authorization";
let credential = WeixinAuthProvider.credential(accessToken, openId)
signIn(credential)
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

 - Go to example>scr>screens>LoginScreen.js and find **loginWithQQ** function. Delete **showUnimplementedAlert** function and comments.
 - Obtain auth access token and open id from QQ authorization by official plugin or third party plugins.
 - Create credential object by calling **credential** function from **QQAuthProvider** and pass access token and open id as parameters.
 - Call signIn function and pass credential object ass parameter.

```js
let accessToken = "Obtain from QQ authorization";
let openId = "Obtain from QQ authorization";
let credential = QQAuthProvider.credential(accessToken, openId);
signIn(credential);
```

#### Login With Weibo 

For using login with Weibo functionality of example app. You will need: 
 - A Weibo for Developers Account If you need one, click [Tencent Open Platform](https://open.qq.com/eng/) and create account.
 - An App Key and App Secret for Your App. After an application created on Weibo platform, Weibo will provide an App Key and App Secret.

 ##### Enable QQ authentication mode from AppGallery Connect

  **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

  **Step 2:** Find your **app** project and click the app.

  **Step 3:** On left menu go to **build** > **Auth Service**

  **Step 4:** Click **enable** on Weibo Authentication Mode.

  **Step 5:** Enter Weibo **App Key** and **App Secret**, then click **OK**

##### Integrating Weibo Login With Auth Plugin

 - Go to example>scr>screens>LoginScreen.js and find **loginWithWeibo** function. Delete **showUnimplementedAlert** function and comments.
 - Obtain auth token and uid from Weibo authorization by official plugin or third party plugins.
 - Create credential object by calling **credential** function from **WeiboAuthProvider** and pass token and uid as parameters.
 - Call signIn function and pass credential object ass parameter.

```js
let token = "Obtain from Weibo authorization";
let uid = "Obtain from Weibo authorization";
let credential = WeiboAuthProvider.credential(token, uid);
signIn(credential);
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

Go to example>android>app>src>main>res>values>strings.xml file and enter your facebook app Id.

```xml
<resources>
    <string name="app_name">example</string>
    <string name="facebook_app_id">FACEBOOK_APP_ID</string>
</resources>
```

##### IOS

Go to example>ios>example>info.plist file. Enter your facebook app Id and display name.

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

#### Login With Apple Account

> IOS Only

##### Enable Apple Account authentication mode from AppGallery Connect

  **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

  **Step 2:** Find your **app** project and click the app.

  **Step 3:** On left menu go to **build** > **Auth Service**

  **Step 4:** Click **enable** on Apple Account Authentication Mode.

##### Integrating Apple Login With Auth Plugin

 - Go to example>scr>screens>LoginScreen.js and find **loginWithApple** function. Delete **showUnimplementedAlert** function and comments.
 - Obtain auth identity token and nonce Google authorization by official plugin or third party plugins.
 - Create credential object by calling **credential** function from **AppleIDAuthProvider** and pass identity token and nonce as parameters.
 - Call signIn function and pass credential object ass parameter.

```js
let identityToken = "Obtain from Apple authorization";
let nonce = "Obtain from Apple authorization";
let credential = AppleIDAuthProvider.credential(identityToken, nonce)
signIn(credential);
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

#### Login With Google

For using Log in with Google functionality you will need a **Firebase** app. Log in to [Firebase Console](https://console.firebase.google.com/) and create an app.

##### Enable Google authentication mode from AppGallery Connect

  **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

  **Step 2:** Find your **app** project and click the app.

  **Step 3:** On left menu go to **build** > **Auth Service**

  **Step 4:** Click **enable** on Google Authentication Mode.

  **Step 5:** Set **Client ID** and **Client Secret** to the values of Client ID and Client secret under Credentials > OAuth 2.0 client IDs > Web client (Auto-created for Google Sign-in) page at [Google API Console](https://console.developers.google.com/apis/credentials).

##### Integrating Google Login With Auth Plugin

 - Go to example>scr>screens>LoginScreen.js and find **signInWithGoogle** function. Delete **showUnimplementedAlert** function and comments.
 - Obtain auth id token Google authorization by official plugin or third party plugins.
 - Create credential object by calling **credential** function from **GoogleAuthProvider** and pass id token as parameter.
 - Call signIn function and pass credential object ass parameter.

```js
let idToken = "Obtain from Google authorization";
let credential = GoogleAuthProvider.credential(idToken);
signIn(credential);
```

---

## 4. Licensing and Terms

AGC React-Native AGC Auth - Demo is licensed under [Apache 2.0 license](LICENCE)
