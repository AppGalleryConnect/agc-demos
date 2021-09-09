# AGC RemoteConfig Cordova Demo

## Contents

- [1. Introduction](#1-introduction)
- [2. Installation Guide](#2-installation-guide)
  - [2.1. Creating a Project in AppGallery Connect](#21-creating-a-project-in-appgallery-connect)
  - [2.2. Obtaining agconnect-services.json and agconnect-services.plist](#22-obtaining-agconnect-servicesjson-and-agconnect-servicesplist)
  - [2.3. Enabling RemoteConfig Service](#24-enabling-RemoteConfig-service)
  - [2.4. Cordova](#26-cordova)
    - [2.4.1. iOS App Development](#261-ios-app-development)
    - [2.4.2. Android App Development](#262-android-app-development)
- [3. Method Definitions](#3-method-definitions)
- [4. Configuration and Description](#4-configuration-and-description)
- [5. Licensing and Terms](#5-licensing-and-terms)

---

## 1. Introduction

This demo application demonstrates the usage of AGC Cloud Functions Cordova plugin.

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

### 2.3. Enabling RemoteConfig Service

1. In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), find your project, and click the app for which you want to use cloud functions.

2. Select **Grow** and **Remote Config** on the left menu and click **Enable now**.

### 2.4. Cordova

1. Install Cordova CLI.

   ```bash
   npm install -g cordova
   ```

2. Create a new Cordova project or use existing Cordova project.

   - To create new Cordova project, you can use **`cordova create path [id [name [config]]] [options]`** command. For more details please follow [CLI Reference - Apache Cordova](https://cordova.apache.org/docs/en/latest/reference/cordova-cli/index.html#cordova-create-command).

3. Update the widget **`id`** property which is specified in the **`config.xml`** file. It must be same with **client > package_name** value of the **`agconnect-services.json`** and **`agconnect-services.plist`** files.

4. Update the '<set_triggerIdentifier>' parameter which is specified in the **`<project_root>/www/js/index.js`** file. 

5. Check preference in your Cordova project config.xml.

   ```xml
   <!--platform iOS-->
    <preference name="deployment-target" value="11.0" />
    <preference name="SwiftVersion" value="5" />
   ```

6. Add the **Android** or **iOS** platform to the project if haven't done before.

   ```bash
   cordova platform add android
   ```

   ```bash
   cordova platform add ios
   ```

7. Install `AGC RemoteConfig Cordova Plugin` to the project.

- Run the following command in the root directory of your project to install it through **npm**.

```bash
cordova plugin add @cordova-plugin-agconnect/remoteconfig
```

#### 2.6.1. iOS App Development

1. Add **`agconnect-services.plist`** file to the app's root directory of your Xcode project.

2. Run the application.

```bash
cordova run ios --device
```

#### 2.6.2. Android App Development

1. Copy **`agconnect-services.json`** file to **`<project_root>/platforms/android/app`** directory your Android project.

2. Run the application.

   ```bash
   cordova run android --device
   ```

---

## 3. Method Definitions

#### Public Method Summary

| Method           | Return Type | Description                                                  |
| ---------------- | ----------- | ------------------------------------------------------------ |
| applyDefault     | void        | Setting Local Default Parameters                             |
| applyLastFetched | void        | Make the latest configuration obtained from the cloud take effect. |
| fetch            | void        | Obtain the latest configuration data from the cloud.         |
| getValue         | void        | Returns the value of the string type corresponding to the key |
| getSource        | void        | Returns the data source corresponding to the key             |
| getMergedAll     | void        | Returns all values after the default value and cloud value are combined |
| clearAll         | void        | Clears all cached data, including the data pulled from the cloud and the transferred default values |
| setDeveloperMode | void        | Set the developer mode.(This API is applicable only to the Android platform) |

---

## 4. Configuration and Description

No.

---

## 6. Licensing and Terms

AGC Cloud Functions Cordova Plugin is licensed under the [Apache 2.0 license](LICENCE).
