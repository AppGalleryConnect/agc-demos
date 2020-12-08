# AGCLinks quickstart

## Introduction

The AGC Links service allows AGC Links. AGC Links are links that work across platforms even on devices where your app is not installed. You can use AGC Links to direct users to promotional information or native app content that they can share with others. You can create AGC Links and send them to users, or users can share AGC Links dynamically generated in your app. Anyone who receives an AGC Link can tap it to access the linked content.

## Preparing the Environment

Before using the quickstart app, prepare your Android development environment.

## Environment Requirements

Android Studio 3.0 or later.

## Configuration

Before running the quickstart app, you need to:

1. If you do not have a HUAWEI Developer account, you need to register an account and pass identity verification.
2. Use your account to sign in to AppGallery Connect, create an app, and set Package type to APK (Android app).
3. Enable the AGC Links service.
4. Download the agconnect-services.json file from AppGallery Connect and replace the JSON file of the quickstart app with it.
5. Create a link prefix in AppLinking and copy and replace DOMAIN_URI_PREFIX in MainActivity.

## Sample Code

Main entry of the app, which processes received links.
Sample code: src\main\java\com\huawei\agc\quickstart\MainActivity.java

Redirection to the target page when a deep link is processed in the app.
Sample code: src\main\java\com\huawei\agc\quickstart\DetailActivity.java

## License

AGC Links quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).
