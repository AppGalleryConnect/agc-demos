# remoteconfig quickstart

English | [中文](https://github.com/AppGalleryConnect/agc-demos/blob/main/iOS/RemoteConfig/README_ZH.md)

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

* A computer with Xcode installed for app development
* An iOS device or simulator must support iOS 9 or a later version

## Getting Started

Before running the quickstart app, you need to:
1. If you do not have a HUAWEI Developer account, you need to [register an account](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-get-started), create an app, and set Package type to iOS.
3. Enable Remote Configuration. (For details, please refer to the development guide.)
4. Download the agconnect-services.plist file from AppGallery Connect and add it to the root directory of the Xcode project.

## Sample Code

Fetch the remote configuration after startup, and apply the configuration immediately when success.
Objective-C Code: AGConnectRemoteConfigDemo/AGConnectRemoteConfigDemo/FirstModeViewController.m
Swift Code: AGConnectRemoteConfigDemo/AGConnectRemoteConfigDemo-Swift/FirstModeViewController.swift

Fetch the remote configuration after startup, and apply the configuration after the next startup.
Objective-C Code: AGConnectRemoteConfigDemo/AGConnectRemoteConfigDemo/SecondModeViewController.m
Swift Code: AGConnectRemoteConfigDemo/AGConnectRemoteConfigDemo-Swift/SecondModeViewController.swift

## Result

**Click 'FETCH DATA AND APPLY'</br>
<img src="images/fetch and apply.gif" alt="resultpage" height="600"/>

**Click 'FETCH DATA AND APPLY NEXT STARTUP'</br>

<img src="images/fetch and apply next start.gif" alt="resultpage" height="600"/>

## Question or issues

If you have questions about how to use AppGallery Connect Demos, try the following options:  

* [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect) is the best place for any programming questions. Be sure to tag your question with huawei-mobile-services.  
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License

Remote Configuration quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
