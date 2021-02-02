# AGC AppLinking Cordova Demo

---

## Contents

- [AGC AppLinking Cordova Demo](#agc-applinking-cordova-demo)
  - [Contents](#contents)
  - [1. Introduction](#1-introduction)
  - [2. Installation Guide](#2-installation-guide)
    - [2.1. Creating a Project in AppGallery Connect](#21-creating-a-project-in-appgallery-connect)
    - [2.2. Obtaining agconnect-services.json and agconnect-services.plist](#22-obtaining-agconnect-servicesjson-and-agconnect-servicesplist)
    - [2.3. Cordova](#23-cordova)
      - [2.3.1. iOS App Development](#231-ios-app-development)
      - [2.3.2. Android App Development](#232-android-app-development)
  - [3. Method Definitions](#3-method-definitions)
    - [Public Method Summary](#public-method-summary)
  - [4. Configuration and Description](#4-configuration-and-description)
    - [Enabling the App Linking Service](#enabling-the-app-linking-service)
    - [Create App Linking URL Prefix](#create-app-linking-url-prefix)
    - [Accessing Analytics Kit](#accessing-analytics-kit)
  - [5. Licencing and Terms](#5-licencing-and-terms)

---

## 1. Introduction

This demo application demonstrates the usage of AGC AppLinking Cordova plugin.

---

## 2. Installation Guide

Before you get started, you must register as a HUAWEI Developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/10104).

### 2.1. Creating a Project in AppGallery Connect

Creating an app in AppGallery Connect is required in order to communicate with the Huawei services. To create an app, perform the following steps:

1. Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select **My projects**.
2. Select your project from the project list or create a new one by clicking the **Add Project** button.
3. Go to **Project settings** > **General information**, and click **Add app**.
   - If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.
4. On the **Add app** page, select platform Android or iOS and then enter the app information, and click **OK**.
5. To use HUAWEI App Linking, you need to enable the App Linking service first and also [create App Linking URL Prefix](#create-app-linking-url-prefix). For more information, please refer to [Enabling the App Linking Service](#enabling-the-app-linking-service).

### 2.2. Obtaining agconnect-services.json and agconnect-services.plist

1. Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select your project from **My Projects**. Then go to **Project settings** > **General information**. In the **App information** field,
   - If platform is Android, click **agconnect-services.json** button to download the configuration file.
   - If platform is iOS, click **agconnect-services.plist** button to download the configuration file.

### 2.3. Cordova

1. Install Cordova CLI.

   ```bash
   npm install -g cordova
   ```

2. Create a new Cordova project or use existing Cordova project.

   - To create new Cordova project, you can use **`cordova create path [id [name [config]]] [options]`** command. For more details please follow [CLI Reference - Apache Cordova](https://cordova.apache.org/docs/en/latest/reference/cordova-cli/index.html#cordova-create-command).

3. Update the widget **`id`** property which is specified in the **`config.xml`** file. It must be same with **client > package_name** value of the **`agconnect-services.json`** and **`agconnect-services.plist`** files.

4. Add the **Android** or **iOS** platform to the project if haven't done before.

   ```bash
   cordova platform add android
   ```

   ```bash
   cordova platform add ios
   ```

5. Install `AGC AppLinking plugin` to the project.

   - Run the following command in the root directory of your project to install it through **npm**.

   ```bash
   cordova plugin add @cordova-plugin-agconnect/applinking
   ```

#### 2.3.1. iOS App Development

1. Copy **`agconnect-services.plist`** file to the app's root directory of your Xcode project.

2. In an iOS app, when a user taps a link of App Linking, the link can be redirected in universal link or custom scheme mode.

  - Configuring a **Universal Link**: A user can be seamlessly redirected to the in-app content after tapping a universal link in iOS 9 and later versions. You only need to complete the following simple configuration to implement link redirection in iOS apps in universal link mode
    1. Generate an asset verification file of the domain. Sign in to AppGallery Connect, select your app, go to **Project settings > General information > App information**, and Team ID to set a team ID in the App Store. After the configuration is complete, AppGallery Connect will generate an asset verification file for the default domain.
    - After the asset verification file is generated, you can view it at https://**your_applinking_url_prefix**/apple-app-site-association. **your_applinking_url_prefix** is the domain name you applied for in AppGallery Connect.
  
    2. Declare the domain name associated with your iOS app. On the Signing & Capabilities tab page of your Xcode project, enable the Associated Domains function, and add a domain name to be supported, in **applinks:your_applinking_url_prefix** format. **your_applinking_url_prefix** is the domain name you applied for in AppGallery Connect.

  - Configuring a **Custom Scheme**: If you want a link to be opened in custom scheme mode in your iOS app, you need to set a custom scheme in the iOS deep link and set URL Schemes under URL Types on the Info tab page of your Xcode project.

3. Run the application.

   ```bash
   cordova run ios --device
   ```

#### 2.3.2. Android App Development

1. Copy **`agconnect-services.json`** file to **`<project_root>/platforms/android/app`** directory your Android project.

2. To use deep links to receive data, you need to add the following configuration to the activity for processing links. Set **android:host** to the domain name in the **deepLink** and **android:scheme** to the custom scheme. When a user taps a link containing this deep link, your app uses this activity to process the link.

   ```xml
   <!-- AndroidManifest.xml. /manifest/application/activity-->
   <intent-filter>
       <action android:name="android.intent.action.VIEW" />
       <category android:name="android.intent.category.DEFAULT" />
       <category android:name="android.intent.category.BROWSABLE" />
       <!-- Add the custom domain name and scheme -->
       <data android:host="<DeepLink_Host>" android:scheme="https" />
   </intent-filter>
   ```

3. Set Android launch mode in your Cordova project config.xml

   ```xml
    <preference name="AndroidLaunchMode" value="standard" />
   ```

4. Run the application.

   ```bash
   cordova run android --device
   ```

---

## 3. Method Definitions

### Public Method Summary

| Method Name                              | Defination                                                                                                                                                                                                                               |
| ---------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| buildShortLink(args: AppLinkingWithInfo) | This method is called to obtain a short link of App Linking with info in asynchronous mode.                                                                                                                                              |
| buildLongLink(args: AppLinkingWithInfo)  | This method is called to obtain a long link of App Linking with info in asynchronous mode.                                                                                                                                               |
| addListener()                            | This method is called to obtain the ResolvedLinkDataResult from the asynchronous execution result, and parse the data, for example, deep links, passed through links of App Linking.  |
| copyShortLink()                          | This method is called to copy shortLink of AppLinking to clipboard.                                                                                                                                                                      |
| copyTestUrl()                            | This method is called to copy testURL of AppLinking to clipboard.                                                                                                                                                                        |
| copyLongLink()                           | This method is called to copy longLink of AppLinking to clipboard.                                                                                                                                                                       |

---

## 4. Configuration and Description

### Enabling the App Linking Service

1. Sign in to AppGallery Connect and click My projects.
2. Find your project from the project list and click the app for which you need to enable App Linking on the project card.
3. Go to Growing > App Linking. On the App Linking page that is displayed, click Enable now.

### Create App Linking URL Prefix

- A URL prefix is a URL contained in a link. To uniquely identify the URL prefix, you need to prefix the free domain name (for example, drcn.agconnect.link) provided by AppGallery Connect with a string

1. Sign in to AppGallery Connect and click My projects.
2. Find your project from the project list and click the app for which you need to create a link of App Linking on the project card.
3. Go to Growing > App Linking and click the URL prefix tab.
4. Click Add URL prefix and set a unique URL prefix, which can contain only lowercase letters and digits.
5. Click Next. A message is displayed, indicating that the URL has been verified.

### Accessing Analytics Kit

To use analytics feature, 

- Navigate into your <root_directory>/platforms/android/app/build.gradle and add build dependencies in the dependencies section.
   
    ```
    dependencies {
        implementation 'com.huawei.hms:hianalytics:5.1.0.301'
    }
    ```
- Navigate into your <root_directory>/platforms/ios file and edit the Podfile file to add the pod dependency 'HiAnalytics'
    
    - Example Podfile file:

        ```
        # Pods for AGCAppLinkingDemo
        pod 'HiAnalytics'
        ```
    
    - Run pod install to install the pods.
    
       ```
       $ pod install
       ```
    
    - Initialize the Analytics SDK using the config API in AppDelegate in iOS platform.

        Sample code for initialization in AppDelegate.m:
    
        ```
        #import "AppDelegate.h"
        #import <HiAnalytics/HiAnalytics.h>
 
        @implementation AppDelegate
        ...
        // Customize the service logic after app launch.
        - (BOOL)application:(UIApplication*)application didFinishLaunchingWithOptions:(NSDictionary*)launchOptions {
          self.viewController = [[MainViewController alloc] init];
        // Initialize the Analytics SDK.
        [HiAnalytics config];   
         return [super application:application didFinishLaunchingWithOptions:launchOptions];
        }
        ...
        @end
        ```
    
    For further information please refer to [Analytics Kit Service Guide](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050745149).

---

## 5. Licencing and Terms

AGC AppLinking Kit Cordova Plugin is licensed under the [Apache 2.0 license](LICENCE).
