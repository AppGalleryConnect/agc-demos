# Dynamic Ability Demo

## Table of Contents

 * [Introduction](#introduction)
 * [Preparing the Environments](#preparing-the-environments)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-code)
 * [Result](#result)
 * [Questions or Issues](#questions-or-issues)
 * [License](#license)

## Introduction
The Dynamic Ability SDK is a set of solutions for the Huawei AppGallery to dynamically load features by referring to the App Bundle technology. After integrating the Dynamic Ability SDK, third-party applications can download features from the Huawei AppGallery when necessary, thus reducing network traffic and consumption of terminal storage space.
[Read more about Dynamic Ability SDK](https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-featuredelivery-introduction).

## Preparing the Environments
Hardware requirements:
1. PC
2. Huawei mobile phone

Software requirements:
1. JDK 1.8 or later
2. Android API 21 or later
3. Android Studio 3.2 or later. This cases related to this guide run on Android Studio 3.5.

## Getting Started
Use Android Studio to open the decompressed project. Create a project and an application in AppGallery Connect.

## Sample Code
The Dynamic Ability SDK supports download and install feature, get install state, abort install feature, delay uninstall feature and some other functions.

Sample Code: src\main\java\com\huawei\android\dynamicfeaturesplit\SampleEntry.java

## Result
After running the app you should see a screen like this:
<img src="assets/2020-02-29-12-14-11.png" height="534" width="300" style="max-width:100%;">

## Questions or Issues
If you have questions about how to use AppGallery Connect Demos, try the following options:
* [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect) is the best place for any programming questions. Be sure to tag your question with appgallery.
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License
Dynamic Ability Demo is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).