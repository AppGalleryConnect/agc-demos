## appmessaging quickstart

##  介绍
    通过App Messaging服务，实现用户使用应用时，向活跃用户发送有针对性的上下文消息,来鼓励用户使用应用的关键功能，从而吸引这些用户。

## 安装环境
    在使用quickstart 之前，开发者需要配置好Android开发环境


## 支持环境
   Android Studio 3.0及其以上版本。
	
## 配置
    在运行quickstart前，您需要
    1、如果没有华为开发者联盟帐号，需要先注册帐号并通过实名认证。
    2、使用申请的帐号登录AppGallery Connect网站创建应用，软件包类型选择“APK(Android应用)”。
    3、开启应用内消息功能，并在控制台创建消息。
    4、在AppGallery Connect 网站上下载agconnect-services.json 文件，并放在应用级根目录下(例如：quickstart是appmessaging/app/)。
    在编译APK之前，请确保项目中包含agconnect-services.json文件，否则会编译出错。

## 代码
    SDK支持消息展示的控制消息调试。
    代码：src\main\java\com\huawei\agc\quickstart\appmessaging\AppMessagingActivity.java


##  许可证
    appmessaging quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

