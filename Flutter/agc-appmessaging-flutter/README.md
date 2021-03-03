# AppGallery Connect App Messaging Kit Flutter Plugin

---

## Contents

- [1. Introduction](#1-introduction)
- [2. Installation Guide](#2-installation-guide)
  - [Creating a Project in App Gallery Connect](#creating-a-project-in-appgallery-connect)
  - [Integrating the Flutter App Messaging Plugin](#integrating-the-flutter-app-messaging-plugin)
    - [Android App Development](#android-app-development)
    - [iOS App Development](#ios-app-development)
    - [Add to Library](#add-to-library)
- [3. API Reference](#3-api-reference)
  - [AgconnectAppmessaging](#agconnectappmessaging)
- [4. Configuration and Description](#4-configuration-and-description)
  - [Configuring Obfuscation Scripts](#configuring-obfuscation-scripts)
  - [Listening for AppMessaging Events](#listening-for-appmessaging-events)
  - [Adding Custom View](#adding-custom-view)
  - [Accessing Analytics Kit](#accessing-analytics-kit)
- [5. Sample Project](#5-sample-project)
- [6. Licensing and Terms](#6-licensing-and-terms)

---

## 1. Introduction

This plugin enables communication between Huawei App Messaging SDK and Cordova platform. It exposes all functionality provided by Huawei App Messaging SDK.

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

### Enabling App Messaging

You need to enable App Messaging before using it. If you have enabled it, skip this step.

**Step 1.** In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), click _My projects_.

**Step 2.** Find your project from the project list and click the app for which you need to enable App Messaging on the project card.

**Step 3:** Go to **Growing > App Messaging**.

**Step 4:** Click **Enable now** in the upper right corner.

**Step 5:** The **App Messaging** service uses HUAWEI Analytics to report in-app message events. Therefore, you need to enable HUAWEI Analytics before integrating the App Messaging SDK. For details, please refer to [Enabling HUAWEI Analytics](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/service-enabling-0000001050745155).

For further details, please refer to [Enabling App Messaging](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-appmessage-getstarted#h1-1606373400682).

### Integrating the Flutter App Messaging Plugin

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

- Set your package name in **defaultConfig > applicationId** and set **minSdkVersion** to **19** or higher. Package name must match with the **package_name** entry in **agconnect-services.json** file.

  ```gradle
  defaultConfig {
      applicationId "<package_name>"
      minSdkVersion 19
      /*
      * <Other configurations>
      */
  }
  ```

**Step 5:** Edit **buildTypes** as follows:

```gradle
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android.txt'), 'proguard-rules.pro'
            signingConfig signingConfigs.debug
        }
    }
```

#### iOS App Development

**Step 1:** Go to **Project Setting > General information** page, under the **App information** field, click **agconnect-services.plist** to download the configuration file.

**Step 2:** Copy the **agconnect-services.plist** file to the app's root directory of your Xcode project.

**NOTE** Debugging Flutter

Other launch paths without a host computer, such as deep links or notifications, won't work on iOS 14 physical devices in debug mode.
You can also build the application or add-to-app module in profile or release mode, or on a simulator, which are not affected.
For more details please refer to [information](https://flutter.dev/docs/development/ios-14)

#### Add to Library

**Step 1:** On your Flutter project directory, find and open your **pubspec.yaml** file and add the
**agconnect_appmessaging** library to dependencies. For more details please refer to the [Using packages](https://flutter.dev/docs/development/packages-and-plugins/using-packages#dependencies-on-unpublished-packages) document.

- To download the package from [pub.dev](https://pub.dev/publishers/developer.huawei.com/packages).

  ```yaml
  dependencies:
    agconnect_appmessaging: { library version }
  ```

**Step 2:** Run the following command to update package info.

```
[project_path]> flutter pub get
```

**Step 3:** Import the library to access the methods.

```dart
import 'package:agconnect_appmessaging/agconnect_appmessaging.dart';
```

**Step 4:** Run the following command to start the app.

```
[project_path]> flutter run
```

---

## 3. API Reference

### AgconnectAppmessaging

Contains classes that provide methods to represents the message processing class.

#### Public Constructor Summary

| Constructor             | Description          |
| ----------------------- | -------------------- |
| AgconnectAppmessaging() | Default constructor. |

#### Public Method Summary

| Method                                                                                                                        | Return Type          | Description                                                                                                                                                                                                                                          |
| ----------------------------------------------------------------------------------------------------------------------------- | -------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| [isDisplayEnable()](#futurebool-isdisplayenable-async)                                                                        | Future\<bool\>       | This API is called to checks whether the App Messaging SDK is allowed to display in-app messages.                                                                                                                                                    |
| [setFetchMessageEnable(bool enable)](#futurevoid-setfetchmessageenablebool-enable-async)                                      | Future\<void\>       | This API is called to sets whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server.                                                                                                             |
| [setDisplayEnable(bool enable)](#futurevoid-setdisplayenablebool-enable-async)                                                | Future\<void\>       | This API is called to sets whether to allow the App Messaging SDK to display in-app messages.                                                                                                                                                        |
| [isFetchMessageEnable()](#futurebool-isfetchmessageenable-async)                                                              | Future\<bool\>       | This API is called to checks whether the App Messaging SDK is allowed to synchronize in-app message data from the AppGallery Connect server.                                                                                                         |
| [setForceFetch()](#futurevoid-setforcefetch-async)                                                                            | Future\<void\>       | This API is called to sets the forcible in-app message data obtaining flag. When the flag is enabled, you can obtain latest in-app message data from the AppGallery Connect server in real time. This method is only to support on Android Platform. |
| [removeCustomView()](#futurevoid-removecustomview-async)                                                                      | Future\<void\>       | This API is called to remove custom in-app message layout. Then the default layout will be used.                                                                                                                                                     |
| [setDisplayLocation(int locationConstant)](#futurevoid-setdisplaylocationint-locationConstant-async)                          | Future\<void\>       | This API is called to sets the display position of a pop-up or image message.                                                                                                                                                                        |
| [trigger(String eventId)](#futurevoid-triggerstring-eventid-async)                                                            | Future\<void\>       | This API is called to triggers a custom event.                                                                                                                                                                                                       |
| [handleCustomViewMessageEvent(Map<String, dynamic> map)](#futurevoid-handlecustomviewmessageeventmapstring-dynamic-map-async) | Future\<void\>       | This API is called to while using custom app message layout, handle custom app message click events.                                                                                                                                                 |
| [onMessageDismiss()](#streamappmessage-get-onmessagedismiss-async)                                                            | Stream\<Appmessage\> | This API is called to adds a listener for dismiss event.                                                                                                                                                                                             |
| [onMessageClick()](#streamappmessage-get-onmessageclick-async)                                                                | Stream\<Appmessage\> | This API is called to adds a listener for click event.                                                                                                                                                                                               |
| [onMessageDisplay()](#streamappmessage-get-onmessagedisplay-async)                                                            | Stream\<Appmessage\> | This API is called to adds a listener for display event.                                                                                                                                                                                             |
| [onMessageError()](#streamappmessage-get-onmessageerror-async)                                                                | Stream\<Appmessage\> | This API is called to adds a listener for error event.                                                                                                                                                                                               |
| [customEvent()](#streamappmessage-get-customevent-async)                                                                      | Stream\<Appmessage\> | This API is called to adds a listener for custom event.                                                                                                                                                                                              |

#### Public Constructors

##### AgconnectAppmessaging()

Constructor for AgconnectAppmessaging object.

#### Public Methods

##### Future\<bool\> isDisplayEnable() _async_

Checks whether to allow the App Messaging SDK to display in-app messages.

| Return Type    | Description                                                                  |
| -------------- | ---------------------------------------------------------------------------- |
| Future\<bool\> | Indicates whether to allow the App Messaging SDK to display in-app messages. |

###### Call Example

```dart
 try {
      bool isDisplayEnable = await agconnectAppmessaging.isDisplayEnable();
      print(isDisplayEnable.toString());
      _showDialog(context, isDisplayEnable.toString());
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }

```

##### Future\<void> setFetchMessageEnable(bool enable) _async_

Sets whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server.

| Parameter | Description                                                                                                                                                                                                  |
| --------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| enable    | Indicates whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server. The options are as follows:<br>true: yes<br>false: no.<br>The default value is true. |

| Return Type    | Description                                          |
| -------------- | ---------------------------------------------------- |
| Future\<void\> | Future result of an execution that returns no value. |

###### Call Example

```dart
    try {
      agconnectAppmessaging.setFetchMessageEnable(true);
      _showDialog(context, 'setFetchMessageEnable true');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }

```

##### Future\<void> setDisplayEnable(bool enable) _async_

Sets whether to allow the App Messaging SDK to display in-app messages.

| Parameter | Description                                                                                                                                                     |
| --------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| enable    | Indicates whether to allow the App Messaging SDK to display in-app messages.The options are as follows:<br>true: yes<br>false: no<br>The default value is true. |

| Return Type    | Description                                          |
| -------------- | ---------------------------------------------------- |
| Future\<void\> | Future result of an execution that returns no value. |

###### Call Example

```dart
    try {
      agconnectAppmessaging.setDisplayEnable(true);
      _showDialog(context, 'setDisplayEnable true');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }

```

##### Future\<bool> isFetchMessageEnable() _async_

Checks whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server.

| Return Type    | Description                                                                                                                                                                   |
| -------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Future\<bool\> | Indicates whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server. The options are as follows:<br>true: yes<br>false: no |

###### Call Example

```dart
    try {
      bool isFetchMessageEnable =
          await agconnectAppmessaging.isFetchMessageEnable();
      print(isFetchMessageEnable.toString());
      _showDialog(context, isFetchMessageEnable.toString());
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }

```

##### Future\<void> setForceFetch() _async_

Sets the flag for whether to obtain in-app message data from the AppGallery Connect server in real time by force.

After this method is called, the App Messaging SDK does not immediately request data from the AppGallery Connect server. When the next trigger event takes place, the App Messaging SDK forcibly obtains in-app message data from the AppGallery Connect server.
**For iOS platform:** Enable the debugging mode for your app. Add the '-AGConnectDeveloperMode' launch parameter to the app scheme. Start your app in Xcode debug mode.
Notice:

- The setForceFetch API can be used only for message testing.
- The forcible data obtaining flag is bound to the AAID of the test device. After you uninstall and reinstall the app or clear app data on the device, the flag will be reset.

| Return Type    | Description                                          |
| -------------- | ---------------------------------------------------- |
| Future\<void\> | Future result of an execution that returns no value. |

###### Call Example

```dart
  Future<void> _setForceFetch(BuildContext context) async {
    try {
      agconnectAppmessaging.setForceFetch();
      _showDialog(context, 'setForceFetch');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
  }

```

##### Future\<void> removeCustomView() _async_

Remove custom in-app message layout. Then the default layout will be used.

| Return Type    | Description                                          |
| -------------- | ---------------------------------------------------- |
| Future\<void\> | Future result of an execution that returns no value. |

###### Call Example

```dart
    try {
      agconnectAppmessaging.removeCustomView();
      _showDialog(context, "removeCustomView");
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }

```

##### Future\<void> setDisplayLocation(int locationConstant) _async_

Sets the display position of a pop-up or image message.

| Parameter        | Description         |
| ---------------- | ------------------- |
| locationConstant | Location instance.. |

| Return Type    | Description                                          |
| -------------- | ---------------------------------------------------- |
| Future\<void\> | Future result of an execution that returns no value. |

###### Call Example

```dart
    try {
      agconnectAppmessaging
          .setDisplayLocation(AppMessagingLocationConstants.CENTER);
      _showDialog(context, 'setDisplayLocation');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }

```

##### Future\<void> trigger(String eventId) _async_

Triggers a custom event.

| Parameter | Description            |
| --------- | ---------------------- |
| eventId   | ID of a custom event.. |

| Return Type    | Description                                          |
| -------------- | ---------------------------------------------------- |
| Future\<void\> | Future result of an execution that returns no value. |

###### Call Example

```dart
    try {
      agconnectAppmessaging.trigger("OnAppForeGround");
      _showDialog(context, 'trigger');
    } on PlatformException catch (e) {
      _showDialog(context, e.toString());
    }
```

##### Future\<void> handleCustomViewMessageEvent(Map<String, dynamic> map) _async_

While using custom app message layout, handle custom app message click events.

| Parameter | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| --------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| map       | When using custom app message layout, handle custom app message click events like below, gets eventType ( AppMessagingEventType.onMessageDisplay, AppMessagingEventType.onMessageClick, AppMessagingEventType.onMessageDismiss(String dismissType), AppMessagingEventType.onMessageError and dismissType param( AppMessagingDismissTypeConstants.CLICK, AppMessagingDismissTypeConstants.CLICK_OUTSIDE, AppMessagingDismissTypeConstants.AUTO, AppMessagingDismissTypeConstants.BACK_BUTTON, AppMessagingDismissTypeConstants.SWIPE) when using AppMessagingDismissTypeConstants.onMessageDismiss(String dismissType). |

| Return Type    | Description                                          |
| -------------- | ---------------------------------------------------- |
| Future\<void\> | Future result of an execution that returns no value. |

###### Call Example

```dart
    _streamSubscription1 = agconnectAppmessaging.customEvent.listen((event) {
      print('customEvent       $event');
      agconnectAppmessaging.handleCustomViewMessageEvent
        (AppMessagingEventType.onMessageDismiss(AppMessagingDismissTypeConstants.CLICK));
    });

```

##### Stream\<AppMessage\> get onMessageDismiss _async_

Add a listener to obtain app message dismiss events.

| Return Type          | Description                                                        |
| -------------------- | ------------------------------------------------------------------ |
| Stream\<AppMessage\> | AppMessage data to be processed, which is returned asynchronously. |

###### Call Example

```dart
    _streamSubscription = agconnectAppmessaging.onMessageDismiss.listen((event) async {
      print(event.toString());

    });

```

##### Stream\<AppMessage\> get onMessageClick _async_

Add a listener to obtain app message click events.

| Return Type          | Description                                                        |
| -------------------- | ------------------------------------------------------------------ |
| Stream\<AppMessage\> | AppMessage data to be processed, which is returned asynchronously. |

###### Call Example

```dart
    _streamSubscription = agconnectAppmessaging.onMessageClick.listen((event) async {
      print(event.toString());

    });

```

##### Stream\<AppMessage\> get onMessageDisplay _async_

Add a listener to obtain app message display events.

| Return Type          | Description                                                        |
| -------------------- | ------------------------------------------------------------------ |
| Stream\<AppMessage\> | AppMessage data to be processed, which is returned asynchronously. |

###### Call Example

```dart
    _streamSubscription = agconnectAppmessaging.onMessageDisplay.listen((event) async {
      print(event.toString());

    });

```

##### Stream\<AppMessage\> get onMessageError _async_

Add a listener to obtain app message error events.

| Return Type          | Description                                                        |
| -------------------- | ------------------------------------------------------------------ |
| Stream\<AppMessage\> | AppMessage data to be processed, which is returned asynchronously. |

###### Call Example

```dart
    _streamSubscription = agconnectAppmessaging.onMessageError.listen((event) async {
      print(event.toString());

    });

```

##### Stream\<AppMessage\> get customEvent _async_

Add a listener to obtain app message customEvent events.

| Return Type          | Description                                                        |
| -------------------- | ------------------------------------------------------------------ |
| Stream\<AppMessage\> | AppMessage data to be processed, which is returned asynchronously. |

###### Call Example

```dart
    _streamSubscription = agconnectAppmessaging.customEvent.listen((event) async {
      print(event.toString());

    });

```

### Public Constants

#### AppMessagingLocationConstants

- Display position of a pop-up or image message.

| Field  | Value | Description |
| ------ | ----- | ----------- |
| BOTTOM | 0     | Bottom.     |
| CENTER | 1     | Center.     |

#### AppMessagingDismissTypeConstants

- Message closing constants.

| Field         | Type   | Description                               |
| ------------- | ------ | ----------------------------------------- |
| CLICK         | String | Close button or redirection link tapping. |
| CLICK_OUTSIDE | String | Tapping outside the message borders.      |
| BACK_BUTTON   | String | Back button tapping.                      |
| AUTO          | String | Auto.                                     |
| SWIPE         | String | SWIPE.                                    |

#### AppMessagingEventType

- Custom events constants for event callbacks.

| Field                                | Type   | Description                        |
| ------------------------------------ | ------ | ---------------------------------- |
| onMessageDisplay                     | String | Listens on message display events. |
| onMessageClick                       | String | Listens on message tap events.     |
| onMessageError                       | String | Listens on message error events.   |
| onMessageDismiss(String dismissType) | String | Listens on message closing events. |

##### AppMessage

- Represents the app message data.

| Field          | Type           | Description                                         |
| -------------- | -------------- | --------------------------------------------------- |
| id             | String         | Obtains the ID of an in-app message.                |
| messageType    | String         | Obtains the message type.                           |
| startTime      | int            | Obtains the start timestamp of a message.           |
| endTime        | int            | Obtains the end timestamp of a message.             |
| frequencyType  | int            | Obtains the display frequency type of a message.    |
| frequencyValue | int            | Obtains the display frequency of a message.         |
| testFlag       | int            | Checks whether an in-app message is a test message. |
| dismissType    | String         | Obtains the dismiss type.                           |
| triggerEvents  | List<dynamic>  | Obtain the ID of a trigger event.                   |
| bannerMessage  | BannerMessage  | Obtains the banner information of a message.        |
| cardMessage    | CardMessage    | Obtains the card information of a message.          |
| pictureMessage | PictureMessage | Obtains the picture information of a message.       |

###### Call Example

```dart
    _streamSubscription =
        agconnectAppmessaging.onMessageDisplay.listen((event) {
      print(event.startTime);
    });
```

##### BannerMessage

- Represents a banner message.

| Field                   | Type   | Description                                                    |
| ----------------------- | ------ | -------------------------------------------------------------- |
| title                   | String | Obtains the title of a banner message.                         |
| titleColor              | String | Obtains the title color of a banner message.                   |
| titleColorOpenness      | num    | Obtains the title color transparency of a banner message.      |
| body                    | String | Obtains the body of a banner message.                          |
| bodyColor               | String | Obtains the body color of a banner message.                    |
| bodyColorOpenness       | num    | Obtains the body color transparency of a banner message.       |
| backgroundColor         | String | Obtains the background color of a banner message.              |
| backgroundColorOpenness | num    | Obtains the background color transparency of a banner message. |
| pictureUrl              | String | Obtains the image URL of a banner message.                     |
| actionUrl               | String | Obtains the redirection URL in a banner message.               |

###### Call Example

```dart
    _streamSubscription =
        agconnectAppmessaging.onMessageDisplay.listen((event) {
      print(event.cardMessage.backgroundColor);
    });
```

##### CardMessage

- Represents a pop-up message.

| Field                   | Type   | Description                                                    |
| ----------------------- | ------ | -------------------------------------------------------------- |
| title                   | String | Obtains the title of a pop-up message.                         |
| titleColor              | String | Obtains the title color of a pop-up message.                   |
| titleColorOpenness      | num    | Obtains the title color transparency of a pop-up message.      |
| body                    | String | Obtains the body of a pop-up message.                          |
| bodyColor               | String | Obtains the body color of a pop-up message.                    |
| bodyColorOpenness       | num    | Obtains the body color transparency of a pop-up message.       |
| backgroundColor         | String | Obtains the background color of a pop-up message.              |
| backgroundColorOpenness | num    | Obtains the background color transparency of a pop-up message. |
| portraitPictureURL      | String | Obtains the URL of the portrait image for a pop-up message.    |
| landscapePictureURL     | String | Obtains the URL of the landscape image for a pop-up message.   |
| majorButton             | Button | Obtains the primary button of a pop-up message.                |
| minorButton             | Button | Obtains the secondary button of a pop-up message.              |

###### Call Example

```dart
    _streamSubscription1 =
        agconnectAppmessaging.onMessageDismiss.listen((event) {
          print(event.cardMessage.bodyColor);
    });
```

##### PictureMessage

- Represents an image message.

| Field      | Type   | Description                                      |
| ---------- | ------ | ------------------------------------------------ |
| pictureURL | String | Image URL of an image message.                   |
| actionURL  | String | Obtains the redirection URL of an image message. |

###### Call Example

```dart
    _streamSubscription1 =
        agconnectAppmessaging.onMessageDisplay.listen((event) {
          print(event.pictureMessage.pictureURL);
    });
```

##### Button

- Represents a pop-up message button.

| Field             | Type   | Description                                             |
| ----------------- | ------ | ------------------------------------------------------- |
| text              | String | Obtains the button name.                                |
| textColor         | String | Obtains the button name text color.                     |
| textColorOpenness | num    | Obtains the transparency of the button name text color. |
| actionURL         | String | Obtains the redirection URL of the button.              |

###### Call Example

```dart
    _streamSubscription1 =
        agconnectAppmessaging.onMessageDisplay.listen((event) {
          print(event.cardMessage.majorButton.text);
    });
```

---

## 4. Configuration and Description

### Configuring Obfuscation Scripts

Before building the APK, configure obfuscation scripts to prevent the AppGallery Connect SDK from being obfuscated. If obfuscation arises, the AppGallery Connect SDK may not function properly. For more information on this topic refer to [this Android developer guide](https://developer.android.com/studio/build/shrink-code).

**<flutter_project>/android/app/proguard-rules. pro**

```
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.agc.**{*;}
-keep class com.huawei.agconnect.**{*;}

## Flutter wrapper
-keep class io.flutter.app.** { *; }
-keep class io.flutter.plugin.**  { *; }
-keep class io.flutter.util.**  { *; }
-keep class io.flutter.view.**  { *; }
-keep class io.flutter.**  { *; }
-keep class io.flutter.plugins.**  { *; }
-dontwarn io.flutter.embedding.**
-keep class com.huawei.agc.flutter.** { *; }
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

### Listening for AppMessaging Events

Add a listener to obtain app message click events.

###### Call Example

```dart
 @override
  void initState() {
    super.initState();

    _streamSubscriptionDisplay =
        agconnectAppmessaging.onMessageDisplay.listen((event) {
      print('onMessageDisplay       $event');
    });
    _streamSubscriptionClick =
        agconnectAppmessaging.onMessageClick.listen((event) {
      print('onMessageClick       $event');
    });
    _streamSubscriptionDismiss =
        agconnectAppmessaging.onMessageDismiss.listen((event) {
      print('onMessageDismiss       $event');
    });

    _streamSubscriptionError =
        agconnectAppmessaging.onMessageError.listen((event) {
      print('onMessageError       $event');
    });
  }
```

### Adding Custom View

App Messaging supports three messages types, namely pop-up, image, and banner messages. The App Messaging SDK provides a default layout for each type. You can customize the message display style that better suits your app to provide personalized experience for users.

Firstly, add a listener to obtain app message. Then you can create your own layout for app message.

###### Call Example

```dart
// Example of dialog box used to display information on screen only.
  void _showDialog(BuildContext context, String content) {
    showDialog(
        context: context,
        builder: (BuildContext context) {
          return AlertDialog(
            key: Key("dialog"),
            title: Text("Result"),
            content: Text(content),
            actions: <Widget>[
              FlatButton(
                child: new Text("Close"),
                onPressed: () {
                  Navigator.of(context).pop();
                },
              )
            ],
          );
        });
  }

/**
 * When using custom app message layout, handle custom app message click events like below.
 * eventType ( AppMessagingEventType.onMessageDisplay,AppMessagingEventType.onMessageClick, AppMessagingEventType.onMessageError, AppMessagingEventType.onMessageDismiss(String dismissType))
 * and dismissType param( AppMessagingDismissTypeConstants.CLICK, AppMessagingDismissTypeConstants.CLICK_OUTSIDE, AppMessagingDismissTypeConstants.AUTO, AppMessagingDismissTypeConstants.SWIPE,AppMessagingDismissTypeConstants.BACK_BUTTON) when using AGCAppMessaging.ON_MESSAGE_DISMISS.
 */
    _streamSubscriptionDisplay = agconnectAppmessaging.customEvent.listen((event) {
      // You can display the message content using a Message dialog or using a different widget
      _showDialog(context ,event.toString());
      agconnectAppmessaging.handleCustomViewMessageEvent
        (AppMessagingEventType.onMessageDismiss(AppMessagingDismissTypeConstants.CLICK));
    });
```

Below lines should be added to the corresponding platforms.

#### IOS:

Navigate into your Flutter/iOS project **info.plist** file.

Create **AGCCustomView** field as **boolean** property:

Set

- YES: adding custom view
- NO: removing custom view

```xml
<dict>
    ...
    <key>AGCCustomView</key>
    <false/>
    ...
</dict>
```

#### Android:

You should add below code to the onCreate method of MainActivity.java.

```java
AGCAppMessagingCustomEventStreamHandler.addCustomView();
```

### Accessing Analytics Kit

#### Installation

Please see [pub.dev](https://pub.dev/packages/huawei_analytics/install) and [AppGallery Connect Configuration](https://developer.huawei.com/consumer/en/doc/development/HMS-Plugin-Guides/config-agc-0000001050171095).

#### Documentation

- [Quick Start](https://developer.huawei.com/consumer/en/doc/development/HMS-Plugin-Guides/initializing-analytics-kit-0000001058525725)
- [Reference](https://developer.huawei.com/consumer/en/doc/development/HMS-Plugin-References/overview-0000001050176764)

For further information please refer to [Analytics Kit Service Guide](https://developer.huawei.com/consumer/en/doc/development/HMS-Plugin-Guides/introduction-0000001050169136).

---

## 5. Sample Project

This plugin includes a demo project in the **example** folder, there you can find more usage examples.

---

## 6. Licensing and Terms

AppGallery Connect App Messaging Kit Flutter Plugin is licensed under [Apache 2.0 license](LICENSE)
