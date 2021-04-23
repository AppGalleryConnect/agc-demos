# remoteconfig quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/Android/Remote%20Configuration/README_ZH.md)

## Table of Contents

 * [Introduction](#introduction)
 * [Preparing the Environments](#preparing-the-environments)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-Code)
 * [Result](#result)
 * [Question or issues](#question-or-issues)
 * [License](#license)

## Introduction

Remote Configuration allows you to manage parameters online. With the service, you can change the behavior and appearance of your app online without requiring users to update the app. Remote Configuration provides cloud-based services, the console, and the client SDK. By integrating the client SDK, your app can periodically obtain parameter values delivered on the console to modify the app's behavior and appearance.

## Preparing the Environment

* A computer with Android Studio installed for app development
* A device or emulator in Android Studio running Android 4.2 or a later version

## Getting Started

Before running the quickstart app, you need to:
1. If you do not have a HUAWEI Developer account, you need to [register an account](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-get-started), create an app, and set Package type to APK (Android app).
3. Enable Remote Configuration. (For details, please refer to the development guide.)
4. Select Project Settings,download the agconnect-services.json file from AppGallery Connect and copy the agconnect-services.json file to the app root directory.

## Sample Code

The fetch API is called before app startup, and data is used after the fetch operation is successful.
    src\main\java\com\huawei\agc\quickstart\Test1Activity.java

Data fetched by the calling the fetch API is applied upon the next app startup.
    src\main\java\com\huawei\agc\quickstart\Test2Activity.java

## Result

**Click 'FETCH DATA AND APPLY'</br>
<img src="images/fetch and apply.gif" alt="resultpage" height="600"/>

**Click 'FETCH DATA AND APPLY NEXT STARTUP'</br>

<img src="images/fetch and apply next start.gif" alt="resultpage" height="600"/>

## Question or issues

If you have questions about how to use AppGallery Connect Demos, try the following options:  

* [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect) is the best place for any programming questions. Be sure to tag your question with appgallery.  
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License

Remote Configuration quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
