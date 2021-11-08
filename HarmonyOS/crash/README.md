# Crash quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/HarmonyOS/crash/README_ZH.md)


## Contents

- [Crash quickstart](#crash-quickstart)
  - [Contents](#contents)
  - [Introduction](#introduction)
  - [Environment Requirements](#environment-requirements)
  - [Getting Started](#getting-started)
  - [Sample Code](#sample-code)
  - [Technical Support](#technical-support)
  - [License](#license)


## Introduction

The Crash service is a lightweight crash analysis service which can be quickly integrated into your app without the need for coding. It helps you understand the version quality of your app, quickly locate the causes of crashes, and evaluate the impact scope of crashes.


## Environment Requirements

* A computer with HUAWEI DevEco installed for app development
* A device or emulator running HarmonyOS

## Getting Started

Before running the Crash quickstart app you need to:
1. If you do not have a Huawei developer account, [register](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) one and make it pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/service/josp/agc/index.html#/), create an app, and set Package type to APP (HarmonyOS app).
3. Select your project and app in My projects, and go to Quality > Crash to enable the Crash service. (The Crash service integrates Analytics Kit for crash event reporting. As a result, you need to enable HUAWEI Analysis Kit before integrating the Crash SDK.)
4. Download the agconnect-services.json file from AppGallery Connect and copy it to the entry directory.

## Sample Code

The Crash SDK supports functions such as crash simulation and exception simulation.

Sample code: src\main\java\com\huawei\crash\slice\MainAbilitySlice.java

## Technical Support

If you have any questions about the sample code, try the following:
- [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect) is a place for any programming questions. Be sure to tag your question with appgallery.
- [Huawei Developer Forum](https://developer.huawei.com/consumer/en/forum/blockdisplay?fid=18) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License

Crash quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0)
