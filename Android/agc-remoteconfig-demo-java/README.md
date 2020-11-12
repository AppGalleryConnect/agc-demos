# Remote Config QuickStart

## Introduction

Remote Configuration allows you to manage parameters online. With the service, you can change the behavior and appearance of your app online without requiring users to update the app. Remote Configuration provides cloud-based services, the console, and the client SDK. By integrating the client SDK, your app can periodically obtain parameter values delivered on the console to modify the app's behavior and appearance.

## Preparing the Environment

Before using the quickstart app, prepare your Android development environment.

## Environment Requirements

Android Studio 3.0 or later.

## Configuration

Before running the quickstart app, you need to:
1. If you do not have a HUAWEI Developer account, you need to register an account and pass identity verification.
2. Use your account to sign in to AppGallery Connect, create an app, and set Package type to APK (Android app).
3. Enable Remote Configuration. (For details, please refer to the development guide.)
4. Download the agconnect-services.json file from AppGallery Connect and replace the JSON file of the quickstart app with it.

## Sample Code

The fetch API is called before app startup, and data is used after the fetch operation is successful.
    src\main\java\com\huawei\agc\quickstart\Test1Activity.java

Data fetched by the calling the fetch API is applied upon the next app startup.
    src\main\java\com\huawei\agc\quickstart\Test2Activity.java

## License

Remote Configuration quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
