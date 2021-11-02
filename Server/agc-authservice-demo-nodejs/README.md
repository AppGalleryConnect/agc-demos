## Auth-server quickstart

English | [ÖÐÎÄ](./README_ZH.md)

## Table of Contents

 * [Introduction](#introduction)
 * [Preparing the Environments](#preparing-the-environments)
 * [Getting Started](#getting-started)
 * [Sample Code](#sample-Code)
 * [Result](#result)
 * [Question or issues](#question-or-issues)
 * [License](#license)

## Introduction
Most apps need to identify and authenticate users to tailor the app experience for individual users. However, building such a system from scratch is a difficult process. Auth Service can quickly build a secure and reliable user authentication system for your app. You only need to access Auth Service capabilities in your app without caring about the facilities and implementation on the cloud.

## Preparing the Environments
* Before using the auth server sdk, your server needs support Node.js v10.12.0 and higher.

## Getting Started
Before running the auth server sdk, you need to:
1. If you do not have a HUAWEI Developer account, you need to [register an account](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/), create a project.
3. Go to Project settings > Server SDK, click Create under API client, then click Download credential.
4. Save the downloaded authentication credential file agc-apiclient-xxx-xxx.json to the specified path. The file will be used during SDK initialization in [index.js](./index.js).
5. Run the following code in terminal in the demo path, and demo will start.
    ``` bash
    # install AGC auth-server sdk
    npm install --save @agconnect/auth-server@1.1.0
    # run demo
    npm run build
    ```
5. More details about [Auth-Server](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-get-started-server-0000001058092593#section1778162811430)

## Sample Code

Sample code: index.js

## Question or issues

If you have questions about how to use AppGallery Connect Demos, try the following options:
* [Stack Overflow](https://stackoverflow.com/) is the best place for any programming questions. Be sure to tag your question with `AppGallery`.
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License
This quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
