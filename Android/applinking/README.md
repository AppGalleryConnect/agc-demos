## AppLinking quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/Android/applinking/README_ZH.md)

## Table of Contents

 * [Introduction](#Introduction)
 * [Preparing the Environments](#preparing-the-environments)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-Code)
 * [Result](#result)
 * [Question or issues](#question-or-issues)
 * [License](#license)

## Introduction
App Linking allows you to create cross-platform links that can work as defined regardless of whether your app has been installed by a user. When a user taps the link on an Android or iOS device, the user will be redirected to the specified in-app content. If a user taps the link in a browser, the user will be redirected to the same content of the web version.

## Preparing the Environments
* A computer with Android Studio installed for app development
* A device or emulator in Android Studio running Android 4.2 or a later version 

## Getting Started

Before running the quickstart app, you need to:

1. If you do not have a HUAWEI Developer account, you need to [register an account](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-get-started), create an app, and set Package type to APK (Android app).
3. Enable AppLinking. (For details, please refer to the development guide.)
4. Select Project Settings,download the agconnect-services.json file from AppGallery Connect and copy the agconnect-services.json file to the app root directory.
5. Create a link prefix in AppLinking and copy and replace DOMAIN_URI_PREFIX in MainActivity.

## Sample Code

Main entry of the app, which processes received links.
Sample code: src\main\java\com\huawei\agc\quickstart\MainActivity.java

Redirection to the target page when a deep link is processed in the app.
Sample code: src\main\java\com\huawei\agc\quickstart\DetailActivity.java

## Result


## Question or issues
If you have questions about how to use AppGallery Connect Demos, try the following options:  
* [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect) is the best place for any programming questions. Be sure to tag your question with appgallery.  
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License
quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
