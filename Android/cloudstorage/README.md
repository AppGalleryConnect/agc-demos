# CloudStorage quickstart

English | [中文](./README_ZH.md)

## Contents
 * [Introduction](#introduction)
 * [Preparing the Environments](#Preparing the Environments)
 * [Getting Started](#Getting Started)
 * [Sample Code](#Sample Code)
 * [Question or issues](#question-or-issues)
 * [License](#License)

## Introduction
This demo demonstrates an example of using the SDK to store pictures, videos, audio, or other user-generated files. For details about the SDK, please refer to https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-get-started.


## Preparing the Environments
* A computer with Android Studio installed for app development
* A device or emulator in Android Studio running Android 4.2 or a later version

## Getting Started

Before running the quickstart app, you need to:

1. Activate cloud storage service: The cloud storage service is not activated by default. You need to manually activate the cloud storage service in AGC.

2. New storage instance: After opening the cloud storage service, AGC will automatically create a default storage instance. If you want to store application system data and user data separately, you can create a new storage instance yourself.

3. Integrated SDK: If you need to use cloud storage-related functions in the application client, you must integrate the cloud storage client SDK.

4. Initialize the cloud storage: Before the application client uses the cloud storage, you need to initialize the cloud storage and specify the storage instance used by the client.

5. File management: The application client can call the API of the cloud storage SDK to perform operations such as uploading files, downloading files, deleting files, and modifying file metadata.


For more development details, please refer to the following link:
Development Guide: https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-get-started

API References: https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-References/cloudstroage

## Sample Code

Sample code: /app/src/main/java/com/huawei/agc/quickstart/storage/MainActivity.java

Kotlin Sample code: /kotlin-app/src/main/java/com/huawei/agconnect/kotlindemo/MainActivityKotlin.kt


## Question or issues
If you have questions about how to use AppGallery Connect Demos, try the following options:
* [Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect) is the best place for any programming questions. Be sure to tag your question with appgallery.
* [Huawei Developer Forum](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) AppGallery Module is great for general questions, or seeking recommendations and opinions.

If you run into a bug in our samples, please submit an [issue](https://github.com/AppGalleryConnect/agc-demos/issues) to the Repository. Even better you can submit a [Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) with a fix.

## License
quickstart is licensed under the [Apache License, version 2.0] (http://www.apache.org/licenses/LICENSE-2.0).


