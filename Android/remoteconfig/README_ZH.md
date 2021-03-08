# remoteconfig quickstart

中文 | [English](https://github.com/AppGalleryConnect/agc-demos/blob/main/Android/Remote%20Configuration/README.md)


## 目录

- [简介](#简介)
- [环境要求](#环境要求)
- [快速入门](#快速入门)
- [示例代码](#示例代码)
- [示例效果](#示例效果) 
- [技术支持](#技术支持)
- [授权许可](#授权许可)  

## 简介

AppGallery Connect远程配置服务提供了在线的配置参数管理能力，可以实现应用在线更改行为和外观，而无需用户下载应用更新。AppGallery Connect远程配置服务提供云端服务，管理台和客户端SDK，应用集成客户端SDK后可以定期获取远程配置管理台配置下发的参数值，实现客户端行为和UI的修改。

## 环境要求

* 在使用quickstart 之前，开发者需要配置好Android开发环境
* 一台Android设备或者模拟器，需要支持Android 4.2及以上版本 

## 快速入门

在运行quickstart前，您需要
1、如果没有华为开发者联盟帐号，需要先[注册账号](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148)并通过实名认证。
2、使用申请的帐号登录[AppGallery Connect](https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-get-started)网站创建应用，软件包类型选择“APK(Android应用)”。
3、开通远程配置服务（详见开发指南）。
4、在AppGallery Connect 网站上下载agconnect-services.json 文件，并拷贝agconnect-services.json文件到app/kotlin-app根目录下。

## 示例代码

每次启动先调用fetch接口，在fetch成功后使用数据。
src\main\java\com\huawei\agc\quickstart\Test1Activity.java

调用fetch接口后下次启动生效。
src\main\java\com\huawei\agc\quickstart\Test2Activity.java

## 示例效果

**点击 'FETCH DATA AND APPLY'**</br>
<img src="images/fetch and apply.gif" alt="resultpage" height="600"/>

**点击 'FETCH DATA AND APPLY NEXT STARTUP'**</br>

<img src="images/fetch and apply next start.gif" alt="resultpage" height="600"/>

## 技术支持

如果您对使用AppGallery Connect示例代码有疑问，请尝试：

- 开发过程遇到问题上[Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect)，在`appgallery`标签下提问，有华为研发专家在线一对一解决您的问题。
- 到[华为开发者论坛](https://developer.huawei.com/consumer/cn/forum/blockdisplay?fid=18) AppGallery Connect板块与其他开发者进行交流。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/AppGalleryConnect/agc-demos/issues)，也欢迎您提交[Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls)。

## 授权许可

该示例代码经过[Apache 2.0 授权许可](http://www.apache.org/licenses/LICENSE-2.0)。
