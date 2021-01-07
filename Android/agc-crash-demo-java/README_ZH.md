# 华为用户身份服务客户端示例代码

中文 | [English](https://github.com/HMS-Core/hms-identity-demo/blob/master/README.md)

该示例代码介绍了华为用户身份服务（HUAWEI Identity Kit）客户端接口以及如何使用这些接口。

[参考文档](https://developer.huawei.com/consumer/cn/doc/development/HMS-Guides/about-the-service).


## 目录

- [简介](#简介)
- [环境要求](#环境要求)
  - [开发要求](#开发要求)
  - [运行环境](#运行环境)
- [安装](#安装)
- [配置](#配置)
- [使用示例](#使用示例)
- [示例代码](#示例代码)
- [授权许可](#授权许可)  


## 简介

根据示例代码，使用华为用户身份服务接口获取用户地址，选择、拼装地址信息，最后返回。


## 环境要求

### 开发要求

为了能够开发，构建，并调试该示例代码，你至少需要准备以下环境：

* 正常的网络连接，用于下载华为和谷歌的包依赖。

* 兼容的集成开发环境（IDE）。推荐安卓集成开发环境（Android Studio）。

* 使用命令行的gradle wrapper命令或者在兼容的IDE下打开下载的文件夹时，需要下载Gradle安装包。

* 安卓SDK安装包。建议API等级为28及以上。

### 运行环境

运行该示例代码需要安卓设备。该设备的EMUI版本为5.0及以上，安卓版本为4.0及以上，并且已预安装了华为移动服务（HMS）。

如果未预安装HMS，设备会在调用华为用户身份服务SDK时，提示你先安装或者升级HMS。

## 安装

1. 克隆或下载此项目，并在Android Studio或者其他兼容的IDE中打开下载的文件夹。

2. 使用IDE的相关功能将配置好的项目安装到设备上。


## 配置

本示例代码预置了agconnect-services.json，HMS依赖项，签名密钥，和应用内商品。这些预置项仅用于该示例代码。具体参见[华为用户身份服务指南](https://developer.huawei.com/consumer/cn/doc/development/HMS-Guides/about-the-service)。


## 使用示例

1. 用演示后，你会看到以下页面。
<img src="images/en-us_image_0210355807.png" alt="mainpage" height="600"/>

2. 点击“获取华为用户地址”按钮。选择具体地址，点击“确定”。将出现以下结果。
<img src="images/en-us_image_0210355809.png" alt="resultpage" height="600"/>


## 示例代码

### 获取用户地址

1. 通过**new UserAddressRequest**方法实例化request对象。然后调用**getUserAdddress**接口。代码位于src/app/src/main/java/com/huawei/demo/identitydemo/MainActivity.java文件的getUserAddress方法中。

2. 通过调用**Status**的**startActivityResult**方法展示地址选择页面。代码位于src/app/src/main/java/com/huawei/demo/identitydemo/MainActivity.java文件的startActivityForResult方法中。

3. 用户选择地址后，调用页面的**onActivityResult**中**UserAddress**的**parseIntent**方法，从返回结果中获取地址。代码位于src/app/src/main/java/com/huawei/demo/identitydemo/MainActivity.java文件的onActivityResult方法中。

## 技术支持
如果您对HMS Core还处于评估阶段，可在[Reddit社区](https://www.reddit.com/r/HuaweiDevelopers/)获取关于HMS Core的最新讯息，并与其他开发者交流见解。

如果您对使用HMS示例代码有疑问，请尝试：
- 开发过程遇到问题上[Stack Overflow](https://stackoverflow.com/questions/tagged/huawei-mobile-services)，在`huawei-mobile-services`标签下提问，有华为研发专家在线一对一解决您的问题。
- 到[华为开发者论坛](https://developer.huawei.com/consumer/cn/forum/blockdisplay?fid=18) HMS Core板块与其他开发者进行交流。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/HMS-Core/hms-identity-demo/issues)，也欢迎您提交[Pull Request](https://github.com/HMS-Core/hms-identity-demo/pulls)。

## 授权许可

该示例代码经过[Apache 2.0 授权许可](http://www.apache.org/licenses/LICENSE-2.0)。
