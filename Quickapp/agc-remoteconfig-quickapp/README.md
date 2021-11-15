## Remote Config quickstart

English | [中文](./README_ZH.md)

## Table of Contents

 * [Introduction](#introduction)
 * [Preparing the Environments](#preparing-the-environments)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-Code)
 * [Result](#result)
 * [Question or issues](#question-or-issues)
 * [License](#license)
 * [Remarks](#Remarks)

## Introduction
AppGallery Connect Remote Configuration allows you to change the behavior and appearance of your app online without requiring users to update the app. With the service, you can provide tailored experience for your users in a timely manner.

## Preparing the Environments
* A computer with Huawei QuickApp IDE
* A device can run QuickApp

## Getting Started
Before running the remote config quickstart, you need to:
1. If you do not have a HUAWEI Developer account, you need to [register an account](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/), create a project and add an app, set app platform to Quick App, set app category to App.
3. Select your project and app in My projects, and go to Grow > Remote Configuration to enable the Remote Config.
4. Select Project Settings, and go to Manage APIs, enable the Remote Configuration.
5. Go to General information, download the agconnect-services.json file from AppGallery Connect, replace [agconnect-services.json](./agconnect-services.json) in this demo with agconnect-services.json file.
6. Select your project and app in My projects, and go to Grow > Remote Configuration, [create parameter values](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-remoteconfig-web-cloudconfig-0000001056699160).
7. Run the following code in terminal in the demo path, demo will start, then you can [fetch parameter values](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-remoteconfig-web-obtainconfig-0000001056621220).
    ``` 
    # install dependencies
    npm install
    
    # install AGC remote config sdk
    npm install @agconnect/remoteconfig@1.3.1 --save
    
    # run demo
    Ctrl+Shift+R
    ```
8. More details about [Remote Config]()

## Sample Code

Sample code: src\RemoteConfig\remoteconfig.ux

## Result

**remoteconfigDemo**</br>
<img src="image/remoteconfigQuickApp.gif" alt="remoteconfigDemo" height="782"/>

## Question or issues

If you have questions about how to use AppGallery Connect Demos, try the following options:
* [Stack Overflow](https://stackoverflow.com/) is the best place for any programming questions. Be sure to tag your question with `AppGallery`.
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License
remote config quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

## Remarks
When you create a serverless fast app with the Huawei fast application IDE, the directory structure of this demo corresponds to the client directory of serverless fast app.