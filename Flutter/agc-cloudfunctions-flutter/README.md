# AppGallery Connect Cloud Functions Kit Flutter Plugin

---

## Contents

- [1. Introduction](#1-introduction)
- [2. Installation Guide](#2-installation-guide)
  - [Creating a Project in App Gallery Connect](#creating-a-project-in-appgallery-connect)
  - [Applying for the Cloud Functions Service](#applying-for-the-cloud-functions-service)
  - [Enabling Cloud Functions Service](#enabling-cloud-functions-service)
  - [Creating a Cloud Function](#creating-a-cloud-function)
  - [Integrating the Flutter Cloud Function Plugin](#integrating-the-flutter-cloud-function-plugin)
    - [Android App Development](#android-app-development)
    - [iOS App Development](#ios-app-development)
    - [Add to Library](#add-to-library)
- [3. API Reference](#3-api-reference)
  - [FunctionCallable](#functioncallable)
- [4. Configuration and Description](#4-configuration-and-description)
- [5. Sample Project](#5-sample-project)
- [6. Licensing and Terms](#6-licensing-and-terms)

---

## 1. Introduction

This plugin enables communication between Huawei Cloud Functions SDK and Flutter platform. It exposes all functionality provided by Huawei Cloud Functions SDK.

---

## 2. Installation Guide

Before you get started, you must register as a HUAWEI Developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/10104).

### Creating a Project in AppGallery Connect

Creating an app in AppGallery Connect is required in order to communicate with the Huawei services. To create an app, perform the following steps:

**Step 1.** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select **My projects**.

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

1. In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), find your project, and click the app for which you want to use cloud functions.

2. Select **Build** and **Cloud functions** on the left menu and click **Enable Cloud Functions service**.

### Creating a Cloud Function

To create a cloud function, please refer [Create a Function](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-getstarted#h1-1592364963757).

**Creating a Trigger:** To call a function in an app, you must create an HTTP trigger. For details, please refer to [Create an HTTP Trigger](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-triggercall#h2-1603096955823). When calling a function in an app, you must transfer the identifier of an HTTP trigger. For details, please refer to [Querying the Trigger Identifier](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-appcall#h1-1578361186845)

### Integrating the Flutter Cloud Function Plugin

#### Android App Development

**Step 1:** Go to **Project Setting > General information** page, under the **App information** field, click **agconnect-services.json** to download the configuration file.

**Step 2:** Copy the **agconnect-services.json** file to the **example/android/app/** directory of your project.

**Step 3:** Open the **build.gradle** file in the **example/android** directory of your project.

- Navigate to the **buildscript** section and configure the Maven repository address and agconnect plugin for the AppGallery Connect SDK.

  ```gradle
  buildscript {
    repositories {
        google()
        jcenter()
        maven { url 'https://developer.huawei.com/repo/' }
    }

    dependencies {
        /*
          * <Other dependencies>
          */
        classpath 'com.huawei.agconnect:agcp:1.4.2.301'
    }
  }
  ```

- Go to **allprojects** and configure the Maven repository address for the AppGallery Connect SDK.

  ```gradle
  allprojects {
    repositories {
        google()
        jcenter()
        maven { url 'https://developer.huawei.com/repo/' }
    }
  }
  ```

**Step 4:** Open the **build.gradle** file in the **example/android/app/** directory.

- Add `apply plugin: 'com.huawei.agconnect'` line after other `apply` entries.

  ```gradle
  apply plugin: 'com.android.application'
  apply from: "$flutterRoot/packages/flutter_tools/gradle/flutter.gradle"
  apply plugin: 'com.huawei.agconnect'
  ```

- Set your package name in **defaultConfig > applicationId** and set **minSdkVersion** to **17** or higher. Package name must match with the **package_name** entry in **agconnect-services.json** file.

  ```gradle
  defaultConfig {
      applicationId "<package_name>"
      minSdkVersion 17
      /*
      * <Other configurations>
      */
  }
  ```

#### iOS App Development

**Step 1:** Go to **Project Setting > General information** page, under the **App information** field, click **agconnect-services.plist** to download the configuration file.

**Step 2:** Copy the **agconnect-services.plist** file to the app's root directory of your Xcode project.

---

#### Add to Library

**Step 1:** On your Flutter project directory, find and open your **pubspec.yaml** file and add the
**agconnect_cloudfunctions** library to dependencies. For more details please refer to the [Using packages](https://flutter.dev/docs/development/packages-and-plugins/using-packages#dependencies-on-unpublished-packages) document.

- To download the package from [pub.dev](https://pub.dev/publishers/developer.huawei.com/packages).

  ```yaml
  dependencies:
    agconnect_cloudfunctions: { library version }
  ```

**Step 2:** Run the following command to update package info.

```
[project_path]> flutter pub get
```

**Step 3:** Import the library to access the methods.

```dart
import 'package:agconnect_cloudfunctions/agconnect_cloudfunctions.dart';
```

**Step 4:** Run the following command to start the app.

```
[project_path]> flutter run
```

## 3. API Reference

### FunctionCallable

Represents the cloud function processing class.

#### Public Constructor Summary

| Constructor                                                               | Description                                           |
| ------------------------------------------------------------------------- | ----------------------------------------------------- |
| FunctionCallable(String httpTriggerURI, [int timeout, AGCTimeUnit units]) | Default constructor.                                  |
| FunctionCallable.fromMap(Map\<dynamic, dynamic\> map)                     | Creates a FunctionCallable object from a map.         |
| FunctionCallable.fromJson(String source)                                  | Creates a FunctionCallable object from a JSON string. |

#### Public Method Summary

| Method                                                                                           | Return Type              | Description                                                    |
| ------------------------------------------------------------------------------------------------ | ------------------------ | -------------------------------------------------------------- |
| [call([dynamic functionParameters])](#futurefunctionresult-calldynamic-functionparameters-async) | Future\<FunctionResult\> | This API is called to configures and calls the cloud function. |

#### Public Constructors

##### FunctionCallable(String httpTriggerURI, [int timeout, AGCTimeUnit units])

Constructor for _FunctionCallable_ object.
| Parameter | Type | Description |
|------------------------|------|----------------------------------------------------------------------------------------|
| httpTriggerURI | String | HTTP trigger identifier of the cloud function to be called. For details about how to query the HTTP trigger identifier, see [Querying the Trigger Identifier](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-appcall#httptriggerurl). |
| timeout | int | Timeout interval of a function. For **Android** the unit is defined by the timeUnit parameter and the default time unit is **seconds**. For **iOS** time unit is **seconds**. This parameter is optional. |
| units | AGCTimeUnit | Defines the time unit. This parameter is optional. The default value is 70. |

#### Public Methods

##### Future\<FunctionResult\> call([dynamic functionParameters]) _async_

Configures and calls the cloud function.

| Parameter          | Description                                                               |
| ------------------ | ------------------------------------------------------------------------- |
| functionParameters | Cloud function configurations and parameters. This parameter is optional. |

| Return Type              | Description                                      |
| ------------------------ | ------------------------------------------------ |
| Future\<FunctionResult\> | The return value after the function is executed. |

###### Call Example

```dart
 FunctionCallable functionCallable = FunctionCallable(httpTrigger);
      Person person1 = Person(1, 'Amy');
      Person person2 = Person(2, 'Gunner');

      List<Map<String, Object>> params = <Map<String, Object>>[
        person1.toMap(),
        person2.toMap()
      ];

      FunctionResult functionResult = await functionCallable.call(params);
      result = functionResult.getValue();

```

### FunctionResult

The result after the function is executed.

#### Public Constructor Summary

| Constructor                                         | Description                                         |
| --------------------------------------------------- | --------------------------------------------------- |
| FunctionResult()                                    | Default constructor.                                |
| FunctionResult.fromMap(Map\<dynamic, dynamic\> map) | Creates a FunctionResult object from a map.         |
| FunctionResult.fromJson(String source)              | Creates a FunctionResult object from a JSON string. |

#### Public Method Summary

| Methods  | Return Type | Description                                      |
| -------- | ----------- | ------------------------------------------------ |
| getValue | String      | The return value after the function is executed. |

#### Public Constructors

##### FunctionResult()

Constructor for FunctionResult object.

##### FunctionResult.fromMap(Map\<dynamic, dynamic\> map)

Creates a FunctionResult object from a map.

| Parameter | Type                    | Description      |
| --------- | ----------------------- | ---------------- |
| map       | Map\<dynamic, dynamic\> | Map as a source. |

##### FunctionResult.fromJson(String source)

Creates a FunctionResult object from a JSON string.

| Parameter | Type   | Description              |
| --------- | ------ | ------------------------ |
| source    | String | JSON string as a source. |

#### Public Methods

##### String getValue

The return value after the function is executed.

| Return Type | Description              |
| ----------- | ------------------------ |
| String      | JSON string as a source. |

### Public Constants

#### AGCTimeUnit

- Constants of time units.

| Value | Field        | Type | Description   |
| ----- | ------------ | ---- | ------------- |
| 0     | NANOSECONDS  | int  | Nanoseconds.  |
| 1     | MICROSECONDS | int  | Microsecons.  |
| 2     | MILLISECONDS | int  | Milliseconds. |
| 3     | SECONDS      | int  | Seconds.      |
| 4     | MINUTES      | int  | Minutes.      |
| 5     | HOURS        | int  | Hours.        |
| 6     | DAYS         | int  | Days.         |

---

## 4. Configuration and Description

### Preparing for Release

Before building the APK, configure obfuscation scripts to prevent the AppGallery Connect SDK from being obfuscated. If obfuscation arises, the AppGallery Connect SDK may not function properly. For more information on this topic refer to [this Android developer guide](https://developer.android.com/studio/build/shrink-code).

**<flutter_project>/android/app/proguard-rules. pro**

```
-ignorewarnings
-keep class com.huawei.agconnect.**{*;}

# Flutter wrapper
-keep class io.flutter.app.** { *; }
-keep class io.flutter.plugin.**  { *; }
-keep class io.flutter.util.**  { *; }
-keep class io.flutter.view.**  { *; }
-keep class io.flutter.**  { *; }
-keep class io.flutter.plugins.**  { *; }
-dontwarn io.flutter.embedding.**
-keep class com.huawei.hianalytics.**{*;}
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.agconnect.**{*;}
-keep class com.huawei.agc.**{*;}
-repackageclasses

```

**<flutter_project>/android/app/build.gradle**

```gradle
buildTypes {
    debug {
        signingConfig signingConfigs.config
    }
    release {

        // Enables code shrinking, obfuscation and optimization for release builds
        minifyEnabled true
        // Unused resources will be removed, resources defined in the res/raw/keep.xml will be kept.
        shrinkResources true
        proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'

	signingConfig signingConfigs.config
    }
}
```

---

## 5. Sample Project

This plugin includes a demo project in the **example** folder, there you can find more usage examples.

---

## 6. Licensing and Terms

AppGallery Connect Cloud Functions Kit Flutter Plugin is licensed under [Apache 2.0 license](LICENSE)
