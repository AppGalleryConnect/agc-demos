# React-Native AGC Cloudfunctions - Demo

---

## Contents

- [Introduction](#1-introduction)
- [Installation](#2-installation)
- [Configuration](#3-configuration)
- [Licensing and Terms](#4-licensing-and-terms)

---

## 1. Introduction

This demo project is an example to demonstrate the features of the **Huawei React-Native Cloudfunctions Kit** Plugin.

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

### Applying for the Cloud Functions Service

Cloud Functions is still in Beta state. To use Cloud Functions, send an application email to agconnect@huawei.com to apply for the service.

Set your email title in the following format: [Cloud Functions]-[Company name]-[Developer account ID]-[Project ID]. For details about how to query the developer account ID and project ID, please refer to [Querying the Developer Account ID and Project ID](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-query-ID).

Huawei operation personnel will reply within 1 to 3 working days.

> This email address is used only to process AppGallery Connect service provisioning applications. Do not send other consultations to this email address.

### Enabling Cloud Functions Service

**Step 1.** In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), find your project, and click the app for which you want to use cloud functions.

**Step 2.** Select any menu under **Cloud functions** and click **Enable Cloud Functions service**.

### Creating a Cloud Function

To create a cloud function, please refer [Create a Function](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-getstarted#h1-1592364963757).

### Creating a Trigger

To call a function in an app, you must create an HTTP trigger. For details, please refer to [Create an HTTP Trigger](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-triggercall#h2-1603096955823). When calling a function in an app, you must transfer the identifier of an HTTP trigger. For details, please refer to [Querying the Trigger Identifier](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-appcall#h1-1578361186845)


### Integrating the React-Native Cloud Functions Plugin

Before using **@react-native-agconnect/cloudfunctions**, ensure that the ReactNative development environment has been installed.

#### Install via NPM

```
npm i @react-native-agconnect/cloudfunctions
```

#### Android App Development

#### Integrating the React-Native AGConnect Cloud Functions into the Android Studio

**Step 1:** Set an unique **Application ID** on the app level build gradle file located on **example/android/app/build.gradle**. You should also change the **package names** for the manifest files in the **/example/android/app/src/** directory to match with the Application ID. 
  ```gradle
  <!-- Other configurations ... -->
    defaultConfig {
      // Specify your own unique Application ID (https://developer.android.com/studio/build/application-id.html). You may need to change the package name on AndroidManifest.xml and MainActivity.java respectively.
      // The Application ID here should match with the Package Name on the AppGalleryConnect
      applicationId "<Enter_Your_Package_Here>" // For ex: "com.example.cloudfunctions"
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

#### Integrating the React-Native AGC Cloud Functions into the Xcode Project

- Add the AppGallery Connect configuration file of the app to your Xcode project.

    **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.
    
    **Step 2:** Find your **app** project and click the app.
    
    **Step 3:** Go to **Project Setting > App information**. In the **App information** area, download the **agconnect-services.plist** file.
    
    **Step 4:** Copy the **agconnect-services.plist** file to the app's root directory of your Xcode project.
    
    Before obtaining the **agconnect-services.plist** file, ensure that you have enabled Cloud Functions. For details, please refer to [Enabling HUAWEI Cloud Functions](#enabling-cloud-functions-service).
    
    If you have made any changes on the Project Setting page, such as setting the data storage location and enabling or managing APIs, you need to download the latest **agconnect-services.plist** file and replace the existing file in the app's root directory.

- Navigate into example/ios and run 
    
    ```
    $ cd ios && pod install
    ```
    
- Initialize the AGC Cloud Functions SDK.

    After the **agconnect-services.plist** file is imported successfully, add the **agconnect-services.plist** file under example/ios folder. 



### Build & Run the project

Set "triggerIdentifier" value in *example > App.js* file with your cloud function's [HTTP trigger identifier](#creating-a-trigger).

```js
const triggerIdentifier = "<your_trigger_identifier>";
```

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

- [Using the Debug Mode for Android and iOS platforms](https://developer.huawei.com/consumer/en/doc/development/HMS-Plugin-Guides/enabling-debug-mode-0000001050132798)

---

## 4. Licensing and Terms

AGC React-Native AGC Cloudfunctions - Demo is licensed under [Apache 2.0 license](LICENCE)
