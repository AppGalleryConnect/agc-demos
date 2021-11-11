## Auth demo快速入门

中文 | [English](./README.md)

## 目录

 * [简介](#简介)
 * [环境要求](#环境要求)
 * [快速入门](#快速入门)
 * [示例代码](#示例代码)
 * [示例效果](#示例效果)
 * [技术支持](#技术支持)
 * [授权许可](#授权许可)

## 简介
大部分的应用都需要对用户的业务访问进行身份认证，以便为用户提供个性化体验。但是端到端完整构建一套用户认证系统会面临很多挑战。认证服务可以为您的应用快速构建安全可靠的用户认证系统，您只需在应用中访问认证服务的相关能力，而不需要关心云侧的设施和实现。

## 环境要求
* 一台可以编译运行Vue项目的计算机

## 快速入门
在运行quickstart前，您需要
1. 如果没有华为开发者联盟帐号，需要先[注册账号](https://developer.huawei.com/consumer/cn/doc/start/registration-and-verification-0000001053628148) 并通过实名认证。
2. 使用申请的帐号登录[AppGallery Connect](https://developer.huawei.com/consumer/cn/service/josp/agc/index.html#/) 网站创建一个项目并添加应用，软件包类型选择“Web”。
3. 在我的项目中进入新建的项目，选择创建的Web应用，进入“构建”>“认证服务”页面，点击“立即开通”，开启认证服务。
4. 点击“项目设置”>“API管理”，开启auth Service。
5. 点击“常规”，复制SDK代码片段并粘贴到[config.js](./src/components/config.js) 。
6. 回到“构建”>“认证服务”页面，点击“认证方式”，启用“手机号码”、“邮箱地址”、“微信”、“QQ”、“匿名账号”。
7. 在命令行中依次运行如下命令以运行demo：
    ```
    # 安装依赖
    npm install

    # 安装 AGC auth sdk
    npm install @agconnect/auth@1.3.0 --save

    # 启动demo
    npm start
    ```
8. 更多详情请点击[Auth](https://developer.huawei.com/consumer/cn/doc/development/AppGallery-connect-Guides/agc-auth-web-getstarted-0000001053612703) 。

## 示例代码

Sample code: src\components\*

## 示例效果

**loginAnonymous**</br>
<img src="images/anonymous.gif" alt="anonymous" height="946"/>

**createUser**</br>
<img src="images/createUser.gif" alt="createUser" height="946"/>

**deleteUser**</br>
<img src="images/deleteUser.gif" alt="deleteUser" height="946"/>

**loginByPin**</br>
<img src="images/login_phone_pin.gif" alt="loginByPin" height="946"/>

**loginbyPWD**</br>
<img src="images/loginbyPWD.gif" alt="loginbyPWD" height="946"/>

**link**</br>
<img src="images/login_phone_link.gif" alt="link" height="946"/>

## 技术支持

如果您对使用AppGallery Connect示例代码有疑问，请通过如下途径寻求帮助：
- 访问[Stack Overflow](https://stackoverflow.com/) , 在`AppGallery`标签下提问，有华为研发专家在线一对一解决您的问题。
- 访问[华为开发者论坛](https://forums.developer.huawei.com/forumPortal/en/home) AppGallery Connect板块与其他开发者进行交流。

如果您在尝试示例代码中遇到问题，请向仓库提交[issue](https://github.com/AppGalleryConnect/agc-demos/issues) ，也欢迎您提交[Pull Request](https://github.com/AppGalleryConnect/agc-demos/pulls) 。

## 授权许可
该示例代码经过[Apache 2.0 授权许可](http://www.apache.org/licenses/LICENSE-2.0) 。
