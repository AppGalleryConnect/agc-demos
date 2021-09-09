# AppGallery Connect Crash Kit Flutter Plugin - Demo

---

## Contents

  - [Introduction](#1-introduction)
  - [Installation](#2-installation)
  - [Configuration](#3-configuration)
  - [Licensing and Terms](#4-licensing-and-terms)

---

## 1. Introduction

This demo project is an example to demonstrate the features of the **AppGallery Connect Flutter Crash Kit
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
      applicationId "<Enter_your_package_name_here>" // For ex: "com.developer.crash"
      minSdkVersion 17
      <!-- Other configurations ... -->
  }
  ```
**Step 2.** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html)  and select **My projects**.

**Step 3.** Select your project from the project list or create a new one by clicking the **Add Project** button.

**Step 4.** Go to **Project Setting** > **General information**, and click **Add app**.
If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.

**Step 5.** On the **Add app** page, enter the **Application ID** you've defined before as the **Package Name** here, then fill the necessary fields and click **OK**.

### Enabling Crash Service

1. In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), find your project, and click the app for which you want to use crash kit.

2. Select **Quality** and **Crash** on the left menu and click **Enable Crash service**.

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

[Enabling Crash Service](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-crash-getstarted-0000001055260538)

## 4. Licensing and Terms

AppGallery Connect Crash Kit Flutter Plugin Demo is licensed under [Apache 2.0 license](../LICENSE)
