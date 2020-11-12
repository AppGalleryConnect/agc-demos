# AppLinking quickstart

## 介绍

    AppLinking是一种不管应用是否已经安装都可以按照所需方式工作的跨平台链接，链接的目标内容可以是您想要推广的产品优惠活动，也可以是用户之间可以互相分享的应用原生内容。您可以创建AppLinking后将链接地址直接发送给用户，或者由用户在应用中动态生成AppLinking并分享给其他用户，接收到AppLinking的用户点击链接后即可跳转到链接指定的内容。

## 安装环境

    在使用quickstart 之前，开发者需要配置好Android开发环境

## 支持环境

Android Studio 3.0 及其以上版本。

## 配置

    在运行quickstart前，您需要
    1、如果没有华为开发者联盟帐号，需要先注册帐号并通过实名认证。
    2、使用申请的帐号登录AppGallery Connect网站创建应用，软件包类型选择“APK(Android应用)”。
    3、开启AppLinking服务。
    4、在AppGallery Connect 网站上下载agconnect-services.json 文件，并替换quickstart的json文件。

## 代码

    应用主入口，处理接收到的链接。
    代码：src\main\java\com\huawei\agc\quickstart\MainActivity.java

    应用内处理Deeplink之后跳转的界面。
    代码：src\main\java\com\huawei\agc\quickstart\DetailActivity.java

## 许可证

    AppLinking quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
