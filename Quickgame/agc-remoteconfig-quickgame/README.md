## Remote Config quickstart

## Table of Contents

 * [Introduction](#introduction)
 * [Preparing the Environments](#preparing-the-environments)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-Code)
 * [Result](#result)
 * [Question or issues](#question-or-issues)
 * [License](#license)

## Introduction
AppGallery Connect Remote Configuration allows you to change the behavior and appearance of your app online without requiring users to update the app. With the service, you can provide tailored experience for your users in a timely manner.

## Preparing the Environments
* A computer with Cocos Creator (version 2.4.4 is recommended)
* An Android device or simulator that can run Quick Game

## Getting Started
Before running the remote config quickstart, you need to:
1. If you do not have a HUAWEI Developer account, you need to [register an account](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/), create a project and add an app, set app platform to Quick App, set app category to Game.
3. Select your project and app in My projects, and go to Grow > Remote Configuration to enable the Remote Config.
4. Select Project Settings, and go to Manage APIs, enable the Remote Configuration.
5. Go to General information, download agconnect-services.json file from AppGallery Connect, copy the JSON in agconnect-services.json to the agconnect-quickgame-init.js file, initialize agconnect with it, and import the agconnect-quickgame-init.js file as plugin.
6. Select your project and app in My projects, and go to Grow > Remote Configuration, [create parameter values](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-remoteconfig-web-cloudconfig-0000001056699160).
7. Download the [JS SDK](https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Library/agc-auth-quickgame-sdkdownload-0000001182308451), put the unzipped JS script into the [Script](./assets/Script) directory and set it as plugin.
8. Build and run the demo using Cocos Creator.
9. you can [fetch parameter values](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-remoteconfig-web-obtainconfig-0000001056621220).

## Sample Code

Sample code: assets\Script\remoteconfigScript.js

## Result

**remoteconfigDemo**</br>
<img src="images/remoteconfigQuickGame.gif" alt="remoteconfigDemo" height="782"/>

## Question or issues

If you have questions about how to use AppGallery Connect Demos, try the following options:
* [Stack Overflow](https://stackoverflow.com/) is the best place for any programming questions. Be sure to tag your question with `AppGallery`.
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License
remote config quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).