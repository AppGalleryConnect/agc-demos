## Auth quickstart

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
* A computer can compile and run Vue project

## Getting Started
Before running the auth quickstart, you need to:
1. If you do not have a HUAWEI Developer account, you need to [register an account](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148) and pass identity verification.
2. Use your account to sign in to [AppGallery Connect](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-get-started), create a project and add an app, set app platform to Web.
3. Select your project and app in My projects, and go to Build > Auth Service to enable the Authentication service.
4. Select Project Settings,and go to Manage APIs, enable the Auth Service.
5. Go to General information, copy the SDK Code Snippet json from AppGallery Connect and paste the json to [config.js](./src/components/config.js) in this demo.
6. Select your project and app in My projects, and go to Build > Auth Service > Authentication mode, enable Mobile number, Email address, WeChat, QQ, Anonymous account.
7. Run the following code in terminal in the demo path, and demo will start.
    ``` bash
    # install dependencies
    npm install

    # install AGC auth sdk
    npm install --save @agconnect/auth

    # run demo
    npm start
    ```
8. More details about [Auth](https://developer.huawei.com/consumer/en/doc/development/AppGallery-connect-Guides/agc-auth-web-getstarted-0000001053612703)

## Sample Code

Sample code: src\components\*

## Result
**loginAnonymous**</br>
<img src="/images/anonymous.gif" alt="anonymous" height="946"/>

**createUser**</br>
<img src="/images/createUser.gif" alt="createUser" height="946"/>

**deleteUser**</br>
<img src="/images/deleteUser.gif" alt="deleteUser" height="946"/>

**loginByPin**</br>
<img src="/images/login_phone_pin.gif" alt="loginByPin" height="946"/>

**loginbyPWD**</br>
<img src="/images/loginbyPWD.gif" alt="loginbyPWD" height="946"/>

**link**</br>
<img src="/images/login_phone_link.gif" alt="link" height="946"/>

## Question or issues

If you have questions about how to use AppGallery Connect Demos, try the following options:
* [Stack Overflow](https://stackoverflow.com/) is the best place for any programming questions. Be sure to tag your question with `AppGallery`.
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License
auth quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
