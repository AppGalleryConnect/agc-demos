## Agconnect Crash Service Xamarin Android Plugin - Demo

##  Introduction

This demo project is an example to demonstrate the features of the Agconnect Crash Service Xamarin Android Plugin.

<img src="../.docs/mainPageAndroid.jpg" width = 40% height = 40% style="margin:1.5em">

## Installation

In the Solution Explorer panel, right click on the solution name and select Manage NuGet Packages. Search for [Huawei.Agconnect.AgconnectCrash](https://www.nuget.org/packages/Huawei.Agconnect.AgconnectCrash) and install the package into your Xamarin.Android projects.

### Place your agconnect-services.json file inside the project

**Step 1:** Sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html) and select your project from **My Projects**. 
Then go to **Project Settings** tab. On the page that is displayed, click `agconnect-services.json` button.

**Step 2:** Once you download your `agconnect-services.json` file, place it under the **Assets** folder of the demo project.

**Step 3:** Package name in the `agconnect-services.json` and the package name in the **AndroidManifest.xml** file should be same

### Environment Setting

- Android 4.2 JellyBean (API level 17) and later versions

- A minimum version of Visual Studio 2019 16.3 or Visual Studio for Mac 2019 8.3 are required to build and compile

## Configuration

### Android Manifest Merging

In AGConnect Services, we were seeing AndroidManifest.xml files from multiple .aar's which contained ```<application><service android:name><metadata ... /></service></application>``` elements where the service name was the same in different files but each contain their own metadata elements. The ending result is need to be a single service element with all the metadata elements from each aar's manifest file. Otherwise AGConnect Services cannot work properly.

Xamarin.Android includes an option to use the same Android manifest merger tool that Android Studio uses to merge AndroidManifest.xml files. 

To enable this for your project, set the $(AndroidManifestMerger) MSBuild property to manifestmerger.jar in the .csproj file:

```xml
<PropertyGroup>
  <AndroidManifestMerger>manifestmerger.jar</AndroidManifestMerger>
</PropertyGroup>
```

So when building your app, the  **AndroidManifestMerger** property  merges all manifest files into a single manifest file that's packaged into your APK.

### Testing a Crash Service

Generally, there is a low probability that an app crashes. Therefore, you are not advised to test the Crash service with a real crash. You can call the API of the Crash SDK to intentionally trigger a crash during app test and view the crash data in AppGallery Connect to check whether the Crash service is running properly.

#### Before You Start

**Step 1:** Enable the Crash service in AppGallery Connect.

**Step 2:** Integrate the Crash Plug-in into your app.

#### Testing Steps

**Step 1:** Create a button in an activity of your app. 

**Step 2:** Add the code for calling **AGConnectCrash.Instance** to initialize the **AGConnectCrash** instance after the button is tapped.

**Step 3.:** Add the code for calling the **AGConnectCrash.Instance.TestIt** method to trigger a crash.

**Step 4.:** Run the application in Release mode.

```csharp
AGConnectCrash.Instance.TestIt(this);//In your activity.
```

When a crash occurs, your app will attempt to report the crash. Ensure that the network connection is normal. View crash information in AppGallery Connect.

## Licensing and Terms

Agconnect Crash Service Xamarin Android Plugin - Demo is licensed under [Apache 2.0 license](LICENSE)