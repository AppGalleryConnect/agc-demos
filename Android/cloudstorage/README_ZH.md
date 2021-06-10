# CloudStorage quickstart

中文 | [English](./README.md)

## 目录

- [简介](#简介)
- [环境要求](#环境要求)
- [快速入门](#快速入门)
- [示例代码](#示例代码)
- [技术支持](#技术支持)
- [授权许可](#授权许可)

## 简介

本Demo演示了使用SDK存储图片、视频、音频或其他用户生成的文件的示例。

有关SDK的详细信息，请参见: https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-get-started

## 环境要求

* 在使用quickstart 之前，开发者需要配置好Android开发环境
* 一台Android设备或者模拟器，需要支持Android 4.2及以上版本

## 快速入门

在运行quickstart前，您需要

1、激活云存储服务：默认不激活云存储服务。您需要在AGC中手动激活云存储服务。

2、新存储实例：打开云存储服务后，AGC将自动创建默认存储实例。如果您希望将应用系统数据和用户数据分开存储，您可以自行创建新的存储实例。

3、集成SDK：如果您需要在应用客户端中使用云存储相关功能，则必须集成云存储客户端SDK。

4、初始化云存储：在应用客户端使用云存储之前，需要初始化云存储，并指定客户端使用的存储实例。

5、文件管理：应用客户端可以调用云存储SDK的API，进行上传文件、下载文件、删除文件、修改文件元数据等操作。

有关更多开发详细信息，请参阅以下链接：
开发指南：https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-get-started

API参考：https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-References/cloudstroage

## 代码

Sample code: /app/src/main/java/com/huawei/agc/quickstart/storage/MainActivity.java

Kotlin Sample code: /kotlin-app/src/main/java/com/huawei/agconnect/kotlindemo/MainActivityKotlin.kt



## 技术支持

如果您对使用AppGallery Connect示例代码有疑问，请尝试：

- 开发过程遇到问题上[Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect)，在`appgallery`标签下提问，有华为研发专家在线一对一解决您的问题。
- 到[华为开发者论坛](https://developer.huawei.com/consumer/cn/forum/blockdisplay?fid=18) AppGallery Connect板块与其他开发者进行交流。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/AppGalleryConnect/agc-demos/issues)，也欢迎您提交[Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls)。

## 授权许可

该示例代码经过[Apache 2.0 授权许可](http://www.apache.org/licenses/LICENSE-2.0)。
