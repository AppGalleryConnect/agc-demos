# AGC App Messaging Cordova Demo

## Contents

- [1. Introduction](#1-introduction)
- [2. Installation Guide](#2-installation-guide)
  - [2.1. Creating a Project in AppGallery Connect](#21-creating-a-project-in-appgallery-connect)
  - [2.2. Obtaining agconnect-services.json and agconnect-services.plist](#22-obtaining-agconnect-servicesjson-and-agconnect-servicesplist)
  - [2.3. Cordova](#23-cordova)
    - [2.3.1. iOS App Development](#231-ios-app-development)
    - [2.3.2. Android App Development](#232-android-app-development)
- [3. Configuration and Description](#3-configuration-and-description)
- [4. Licencing and Terms](#5-licencing-and-terms)

---

## 1. Introduction

This demo application demonstrates the usage of AGC App Messaging Cordova plugin.

---

## 2. Installation Guide

Before you get started, you must register as a HUAWEI Developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/10104).

### 2.1. Creating a Project in AppGallery Connect

Creating an app in AppGallery Connect is required in order to communicate with the Huawei services. To create an app, perform the following steps:

1. Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select **My projects**.
2. Select your project from the project list or create a new one by clicking the **Add Project** button.
3. Go to **Project settings** > **General information**, and click **Add app**.
   If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.
4. On the **Add app** page, enter the app information, and click **OK**.

### 2.2. Obtaining agconnect-services.json and agconnect-services.plist

1. Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select your project from **My Projects**. Then go to **Project settings** > **General information**. In the **App information** field,

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
   cordova platform add android
   ```

   ```bash
   cordova platform add ios
   ```

5. Install `AGC App Messaging` to the project.

   - Run the following command in the root directory of your project to install it through **npm**.

       ```bash
       cordova plugin add @cordova-plugin-agconnect/appmessaging
       ```

#### 2.3.1. iOS App Development

1. Add **`agconnect-services.plist`** file to the app's root directory of your Xcode project.

2. Check Signing & Capabilities tab page of your Xcode project.

3.  Run the application.

    ```bash
    cordova run ios --device
    ```

#### 2.3.2. Android App Development

1. Copy **`agconnect-services.json`** file to **`<project_root>/platforms/android/app`** directory your Android project.

2. Run the application.

   ```bash
   cordova run android --device
   ```
   
NOTICE:

App Messaging Cordova Plugin currently doesn't support changing orientation. Only a single landscape or portrait layout can be used.

---

## 3. Configuration and Description

### Accessing Analytics Kit

Currently, HMS Analytics Cordova Plugin is not supported with AGC AppMessaging Plugin. 

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
        # Pods for AGCAppMessagingDemo
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

## 4. Licencing and Terms

AGC App Messaging Cordova Plugin is licensed under the [Apache 2.0 license](LICENCE).
