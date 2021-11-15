## Cloud functions quickstart

## Table of Contents

 * [Introduction](#introduction)
 * [Preparing the Environments](#preparing-the-environments)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-Code)
 * [Result](#result)
 * [Question or issues](#question-or-issues)
 * [License](#license)
 
## Introduction
Cloud Functions enables serverless computing. It provides the Function as a Service (FaaS) capabilities to simplify app development and O&M so your functions can be implemented more easily and your service capabilities can be built more quickly.

## Preparing the Environments
* A computer with Cocos Creator (version 2.4.4 is recommended)
* An Android device or simulator that can run Quick Game

## Getting Started
Before running the cloud functions quickstart, you need to:
1. If you do not have a HUAWEI Developer account, you need to [register an account](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/), create a project and add an app, set app platform to Quick App, set app category to Game.
3. Select your project and app in My projects, and go to Build > Cloud Functions to enable the Cloud Functions.
4.  Go to General information, download agconnect-services.json file from AppGallery Connect, copy the JSON in agconnect-services.json to the agconnect-quickgame-init.js file, initialize agconnect with it, and import the agconnect-quickgame-init.js file as plugin.
5. Select your project and app in My projects, and go to Build > Cloud Functions, [create a function and an HTTP trigger.](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-cloudfunction-appcall-web)
6. Download the [JS SDK](https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Library/agc-auth-quickgame-sdkdownload-0000001182308451), put the unzipped JS script into the [Script](./assets/Script) directory and set it as plugin.
7. Build and run the demo using Cocos Creator.
8. Input your HTTP trigger and request body in the demo and click run.

## Sample Code

Sample code: assets\Script\functionScript.js

## Result

**cloudfunctionsDemo**</br>
<img src="images/cloudfunctionsQuickGame.gif" alt="cloudfunctionsDemo" height="782"/>

## Question or issues

If you have questions about how to use AppGallery Connect Demos, try the following options:
* [Stack Overflow](https://stackoverflow.com/) is the best place for any programming questions. Be sure to tag your question with `AppGallery`.
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License
cloud functions quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).