# React-Native App Messaging

---

## Contents

  - [1. Introduction](#1-introduction)
  - [2. Installation Guide](#2-installation-guide)
    - [Creating a Project in App Gallery Connect](#creating-a-project-in-appgallery-connect)
    - [Enabling App Messaging](#enabling-app-messaging)
    - [Integrating the React-Native AppMessaging Plugin](#integrating-the-react-native-appmessaging-plugin)
        - [Android App Development](#android-app-development)
            - [Integrating the React-Native AGC AppMessaging into the Android Studio](#integrating-the-react-native-agc-appmessaging-into-the-android-studio)
        - [iOS App Development](#ios-app-development)
            - [Integrating the React-Native AGC AppMessaging into the Xcode Project](#integrating-the-react-native-agc-appmessaging-into-the-xcode-project)
  - [3. API Reference](#3-api-reference)
    - [AGCAppMessaging](#agcappmessaging)
    - [Constants](#constants)
  - [4. Configuration and Description](#4-configuration-and-description)
     - [Accessing Analytics Kit](#accessing-analytics-kits)
     - [Configuring Obfuscation Scripts](#configuring-obfuscation-scripts)
     - [Listening for AppMessaging Events](#listening-for-appmessaging-events)
     - [Adding Custom View](#adding-custom-view)
  - [5. Sample Project](#5-sample-project)
  - [6. Licensing and Terms](#6-licensing-and-terms)

---

## 1. Introduction

This module enables communication between **HUAWEI AppMessaging Kit** and React Native platform. It exposes all functionality provided by **HUAWEI AppMessaging Kit** which allows you to send relevant messages to target users actively using your app to encourage them to use key app functions. For example, you can send in-app messages to encourage users to subscribe to certain products, provide tips on passing a game level, or recommend activities of a restaurant.

App Messaging even allows you to customize how your messages look and the way they will be sent, and define events for triggering message sending to your users at the right moment.

---

## 2. Installation Guide

Before you get started, you must register as a HUAWEI Developer and complete identity verification on the [HUAWEI Developer](https://developer.huawei.com/consumer/en/) website. For details, please refer to [Register a HUAWEI ID](https://developer.huawei.com/consumer/en/doc/10104).

### Creating a Project in AppGallery Connect

Creating an app in AppGallery Connect is required in order to communicate with the Huawei services. To create an app, perform the following steps:

**Step 1.** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html)  and select **My projects**.

**Step 2.** Select your project from the project list or create a new one by clicking the **Add Project** button.

**Step 3.** Go to **Project Setting** > **General information**, and click **Add app**.
If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.

**Step 4.** On the **Add app** page, enter the app information, and click **OK**.

### Enabling App Messaging

You need to enable App Messaging before using it. If you have enabled it, skip this step.

**Step 1.** In [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html), click *My projects*.

**Step 2.** Find your project from the project list and click the app for which you need to enable App Messaging on the project card.

**Step 3:** Go to **Growing > App Messaging**. 

**Step 4:** Click **Enable now** in the upper right corner. 

**Step 5:**  The **App Messaging** service uses HUAWEI Analytics to report in-app message events. Therefore, you need to enable HUAWEI Analytics before integrating the App Messaging SDK. For details, please refer to [Enabling HUAWEI Analytics](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/service-enabling-0000001050745155).

For further details, please refer to [Enabling App Messaging](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-appmessage-getstarted#h1-1606373400682).

### Integrating the React-Native AppMessaging Plugin

Before using **@react-native-agconnect/appmessaging**, ensure that the ReactNative development environment has been installed.

### Install via NPM

```
npm i @react-native-agconnect/appmessaging
```

#### Android App Development

#### Integrating the React-Native AGC AppMessaging into the Android Studio

- Add the AppGallery Connect configuration file of the app to your Android Studio project.

    **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.
    
    **Step 2:** Find your **app** project and click the app.
    
    **Step 3:** Go to **Project Setting > App information**. In the **App information** area, download the **agconnect-services.json** file.
    
    **Step 4:** Copy the **agconnect-services.json** file to the app's root directory of your project.
    
- Open the **build.gradle** file in the **android** directory of your React Native project. Navigate into **buildscript**, configure the Maven repository address and agconnect plugin.

    ```groovy
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

- Go to **allprojects** then configure the Maven repository address.

  ```groovy
  allprojects {
      repositories {
          /*
           * <Other repositories>
           */
          maven { url 'https://developer.huawei.com/repo/' }
      }
  }
  ```

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
    
- By default, App Messaging SDK works in any one of portrait-primary, portrait-secondary, landscape-primary and landscape-secondary. To make your app's orientation work by default, **orientation** keyword should be removed from **android:configChanges** property of Activity tag in **AndroidManifest.xml** file. 

  Below is an example:


  ```xml
  <!-- AndroidManifest.xml. -->
  <activity
          android:name="com.huawei.agc.rn.appmessaging.example.MainActivity"
          android:label="@string/app_name"
          android:configChanges="keyboard|keyboardHidden|screenSize|uiMode"
          android:launchMode="singleTask"
          android:windowSoftInputMode="adjustResize">
      <intent-filter>
          <action android:name="android.intent.action.MAIN" />
          <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
  </activity>
  ```


#### iOS App Development

#### Integrating the React-Native AGC AppMessaging into the Xcode Project

- Navigate into your project directory and run below command.

    ```
    [project_path]> cd ios/ && pod install
    ```

- Add the AppGallery Connect configuration file of the app to your Xcode project.

    **Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.
    
    **Step 2:** Find your **app** project and click the app.
    
    **Step 3:** Go to **Project Setting > App information**. In the **App information** area, download the **agconnect-services.plist** file.
    
    **Step 4:** Copy the **agconnect-services.plist** file to the app's root directory of your Xcode project.
    
    Before obtaining the **agconnect-services.plist** file, ensure that you have enabled HUAWEI AppMessaging. For details, please refer to [Enabling App Messaging](#enabling-app-messaging).
    
    If you have made any changes on the Project Setting page, such as setting the data storage location and enabling or managing APIs, you need to download the latest **agconnect-services.plist** file and replace the existing file in the app's root directory.
    
- Initialize the AGCAppMessaging Plugin for React-Native.

    After the **agconnect-services.plist** file is imported successfully, initialize the AGCAppMessaging Plugin for React-Native using the config API in AppDelegate.
    
    
    Swift sample code for initialization in **AppDelegate.swift**:
    
    
    ```swift
  import AGConnectCore

  @UIApplicationMain
  class AppDelegate: UIResponder, UIApplicationDelegate {
    ...
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        ...
        // Initializate the AGCAppMessaging Plugin for React-Native in AppDelegate
        self.agcAppMessagingStartUp()
        ...
        return true
    }
    ```


    Objective-C sample code for initialization in **AppDelegate.m**:


    ```objc
    #import "AppDelegate.h"
    ...
    #import <AGConnectCore/AGConnectCore.h>


    @implementation AppDelegate

    - (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions
    {
        ...
        // Initializate the AGCAppMessaging Plugin for React-Native in AppDelegate
        [self agcAppMessagingStartUp];
    }
    }];
        return YES;
    }
    ...
    @end
    ```
    
    Objective-C sample code for initialization in **AppDelegate.h**:
    
    ```objc
    @interface AppDelegate : UIResponder <UIApplicationDelegate, RCTBridgeDelegate>
    ...
    - (void)agcAppMessagingStartUp;
    ...
    @end
    ```

- All the React-Native AGC AppMessaging plugin implementations are written in **swift**. 

    Make sure your project/ios **Xcode target -> Swift Compiler - General tab** includes **Objective-C Bridging Header** and **Objective-C Generated Interface Header Name** like below:

    <img src="../example/.docs/ios/_ObjC_Header.png"> 

---

## 3. API Reference

## Module Overview

| Module        | Description|
| ------------- | -------------------------------------------- |
| [AGCAppMessaging](#agcappmessaging)  | Provides methods to initialize AppMessaging Kit and implement appmessaging functions. |

## AGCAppMessaging

Represents the message processing class.

### Public Method Summary

| Method                                                 | Return Type        | Description                                                                                                                                                                                                                    |
| ------------------------------------------------------ | ------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| [setFetchMessageEnable(enabled)](#setfetchmessageenableenabled)                 | `Promise<boolean>`    | Sets whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server.                                                                                                             |
| [setDisplayEnable(enabled)](#setdisplayenableenabled)                      | `Promise<boolean>`    | Sets whether to allow the App Messaging SDK to display in-app messages.                                                                                                                                                        |
| [isDisplayEnable()](#isdisplayenable)                                      | `Promise<boolean>` | Checks whether the App Messaging SDK is allowed to display in-app messages.                                                                                                                                                    |
| [isFetchMessageEnable()](#isfetchmessageenable)                                 | `Promise<boolean>` | Checks whether the App Messaging SDK is allowed to synchronize in-app message data from the AppGallery Connect server.                                                                                                         |
| [setForceFetch()](#setforcefetch)                                        | `Promise<boolean>`    | Sets the forcible in-app message data obtaining flag. When the flag is enabled, you can obtain latest in-app message data from the AppGallery Connect server in real time. This method is only to support on Android Platform. |
| [removeCustomView()](#removecustomview)                                     | `Promise<boolean>`    | Remove custom in-app message layout. Then the default layout will be used.                                                                                                                                                     |
| [setDisplayLocation(object)](#setdisplaylocationlocation)                  | `Promise<boolean>`    | Sets the display position of a pop-up or image message.                                                                                                                                                                        
| [trigger(eventId)](#triggereventid)                                | `Promise<boolean>`    | Triggers a custom event.                                                                                                                                                                                                       |
| [handleCustomViewMessageEvent(object)](#handlecustomviewmessageeventobject)                 | `Promise<boolean>`    | While using custom app message layout, handle custom app message click events.                                                                                                                                                                        |
| [addMessageDisplayListener()](#addMessageDisplayListener)                 | `Promise<object>`    | While using default app message layout, listen for app message display events.                                                                                                                                                                        |
| [addMessageClickListener()](#addMessageClickListener)                 | `Promise<object>`    | While using default app message layout, listen for app message click events.                                                                                                                                                                      |
| [addMessageErrorListener()](#addMessageErrorListener)                 | `Promise<object>`    | While using default app message layout, listen for app message error events.                                                                                                                                                                        |
| [addMessageDismissListener()](#addMessageDismissListener)                 | `Promise<object>`    | While using default app message layout, listen for app message dismiss events.                                                                                                                                                                       |
| [addMessageCustomViewListener()](#addMessageCustomViewListener)                 | `Promise<object>`    | While using custom app message layout, listen for custom app message display events.                                                                                                                                                                      |

## Public Methods

### setFetchMessageEnable(enabled)

Sets whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server.

###### Parameters

| Name    | Type    | Description                                                                                                                                                                                                  |
| ------- | ------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| enabled | boolean | Indicates whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server. The options are as follows:<br>true: yes<br>false: no.<br>The default value is true. |

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<boolean>` | In the success scenario, Promise<boolean> instance, true, is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * Data synchronization from the AppGallery Connect server.
 */
AGCAppMessaging.setFetchMessageEnable(true).then(result => {
    Alert.alert("[setFetchMessageEnable] " + JSON.stringify(result));
}).catch((err) => {
    Alert.alert("[setFetchMessageEnable] Error/Exception: " + JSON.stringify(err));
});
```

### setDisplayEnable(enabled)

Sets whether to allow the App Messaging SDK to display in-app messages.

###### Parameters

| Name    | Type    | Description                                                                                                                                                     |
| ------- | ------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| enabled | boolean | Indicates whether to allow the App Messaging SDK to display in-app messages.The options are as follows:<br>true: yes<br>false: no<br>The default value is true. |

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<boolean>` | In the success scenario, Promise<boolean> instance, true, is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * Sets whether to allow the App Messaging SDK to display in-app messages.
 */
AGCAppMessaging.setDisplayEnable(false).then(result => {
    Alert.alert("[setDisplayEnable] " + JSON.stringify(result));
}).catch((err) => {
    Alert.alert("[setDisplayEnable] Error/Exception: " + JSON.stringify(err));
});

```

### isDisplayEnable()

Checks whether to allow the App Messaging SDK to display in-app messages.

###### Return Type

| Type               | Description                                                                                                                        |
| ------------------ | ---------------------------------------------------------------------------------------------------------------------------------- |
| `Promise<boolean>` | Indicates whether to allow the App Messaging SDK to display in-app messages. The options are as follows:<br>true: yes<br>false: no |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * Checks whether to allow the App Messaging SDK to display in-app messages.
 */
AGCAppMessaging.isDisplayEnable().then(result => {
    Alert.alert("[isDisplayEnable] " + JSON.stringify(result));
}).catch((err) => {
    Alert.alert("[isDisplayEnable] Error/Exception: " + JSON.stringify(err));
});
```

### isFetchMessageEnable()

Checks whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server.

###### Return Type

| Type               | Description                                                                                                                                                                   |
| ------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| `Promise<boolean>` | Indicates whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server. The options are as follows:<br>true: yes<br>false: no |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * Checks whether to allow the App Messaging SDK to synchronize in-app message data from the AppGallery Connect server.
 */
AGCAppMessaging.isFetchMessageEnable().then(result => {
    Alert.alert("[isFetchMessageEnable] " + JSON.stringify(result));
}).catch((err) => {
    Alert.alert("[isFetchMessageEnable] Error/Exception: " + JSON.stringify(err));
});
```

### setForceFetch()

Sets the flag for whether to obtain in-app message data from the AppGallery Connect server in real time by force.

After this method is called, the App Messaging SDK does not immediately request data from the AppGallery Connect server. When the next trigger event takes place, the App Messaging SDK forcibly obtains in-app message data from the AppGallery Connect server.

**If you're developing your app using iOS:** Enable the debugging mode for your app. Add the '-AGConnectDeveloperMode' launch parameter to the app scheme. Start your app in Xcode debug mode.
Notice:

- The setForceFetch API can be used only for message testing.
- The forcible data obtaining flag is bound to the AAID of the test device. After you uninstall and reinstall the app or clear app data on the device, the flag will be reset.

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<boolean>` | In the success scenario, Promise<boolean> instance, true, is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * Sets the flag for whether to obtain in-app message data from the AppGallery Connect server in real time by force.
 */
AGCAppMessaging.setForceFetch().then(result => {
    Alert.alert("[setForceFetch] " + JSON.stringify(result));
}).catch((err) => {
    Alert.alert("[setForceFetch] Error/Exception: " + JSON.stringify(err));
});
```

### removeCustomView()

In Android platforms, call removeCustomView() function to remove custom in-app message layout, then the default layout will be used. 

**If you're developing your app using iOS:**  refer to your project/Info.plist file to whether remove or set false **AGCCustomView** parameter.

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<boolean>` | In the success scenario, Promise<boolean> instance, true, is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * Remove custom in-app message layout. Then the default layout will be used.
 * In iOS, Refer to your project/Info.plist file to whether remove or set false AGCCustomView parameter.
 */
AGCAppMessaging.removeCustomView().then(result => {
    Alert.alert("[removeCustomView] " + JSON.stringify(result));
}).catch((err) => {
    Alert.alert("[removeCustomView] Error/Exception: " + JSON.stringify(err));
});
```

### setDisplayLocation(location)

Sets the display position of a pop-up or image message.

###### Parameters

| Name     | Type     | Description        |
| -------- | -------- | ------------------ |
| location | AGCAppMessaging Constant | AGCAppMessaging Location Type Constant. |

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<boolean>` | In the success scenario, Promise<boolean> instance, true, is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * Sets display location of appMessage whether at the bottom or the center.
 */
const bottomType = AGCAppMessaging.LOCATION_TYPE_CENTER;
AGCAppMessaging.setDisplayLocation(bottomType).then(result => {
    Alert.alert("[setDisplayLocation] " + JSON.stringify(result));
}).catch((err) => {
    Alert.alert("[setDisplayLocation] Error/Exception: " + JSON.stringify(err));
});
```

### trigger(eventId)

Triggers a custom event.

###### Parameters

| Name    | Type   | Description           |
| ------- | ------ | --------------------- |
| eventId | string | ID of a custom event. |

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<boolean>` | In the success scenario, Promise<boolean> instance, true, is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * Triggers message display.
 */
AGCAppMessaging.trigger("SOME_EVENT_ID").then(result => {
    Alert.alert("[trigger] " + JSON.stringify(result));
}).catch((err) => {
    Alert.alert("[trigger] Error/Exception: " + JSON.stringify(err));
});
```

### addMessageCustomViewListener()

While using custom app message layout, listen for custom app message display events.

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<object>` | In the success scenario, Promise<object> instance with app message detail info is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * While using custom app message layout, listen for custom app message display events.
 */
addMessageCustomViewListener = () => {
    AGCAppMessaging.getInstance().addMessageCustomViewListener((result) => {
        Alert.alert(AGCAppMessaging.EventTypes.CUSTOM_VIEW + JSON.stringify(result))
        /*
         * Add Custom Message Event Handler method.
         * When using custom app message layout, handle custom app message click events like below.
         * gets eventType ( AGCAppMessaging.ON_MESSAGE_DISPLAY, AGCAppMessaging.ON_MESSAGE_CLICK, AGCAppMessaging.ON_MESSAGE_DISMISS, AGCAppMessaging.ON_MESSAGE_ERROR)
         * and dismissType param( AGCAppMessaging.DISMISS_TYPE_CLICK, AGCAppMessaging.DISMISS_TYPE_CLICK_OUTSIDE, AGCAppMessaging.DISMISS_TYPE_AUTO, AGCAppMessaging.DISMISS_TYPE_SWIPE) when using AGCAppMessaging.ON_MESSAGE_DISMISS.
         */
        this.handleCustomViewMessageEvent();
    });
}
```

### handleCustomViewMessageEvent(object)

While using custom app message layout, handle custom app message click events. 

###### Parameters

| Name    | Type   | Description           |
| ------- | ------ | --------------------- |
| object | object | Custom message event request. |

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<boolean>` | In the success scenario, Promise<boolean> instance, true, is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

//Below example creates alert, once the customView event is triggered sends a message display event.
/**
 * Creating app and listening all the events.
 */
componentDidMount() {
    //Creating & Calling Event Listeners
    this.addMessageCustomViewListener();
}
/**
 * While using custom app message layout, listen for custom app message display events.
 */
addMessageCustomViewListener = () => {
    AGCAppMessaging.getInstance().addMessageCustomViewListener((result) => {
        Alert.alert(AGCAppMessaging.EventTypes.CUSTOM_VIEW + JSON.stringify(result))
        /*
         * Add Custom Message Event Handler method.
         * When using custom app message layout, handle custom app message click events like below.
         * gets eventType ( AGCAppMessaging.ON_MESSAGE_DISPLAY, AGCAppMessaging.ON_MESSAGE_CLICK, AGCAppMessaging.ON_MESSAGE_DISMISS, AGCAppMessaging.ON_MESSAGE_ERROR)
         * and dismissType param( AGCAppMessaging.DISMISS_TYPE_CLICK, AGCAppMessaging.DISMISS_TYPE_CLICK_OUTSIDE, AGCAppMessaging.DISMISS_TYPE_AUTO, AGCAppMessaging.DISMISS_TYPE_SWIPE) when using AGCAppMessaging.ON_MESSAGE_DISMISS.
         */
        this.handleCustomViewMessageEvent();
    });
}

/**
 * When using custom app message layout, handle custom app message click events like below.
 * gets eventType ( AGCAppMessaging.ON_MESSAGE_DISPLAY, AGCAppMessaging.ON_MESSAGE_CLICK, AGCAppMessaging.ON_MESSAGE_DISMISS, AGCAppMessaging.ON_MESSAGE_ERROR)
 * and dismissType param( AGCAppMessaging.DISMISS_TYPE_CLICK, AGCAppMessaging.DISMISS_TYPE_CLICK_OUTSIDE, AGCAppMessaging.DISMISS_TYPE_AUTO, AGCAppMessaging.DISMISS_TYPE_SWIPE) when using AGCAppMessaging.ON_MESSAGE_DISMISS.
 */
handleCustomViewMessageEvent() {
    const customMessageDisplayReq = {
        "eventType" : AGCAppMessaging.ON_MESSAGE_DISPLAY
    }

    AGCAppMessaging.handleCustomViewMessageEvent(customMessageDisplayReq).then(result => {
        Alert.alert("[handleCustomViewMessageEvent] " + JSON.stringify(result));
        this.createCustomView("handleCustomViewMessageEvent :  ", JSON.stringify(result) + "")
    }).catch((err) => {
        Alert.alert("[handleCustomViewMessageEvent] Error/Exception: " + JSON.stringify(err));
        this.createCustomView("[handleCustomViewMessageEvent] Error/Exception: ", JSON.stringify(err) + "")
    });
}

```

### addMessageDisplayListener()

While using default app message layout, listen for app message display events.

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<object>` | In the success scenario, Promise<object> instance with app message detail info is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * While using default app message layout, listen for app message display events.
 */
addMessageDisplayListener = () => {
    AGCAppMessaging.getInstance().addMessageDisplayListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_DISPLAY + JSON.stringify(result)));
}
```

### addMessageClickListener()

While using default app message layout, listen for app message click events.

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<object>` | In the success scenario, Promise<object> instance with app message detail info is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * While using default app message layout, listen for app message click events.
 */
addMessageClickListener = () => {
    AGCAppMessaging.getInstance().addMessageClickListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_CLICK + JSON.stringify(result)));
}
```

### addMessageErrorListener()

While using default app message layout, listen for app message error events.

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<object>` | In the success scenario, Promise<object> instance with app message detail info is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 *  While using default app message layout, listen for app message error events.
 */
addMessageErrorListener = () => {
    AGCAppMessaging.getInstance().addMessageErrorListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_ERROR + JSON.stringify(result)));
}
```

### addMessageDismissListener()

While using default app message layout, listen for app message dismiss events.

###### Return Type

| Type            | Description    |
| --------------- | -------------- |
| `Promise<object>` | In the success scenario, Promise<object> instance with app message detail info is returned. |

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * While using default app message layout, listen for app message dismiss events.
 */
addMessageDismissListener = () => {
    AGCAppMessaging.getInstance().addMessageDismissListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_DISMISS + JSON.stringify(result)));
}
```

## Constants

### EventTypes

- When using custom app message layout, handle custom app message display, click, dismiss and error events.

  | Field | Type   | Description                                                                                  |
  | ----- | ------ | -------------------------------------------------------------------------------------------- |
  | ON_MESSAGE_DISPLAY | string | When using custom app message layout, handle custom app message display events. |
  | ON_MESSAGE_CLICK  | string | When using custom app message layout, handle custom app message click events.                        |
  | ON_MESSAGE_DISMISS  | string | When using custom app message layout, handle custom app message dismiss events.                        |
  | ON_MESSAGE_ERROR  | string | When using custom app message layout, handle custom app message error events.                       |
  | CUSTOM_VIEW  | string |    When using custom app message layout, handle custom app message view events.                   |

### DismissTypes

- While using AGCAppMessaging.DismissTypes.ON_MESSAGE_DISMISS in handling custom app messages, add dismissType param via below constants.

  | Field | Type   | Description                                                                                  |
  | ----- | ------ | -------------------------------------------------------------------------------------------- |
  | DISMISS_TYPE_CLICK | string | Click event in dismiss. |
  | DISMISS_TYPE_CLICK_OUTSIDE  | string | Click outside event in dismiss.  |
  | DISMISS_TYPE_AUTO  | string | Auto event in dismiss.   |
  | DISMISS_TYPE_SWIPE  | string | Click event in dismiss.  |

### LocationTypes

- Sets display location of appMessage whether at the bottom or the center.

  | Field       | Type   | Description                                                                        |
  | ----------- | ------ | ---------------------------------------------------------------------------------- |
  | LOCATION_TYPE_BOTTOM    | string | Sets display location of appMessage at the bottom.                                    |
  | LOCATION_TYPE_CENTER | string | Sets display location of appMessage at the center. |

## 4. Configuration and Description

[Enabling App Messaging](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-appmessage-getstarted#h1-1606373400682)

### Accessing Analytics Kit

To use analytics feature, 

- Navigate into your /android/app/build.gradle and add build dependencies in the dependencies section.
   
    ```
    dependencies {
        implementation 'com.huawei.hms:hianalytics:5.1.0.301'
    }
    ```
- Navigate into your /ios file and edit the Podfile file to add the pod dependency 'HiAnalytics'
    
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
 
        @interface AppDelegate ()
 
        @end
 
        @implementation AppDelegate
        ...
        // Customize the service logic after app launch.
        - (BOOL)Application:(UIApplication *)Application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
        // Initialize the Analytics SDK.
        [HiAnalytics config];   
         return YES;
        }
        ...
        @end
        ```
    
    For further information please refer to [Analytics Kit Service Guide](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050745149).
    

### Configuring Obfuscation Scripts

Before building the APK, configure obfuscation scripts to prevent the AppGallery Connect SDK from being obfuscated. If obfuscation arises, the AppGallery Connect SDK may not function properly.

```
-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keep class com.hianalytics.android.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}
-keep class com.huawei.agc.**{*;}
-keep class com.huawei.agconnect.**{*;}
-repackageclasses
```
### Listening for AppMessaging Events

Add a listener to obtain app message click, view, dismiss, error events. 

###### Call Example

```js
import AGCAppMessaging from '@react-native-agconnect/appmessaging';
import {Alert} from 'react-native';

/**
 * Creating app and listening all the events.
 */
componentDidMount() {
    //Creating & Calling Event Listeners
    this.addMessageDisplayListener();
    this.addMessageClickListener();
    this.addMessageErrorListener();
    this.addMessageDismissListener();
}

addMessageDisplayListener = () => {
    AGCAppMessaging.getInstance().addMessageDisplayListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_DISPLAY + JSON.stringify(result)));
}

addMessageClickListener = () => {
    AGCAppMessaging.getInstance().addMessageClickListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_CLICK + JSON.stringify(result)));
}

addMessageErrorListener = () => {
    AGCAppMessaging.getInstance().addMessageErrorListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_ERROR + JSON.stringify(result)));
}

addMessageDismissListener = () => {
    AGCAppMessaging.getInstance().addMessageDismissListener((result) => Alert.alert(AGCAppMessaging.EventTypes.ON_MESSAGE_DISMISS + JSON.stringify(result)));
}

```

### Adding Custom View

App Messaging supports three message types, namely pop-up, image, and banner messages. The App Messaging SDK provides a default layout for each type. You can customize the message display style that better suits your app to provide personalized experience for users.

Firstly, add a listener to obtain app message. Then you can create your own layout for app message. 

You can add custom App message events as well. Such as: message click, message display, message dismiss, message error.

Below example creates alert, once the customView event is triggered sends a message display event.

###### Call Example

```js
/**
 * Creating app and listening all the events.
 */
componentDidMount() {
    //Creating & Calling Event Listeners
    this.addMessageCustomViewListener();
}

addMessageCustomViewListener = () => {
    AGCAppMessaging.getInstance().addMessageCustomViewListener((result) => {
        Alert.alert(AGCAppMessaging.EventTypes.CUSTOM_VIEW + JSON.stringify(result))
        /*
         * Add Custom Message Event Handler method.
         * When using custom app message layout, handle custom app message click events like below.
         * gets eventType ( AGCAppMessaging.ON_MESSAGE_DISPLAY, AGCAppMessaging.ON_MESSAGE_CLICK, AGCAppMessaging.ON_MESSAGE_DISMISS, AGCAppMessaging.ON_MESSAGE_ERROR)
         * and dismissType param( AGCAppMessaging.DISMISS_TYPE_CLICK, AGCAppMessaging.DISMISS_TYPE_CLICK_OUTSIDE, AGCAppMessaging.DISMISS_TYPE_AUTO, AGCAppMessaging.DISMISS_TYPE_SWIPE) when using AGCAppMessaging.ON_MESSAGE_DISMISS.
         */
        this.handleCustomViewMessageEvent();
    });
}

/**
 * When using custom app message layout, handle custom app message click events like below.
 * gets eventType ( AGCAppMessaging.ON_MESSAGE_DISPLAY, AGCAppMessaging.ON_MESSAGE_CLICK, AGCAppMessaging.ON_MESSAGE_DISMISS, AGCAppMessaging.ON_MESSAGE_ERROR)
 * and dismissType param( AGCAppMessaging.DISMISS_TYPE_CLICK, AGCAppMessaging.DISMISS_TYPE_CLICK_OUTSIDE, AGCAppMessaging.DISMISS_TYPE_AUTO, AGCAppMessaging.DISMISS_TYPE_SWIPE) when using AGCAppMessaging.ON_MESSAGE_DISMISS.
 */
handleCustomViewMessageEvent() {
    const customMessageDisplayReq = {
        "eventType" : AGCAppMessaging.ON_MESSAGE_DISPLAY
    }

    AGCAppMessaging.handleCustomViewMessageEvent(customMessageDisplayReq).then(result => {
        Alert.alert("[handleCustomViewMessageEvent] " + JSON.stringify(result));
        this.createCustomView("handleCustomViewMessageEvent :  ", JSON.stringify(result) + "")
    }).catch((err) => {
        Alert.alert("[handleCustomViewMessageEvent] Error/Exception: " + JSON.stringify(err));
        this.createCustomView("[handleCustomViewMessageEvent] Error/Exception: ", JSON.stringify(err) + "")
    });
}

```

Below lines should be added to the corresponding platforms.

#### IOS:

Navigate into your React-Native/iOS project **info.plist** file,

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

Before adding custom view make sure you have linked `@react-native-agconnect/appmessaging` in your project. 
Then, you should add below code to the onCreate method of your React-Native project /Android/MainApplication.java.

```java
AgcAppMessagingModule.addCustomView();
```


---

## 5. Sample Project

This plugin includes a demo project in the [example](example) folder, there you can find more usage examples.

---

## 6. Licensing and Terms

AGC React-Native Plugin is licensed under [Apache 2.0 license](LICENCE)

