
## 简介

    Cloud Functions是一项Serverless计算服务，提供FaaS（Function as a Service）能力，可以帮助开发者大幅简化应用开发与运维相关的事务，降低创意实现门槛，快速构建业务能力。
    1、Cloud Functions提供了高效可靠的函数开发与运行框架，替开发者完全解决传统应用开发与运维中的诸多复杂事务（如服务器配置与管理、代码部署、负载均衡、弹性伸缩、高可用保证等等），
       开发者只需聚焦业务逻辑、开发并上传函数代码，即可构建高可用、可伸缩的Serverless应用。
    2、Cloud Functions作为Serverless的核心与枢纽，支持方便连接和扩展周边云服务能力，开发者可以像拼搭积木一样自由便捷地组织各项服务来实现业务逻辑。

## 环境要求

* 在使用quickstart 之前，开发者需要配置好Android开发环境
* 一台Android设备或者模拟器，需要支持Android 4.2及以上版本

## 快速入门

在运行quickstart前，您需要
1、如果没有华为开发者联盟帐号，需要先[注册账号](https://developer.huawei.com/consumer/en/doc/start/registration-and-verification-0000001053628148)并通过实名认证。
2、使用申请的帐号登录[AppGallery Connect](https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-get-started)网站创建应用，软件包类型选择“APK(Android应用)”。
3、创建云函数（详见开发指南）：
  3.1 登录AppGallery Connect，点击“我的应用”，点击需要启动认证服务的应用所属的产品。
  3.2 开发 > 构建 > 云函数，进入“Cloud Functions”页面
  3.3 在“函数”页面，点击“创建函数”。
  3.4 在创建界面中，完成函数定义。
  3.5 函数创建完成后单击“保存”。
 4、在AppGallery Connect网站上下载agconnect-services.json 文件，并放在应用级根目录下(例如：quickstart是function/app/)。在编译APK之前，请确保项目中包含agconnect-services.json文件，否则会编译出错。

## 示例代码
   sdk 支持对云函数的调用并返回调用结果。
   代码：src\main\java\com\huawei\agc\quickstart\MainActivity.java

## 示例效果
**调用云函数**</br>
<img src="images/function_result.gif" alt="function_result" height="600"/>


## 技术支持

如果您对使用AppGallery Connect示例代码有疑问，请尝试：
- 开发过程遇到问题上[Stack Overflow](https://stackoverflow.com/users/14194729/appgallery-connect)，在`AppGallery`标签下提问，有华为研发专家在线一对一解决您的问题。
- 到[华为开发者论坛](https://forums.developer.huawei.com/forumPortal/en/home?fid=0101188387844930001) AppGallery Connect板块与其他开发者进行交流。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/AppGalleryConnect/agc-demos/issues)，也欢迎您提交[Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls)。

## 授权许可

该示例代码经过[Apache 2.0 授权许可](http://www.apache.org/licenses/LICENSE-2.0)。

