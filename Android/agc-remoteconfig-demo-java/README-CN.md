# Remote Config QuickStart

## 介绍

    AppGallery Connect远程配置服务提供了在线的配置参数管理能力，可以实现应用在线更改行为和外观，而无需用户下载应用更新。AppGallery Connect远程配置服务提供云端服务，管理台和客户端SDK，应用集成客户端SDK后可以定期获取远程配置管理台配置下发的参数值，实现客户端行为和UI的修改。

## 安装环境

    在使用quickstart 之前，开发者需要配置好Android开发环境

## 支持环境

Android Studio 3.0 及其以上版本。

## 配置

    在运行quickstart前，您需要
    1、如果没有华为开发者联盟帐号，需要先注册帐号并通过实名认证。
    2、使用申请的帐号登录AppGallery Connect网站创建应用，软件包类型选择“APK(Android应用)”。
    3、开通远程配置服务（详见开发指南）：
    4、在AppGallery Connect 网站上下载agconnect-services.json 文件，并替换quickstart的json文件。

## 代码

    每次启动先调用fetch接口，在fetch成功后使用数据。
    src\main\java\com\huawei\agc\quickstart\Test1Activity.java

    调用fetch接口后下次启动生效。
    src\main\java\com\huawei\agc\quickstart\Test2Activity.java

## 许可证

    remote config quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
