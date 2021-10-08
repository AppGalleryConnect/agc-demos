# App Linking quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/HarmonyOS/applinking/README_ZH.md)

## Contents

- [App Linking quickstart](#app-linking-quickstart)
  - [Contents](#contents)
  - [Introduction](#introduction)
  - [Environment Requirements](#environment-requirements)
  - [Getting Started](#getting-started)
  - [Sample Code](#sample-code)
  - [Sample Effect](#sample-effect)
  - [Technical Support](#technical-support)
  - [License](#license)

## Introduction

App Linking allows links to work across platforms even on devices where your app is not installed. You can use these links to direct users to promotional information or native app content that they can share with others. You can create links of App Linking and send them to users, or allow users to share links dynamically generated in your app. Anyone who receives a link can tap it to access the linked content.

## Environment Requirements

* A computer with HUAWEI DevEco installed for app development
* A device or emulator running HarmonyOS 2.0 or later

## Getting Started

Before running the App Linking quickstart app, you need to:
1. If you do not have a HUAWEI Developer account, [register](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) one and make it pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/), create an app, and set Package type to APP (HarmonyOS app).
3. Enable App Linking. For details, please refer to the App Linking development guide.
4. Download the agconnect-services.json file from AppGallery Connect and copy it to the entry directory.
5. Add a URL prefix when you create a link of App Linking. Copy the prefix and use it to replace the value of DOMAIN_URI_PREFIX in MainAbilitySlice.

## Sample Code

Process the received links in the main entry of the app.
Sample code: src\main\java\com\huawei\agc\quickstart\MainActivitySlice.java

## Sample Effect

## Technical Support

If you have any questions about the sample code, try the following:

- [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect) is a place for any programming questions. Be sure to tag your question with appgallery.
- [Huawei Developer Forum](https://developer.huawei.com/consumer/en/forum/blockdisplay?fid=18) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License

App Linking quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
