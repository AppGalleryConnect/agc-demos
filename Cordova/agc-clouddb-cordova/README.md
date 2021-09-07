# AGC CloudDB Cordova Demo

## Contents

- [1. Introduction](#1-introduction)
- [2. Installation Guide](#2-installation-guide)
  - [2.1. Creating a Project in AppGallery Connect](#21-creating-a-project-in-appgallery-connect)
  - [2.2. Importing and Exporting Object Types](#22-importing-and-exporting-object-types)
  - [2.3. Obtaining agconnect-services.json and agconnect-services.plist](#23-obtaining-agconnect-servicesjson-and-agconnect-servicesplist)
  - [2.4. Cordova](#24-cordova)
    - [2.4.1. iOS App Development](#241-ios-app-development)
    - [2.4.2. Android App Development](#242-android-app-development)
- [3. Configuration and Description](#3-configuration-and-description)
- [4. Development Environment](#4-development-environment)
- [5. FAQs](#5-faqs)
- [6. Licencing and Terms](#6-licencing-and-terms)

---

## 1. Introduction

This demo application demonstrates the usage of AGC CloudDB Cordova plugin.

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

### 2.2. Importing and Exporting Object Types
Click Cloud DB on the navigation bar and enable database service. Then, perform the following operations:

1. Create a schema by importing a template file stored in **CloudDBQuickStart_1.json** in the root directory of the project. Alternatively, create a schema named **BookInfo** and ensure that all fields must be the same as those in **CloudDBQuickStart_1.json**.

2. Create a Cloud DB zone. On the Cloud DB Zone tab page, click Add to create a Cloud DB zone named **QuickStartDemo**.

3. Export Object type with Java and Objective-C file format.

### 2.3. Obtaining agconnect-services.json and agconnect-services.plist

1. Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html?ha_source=hms1) and select your project from **My Projects**. Then go to **Project settings** > **General information**. In the **App information** field,

   - If platform is Android, click **agconnect-services.json** button to download the configuration file.
   - If platform is iOS, click **agconnect-services.plist** button to download the configuration file.

### 2.4. Cordova

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

5. Install `AGC CloudDB Cordova Plugin` to the project.

   - Run the following command in the root directory of your project to install it through **npm**.

     ```bash
     cordova plugin add @cordova-plugin-agconnect/clouddb
     ```

6. Install `AGC Auth Cordova Plugin` to the project.

   - Run the following command in the root directory of your project to install it through **npm**.

     ```bash
     cordova plugin add @cordova-plugin-agconnect/auth
     ```

#### 2.4.1. iOS App Development

1. Add **`agconnect-services.plist`** file to the app's root directory of your Xcode project.

2. Adding Object Type Files
- During application development, you can directly add the **Objective-C** files exported from the AppGallery Connect console to the local development environment.

<img src="img\xcode_add_files.png" width="400" alt="ios">
<img src="img\xcode_add_to_targets.png" width="330" alt="ios">
<img src="img\xcode_files.png" width="200" alt="ios">

3. Check Signing & Capabilities tab page of your Xcode project.

4. Run the application.

   ```bash
   cordova run ios --device
   ```

#### 2.4.2. Android App Development

1. Copy **`agconnect-services.json`** file to **`<project_root>/platforms/android/app`** directory your Android project.

2. Copying Object Type Files
- During application development, you can directly add the **Java** files exported from the AppGallery Connect console to the local development environment with **com.development.clouddb** package name.

<img src="img\android_export_java_files.png" width="600" alt="android">

- Copy exported Java files to **`<project_root>/platforms/android/app/src/main/java/com/development/clouddb`** directory your Android project.

3. Run the application.

   ```bash
   cordova run android --device
   ```

---

## 3. Configuration and Description
No.

---

## 4. Development Environment
You are advised to use the plug-ins in an environment that meets the following requirements.

| Software        | Allowed Version Range | Description  |
| --------------- | --------------------- | ------------ |
| cordova         | 9.0.0 or later        | Platform     |
| cordova-android | >=8.1.0 or later      | Platform     |
| cordova-ios     | 5.0.0 or later        | Platform     |
| npm             | 6.4.1 or later        | Tool         |

---
 
## 5. FAQs

Why is the message 'google/protobuf/any.h' file not found file not found displayed when I run pod install?

Protobuf-C++ causes the problem.

You can fix this problem by following the steps below.

1. Delete `<project_root>/platforms/ios/Podfile.lock` file.
2. Delete `use_frameworks!` line in `<project_root>/platforms/ios/Podfile` file.
3. cd `<project_root>/platforms/ios` directory and run this command `pod install`

## 6. Licencing and Terms

AGC Cloud DB Cordova Plugin is licensed under the [Apache 2.0 license](LICENCE).
