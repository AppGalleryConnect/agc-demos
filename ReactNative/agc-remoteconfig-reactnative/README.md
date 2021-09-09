# React-Native AGC RemoteConfig - Demo

---

## Contents

- [Introduction](#1-introduction)
- [Installation](#2-installation)
- [Configuration](#3-configuration)
- [Licensing and Terms](#4-licensing-and-terms)

---

## 1. Introduction

This demo project is an example to demonstrate the features of the **AGC React-Native RemoteConfig Kit** Plugin.


---

## 2. Installation

Before you get started, you must register as a HUAWEI Developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/10104).

### Creating a Project in AppGallery Connect

Creating an app in AppGallery Connect is required in order to communicate with the Huawei services. To create an app, perform the following steps:

**Step 1.** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html)  and select **My projects**.

**Step 2.** Select your project from the project list or create a new one by clicking the **Add Project** button.

**Step 3.** Go to **Project Setting** > **General information**, and click **Add app**.
If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.

**Step 4.** On the **Add app** page, enter the app information, and click **OK**.

### Enabling RemoteConfig Service

You need to enable RemoteConfig Service before using it. If you have enabled it, skip this step.

**Step 1.** In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), click *My projects*.

**Step 2.** Find your project from the project list and click the app for which you need to enable RemoteConfig Service on the project card.

**Step 3:** Go to **Quality > RemoteConfig Service**. 

**Step 4:** Click **Enable now** in the upper right corner. 

For further details, please refer to [Enabling RemoteConfig Service](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-remoteconfig-android-getstarted-0000001056347165).

### Integrating the React-Native RemoteConfig Plugin

Before using **@react-native-agconnect/remoteconfig**, ensure that the ReactNative development environment has been installed.

### Install via NPM

```
npm i @react-native-agconnect/remoteconfig
```

#### Android App Development

#### Integrating the React-Native AGC RemoteConfig into the Android Studio

- Add the AppGallery Connect configuration file of the app to your Android Studio project.

    **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.
    
    **Step 2:** Find your **app** project and click the app.
    
    **Step 3:** Go to **Project Setting > App information**. In the **App information** area, download the **agconnect-services.json** file.
    
    **Step 4:** Copy the **agconnect-services.json** file to the app's root directory of your project.
    
- Open the **build.gradle** file in the **android/app** directory of your React Native project.

    Set your package name in **defaultConfig** > **applicationId** and set **minSdkVersion** to **19** or **higher**.

    ```groovy
    defaultConfig {
     applicationId "<package_name>"
     minSdkVersion 19
     /*
      * <Other configurations>
      */
    }
    ```
    
    **Package name must match with the _package_name_ entry in _agconnect-services.json_ file.**

- Configure the signature file.
    
    ```gradle
    android {
        /*
         * <Other configurations>
         */

        signingConfigs {
            config {
                storeFile file('<keystore_file>.jks')
                storePassword '<keystore_password>'
                keyAlias '<key_alias>'
                keyPassword '<key_password>'
            }
        }

        buildTypes {
            debug {
                signingConfig signingConfigs.config
            }
            release {
                signingConfig signingConfigs.config
            }
        }
    }
    ```

#### iOS App Development

#### Integrating the React-Native AGC RemoteConfig into the Xcode Project

- Navigate into your project directory and run below command.

    ```
    [project_path]> cd ios/ && pod install
    ```

- Add the AppGallery Connect configuration file of the app to your Xcode project.

    **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.
    
    **Step 2:** Find your **app** project and click the app.
    
    **Step 3:** Go to **Project Setting > App information**. In the **App information** area, download the **agconnect-services.plist** file.
    
    **Step 4:** Copy the **agconnect-services.plist** file to the app's root directory of your Xcode project.

    If you have made any changes on the Project Setting page, such as setting the data storage location and enabling or managing APIs, you need to download the latest **agconnect-services.plist** file and replace the existing file in the app's root directory.
 
---


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

[Enabling RemoteConfig Service](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-remoteconfig-android-getstarted-0000001056347165)

## 4. Licensing and Terms

AGC React-Native RemoteConfig - Demo is licensed under [Apache 2.0 license](LICENCE)


