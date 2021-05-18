## AGC App Messaging Xamarin iOS Plugin - Demo

##  Introduction

This demo project is an example to demonstrate the features of the AGC App Messaging Xamarin iOS Plugin.

<img src="../.docs/mainPageiOS.png" width = 40% height = 40% style="margin:1.5em">


## Installation 

In the Solution Explorer panel, right click on the solution name and select Manage NuGet Packages. Search for [Huawei.Agconnect.iOS.Appmessaging](https://www.nuget.org/packages/Huawei.Agconnect.iOS.Appmessaging) and install the package into your Xamarin.Android projects.


### Environment Setting

Before using the Xamarin SDK code, ensure that the below environments are set:

 - Visual Studio for Mac or Visual Studio - The latest version of Visual Studio for Mac or Visual Studio should be installed and configured on the development computer. 
  - A Mac with macOS High Sierra 10.13 or above required for developing a Xamarin.iOS application, and when using Visual Studio you must be connected to a Xamarin.iOS build host.
  - Xcode and iOS SDK - Apple's Xcode and the latest iOS API need to be installed and configured on the developer's computer. The latest stable version is usually recommended.
 - An Apple ID. If you don't have an Apple ID already you can create a new one at https://appleid.apple.com. It's necessary to have an Apple ID for installing and signing into Xcode.

### Enabling App Messaging 

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select your project from **My Projects**.  Then go to **Grow > App Messaging** tab. On the page that is displayed, click Enable now in the upper right corner.

**Step 2:** The App Messaging service uses HUAWEI Analytics to report in-app message events. Therefore, you need to enable HUAWEI Analytics before integrating the App Messaging SDK. For details, please refer to [Enabling HUAWEI Analytics](#https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/service-enabling-0000001050745155). 


### Place your agconnect-services.plist file inside the project

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select your project from **My Projects**. 
Then go to **Project Settings** tab. On the page that is displayed, click `agconnect-services.plist` button.

**Step 2:** Once you download your `agconnect-services.plist` file, place it under the root of the demo project. And set the build action as **"BundleResource"**.

**Step 3:** Bundle identifier in the `agconnect-services.plist` and the  **Info.plist** file should be the same.



## 3. Configuration

### Configuring the Free Provisioning Profile

To use free provisioning to deploy an app to a device and to use specific entitlements follow these steps:

**Step 1.** Use Xcode to create the necessary signing identity (developer certificate and private key) and provisioning profile (containing an explicit App ID and the UDID of a connected iOS device).

**Step 2.** Use the signing identity and provisioning profile created by Xcode in Visual Studio for Mac or Visual Studio 2019 to deploy your Xamarin.iOS application.

**Step 2.** Open the Properties of the demo application. Select **iOS Bundle Signing** and select your profile.

### Set Keychain Access in the Entitlements.plist

You need to enable the Keychain Sharing capability in Visual Studio or App Messaging failed to send reports to the App Gallery Console. 

**Step 1:** Click and open the **Entitlements.plist** file. 

**Step 2:** Select the Entitlements tab below and check **Enable Keychain** in the Keychain section.

**Note:** A free or paid provisioning profile is required to use entitlements. 


### Testing an In-App Message

In-app messages can only be displayed to users who have installed your officially released app version. App Messaging allows you to test an in-app message when your app is still under tests. 

The testing process is as follows:

**Step 1:** Obtain the anonymous application identifier (AAID) of the test device. 

**Step 2:** Sign in to [AppGallery Connect](#https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and click **My projects**.

**Step 3.:** Go to **Growing > App Messaging > Management**, find the message that you need to test, and click Test in the Operation column.

**Step 4.:** Click **Add test user** on the **Test app message** page and enter the AAID of the test device in the text box.

**Step 5.:** Click **Save** to set the in-app message to a test message and save the device as a test device.

**Step 6.:** Enable in app message testing for your app.

* Visual Studio for Mac
  * Open your project settings
  * Go to Run/Configuration and select your desired configuration
  * In **Extra mlaunch Arguments**, type the command line showed below.

* Visual Studio for Windows
  * Open your project settings
  * Go **iOS Run Options**
  * In **Extra mlaunch Arguments**, type the command line showed below.

```
--argument=-AGConnectDeveloperMode
```
Now you can test whether the message can be properly displayed and allow correct interactions.

### Run & debug your application

You can now run your application and it should automatically start up on your mobile device.


## 4. Licensing and Terms

AGC App Messaging Xamarin iOS Plugin - Demo uses the Apache 2.0 license.