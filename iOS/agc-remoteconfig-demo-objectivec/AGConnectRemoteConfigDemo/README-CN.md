## auth quickstart

##  介绍
    AppGallery Connect远程配置服务提供了在线的配置项管理能力，可以实现应用在线更改行为和外观，而无需用户下载应用更新。

    AppGallery Connect远程配置服务提供云端，管理台和客户端SDK，应用集成客户端SDK后可以定期获取云端管理台配置的配置项和配置参数值，实现客户端应用行为和UI的修改。

## 安装环境
    在使用quickstart 之前，开发者需要配置好iOS开发环境


## 支持环境
   XCode8.0及其以上版本。
	
## 配置
    在AppGallery Connect上开通应用的远程配置服务，并设置相关的配置项和配置条件。

    应用客户端集成AppGallery Remote Config Service SDK。

    设置应用内默认值，以便您的应用程序在连接到远程配置服务之前可以按照预期运行

    调用SDK的fetch接口，这样您可以周期性的从远程配置服务获取配置项值。远程配置服务器。收到客户端请求后，会根据上报的配置项进行条件的匹配，并返回对应的配置项值。

    应用客户端获取到配置项后，通过调用apply()接口使配置项生效，您可以在获取配置项成功后立刻调用更新以生效，也可以根据业务在其他时间段生效。

    应用客户端可以通过SDK提供的各种get方法来动态获取配置项。

##  许可证
    auth quickstart is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0).

