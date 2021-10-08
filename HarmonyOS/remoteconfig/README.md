# Remote Configuration quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/HarmonyOS/remoteconfig/README_ZH.md)


## Contents

- [Remote Configuration quickstart](#remote-configuration-quickstart)
  - [Contents](#contents)
  - [Introduction](#introduction)
  - [Environment Requirements](#environment-requirements)
  - [Getting Started](#getting-started)
  - [Sample Code](#sample-code)
  - [Technical Support](#technical-support)
  - [License](#license)

## Introduction

AppGallery Connect Remote Configuration allows you to manage parameters online. With the service, you can change the behavior and appearance of your app online without requiring users to update the app. Remote Configuration provides cloud-based services, the console, and the client SDK. By integrating the client SDK, your app can periodically fetch parameter values delivered on the console to modify the app's behavior and appearance.

## Environment Requirements

* A computer with HUAWEI DevEco installed for app development
* A device or emulator running HarmonyOS

## Getting Started

Before running the Remote Configuration quickstart app you need to:
1. If you do not have a HUAWEI Developer account, [register](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) one and make it pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/), create an app, and set Package type to APP (HarmonyOS app).
3. Enable Remote Configuration. For details, please refer to the Remote Configuration development guide.
4. Download the agconnect-services.json file from AppGallery Connect and copy it to the entry directory.

## Sample Code

Call the fetch API to fetch values before every app launch. Use the fetched values after the call is successful.
entry\src\main\java\com\huawei\quickstart\remoteconfig\slice\Test1Activity.java

The fetched values take effect next time the app is launched.
entry\src\main\java\com\huawei\quickstart\remoteconfig\slice\Test2Activity.java

## Technical Support

If you have any questions about the sample code, try the following:

- [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect) is a place for any programming questions. Be sure to tag your question with appgallery.
- [Huawei Developer Forum](https://developer.huawei.com/consumer/en/forum/blockdisplay?fid=18) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License

Remote Configuration quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
