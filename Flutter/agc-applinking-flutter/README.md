# AppGallery Connect Applinking Kit Flutter Plugin - Demo

---

## Contents

  - [Introduction](#1-introduction)
  - [Installation](#2-installation)
  - [Configuration](#3-configuration)
  - [Licensing and Terms](#4-licensing-and-terms)

---

## 1. Introduction

This demo project is an example to demonstrate the features of the **AppGallery Connect Flutter Applinking Kit** Plugin.

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
      applicationId "<Enter_your_package_name_here>" // For ex: "com.developer.applinking"
      minSdkVersion 19
      <!-- Other configurations ... -->
  }
  ```
**Step 2.** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html)  and select **My projects**.

**Step 3.** Select your project from the project list or create a new one by clicking the **Add Project** button.

**Step 4.** Go to **Project Setting** > **General information**, and click **Add app**.
If an app exists in the project and you need to add a new one, expand the app selection area on the top of the page and click **Add app**.

**Step 5.** On the **Add app** page, enter the **Application ID** you've defined before as the **Package Name** here, then fill the necessary fields and click **OK**.

### Configuring the Signing Certificate Fingerprint

Before building the APK, configure obfuscation scripts to prevent th AppGallery Connect SDK from being obfuscated. If
 obfuscation arises, the AppGalley Connect SDK may not function properly. You must locally generate a signing certificate fingerprint and configure it in the **AppGallery Connect**. You can refer to 3rd and 4th steps of [Generating a Signing Certificate](https://developer.huawei.com/consumer/en/codelab/HMSPreparation/index.html#2) codelab tutorial for the certificate generation. Perform the following steps after you have generated the certificate.

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select your project from **My Projects**. Then go to **Project Setting** > **General information**. In the **App information** field, click the add icon next to SHA-256 certificate fingerprint, and enter the obtained **SHA-256 certificate fingerprint**.

**Step 2:**  After completing the configuration, click **OK** (Check mark icon) to save the changes.

**Step 3:** Create a file named **key.properties** under the **example/android/** folder and enter the properties of the key you generated on the earlier step to the file as shown below: 

```
storeFile file('<keystore_file>') for example: D:\\Users\\<user_name>\\key.jks>
storePassword '<keystore_password>'
keyAlias '<key_alias>'
keyPassword '<key_password>' 
```
> Warning: Keep this file private and don't include it on the public source control.

### Enabling the AppGallery Connect AppLinking Service 

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select your project from **My Projects**. Then go to the **Manage APIs** tab on the **Project Settings** page to check if the AppLinking service is enabled.

**Step 2:** Go to **Project Setting > General information** page, under the **App information** field, click **agconnect-services.json** to download the configuration file.

**Step 3:** Copy the **agconnect-services.json** file to the **example/android/app/** directory of the project. 

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


[Enabling App Linking](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-applinking-getStarted-ios)

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

        Sample code for initialization in AppDelegate:
    
  ```
	import UIKit
	import Flutter
    import HiAnalytics

	@UIApplicationMain
	@objc class AppDelegate: FlutterAppDelegate {
    	    override func application(
        	_ application: UIApplication,
        	didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?
    	    ) -> Bool {
          // Initialize the Analytics SDK.
          HiAnalytics.config();  

        	GeneratedPluginRegistrant.register(with: self)
        	return super.application(application, didFinishLaunchingWithOptions: launchOptions)
    	}
    }
  ```
    
    For further information please refer to [Analytics Kit Service Guide](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/introduction-0000001050745149).
    
---

## 4. Licensing and Terms

AppGallery Connect Kit Flutter Plugin Demo is licensed under [Apache 2.0 license](../LICENSE)
