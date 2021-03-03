# AGC Cloud Functions Cordova Demo

## Contents

- [1. Introduction](#1-introduction)
- [2. Installation Guide](#2-installation-guide)
  - [2.1. Creating a Project in AppGallery Connect](#21-creating-a-project-in-appgallery-connect)
  - [2.2. Obtaining agconnect-services.json and agconnect-services.plist](#22-obtaining-agconnect-servicesjson-and-agconnect-servicesplist)
  - [2.3. Applying for the Cloud Functions Service](#23-applying-for-the-cloud-functions-service)
  - [2.4. Enabling Cloud Functions Service](#24-enabling-cloud-functions-service)
  - [2.5. Creating a Cloud Function](#25-creating-a-cloud-function)
  - [2.6. Cordova](#26-cordova)
    - [2.6.1. iOS App Development](#261-ios-app-development)
    - [2.6.2. Android App Development](#262-android-app-development)
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

### 2.3. Applying for the Cloud Functions Service

Cloud Functions is still in Beta state. To use Cloud Functions, send an application email to agconnect@huawei.com to apply for the service.

Set your email title in the following format: [Cloud Functions]-[Company name]-[Developer account ID]-[Project ID]. For details about how to query the developer account ID and project ID, please refer to [Querying the Developer Account ID and Project ID](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-query-ID).

Huawei operation personnel will reply within 1 to 3 working days.

> This email address is used only to process AppGallery Connect service provisioning applications. Do not send other consultations to this email address.

### 2.4. Enabling Cloud Functions Service

1. In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), find your project, and click the app for which you want to use cloud functions.

2. Select **Build** and **Cloud functions** on the left menu and click **Enable Cloud Functions service**.

### 2.5. Creating a Cloud Function

To create a cloud function, please refer [Create a Function](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-getstarted#h1-1592364963757).

**Creating a Trigger:** To call a function in an app, you must create an HTTP trigger. For details, please refer to [Create an HTTP Trigger](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-triggercall#h2-1603096955823). When calling a function in an app, you must transfer the identifier of an HTTP trigger. For details, please refer to [Querying the Trigger Identifier](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-appcall#h1-1578361186845)

### 2.6. Cordova

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

7. Install `AGC Cloud Functions Cordova Plugin` to the project.

- Run the following command in the root directory of your project to install it through **npm**.

```bash
cordova plugin add @cordova-plugin-agconnect/cloudfunctions
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

| Method          | Return Type | Description                                |
| --------------- | ----------- | ------------------------------------------ |
| call()          | `void`      | Calls a function without input parameters. |
| callWithParam() | `void`      | Calls a function with input parameters.    |

---

## 4. Configuration and Description

No.

---

## 6. Licensing and Terms

AGC Cloud Functions Cordova Plugin is licensed under the [Apache 2.0 license](LICENCE).
